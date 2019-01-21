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
    <title><fmt:message key="Registration"/></title>
</head>
<body>
<div class="page">
    <%@include file="WEB-INF/jspf/navbar.jspx"%>
    <div class="row">
        <div class="hidden-xs col-md-2 col-lg-3">

        </div>
        <div class="col-xs-12 col-md-8 col-lg-6">
            <form method="POST" action="/Controller?controller=RegistrationController" class="form-signin">
                <h2 class="form-heading">Registration</h2>
                <fmt:message key="Name" var="NameMsg"/>
                <fmt:message key="Surname" var="SurnameMsg"/>
                <fmt:message key="Address" var="AddressMsg"/>
                <fmt:message key="Password" var="PasswordMsg"/>
                <fmt:message key="Language" var="LanguageMsg"/>

                <div class="form-group">
                    <h2 class="form-heading"><fmt:message key="Registration"/></h2>
                    <div class="alert alert-danger" style="${errorMessage==null?'display:none;':'display:block'}" role="alert">${errorMessage}</div>
                    <br/>
                    <input name="email" type="text" class="form-control" placeholder="Email"
                           autofocus="true"/>
                    <br/>
                    <input name="username" type="text" class="form-control" placeholder="${NameMsg}"/>
                    <br/>
                    <input name="usersurname" type="text" class="form-control" placeholder="${SurnameMsg}"/>
                    <br/>
                    <input name="useraddress" type="text" class="form-control" placeholder="${AddressMsg}"/>
                    <br/>
                    <input name="password" type="password" class="form-control" placeholder="${PasswordMsg}"/>
                    <br/>
                    <div class="form-group">
                        <label for="languageSelection">${LanguageMsg}:</label>
                        <select class="form-control" id="languageSelection" name="language">
                            <option value="en_GB">English</option>
                            <option value="uk_UA">Українська</option>
                            <option value="ru_RU">Русский</option>
                        </select>
                    </div>
                    <br/>
                    <button class="btn btn-lg btn-primary btn-block" type="submit"><fmt:message key="Registration"/></button>
                    <br/>
                    <h4 class="text-center"><a href="/login.jsp"><fmt:message key="Login"/></a></h4>
                </div>
            </form>
        </div>
        <div class="hidden-xs col-md-2 col-lg-3">

        </div>
    </div>
</div>
</body>
</html>
