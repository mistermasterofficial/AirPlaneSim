package com.mistermaster.airsim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Sphere;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;

public class AirPlaneObject {
    public Camera getCamera() {
        return this.cam;
    }

    private Camera cam;

    private float rotation_speed = 0f;
    private float moving_speed = 1f;
    private float up_rotation_speed = 0f;

    private float acceleration = 1f;

    private float gravity_speed = 1f;

    public AirPlaneObject(Camera cam) {
        this.cam=cam;
    }

    public void update(float deltaTime){
        if(!(Root.getInstance().isGameOver() || Root.getInstance().isRestartGame())) {
            if(GamepadControl.EventButtonMap.get("CIRCLE")){
                rotation_speed = 0;
                up_rotation_speed = 0;
            }

            if(GamepadControl.EventAxisMap.get("LEFT_AXIS_X")!=0f){
                float value = GamepadControl.EventAxisMap.get("LEFT_AXIS_X");
                rotation_speed += value;
            }

            if(GamepadControl.EventAxisMap.get("LEFT_AXIS_Y")!=0f){
                float value = GamepadControl.EventAxisMap.get("LEFT_AXIS_Y");
                up_rotation_speed += -value;
            }

            if(GamepadControl.EventButtonMap.get("L2")&&GamepadControl.EventButtonMap.get("R2")){
                moving_speed += acceleration * deltaTime;
                Gdx.app.log("KEY", "AAAAAAAAA is pressed");
            }
            flight(deltaTime);
            gravity(deltaTime);
        }

        cam.update();
    }

    private void gravity(float deltaTime){
        Vector3 direction = new Vector3(cam.direction).sub(new Vector3(0,cam.direction.y,0)).nor();
        Vector3 wing = new Vector3(cam.direction).rotate(cam.up, 90f);
        float scalar = (Math.abs(direction.dot(cam.direction))+Math.abs(wing.dot(Vector3.Y)))/2;
        cam.position.add(new Vector3(0,-gravity_speed*deltaTime*scalar,0));
        moving_speed+=cam.up.y>=0?direction.dot(cam.up)*gravity_speed*deltaTime:direction.dot(new Vector3(cam.up).scl(-1))*gravity_speed*deltaTime;
        moving_speed = Math.max(0,moving_speed);
    }

    private void flight(float deltaTime){
        cam.rotate(new Vector3(cam.direction), rotation_speed * deltaTime);
        cam.rotate(cam.up, -rotation_speed * deltaTime);

        cam.rotate(new Vector3(cam.direction).rotate(cam.up, 90f),up_rotation_speed*deltaTime);

        cam.position.add(new Vector3(cam.direction).scl(moving_speed * deltaTime));
    }

    public String getInfo(){
        String res = "Speed: "+moving_speed+"\n"+
                "Horizontal rotation speed: "+String.format("%.1f", rotation_speed)+"\n"+
                "Vertical rotation speed: "+String.format("%.1f", up_rotation_speed)+"\n"+
                "Position: \n"+String.format("%.1f", cam.position.x)+
                "\n"+String.format("%.1f", cam.position.y)+
                "\n"+String.format("%.1f", cam.position.z);
        return res;
    }

    public void reset(){
        rotation_speed = 0f;
        moving_speed = 1f;
        up_rotation_speed = 0f;


        cam.direction.set(new Vector3(1,0,0));
        cam.up.set(new Vector3(0,1,0));

        cam.position.set(new Vector3(0,0,0));
    }
}
