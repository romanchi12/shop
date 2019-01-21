<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    if(request.getSession().getAttribute("user")==null){
        response.sendRedirect("/login.jsp");
        return;
    }
%>
<html>
<head>
    <%@include file="WEB-INF/jspf/includes.jspx"%>
    <title><fmt:message key="Success"/></title>
</head>
<body>
<div class="page">
    <%@include file="WEB-INF/jspf/navbar.jspx"%>
    <div class="row">
        <div class="hidden-xs col-md-2 col-lg-3">

        </div>
        <div class="col-xs-12 col-md-8 col-lg-6">
            <div class="panel panel-default">
                <div class="panel-body">
                    ${completed}
                </div>
            </div>
        </div>
        <div class="hidden-xs col-md-2 col-lg-3">

        </div>
    </div>
</div>

</body>
</html>
