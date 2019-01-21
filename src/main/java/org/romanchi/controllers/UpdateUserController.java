package org.romanchi.controllers;

import org.apache.commons.text.StringEscapeUtils;
import org.romanchi.Wired;
import org.romanchi.database.entities.User;
import org.romanchi.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateUserController implements Controller {

    @Wired
    UserService userService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User user = (User)request.getSession().getAttribute("user");
        String email = StringEscapeUtils.escapeHtml4(request.getParameter("useremail"));
        String username = StringEscapeUtils.escapeHtml4(request.getParameter("username"));
        String usersurname = StringEscapeUtils.escapeHtml4(request.getParameter("usersurname"));
        String useraddress = StringEscapeUtils.escapeHtml4(request.getParameter("useraddress"));
        String password = StringEscapeUtils.escapeHtml4(request.getParameter("userpassword"));
        String language = StringEscapeUtils.escapeHtml4(request.getParameter("userlanguage"));
        user.setUserPassword(password);
        user.setUserAddress(useraddress);
        user.setUserLanguage(language);
        user.setUserEmail(email);
        user.setUserName(username);
        user.setUserSurname(usersurname);
        userService.saveUser(user);
        request.getSession().setAttribute("user",user);
        return "/Controller?controller=GetProductsPageController";
    }
}
