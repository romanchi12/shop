package org.romanchi.controllers;

import org.romanchi.Application;
import org.romanchi.Wired;
import sun.rmi.runtime.Log;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

public class DispatcherServlet extends HttpServlet {

    private final static Logger logger = Application.getContext().getBean(Logger.class);

    private ControllerManager controllerManager = new ControllerManager();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String controller = request.getParameter("controller");
        logger.info(controller);
        String path = controllerManager.getController(controller).execute(request, response);
        logger.info(path);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
        requestDispatcher.forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
