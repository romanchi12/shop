package org.romanchi.controllers;

import org.romanchi.Wired;
import org.romanchi.database.entities.User;
import org.romanchi.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserController implements Controller{

    @Wired
    UserService userService;


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User user = userService.getUserById(1);
        if(user != null){
            request.setAttribute("user", user.toString());
            return "/user.jsp";
        }else{
            request.setAttribute("error", "No such user");
            return "/error.jsp";
        }
    }
}
