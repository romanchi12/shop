package org.romanchi.controllers;

import org.romanchi.Wired;
import org.romanchi.database.dto.OrderItemDTO;
import org.romanchi.database.entities.Order;
import org.romanchi.database.entities.OrderItem;
import org.romanchi.database.entities.User;
import org.romanchi.services.OrderService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class GetHistoryPageController implements Controller{

    @Wired
    OrderService orderService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        User user =(User) request.getSession().getAttribute("user");
        List<Order> orders = orderService.findOrdersByUser(user);
        Comparator<Order> orderComparator = new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) { //DESC
                if(o1.getOrderId()<o2.getOrderId()){
                    return 1;
                }else if(o1.getOrderId()==o2.getOrderId()){
                    return 0;
                }else{
                    return -1;
                }
            }
        };
        Map<Order, List<OrderItemDTO>> orderListMap = new TreeMap<>(orderComparator);
        orders.forEach(order -> {
            List<OrderItemDTO> orderItemDTOList = orderService.
                    getOrderItemsDTOsByOrderId(order.getOrderId());
            orderListMap.put(order,orderItemDTOList);
        });
        request.setAttribute("ordersMap", orderListMap);
        return "/history.jsp";
    }
}
