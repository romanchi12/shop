<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
    <title>User</title>
</head>
<body>
    User: ${requestScope.user}
<br>
<hr>
    Users Amount: ${requestScope.usersAmount}


    <br>
<hr>
    Exists by id: ${requestScope.existsById}
<br>
<hr>
    new user id: ${requestScope.newuserid}
    <br>
    <hr>
    <table>
        <c:forEach items="${requestScope.users}" var="user">
            <tr>
                ${user}
            </tr>
        </c:forEach>
    </table>
</body>
</html>
