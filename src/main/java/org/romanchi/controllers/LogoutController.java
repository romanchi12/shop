package org.romanchi.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutController implements Controller {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().invalidate();
        return "/Controller?controller=GetProductsPageController";
    }
}
