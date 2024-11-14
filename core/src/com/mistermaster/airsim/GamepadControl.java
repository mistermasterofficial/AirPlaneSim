package com.mistermaster.airsim;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.ControllerMapping;
import com.badlogic.gdx.controllers.Controllers;

import java.util.HashMap;
import java.util.Map;

public class GamepadControl implements ControllerListener {

    public static final HashMap<Integer,String> Map = new HashMap<>();
    public static final HashMap<String,Boolean> EventButtonMap = new HashMap<>();
    public static final HashMap<String,Float> EventAxisMap = new HashMap<>();

    public static int debug_btn = -1;

    public static boolean isPressed() {
        boolean res = isPressed;
        isPressed = false;
        return res;
    }

    private static boolean isPressed = false;

    static {
        ControllerMapping temp_map = Controllers.getCurrent().getMapping();
//        Map.put(98,"CIRCLE");
//        Map.put(99,"TRIANGLE");
//        Map.put(96,"SQUARE");
//        Map.put(97,"CROSS");
//
        Map.put(temp_map.axisLeftX,"LEFT_AXIS_X");
        Map.put(temp_map.axisLeftY,"LEFT_AXIS_Y");
        Map.put(temp_map.axisRightX,"RIGHT_AXIS_X");
        Map.put(temp_map.axisRightY,"RIGHT_AXIS_Y");
//
//        Map.put(100,"L1");
//        Map.put(101,"R1");
//        Map.put(102,"L2");
//        Map.put(103,"R2");
//
//        Map.put(109,"LEFT_AXIS_BUTTON");
//        Map.put(108,"RIGHT_AXIS_BUTTON");




        Map.put(temp_map.buttonDpadUp,"UP");
        Map.put(temp_map.buttonDpadLeft,"LEFT");
        Map.put(temp_map.buttonDpadRight,"RIGHT");
        Map.put(temp_map.buttonDpadDown,"DOWN");

        Map.put(temp_map.buttonY,"TRIANGLE");
        Map.put(temp_map.buttonX,"SQUARE");
        Map.put(temp_map.buttonB,"CIRCLE");
        Map.put(temp_map.buttonA,"CROSS");

        Map.put(109,"SHARE");
        Map.put(temp_map.buttonStart,"START");
        Map.put(108,"OPTIONS");

//        Map.put(7,"LEFT_AXIS_X");
//        Map.put(6,"LEFT_AXIS_Y");
//        Map.put(3,"RIGHT_AXIS_X");
//        Map.put(2,"RIGHT_AXIS_Y");

        Map.put(temp_map.buttonLeftStick,"LEFT_AXIS_BUTTON");
        Map.put(temp_map.buttonRightStick,"RIGHT_AXIS_BUTTON");

        Map.put(temp_map.buttonL1,"L1");
        Map.put(temp_map.buttonR1,"R1");
        Map.put(temp_map.buttonL2,"L2");
        Map.put(temp_map.buttonR2,"R2");

        Map.put(4,"L2_AXIS");
        Map.put(5,"L2_AXIS");
        Map.put(0,"R2_AXIS");
        Map.put(1,"R2_AXIS");


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
            debug_btn = axisCode;
            EventAxisMap.put(Map.get(axisCode), value);
        }else{
            EventAxisMap.put(Map.get(axisCode), 0f);
        }
        return false;
    }
}
