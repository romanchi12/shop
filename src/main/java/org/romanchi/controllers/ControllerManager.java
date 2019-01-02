package org.romanchi.controllers;

import java.util.HashMap;

import java.util.Map;
import static org.romanchi.Application.getContext;

public class ControllerManager {
    private Map<String,Controller> controllerHashMap = new HashMap<>();
    ControllerManager(){
        controllerHashMap.put("NoController", getContext().getBean(NoController.class));
        controllerHashMap.put("TestController",getContext().getBean(TestController.class));
        controllerHashMap.put("UserController", getContext().getBean(UserController.class));
    }
    public Controller getController(String controllerName){
        if(controllerHashMap.containsKey(controllerName)){
            return controllerHashMap.get(controllerName);
        }else{
            return controllerHashMap.get("NoController");
        }
    }
}
