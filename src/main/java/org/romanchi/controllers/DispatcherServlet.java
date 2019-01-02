package org.romanchi.controllers;

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

    private final static Logger logger = Logger.getLogger(DispatcherServlet.class.getName());

    ControllerManager controllerManager = new ControllerManager();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String controller = request.getParameter("controller");
        logger.info(controller);
        String path = controllerManager.getController(controller).execute(request, response);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
        requestDispatcher.forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
