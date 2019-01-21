package org.romanchi.controllers;

import org.apache.commons.text.StringEscapeUtils;
import org.romanchi.Wired;
import org.romanchi.database.entities.Category;
import org.romanchi.services.ProductService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddCategoryController implements Controller {

    @Wired
    ProductService productService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String categoryName = StringEscapeUtils.escapeHtml4(request.getParameter("categoryname"));
        Category category = new Category();
        category.setCategoryName(categoryName);
        productService.saveCategory(category);
        return "/Controller?controller=AdminCategoriesController";
    }
}
