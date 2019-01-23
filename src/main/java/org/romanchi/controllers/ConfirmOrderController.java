package org.romanchi.controllers;

import org.romanchi.Wired;
import org.romanchi.database.OrderStatus;
import org.romanchi.database.entities.Order;
import org.romanchi.database.entities.User;
import org.romanchi.services.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public class ConfirmOrderController implements Controller {

    @Wired
    Logger logger;

    @Wired
    OrderService orderService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User user = (User)request.getSession().getAttribute("user");
        Order order = orderService.findOpenedOrderByUserId(user.getUserId());
        if(order==null){
            return "/Controller?controller=GetProductsPageController";
        }
        order.setOrderStatus(OrderStatus.CLOSED);
        long orderId = orderService.saveOrder(order);
        request.setAttribute("orderId", orderId);
        String completed = orderService.sendEmail(user, order.getOrderId());
        logger.finest(completed);
        request.setAttribute("completed", completed);
        return "/afterconfirm.jsp";
    }
}
