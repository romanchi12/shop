package org.romanchi.controllers;

import org.apache.commons.text.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.romanchi.Wired;
import org.romanchi.database.entities.Product;
import org.romanchi.services.ProductService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.logging.Logger;

public class SearchProductsAjaxController implements Controller {

    @Wired
    Logger logger;

    @Wired
    ProductService productService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String query = StringEscapeUtils.escapeHtml4(request.getParameter("query"));
        logger.finest(query);
        List<Product> productList = productService.search(query);
        JSONArray jsonArray = new JSONArray();
        productList.forEach(product -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("productId", product.getProductId());
            jsonObject.put("productName", product.getProductName());
            jsonObject.put("productImgSrc", product.getProductImgSrc());
            jsonArray.add(jsonObject);
        });
        return jsonArray.toJSONString();
    }
}
