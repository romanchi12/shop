package org.romanchi.controllers;

import org.apache.commons.text.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.romanchi.Message;
import org.romanchi.Wired;
import org.romanchi.database.entities.Product;
import org.romanchi.database.entities.WarehouseItem;
import org.romanchi.services.ProductService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class UpdateProductAjaxController implements Controller {

    @Wired
    Logger logger;

    @Wired
    ProductService productService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> respMap = new HashMap<>();
        long productId = 0;
        Double productPrice = null;
        Integer productQuantity = null;
        try {
            productId = Long.valueOf(StringEscapeUtils.escapeHtml4(request.getParameter("productId")));
            productPrice = Double.valueOf(StringEscapeUtils.escapeHtml4(request.getParameter("productPrice")));
            productQuantity = Integer.valueOf(StringEscapeUtils.escapeHtml4(request.getParameter("productQuantity")));
        } catch (NumberFormatException e) {
            respMap.put("status","error");
            respMap.put("errorMessage", Message.BAD_PARAMETERS.getLocalized(request));
            logger.info("Bad parameters");
            return new JSONObject(respMap).toJSONString();
        }

        String productName = StringEscapeUtils.escapeHtml4(request.getParameter("productName"));
        String productDecsription = StringEscapeUtils.
                escapeHtml4(request.getParameter("productDecsription"));
        logger.finest(productId + " "
                + productPrice + " " + productQuantity + " " + productName + " " + productDecsription);
        Product product = productService.getProductById(productId);
        if(product==null){
            respMap.put("status","error");
            respMap.put("errorMessage", Message.NO_SUCH_PRODUCT.getLocalized(request));
            logger.info("No such product");
            return new JSONObject(respMap).toJSONString();
        }
        product.setProductPrice(productPrice);
        product.setProductName(productName);
        product.setProductDescription(productDecsription);
        productService.saveProduct(product);
        WarehouseItem warehouseItem = product.getWarehouseItem();
        warehouseItem.setWarehouseItemQuantity(productQuantity);
        productService.saveWarehouseItem(warehouseItem);
        respMap.put("status","ok");
        respMap.put("successMessage",Message.SAVED.getLocalized(request));
        return new JSONObject(respMap).toJSONString();
    }
}
