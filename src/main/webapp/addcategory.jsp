<%@ page import="org.romanchi.database.entities.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    if(session.getAttribute("user")==null){
        response.sendRedirect("/login.jsp");
        return;
    }
    if(!((User)session.getAttribute("user")).getUserUserRole().getUserRoleName().equals("admin")){
        response.sendRedirect("/profile.jsp");
        return;
    }
%>
<html>
<head>
    <%@include file="WEB-INF/jspf/includes.jspx"%>
    <title><fmt:message key="ACat"/> | Admin page</title>
</head>
<body>

<div class="page">
    <%@include file="WEB-INF/jspf/navbar.jspx"%>
    <div class="row">
        <div class="hidden-xs col-md-2 col-lg-3">

        </div>
        <div class="col-xs-12 col-md-8 col-lg-6">
            <form method="post" action="/Controller?controller=AddCategoryController">

                <div class="input-group">
                    <span class="input-group-addon" id="basic-addon1"><fmt:message key="CategoryName" var="categoryNameMsg"/>${categoryNameMsg}</span>
                    <input name="categoryname" type="text" class="form-control" placeholder="${categoryNameMsg}" aria-describedby="basic-addon1"/>
                </div>
                <br/>
                <button type="submit" class="btn btn-success"><fmt:message key="ACat"/></button>
            </form>
        </div>
        <div class="hidden-xs col-md-2 col-lg-3">

        </div>
    </div>
</div>
</body>
</html>
