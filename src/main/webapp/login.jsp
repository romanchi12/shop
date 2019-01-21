<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    if(session.getAttribute("user")!=null){
        response.sendRedirect("/profile.jsp");
        return;
    }
%>
<html>
<head>
    <%@include file="WEB-INF/jspf/includes.jspx"%>
    <title><fmt:message key="Login"/></title>
</head>
<body>
    <div class="page">
        <%@include file="WEB-INF/jspf/navbar.jspx"%>
        <div class="row">
            <div class="hidden-xs col-md-2 col-lg-3">

            </div>
            <div class="col-xs-12 col-md-8 col-lg-6">
                <form method="POST" action="/Controller?controller=LoginController" class="form-signin">
                    <h2 class="form-heading"><fmt:message key="Login" var="LoginMsg"/>${LoginMsg}</h2>

                    <div class="form-group">
                        <input name="email" type="text" class="form-control" placeholder="Email"
                               autofocus="true"/>
                        <br/>
                        <fmt:message key="Password" var="PasswordMsg"/>
                        <input name="password" type="password" class="form-control" placeholder="${PasswordMsg}"/>
                        <br/>
                        <button class="btn btn-lg btn-primary btn-block" type="submit"><fmt:message key="Login"/></button>
                        <br/>
                        <h4 class="text-center"><a href="/registration.jsp"><fmt:message key="CreateAnAccount"/></a></h4>
                    </div>
                </form>
            </div>
            <div class="hidden-xs col-md-2 col-lg-3">

            </div>
        </div>
    </div>
</body>
</html>
