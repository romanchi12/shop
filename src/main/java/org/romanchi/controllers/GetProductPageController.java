package org.romanchi.controllers;

import org.apache.commons.text.StringEscapeUtils;
import org.romanchi.Wired;
import org.romanchi.database.entities.Product;
import org.romanchi.services.ProductService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebEndpoint;
import java.util.logging.Logger;

public class GetProductPageController implements Controller {

    @Wired
    Logger logger;

    @Wired
    ProductService productService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        long productId = 0;
        try {
            productId = (request.getParameter("productId"))==null?-1:
                    Long.valueOf(StringEscapeUtils.escapeHtml4(request.getParameter("productId")));
            logger.finest(String.valueOf(productId));
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage","Bad parameters");
            logger.info("Bad parameters");
            return "/error.jsp";
        }
        Product product = productService.getProductById(productId);
        if(product==null){
            return "/Controller?controller=GetProductsPageController";
        }
        request.setAttribute("product", product);
        return "/product.jsp";
    }
}
