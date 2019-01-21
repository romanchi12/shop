package org.romanchi.filters;

import org.apache.commons.text.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.romanchi.database.entities.User;
import org.romanchi.database.entities.UserRole;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class AccessFilter implements Filter {

    private static final Logger logger = Logger.getLogger(AccessFilter.class.getName());

    List<String> publicControllers = new ArrayList<>();
    List<String> defaultUserControllers = new ArrayList<>();
    List<String> adminControllers = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        publicControllers.add("GetProductsPageController");
        publicControllers.add("GetProductPageController");
        publicControllers.add("LoginController");
        publicControllers.add("GetAllCategoriesAjaxController");
        publicControllers.add("RegistrationController");
        publicControllers.add("SetSortingOrderAjaxController");
        publicControllers.add("SearchProductsAjaxController");

        defaultUserControllers.add("LogoutController");
        defaultUserControllers.add("UpdateUserController");
        defaultUserControllers.add("GetCartPageController");
        defaultUserControllers.add("ConfirmOrderController");
        defaultUserControllers.add("AddProductToCartAjaxController");
        defaultUserControllers.add("DeleteProductFromCartAjaxController");
        defaultUserControllers.add("GetHistoryPageController");

        adminControllers.add("AddCategoryController");
        adminControllers.add("AddProductController");
        adminControllers.add("AdminCategoriesController");
        adminControllers.add("AdminPageController");
        adminControllers.add("DeleteCategoryAjaxController");
        adminControllers.add("DeleteProductAjaxController");
        adminControllers.add("ImageUploadAjaxController");
        adminControllers.add("UpdateCategoryAjaxController");
        adminControllers.add("UpdateProductAjaxController");

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        String controllerParameter = servletRequest.getParameter("controller");
        if(controllerParameter==null){
            if(((HttpServletRequest) servletRequest).getRequestURI().contains("Ajax")){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("status","error");
                jsonObject.put("errorMessage", "No such controller");
                response.setHeader("Content-Type","application/json");
                response.getOutputStream().print(jsonObject.toJSONString());
                response.getOutputStream().flush();
                return;
            }
            response.sendRedirect("/error.jsp");
            return;
        }
        String controller = StringEscapeUtils.escapeHtml4(controllerParameter);
        if(request.getSession().getAttribute("user")==null){
            if(defaultUserControllers.contains(controller)||adminControllers.contains(controller)){
                if(((HttpServletRequest)servletRequest).getRequestURI().contains("Ajax")){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("status","error");
                    jsonObject.put("errorMessage", "You need to login");
                    response.setHeader("Content-Type","application/json");
                    response.getOutputStream().print(jsonObject.toJSONString());
                    response.getOutputStream().flush();
                    return;
                }
                response.sendRedirect("/login.jsp");
                return;
            }else{
                filterChain.doFilter(servletRequest,servletResponse);
            }
        }else{
            User user = (User) request.getSession().getAttribute("user");
            UserRole userRole = user.getUserUserRole();
            if(userRole==null){
                logger.severe("UserRole == null");
                response.sendError(500);
            }
            if(!"admin".equals(userRole.getUserRoleName())){
                //not admin
                if(adminControllers.contains(controller)){
                    response.sendRedirect("/Controller?controller=GetProductsPageController");
                    return;
                }else{
                    filterChain.doFilter(servletRequest,servletResponse);
                }
            }else{
                //admin
                filterChain.doFilter(servletRequest,servletResponse);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
