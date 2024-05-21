package com.mistermaster.airsim;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneModel;

public class MyObject extends Scene {
    BoundingBox bounds = new BoundingBox();

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        modelInstance.transform.translate(position);
        this.position = position;
        bounds.set(bounds.getMin(new Vector3()).sub(bounds.getCenter(new Vector3())).add(position),bounds.getMax(new Vector3()).sub(bounds.getCenter(new Vector3())).add(position));
    }

    private Vector3 position;



    public MyObject(SceneModel scene) {
        super(scene);
        modelInstance.calculateBoundingBox(bounds);
        setPosition(new Vector3());
    }

    public float getDistance(Vector3 pos){
        float res = bounds.getCenter(new Vector3()).dst2(pos);

        if(bounds.contains(pos)){
            res = 0;
        }

        return res;
    }

    public boolean isCollideWith(Vector3 pos){
        if(this.getDistance(pos)<=0){
            return true;
        }
        return false;
    }
}
