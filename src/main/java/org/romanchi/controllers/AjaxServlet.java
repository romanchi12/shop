package org.romanchi.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class AjaxServlet extends HttpServlet {
    ControllerManager controllerManager = new ControllerManager();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String controller = req.getParameter("controller");
        String json = controllerManager.getController(controller).execute(req, resp);
        Logger.getLogger("").info(json);
        Writer writer = new PrintWriter(new OutputStreamWriter(
                resp.getOutputStream(), StandardCharsets.UTF_16), true);
        writer.write(json);
        writer.close();
        resp.setHeader("Content-Type", "application/json");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        doGet(req, resp);
    }
}
