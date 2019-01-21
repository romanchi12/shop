<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    if(request.getSession().getAttribute("user")==null){
        response.sendRedirect("/login.jsp");
        return;
    }
    if(request.getAttribute("ordersMap")==null){
        response.sendRedirect("/Controller?controller=GetHistoryPageController");
        return;
    }
%>
<html>
<head>
    <%@include file="WEB-INF/jspf/includes.jspx"%>
    <title><fmt:message key="History"/> | ${sessionScope.user.userName}</title>
</head>
<body>
<div class="page">
    <%@include file="WEB-INF/jspf/navbar.jspx"%>
    <div class="row">
        <div class="hidden-xs col-md-2 col-lg-3">

        </div>
        <div class="col-xs-12 col-md-8 col-lg-6">
            <c:forEach items="${ordersMap}" var="order">
                <div class="panel panel-${order.key.orderStatus==1?'success':'info'}">
                    <div class="panel-heading">#${order.key.orderId}</div>
                    <div class="panel-body">
                        <p><fmt:message key="SummaryPrice"/>: ${order.key.summaryPrice}</p>
                    </div>
                    <table class="table">
                        <thead>
                        <tr>
                            <th scope="col"><fmt:message key="ProductName"/></th>
                            <th scope="col"><fmt:message key="Quantity"/></th>
                            <th scope="col"><fmt:message key="ProductPrice"/></th>
                        </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${order.value}" var="orderitemdto">
                                <tr class="orderitem">
                                    <th scope="row">${orderitemdto.productName}</th>
                                    <td>${orderitemdto.orderItemQuantity}</td>
                                    <td>${orderitemdto.productPrice}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:forEach>
        </div>
        <div class="hidden-xs col-md-2 col-lg-3">

        </div>
    </div>
</div>
</body>
</html>
