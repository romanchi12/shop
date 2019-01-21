package org.romanchi.controllers;

import org.apache.commons.text.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.romanchi.Wired;
import org.romanchi.database.entities.Category;
import org.romanchi.services.ProductService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class UpdateCategoryAjaxController implements Controller {

    @Wired
    ProductService productService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> respMap = new HashMap<>();

        long categoryId = 0;
        try {
            categoryId = Long.parseLong(StringEscapeUtils.escapeHtml4(request.getParameter("categoryId")));
        } catch (NumberFormatException e) {
            respMap.put("status","error");
            respMap.put("errorMessage","Bad parameters");
            return new JSONObject(respMap).toJSONString();
        }

        String categoryName = StringEscapeUtils.escapeHtml4(request.getParameter("categoryName"));
        Category category = new Category();
        category.setCategoryId(categoryId);
        category.setCategoryName(categoryName);
        productService.saveCategory(category);
        respMap.put("status","ok");
        respMap.put("successMessage","Saved");
        return new JSONObject(respMap).toJSONString();
    }
}
