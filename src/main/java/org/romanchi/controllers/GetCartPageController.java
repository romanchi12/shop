package org.romanchi.controllers;

import org.romanchi.Wired;
import org.romanchi.database.dto.OrderItemDTO;
import org.romanchi.database.entities.Order;
import org.romanchi.database.entities.User;
import org.romanchi.services.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GetCartPageController implements Controller {

    @Wired
    OrderService orderService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User user = (User)request.getSession().getAttribute("user");
        Order order = orderService.findOpenedOrderByUserId(user.getUserId());
        if(order==null){
            request.setAttribute("orderitemdtos", new ArrayList<>());
            return "/cart.jsp";
        }
        List<OrderItemDTO> orderItemDTOList = orderService.getOrderItemsDTOsByOrderId(order.getOrderId());
        request.setAttribute("orderitemdtos", orderItemDTOList);
        return "/cart.jsp";
    }
}
