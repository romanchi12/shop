package org.romanchi.controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.romanchi.Wired;
import org.romanchi.database.entities.Category;
import org.romanchi.services.CategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GetAllCategoriesAjaxController implements Controller {
    @Wired
    CategoryService categoryService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        List<Category> categories = categoryService.getAllCategories();
        List<JSONObject> categoriesJsonObjects = new ArrayList<>();
        categories.forEach(category -> categoriesJsonObjects.add(new JSONObject(category.toMap())));
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(categoriesJsonObjects);
        return jsonArray.toJSONString();
    }
}
