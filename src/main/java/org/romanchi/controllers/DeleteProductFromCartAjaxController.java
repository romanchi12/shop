package org.romanchi.controllers;

import org.apache.commons.text.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.romanchi.Wired;
import org.romanchi.database.entities.*;
import org.romanchi.services.OrderService;
import org.romanchi.services.ProductService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class DeleteProductFromCartAjaxController implements Controller {

    @Wired
    OrderService orderService;
    @Wired
    ProductService productService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> respMap = new HashMap<>();
        if(request.getParameter("productId")==null){
            respMap.put("status","error");
            respMap.put("errorMessage", "Bad product id");
            return new JSONObject(respMap).toJSONString();
        }
        long productId = 0;
        try {
            productId = Long.valueOf(StringEscapeUtils.escapeHtml4(request.getParameter("productId")));
        } catch (NumberFormatException e) {
            respMap.put("status","error");
            respMap.put("errorMessage", "Bad product id");
            return new JSONObject(respMap).toJSONString();
        }
        User user = (User) request.getSession().getAttribute("user");
        Order order = orderService.findOpenedOrderByUserId(user.getUserId());
        if(order==null){
            respMap.put("status","error");
            respMap.put("errorMessage", "No orders opened");
            return new JSONObject(respMap).toJSONString();
        }
        Product product = productService.getProductById(productId);
        OrderItem orderItem = orderService.findOrderItemByProductAndOrder(product,order);
        int resultCode = orderService.deleteOrderItem(orderItem);
        if(resultCode==1){
            WarehouseItem warehouseItem = product.getWarehouseItem();
            warehouseItem.setWarehouseItemQuantity(warehouseItem.getWarehouseItemQuantity()
                    +orderItem.getOrderItemQuantity());
            order.setSummaryPrice(order.getSummaryPrice()-product.getProductPrice()*orderItem.getOrderItemQuantity());
            orderService.saveOrder(order);
            productService.saveWarehouseItem(warehouseItem);
            respMap.put("status","ok");
            respMap.put("successMessage","Deleted");
            return new JSONObject(respMap).toJSONString();
        }else {
            respMap.put("status","error");
            respMap.put("errorMessage", "No such order item");
            return new JSONObject(respMap).toJSONString();
        }
    }
}
