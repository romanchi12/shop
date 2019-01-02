package org.romanchi.controllers;

import org.romanchi.Wired;
import org.romanchi.services.TestService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestController implements Controller {

    @Wired
    TestService testService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("data", testService.getData());
        return "data.jsp";
    }
}
