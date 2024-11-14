package com.mistermaster.airsim;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;

import net.mgsx.gltf.loaders.gltf.GLTFLoader;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneAsset;
import net.mgsx.gltf.scene3d.scene.SceneManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AirSim extends ApplicationAdapter {
	private float[] background_color = {102f/255f, 150f/255f, 252f/255f};
	private BitmapFont font;
	private SpriteBatch batch;

	private SceneAsset sceneAsset;
	private SceneManager sceneManager;
	
	private MyObject[] surfaces = new MyObject[3];

	private int current_surface;

	private List<MyObject> coins = new ArrayList<>();
	private List<TurbulenceObject> turbulences = new ArrayList<>();
	private AirPlaneObject airPlaneObject;

	private float width;
	private float height;
	private float depth;

	private float border_width;

	private int max_coin_count = 200;
	private int max_turbulence_count = 100;

	Root root;

	public void centerAlignTextDraw(BitmapFont font, Batch batch, String string, float x, float y){
		GlyphLayout layout = new GlyphLayout(font, string);
		font.draw(batch,string,x- layout.width/2,y+ layout.height/2);
	}
	public void topAlignTextDraw(BitmapFont font, Batch batch, String string, float x, float y){
		GlyphLayout layout = new GlyphLayout(font, string);
		font.draw(batch,string,x- layout.width/2,y+ layout.height);
	}
	public void bottomAlignTextDraw(BitmapFont font, Batch batch, String string, float x, float y){
		GlyphLayout layout = new GlyphLayout(font, string);
		font.draw(batch,string,x- layout.width/2,y);
	}

	public void generateCoin(){
		MyObject coin = new MyObject(new GLTFLoader().load(Gdx.files.internal("models/coin/coin.gltf")).scene);
		Random random = new Random();
		coin.setPosition(new Vector3((random.nextFloat()*2-1)*(width/2-border_width),(random.nextFloat())*(height-border_width*2)+border_width,(random.nextFloat()*2-1)*(depth/2-border_width)));
		coins.add(coin);
		sceneManager.addScene(coin);
	}
	public void generateTurbulence(){
		Random random = new Random();
		TurbulenceObject turbulenceObject = new TurbulenceObject(new Vector3((random.nextFloat()*2-1)*(width/2),
				(random.nextFloat())*height,
				(random.nextFloat()*2-1)*(depth/2)),
				random.nextFloat()*(width/20), random.nextFloat()*100+20);
		turbulences.add(turbulenceObject);
	}


	public void restartGame(){
		generateSurface();
		for (int i = 0; i < coins.size(); i++) {
			sceneManager.removeScene(coins.get(0));
			coins.remove(coins.get(0));
		}
		for (int i = 0; i < max_coin_count-coins.size(); i++) {
			generateCoin();
		}
		turbulences.clear();
		for (int i = 0; i < max_turbulence_count-turbulences.size(); i++) {
			generateTurbulence();
		}
		root.setScore(0);
		airPlaneObject.reset();
		airPlaneObject.getCamera().position.set(new Vector3(0f,10f,0f));
		airPlaneObject.getCamera().update();
	}

	public void generateSurface(){
		sceneManager.removeScene(surfaces[current_surface]);
		current_surface = new Random().nextInt(3);
		sceneManager.addScene(surfaces[current_surface]);
	}

	@Override
	public void create() {
		Bullet.init();
		font = new BitmapFont(Gdx.files.internal("font/Karma Future.fnt"),Gdx.files.internal("font/Karma Future.png"),false);
		batch = new SpriteBatch();

		root = Root.getInstance();

		sceneManager = new SceneManager();

		surfaces[0] = new MyObject(new GLTFLoader().load(Gdx.files.internal("models/surface/surface.gltf")).scene);
		surfaces[1] = new MyObject(new GLTFLoader().load(Gdx.files.internal("models/surface1/surface.gltf")).scene);
		surfaces[2] = new MyObject(new GLTFLoader().load(Gdx.files.internal("models/surface2/surface.gltf")).scene);

		surfaces[0].setPosition(new Vector3(0,surfaces[0].bounds.getCenterY()-surfaces[0].bounds.getMax(new Vector3()).y,0));
		surfaces[1].setPosition(new Vector3(0,surfaces[1].bounds.getCenterY()-surfaces[1].bounds.getMax(new Vector3()).y,0));
		surfaces[2].setPosition(new Vector3(0,surfaces[2].bounds.getCenterY()-surfaces[2].bounds.getMax(new Vector3()).y,0));

		current_surface = 0;
		sceneManager.addScene(surfaces[current_surface]);

		Camera cam = new PerspectiveCamera(120, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		cam.near = 0.1f;
		cam.far = 15f;
		airPlaneObject = new AirPlaneObject(cam);

		sceneManager.setCamera(airPlaneObject.getCamera());

		GamepadControl.init();

		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		sceneManager.environment = new Environment();
		sceneManager.environment.set(new ColorAttribute(ColorAttribute.AmbientLight, .1f, .1f, .1f, 1f));
		sceneManager.environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

		width = surfaces[current_surface].bounds.getWidth();
		height = 30f;
		depth = surfaces[current_surface].bounds.getDepth();
		border_width = 5f;

		restartGame();
	}

	@Override
	public void render() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		LogFPS(1/deltaTime+"");

		update(deltaTime);
		draw(deltaTime);
	}

	private void draw(float deltaTime){
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glClearColor(background_color[0],background_color[1],background_color[2], 1);

		sceneManager.update(deltaTime);
		sceneManager.render();

		batch.begin();
		if(root.isRestartGame()){
			centerAlignTextDraw(font,
					batch,
					"Enter Circle Button to Start!",
					(float) Gdx.graphics.getWidth() /2, (float) Gdx.graphics.getHeight() /2);
		} else if (root.isGameOver()) {
			centerAlignTextDraw(font,
					batch,
					"Game Over!\nYour score: "+ root.getScore() +
							"\nEnter Circle Button to Continue!",
					(float) Gdx.graphics.getWidth() /2,
					(float) Gdx.graphics.getHeight() /2);
		} else {
			if(GamepadControl.EventButtonMap.get("CROSS")){
				centerAlignTextDraw(font,
						batch,
						airPlaneObject.getInfo(),
						(float) Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight()/2);
			}
			if(GamepadControl.EventButtonMap.get("OPTIONS")){
				centerAlignTextDraw(font,
						batch,
						"FPS: "+Integer.toString((int)(1/deltaTime))+"\n"+"AXIS: "+GamepadControl.debug_btn,
						(float) Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight()/2);
			}
			if(GamepadControl.EventButtonMap.get("SQUARE")){
				centerAlignTextDraw(font,
						batch,
						"Score: "+root.getScore(),
						(float) Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight()/2);
			}

			for (TurbulenceObject turbulence : turbulences) {
				if (turbulence.isCollideWith(airPlaneObject.getCamera().position)){
					topAlignTextDraw(font,
							batch,
							"Warning: Turbulence!",
							(float) Gdx.graphics.getWidth() /2, 0);
					break;
				}
			}

			if((Math.abs(airPlaneObject.getCamera().position.x) > surfaces[current_surface].bounds.getMax(new Vector3()).x-border_width ||
					Math.abs(airPlaneObject.getCamera().position.z) > surfaces[current_surface].bounds.getMax(new Vector3()).z-border_width)){
				bottomAlignTextDraw(font,
						batch,
						"Warning: Border!",
						(float) Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight());
			} else if (airPlaneObject.getCamera().position.y <= border_width) {
				bottomAlignTextDraw(font,
						batch,
						"Warning: Low height!",
						(float) Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight());
			} else if (airPlaneObject.getCamera().position.y > height-border_width) {
				bottomAlignTextDraw(font,
						batch,
						"Warning: High height!",
						(float) Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight());
			}
		}
		batch.end();
	};

	private void update(float deltaTime){
		if(!(root.isGameOver() || root.isRestartGame())){
			for (MyObject coin : coins) {
				if (coin.isCollideWith(airPlaneObject.getCamera().position)) {
					root.setScore(root.getScore() + 1);
					coins.remove(coin);
					sceneManager.removeScene(coin);
					generateCoin();
					break;
				}
			}

			for (TurbulenceObject turbulence : turbulences) {
				if (turbulence.isCollideWith(airPlaneObject.getCamera().position)){
					airPlaneObject.getCamera().position.set(turbulence.doTurbulence(airPlaneObject.getCamera().position,deltaTime));
				}
			}

			if ((surfaces[current_surface].isCollideWith(airPlaneObject.getCamera().position))||
				(Math.abs(airPlaneObject.getCamera().position.x) > surfaces[current_surface].bounds.getMax(new Vector3()).x ||
				Math.abs(airPlaneObject.getCamera().position.z) > surfaces[current_surface].bounds.getMax(new Vector3()).z)||
			airPlaneObject.getCamera().position.y > height || airPlaneObject.getCamera().position.y <= 0) {
				root.setGameOver(true);
			}

			airPlaneObject.update(deltaTime);
		}

		if(GamepadControl.EventButtonMap.get("CIRCLE") && root.isRestartGame() && GamepadControl.isPressed()){
			root.setRestartGame(false);
		} else if(GamepadControl.EventButtonMap.get("CIRCLE") && root.isGameOver() && GamepadControl.isPressed()){
			root.setGameOver(false);
			restartGame();
			root.setRestartGame(true);
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		sceneManager.dispose();
		sceneAsset.dispose();
	}

	void Log(String message){
		Gdx.app.debug("MY_TAG",message);
	}
	void LogFPS(String message){
		Gdx.app.debug("FPS",message);
	}
}
