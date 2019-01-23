package org.romanchi.controllers;

import org.apache.commons.text.StringEscapeUtils;
import org.romanchi.Wired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.logging.Logger;

public class SetSortingOrderAjaxController implements Controller {

    @Wired
    Logger logger;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String sort = StringEscapeUtils.escapeHtml4(request.getParameter("sort"));
        logger.finest(sort);
        if(!(sort.equals("ID")||sort.equals("PRICE_DESC")||sort.equals("PRICE_ASC"))){
            return "{}";
        }

        request.getSession().setAttribute("sort",sort);
        return "{}";
    }
}
