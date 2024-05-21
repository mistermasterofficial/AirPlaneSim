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
	private BitmapFont font;
	private SpriteBatch batch;

	private SceneAsset sceneAsset;
	private SceneManager sceneManager;

	private MyObject surfaceObject;
	private List<MyObject> coins = new ArrayList<>();
	private AirPlaneObject airPlaneObject;

	Root root;

	public void centerAlignTextDraw(BitmapFont font, Batch batch, String string, float x, float y){
		GlyphLayout layout = new GlyphLayout(font, string);
		font.draw(batch,string,x- layout.width/2,y-layout.height/2);
	}

	public void generateCoin(){
		MyObject coin = new MyObject(new GLTFLoader().load(Gdx.files.internal("models/coin/scene.gltf")).scene);
		Random random = new Random();
		coin.setPosition(new Vector3((random.nextFloat()*2-1)*10,(random.nextFloat())*10+10,(random.nextFloat()*2-1)*10));
		coins.add(coin);
		sceneManager.addScene(coin);
	}


	public void restartGame(){
		coins.clear();
		for (int i = 0; i < 10-coins.size(); i++) {
			generateCoin();
		}
		root.setScore(0);
		airPlaneObject.getCamera().position.set(new Vector3(0f,10f,0f));
		airPlaneObject.getCamera().update();
	}


	@Override
	public void create() {
		Bullet.init();
		font = new BitmapFont(Gdx.files.internal("font/Karma Future.fnt"),Gdx.files.internal("font/Karma Future.png"),false);
		batch = new SpriteBatch();

		root = Root.getInstance();

		sceneManager = new SceneManager();

		sceneAsset = new GLTFLoader().load(Gdx.files.internal("models/surface/surface.gltf"));
		surfaceObject = new MyObject(sceneAsset.scene);
		surfaceObject.setPosition(new Vector3(0,surfaceObject.bounds.getCenterY()-surfaceObject.bounds.getMax(new Vector3()).y,0));
		sceneManager.addScene(surfaceObject);

//		sceneAsset = new GLTFLoader().load(Gdx.files.internal("models/airplane/airplane.gltf"));
//		scene = new Scene(sceneAsset.scene);
//		scene.modelInstance.transform.setTranslation(new Vector3(5f,5f,5f));
//		sceneManager.addScene(scene);

		Camera cam = new PerspectiveCamera(90, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		cam.near = 0.001f;
		cam.far = 300f;
		airPlaneObject = new AirPlaneObject(cam);

		sceneManager.setCamera(airPlaneObject.getCamera());

		GamepadControl.init();

		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		sceneManager.environment = new Environment();
//		sceneManager.environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		sceneManager.environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

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
						"Position: \n"+airPlaneObject.getCamera().position.x+
								"\n"+airPlaneObject.getCamera().position.y+
								"\n"+airPlaneObject.getCamera().position.z,
						(float) Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight());
			}
			if(GamepadControl.EventButtonMap.get("TRIANGLE")){
				centerAlignTextDraw(font,
						batch,
						airPlaneObject.getInfo(),
						(float) Gdx.graphics.getWidth() /2, Gdx.graphics.getHeight());
			}
			if(GamepadControl.EventButtonMap.get("SQUARE")){
				centerAlignTextDraw(font,
						batch,
						"Score: "+root.getScore(),
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
					Log("COIN!");
					break;
				}
			}

			if ((surfaceObject.isCollideWith(airPlaneObject.getCamera().position))||
				(airPlaneObject.getCamera().position.x > surfaceObject.bounds.getMax(new Vector3()).x ||
				airPlaneObject.getCamera().position.z > surfaceObject.bounds.getMax(new Vector3()).z ||
				airPlaneObject.getCamera().position.z < surfaceObject.bounds.getMin(new Vector3()).z ||
				airPlaneObject.getCamera().position.x < surfaceObject.bounds.getMin(new Vector3()).x)) {
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

//		Log(airPlaneObject.getCamera().position+"");
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
