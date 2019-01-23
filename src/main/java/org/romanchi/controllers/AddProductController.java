package org.romanchi.controllers;

import org.apache.commons.text.StringEscapeUtils;
import org.romanchi.Message;
import org.romanchi.Wired;
import org.romanchi.database.entities.Category;
import org.romanchi.database.entities.Product;
import org.romanchi.database.entities.WarehouseItem;
import org.romanchi.services.ProductService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public class AddProductController implements Controller {

    @Wired
    Logger logger;

    @Wired
    ProductService productService;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {

        try{
            String productName = StringEscapeUtils.escapeHtml4(request.getParameter("productname"));
            Integer productQuantity = Integer.
                    valueOf(StringEscapeUtils.escapeHtml4(request.getParameter("productquantity")));
            String productDescribe = StringEscapeUtils.escapeHtml4(request.getParameter("productdescribe"));
            Double productPrice = Double.
                    valueOf(StringEscapeUtils.escapeHtml4(request.getParameter("productprice")));
            Integer categoryId = Integer.
                    valueOf(StringEscapeUtils.escapeHtml4(request.getParameter("categorySelection")));
            String productImageSrc = StringEscapeUtils.escapeHtml4(request.getParameter("productimagesrc"));

            logger.finest(productName + " " + productQuantity + " " + productDescribe + " " + productPrice + " " + categoryId + " " + productImageSrc);

            WarehouseItem warehouseItem = new WarehouseItem();
            warehouseItem.setWarehouseItemQuantity(productQuantity);
            long warehouseItemId = productService.saveWarehouseItem(warehouseItem);
            warehouseItem.setWarehouseItemId(warehouseItemId);

            Category category = productService.getCategoryById(categoryId);

            if(category==null){
                logger.info("No such category exist");
                request.setAttribute("errorMessage",Message.NO_SUCH_CATEGORY_EXIST.getLocalized(request));
                return "/error.jsp";
            }

            Product product = new Product();
            product.setWarehouseItem(warehouseItem);
            product.setCategory(category);
            product.setProductName(productName);
            product.setProductDescription(productDescribe);
            product.setProductPrice(productPrice);
            product.setProductImgSrc(productImageSrc);

            long productId = productService.saveProduct(product);

            return "/Controller?controller=AdminPageController&page=0";
        }catch (NumberFormatException ex){
            logger.info("Bad parameters");
            request.setAttribute("errorMessage", Message.BAD_PARAMETERS.getLocalized(request));
            return "/error.jsp";
        }
    }

}
