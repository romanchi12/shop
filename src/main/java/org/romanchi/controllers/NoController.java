package org.romanchi.controllers;

import org.romanchi.Wired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public class NoController implements Controller{
    @Wired
    Logger logger;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return "error.jsp";
    }
}
