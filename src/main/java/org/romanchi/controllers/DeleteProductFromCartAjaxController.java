package org.romanchi.controllers;

import org.apache.commons.text.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.romanchi.Message;
import org.romanchi.Wired;
import org.romanchi.database.entities.*;
import org.romanchi.services.OrderService;
import org.romanchi.services.ProductService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class DeleteProductFromCartAjaxController implements Controller {

    @Wired
    Logger logger;

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
            logger.info("Bad product id");
            return new JSONObject(respMap).toJSONString();
        }
        long productId = 0;
        try {
            productId = Long.valueOf(StringEscapeUtils.escapeHtml4(request.getParameter("productId")));
            logger.finest(String.valueOf(productId));
        } catch (NumberFormatException e) {
            respMap.put("status","error");
            respMap.put("errorMessage", Message.BAD_PARAMETERS.getLocalized(request));
            logger.info("Bad product id");
            return new JSONObject(respMap).toJSONString();
        }
        User user = (User) request.getSession().getAttribute("user");
        Order order = orderService.findOpenedOrderByUserId(user.getUserId());
        if(order==null){
            respMap.put("status","error");
            respMap.put("errorMessage", Message.NO_ORDERS_OPENED.getLocalized(request));
            logger.info("No orders opened");
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
            respMap.put("successMessage", Message.DELETED.getLocalized(request));
            return new JSONObject(respMap).toJSONString();
        }else {
            respMap.put("status","error");
            respMap.put("errorMessage", Message.NO_ORDER_ITEM.getLocalized(request));
            logger.info("No such order item");
            return new JSONObject(respMap).toJSONString();
        }
    }
}
