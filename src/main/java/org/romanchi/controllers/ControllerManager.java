package org.romanchi.controllers;

import java.util.HashMap;

import java.util.Map;
import static org.romanchi.Application.getContext;

public class ControllerManager {
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private Map<String,Controller> controllerHashMap = new HashMap<>();
    ControllerManager(){
        controllerHashMap.put("NoController", getContext().getBean(NoController.class));
        controllerHashMap.put("UserController", getContext().getBean(UserController.class));
        controllerHashMap.put("TestController",getContext().getBean(TestController.class));
        controllerHashMap.put("NewUserController", getContext().getBean(NewUserController.class));
    }
    public Controller getController(String controllerName){
        if(controllerHashMap.containsKey(controllerName)){
            return controllerHashMap.get(controllerName);
        }else{
            return controllerHashMap.get("NoController");
        }
    }
}
