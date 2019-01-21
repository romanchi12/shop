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
    <title>${sessionScope.user.userName}</title>
    <%@include file="WEB-INF/jspf/includes.jspx"%>
</head>
<body>
    <div class="page">
        <%@include file="WEB-INF/jspf/navbar.jspx"%>
        <div class="row">
            <div class="hidden-xs col-md-2 col-lg-3">

            </div>
            <div class="col-xs-12 col-md-8 col-lg-6">
                <c:if test="${sessionScope.user!=null}">
                    <form action="/Controller?controller=UpdateUserController" method="post">
                        <table class="table-view table">
                            <tr>
                                <td><fmt:message key="Name"/>: </td>
                                <td class="data-field" contenteditable="true">${sessionScope.user.userName}</td>
                                <input type="hidden" name="username" value="${sessionScope.user.userName}"/>
                            </tr>
                            <tr>
                                <td><fmt:message key="Surname"/>: </td>
                                <td class="data-field" contenteditable="true">${sessionScope.user.userSurname}</td>
                                <input type="hidden" name="usersurname" value="${sessionScope.user.userSurname}"/>
                            </tr>
                            <tr>
                                <td><fmt:message key="Address"/>: </td>
                                <td class="data-field" contenteditable="true">${sessionScope.user.userAddress}</td>
                                <input type="hidden" name="useraddress" value="${sessionScope.user.userAddress}"/>
                            </tr>
                            <tr>
                                <td>Email: </td>
                                <td class="data-field" contenteditable="true">${sessionScope.user.userEmail}</td>
                                <input type="hidden" name="useremail" value="${sessionScope.user.userEmail}"/>
                            </tr>
                            <tr>
                                <td><fmt:message key="Password"/>: </td>
                                <td class="data-field" contenteditable="true">${sessionScope.user.userPassword}</td>
                                <input type="hidden" name="userpassword" value="${sessionScope.user.userPassword}"/>
                            </tr>
                            <tr>
                                <td><fmt:message key="Language"/>:</td>
                                <td>
                                    <div class="form-group">
                                        <select class="form-control" id="languageSelection" name="userlanguage">
                                            <option ${"en_GB".equals(sessionScope.user.userLanguage)?"selected":""} value="en_GB">English</option>
                                            <option ${"uk_UA".equals(sessionScope.user.userLanguage)?"selected":""} value="uk_UA">Українська</option>
                                            <option ${"ru_RU".equals(sessionScope.user.userLanguage)?"selected":""} value="ru_RU">Русский</option>
                                        </select>
                                    </div>
                                </td>
                            </tr>
                        </table>
                        <button style="max-width: 200px;" class="btn btn-lg btn-primary btn-block" type="submit"><fmt:message key="Save"/></button>
                    </form>
                </c:if>
            </div>
            <div class="hidden-xs col-md-2 col-lg-3">

            </div>
        </div>
    </div>
<script>
$(document).ready(function(){
    $(".data-field").focusout(function(){
        self = $(this);
        input = self.next("input");
        input.val(self.text());
    });
});
</script>
</body>
</html>
