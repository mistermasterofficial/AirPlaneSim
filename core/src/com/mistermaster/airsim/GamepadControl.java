package com.mistermaster.airsim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;

import java.util.HashMap;
import java.util.Map;

public class GamepadControl implements ControllerListener {

    public static final HashMap<Integer,String> Map = new HashMap<>();
    public static final HashMap<String,Boolean> EventButtonMap = new HashMap<>();
    public static final HashMap<String,Float> EventAxisMap = new HashMap<>();

    public static boolean isPressed() {
        boolean res = isPressed;
        isPressed = false;
        return res;
    }

    private static boolean isPressed = false;

    static {
        Map.put(98,"CIRCLE");
        Map.put(99,"TRIANGLE");
        Map.put(96,"SQUARE");
        Map.put(97,"CROSS");

        Map.put(0,"LEFT_AXIS_X");
        Map.put(1,"LEFT_AXIS_Y");
        Map.put(2,"RIGHT_AXIS_X");
        Map.put(5,"RIGHT_AXIS_Y");

        Map.put(100,"L1");
        Map.put(101,"R1");
        Map.put(102,"L2");
        Map.put(103,"R2");

        Map.put(109,"LEFT_AXIS_BUTTON");
        Map.put(108,"RIGHT_AXIS_BUTTON");




//        Map.put(19,"UP");
//        Map.put(21,"LEFT");
//        Map.put(22,"RIGHT");
//        Map.put(20,"DOWN");

//        Map.put(100,"TRIANGLE");
//        Map.put(99,"SQUARE");
//        Map.put(97,"CIRCLE");
//        Map.put(96,"CROSS");

//        Map.put(109,"SHARE");
//        Map.put(110,"START");
//        Map.put(108,"OPTIONS");
//
//        Map.put(7,"LEFT_AXIS_X");
//        Map.put(6,"LEFT_AXIS_Y");
//        Map.put(3,"RIGHT_AXIS_X");
//        Map.put(2,"RIGHT_AXIS_Y");

//        Map.put(106,"LEFT_AXIS_BUTTON");
//        Map.put(107,"RIGHT_AXIS_BUTTON");

//        Map.put(102,"L1");
//        Map.put(103,"R1");
//        Map.put(104,"L2");
//        Map.put(105,"R2");

//        Map.put(4,"L2_AXIS");
//        Map.put(5,"L2_AXIS");
//        Map.put(0,"R2_AXIS");
//        Map.put(1,"R2_AXIS");


        EventButtonMap.put("UP",false);
        EventButtonMap.put("LEFT",false);
        EventButtonMap.put("RIGHT",false);
        EventButtonMap.put("DOWN",false);

        EventButtonMap.put("TRIANGLE",false);
        EventButtonMap.put("SQUARE",false);
        EventButtonMap.put("CIRCLE",false);
        EventButtonMap.put("CROSS",false);

        EventButtonMap.put("SHARE",false);
        EventButtonMap.put("START",false);
        EventButtonMap.put("OPTIONS",false);

        EventButtonMap.put("LEFT_AXIS_BUTTON",false);
        EventButtonMap.put("RIGHT_AXIS_BUTTON",false);

        EventButtonMap.put("L1",false);
        EventButtonMap.put("R1",false);
        EventButtonMap.put("L2",false);
        EventButtonMap.put("R2",false);



        EventAxisMap.put("LEFT_AXIS_X",0f);
        EventAxisMap.put("LEFT_AXIS_Y",0f);
        EventAxisMap.put("RIGHT_AXIS_X",0f);
        EventAxisMap.put("RIGHT_AXIS_Y",0f);

        EventAxisMap.put("L2_AXIS",0f);
        EventAxisMap.put("R2_AXIS",0f);
    }
    
    public static void init(){
        for (Controller controller : Controllers.getControllers()) {
            controller.addListener(new GamepadControl());
        }
    }

    @Override
    public void connected(Controller controller) {

    }

    @Override
    public void disconnected(Controller controller) {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        Gdx.app.log("KEY", buttonCode+" is pressed");
        EventButtonMap.put(Map.get(buttonCode),true);
        isPressed = true;
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        Gdx.app.log("KEY", buttonCode+" is NOT pressed");
        EventButtonMap.put(Map.get(buttonCode),false);
        isPressed = false;
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        Gdx.app.log("KEY", axisCode+" is moved");
        if(value!=0.003921628f) {
            EventAxisMap.put(Map.get(axisCode), value);
        }else{
            EventAxisMap.put(Map.get(axisCode), 0f);
        }
        return false;
    }
}
