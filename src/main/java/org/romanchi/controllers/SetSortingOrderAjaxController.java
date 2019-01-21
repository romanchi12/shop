package org.romanchi.controllers;

import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class SetSortingOrderAjaxController implements Controller {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String sort = StringEscapeUtils.escapeHtml4(request.getParameter("sort"));
        if(!(sort.equals("ID")||sort.equals("PRICE_DESC")||sort.equals("PRICE_ASC"))){
            return "{}";
        }

        request.getSession().setAttribute("sort",sort);
        return "{}";
    }
}
