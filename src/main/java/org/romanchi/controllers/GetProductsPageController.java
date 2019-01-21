package org.romanchi.controllers;

import org.apache.commons.text.StringEscapeUtils;
import org.romanchi.Wired;
import org.romanchi.database.dto.ProductDTO;
import org.romanchi.database.entities.Category;
import org.romanchi.services.ProductService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.logging.Logger;

public class GetProductsPageController implements Controller {

    @Wired
    ProductService productService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Integer page = null;
        Long categoryId = null;
        String sort = request.getSession().getAttribute("sort")==null? "ID":(String)request.getSession().getAttribute("sort");

        try {
            page = request.getParameter("page")==null?
                    new Integer(0):
                    Integer.valueOf(StringEscapeUtils.escapeHtml4(request.getParameter("page")));
            categoryId = request.getParameter("categoryId")==null?
                    Long.valueOf(-1):
                    Long.valueOf(StringEscapeUtils.escapeHtml4(request.getParameter("categoryId")));
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage","Bad parameters");
            return "/error.jsp";
        }
        request.setAttribute("page",page);
        request.setAttribute("categoryId", categoryId);
        List<ProductDTO> productDTOS = null;
        if(categoryId.equals(Long.valueOf(-1))){
            productDTOS = productService.getProductDTOs(page, sort);
        }else{
            productDTOS = productService.getProductDTOs(page, categoryId, sort);
        }
        request.setAttribute("products", productDTOS);
        List<Category> categories = productService.getAllCategories();
        request.setAttribute("categories", categories);
        return "/products.jsp";
    }
}
