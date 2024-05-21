package com.mistermaster.airsim;

import com.badlogic.gdx.math.Vector3;

import sun.font.DelegatingShape;

public class TurbulenceObject {
    Vector3 position;
    float radius;
    float impact;

    public TurbulenceObject(Vector3 position, float radius, float impact) {
        this.position = position;
        this.radius = radius;
        this.impact = impact;
    }

    public Vector3 doTurbulence(Vector3 frompos){
        Vector3 topos = new Vector3(frompos);
        float dist = Math.min(0,radius-position.dst2(frompos));
        Vector3 delta = new Vector3().setToRandomDirection().scl(impact);
        return topos;
    }
}
