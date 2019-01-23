package org.romanchi.controllers;

import org.apache.commons.text.StringEscapeUtils;
import org.romanchi.Wired;
import org.romanchi.database.entities.User;
import org.romanchi.database.entities.UserRole;
import org.romanchi.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.logging.Logger;

public class RegistrationController implements Controller {

    @Wired
    Logger logger;

    @Wired
    UserService userService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String email = StringEscapeUtils.escapeHtml4(request.getParameter("email"));
        String username = StringEscapeUtils.escapeHtml4(request.getParameter("username"));
        String usersurname = StringEscapeUtils.escapeHtml4(request.getParameter("usersurname"));
        String useraddress = StringEscapeUtils.escapeHtml4(request.getParameter("useraddress"));
        String password = StringEscapeUtils.escapeHtml4(request.getParameter("password"));
        String language = StringEscapeUtils.escapeHtml4(request.getParameter("language"));
        logger.finest(email + " " + username + " " + usersurname + " " + useraddress + " " + language);
        User user = new User();
        user.setUserName(username);
        user.setUserSurname(usersurname);
        user.setUserEmail(email);
        user.setUserAddress(useraddress);
        user.setUserLanguage(language);
        user.setUserPassword(password);
        UserRole defaultUserRole = userService.getUserRoleByName("default");
        user.setUserUserRole(defaultUserRole);

        Optional<User> userOptional = userService.register(user);
        if(!userOptional.isPresent()){
            request.setAttribute("errorMessage", "Bad email");
            logger.info("Bad email");
            return "/registration.jsp";
        }
        user.setUserId(userOptional.get().getUserId());
        request.getSession().setAttribute("user", user);
        return "/Controller?controller=GetProductsPageController";

    }
}
