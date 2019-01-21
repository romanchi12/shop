package org.romanchi.controllers;

import org.romanchi.UserController;

import java.util.HashMap;

import java.util.Map;
import static org.romanchi.Application.getContext;

public class ControllerManager {
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private Map<String,Controller> controllerHashMap = new HashMap<>();
    ControllerManager(){
        controllerHashMap.put("NoController", getContext().getBean(NoController.class));
        controllerHashMap.put("LoginController", getContext().getBean(LoginController.class));
        controllerHashMap.put("LogoutController", getContext().getBean(LogoutController.class));
        controllerHashMap.put("AddProductController", getContext().getBean(AddProductController.class));
        controllerHashMap.put("GetAllCategoriesAjaxController",
                getContext().getBean(GetAllCategoriesAjaxController.class));
        controllerHashMap.put("ImageUploadAjaxController",getContext().getBean(ImageUploadAjaxController.class));
        controllerHashMap.put("AdminPageController",getContext().getBean(AdminPageController.class));
        controllerHashMap.put("UpdateProductAjaxController",
                getContext().getBean(UpdateProductAjaxController.class));
        controllerHashMap.put("DeleteProductAjaxController",
                getContext().getBean(DeleteProductAjaxController.class));
        controllerHashMap.put("AddCategoryController", getContext().getBean(AddCategoryController.class));
        controllerHashMap.put("AdminCategoriesController",
                getContext().getBean(AdminCategoriesController.class));
        controllerHashMap.put("UpdateCategoryAjaxController",
                getContext().getBean(UpdateCategoryAjaxController.class));
        controllerHashMap.put("DeleteCategoryAjaxController",
                getContext().getBean(DeleteCategoryAjaxController.class));
        controllerHashMap.put("GetProductsPageController",
                getContext().getBean(GetProductsPageController.class));
        controllerHashMap.put("GetProductPageController",
                getContext().getBean(GetProductPageController.class));
        controllerHashMap.put("RegistrationController", getContext().getBean(RegistrationController.class));
        controllerHashMap.put("UpdateUserController", getContext().getBean(UpdateUserController.class));
        controllerHashMap.put("GetCartPageController", getContext().getBean(GetCartPageController.class));
        controllerHashMap.put("ConfirmOrderController", getContext().getBean(ConfirmOrderController.class));
        controllerHashMap.put("AddProductToCartAjaxController", getContext().getBean(AddProductToCartAjaxController.class));
        controllerHashMap.put("DeleteProductFromCartAjaxController", getContext().getBean(DeleteProductFromCartAjaxController.class));
        controllerHashMap.put("GetHistoryPageController",
                getContext().getBean(GetHistoryPageController.class));
        controllerHashMap.put("SetSortingOrderAjaxController",
                getContext().getBean(SetSortingOrderAjaxController.class));
        controllerHashMap.put("SearchProductsAjaxController",
                getContext().getBean(SearchProductsAjaxController.class));
    }
    public Controller getController(String controllerName){
        if(controllerHashMap.containsKey(controllerName)){
            return controllerHashMap.get(controllerName);
        }else{
            return controllerHashMap.get("NoController");
        }
    }
}
