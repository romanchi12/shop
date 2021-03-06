package org.romanchi.controllers;

import org.apache.commons.text.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.romanchi.Message;
import org.romanchi.Wired;
import org.romanchi.database.entities.Product;
import org.romanchi.services.ProductService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class DeleteProductAjaxController implements Controller {

    @Wired
    Logger logger;

    @Wired
    ProductService productService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> respMap = new HashMap<>();
        long productId = 0;
        try {
            productId = Long.
                    parseLong(StringEscapeUtils.escapeHtml4(request.getParameter("productId")));
            logger.finest(String.valueOf(productId));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            respMap.put("status", "error");
            respMap.put("errorMessage", Message.BAD_PARAMETERS.getLocalized(request));
            logger.info("Bad product id");
            return new JSONObject(respMap).toJSONString();
        }
        Product product = new Product();
        product.setProductId(productId);
        productService.deleteProduct(product);
        respMap.put("status","ok");
        respMap.put("successMessage",Message.DELETED.getLocalized(request));
        return new JSONObject(respMap).toJSONString();
    }
}
