package org.romanchi.controllers;

import org.romanchi.Wired;
import org.romanchi.database.dao.TestDao;
import org.romanchi.database.dao.UserDao;
import org.romanchi.services.NewUserService;
import org.romanchi.services.TestService;
import org.romanchi.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NewUserController implements Controller {

    @Wired
    NewUserService newUserService;


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("data", newUserService.getData());
        return "/data.jsp";
    }
}
