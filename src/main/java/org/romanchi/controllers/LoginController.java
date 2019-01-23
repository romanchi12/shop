package org.romanchi.controllers;

import org.apache.commons.text.StringEscapeUtils;
import org.romanchi.Wired;
import org.romanchi.database.entities.User;
import org.romanchi.services.UserService;
import sun.security.provider.MD5;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.logging.Logger;

public class LoginController implements Controller {
    @Wired
    Logger logger;

    @Wired
    UserService userService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String email = StringEscapeUtils.escapeHtml4(request.getParameter("email"));
        String password = StringEscapeUtils.escapeHtml4(request.getParameter("password"));
        logger.finest(email + " " /* + password  (may cause security problems)*/);
        Optional<User> userToLoginOptional = userService.login(email, password);
        if(userToLoginOptional.isPresent()){
            User userToLogin = userToLoginOptional.get();
            request.getSession().setAttribute("user",userToLogin);
            request.setAttribute("user", userToLogin);
            return "/Controller?controller=GetProductsPageController";
        }
        request.setAttribute("errorMessage","Bad email or password");
        logger.info("Bad email or password");
        return "/login.jsp";
    }
}
