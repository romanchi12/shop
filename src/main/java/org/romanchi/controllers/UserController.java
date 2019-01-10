package org.romanchi.controllers;

import org.romanchi.Wired;
import org.romanchi.database.entities.User;
import org.romanchi.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class UserController implements Controller{

    @Wired
    UserService userService;

    @Wired
    private Logger logger;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("data", userService.getData());
        return "/data.jsp";
        /*long userid = Long.valueOf(request.getParameter("userId"));
        logger.info("param: " + userid);
        Optional<User> user = userService.getUserById(userid);
        logger.info(user.toString());
        long usersAmount = userService.getUsersAmount();
        boolean existsById = userService.existById(userid);
        long newuserid = userService.newUser();
        List<User> users = userService.getUsers();
        if(user.isPresent()){
            request.setAttribute("user", user.get());
            request.setAttribute("usersAmount", usersAmount);
            request.setAttribute("existsById", existsById);
            request.setAttribute("newuserid", newuserid);
            request.setAttribute("users", users);
            return "/user.jsp";
        }else{
            request.setAttribute("error", "No such user");
            return "/error.jsp";
        }
*/
    }
}
