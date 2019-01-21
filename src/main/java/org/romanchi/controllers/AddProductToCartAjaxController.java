package org.romanchi.controllers;

import org.apache.commons.text.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.romanchi.Wired;
import org.romanchi.database.OrderStatus;
import org.romanchi.database.entities.*;
import org.romanchi.services.OrderService;
import org.romanchi.services.ProductService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class AddProductToCartAjaxController implements Controller {

    @Wired
    Logger logger;

    @Wired
    ProductService productService;

    @Wired
    OrderService orderService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> respMap = new HashMap<>();
        if(request.getParameter("productId")==null){
            respMap.put("status","error");
            respMap.put("errorMessage","Bad product id");
            return new JSONObject(respMap).toJSONString();
        }

        Double productQuantity = null;
        long productId = 0;
        try {
            productQuantity = (request.getParameter("productQuantity")==null)? Double.valueOf(1)
                    :Double.valueOf(StringEscapeUtils.escapeHtml4(request.getParameter("productQuantity")));
            productId = Long.valueOf(StringEscapeUtils.escapeHtml4(request.getParameter("productId")));
            if(productQuantity<=0){
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            respMap.put("status","error");
            respMap.put("errorMessage","Bad parameters");
            return new JSONObject(respMap).toJSONString();
        }

        Product product = productService.getProductById(productId);
        if(product==null){
            respMap.put("status","error");
            respMap.put("errorMessage", "Bad product id");
            return new JSONObject(respMap).toJSONString();
        }
        WarehouseItem warehouseItem = product.getWarehouseItem();
        if(warehouseItem.getWarehouseItemQuantity()<productQuantity){
            respMap.put("status","error");
            respMap.put("errorMessage", "Not enough products in stock");
            return new JSONObject(respMap).toJSONString();
        }

        warehouseItem.setWarehouseItemQuantity(warehouseItem.getWarehouseItemQuantity()-productQuantity);
        productService.saveWarehouseItem(warehouseItem);
        User user = (User)request.getSession().getAttribute("user");
        Order order = orderService.findOpenedOrderByUserId(user.getUserId());
        if(order!=null){
            //update
            order.setSummaryPrice(order.getSummaryPrice()+product.getProductPrice()*productQuantity);

            OrderItem orderItem = orderService.findOrderItemByProductAndOrder(product,order);
            logger.info(String.valueOf(orderItem));
            if(orderItem==null){
                orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setOrderItemQuantity(productQuantity);
            }else{
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setOrderItemQuantity(orderItem.getOrderItemQuantity()+productQuantity);
            }
            orderService.saveOrder(order);
            orderService.saveOrderItem(orderItem);
        }else{
            //create order
            order = new Order();
            order.setUser(user);
            order.setOrderStatus(OrderStatus.OPENED);
            order.setSummaryPrice(product.getProductPrice()*productQuantity);
            long orderId = orderService.saveOrder(order);
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemQuantity(productQuantity);
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderService.saveOrder(order);
            orderService.saveOrderItem(orderItem);
        }

        productService.saveWarehouseItem(warehouseItem);
        respMap.put("status","ok");
        respMap.put("successMessage","Added");
        String json = new JSONObject(respMap).toJSONString();
        return json;
    }
}
