package org.romanchi.controllers;

import org.apache.commons.text.StringEscapeUtils;
import org.romanchi.Wired;
import org.romanchi.database.entities.User;
import org.romanchi.database.entities.UserRole;
import org.romanchi.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegistrationController implements Controller {

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
        User user = new User();
        user.setUserName(username);
        user.setUserSurname(usersurname);
        user.setUserEmail(email);
        user.setUserAddress(useraddress);
        user.setUserLanguage(language);
        user.setUserPassword(password);
        UserRole defaultUserRole = userService.getUserRoleByName("default");
        user.setUserUserRole(defaultUserRole);

        long userId = userService.saveUser(user);
        user.setUserId(userId);
        request.getSession().setAttribute("user", user);
        return "/Controller?controller=GetProductsPageController";

    }
}
