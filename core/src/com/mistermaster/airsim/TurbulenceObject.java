package com.mistermaster.airsim;

import com.badlogic.gdx.math.Vector3;

import java.util.Random;

import sun.font.DelegatingShape;

public class TurbulenceObject {
    Vector3 position;
    float radius;
    float impact;

    Random random;

    public TurbulenceObject(Vector3 position, float radius, float impact) {
        this.position = position;
        this.radius = radius;
        this.impact = impact;
        random = new Random();
    }

    public boolean isCollideWith(Vector3 position){
        return new Vector3(position).sub(this.position).len() <= radius;
    }

    public Vector3 doTurbulence(Vector3 frompos,float deltaTime){
        float dist = Math.max(0,radius-position.dst2(frompos))/radius;
        Vector3 delta = new Vector3(0,random.nextFloat()*2-1,0).scl(dist*impact*deltaTime);
        return new Vector3(frompos).add(delta);
    }
}
