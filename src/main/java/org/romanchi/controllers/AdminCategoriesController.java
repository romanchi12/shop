package org.romanchi.controllers;

import org.apache.commons.text.StringEscapeUtils;
import org.romanchi.Wired;
import org.romanchi.database.dto.ProductDTO;
import org.romanchi.database.entities.Category;
import org.romanchi.database.entities.Product;
import org.romanchi.services.ProductService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class AdminCategoriesController implements Controller {

    @Wired
    ProductService productService;
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        try{
            Integer page = request.getParameter("page")==null?
                    new Integer(0):
                    Integer.valueOf(StringEscapeUtils.escapeHtml4(request.getParameter("page")));
            request.setAttribute("page", page);
            List<Category> categories = productService.getCategories(page);
            request.setAttribute("categories", categories);
            return "/admincategories.jsp";
        }catch (NumberFormatException ex){
            request.setAttribute("errorMessage", "Bad parameters");
            return "/error.jsp";
        }
    }
}
