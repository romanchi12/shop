<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    if(session.getAttribute("user")==null){
        response.sendRedirect("/login.jsp");
        return;
    }
    if(request.getAttribute("orderitemdtos")==null){
        response.sendRedirect("/Controller?controller=GetCartPageController");
        return;
    }
%>
<html>
<head>
    <%@include file="WEB-INF/jspf/includes.jspx"%>
    <title><fmt:message key="Cart"/></title>
</head>
<body>
<div class="page">
    <%@include file="WEB-INF/jspf/navbar.jspx"%>
    <div class="row">
        <div class="hidden-xs col-md-2 col-lg-3">

        </div>
        <div class="col-xs-12 col-md-10 col-lg-9">
            <c:choose>
                <c:when test="${orderitemdtos.size()>0}">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th scope="col"></th>
                            <th scope="col"><fmt:message key="ProductName"/></th>
                            <th scope="col"><fmt:message key="ProductDescription"/></th>
                            <th scope="col"><fmt:message key="ProductPrice"/></th>
                            <th scope="col"><fmt:message key="Quantity"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${orderitemdtos}" var="orderitemdto">
                            <tr class="orderitemdto">
                                <td contenteditable="true">
                                    <img src="/images/${orderitemdto.productImageSrc}"  style="max-height: 25px;" class="img-responsive"/>
                                </td>
                                <td productId="${orderitemdto.productId}">${orderitemdto.productName}</td>
                                <td>${orderitemdto.productDescription}</td>
                                <td>${orderitemdto.productPrice}</td>
                                <td>${orderitemdto.orderItemQuantity}</td>
                                <td><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <hr/>
                    <fmt:message key="SummaryPrice"/>: ${orderitemdtos.get(0).summaryPrice}
                    <hr/>
                    <a href="Controller?controller=ConfirmOrderController">
                        <button type="submit" class="btn btn-success"><fmt:message key="ConfirmOrder"/></button>
                    </a>
                </c:when>
                <c:otherwise>
                    <div class="panel panel-default">
                        <div class="panel-body">
                            <fmt:message key="YourCartIsEmpty"/>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>
        <div class="hidden-xs hidden-md hidden-lg">

        </div>
    </div>
</div>
<script>
$(document).ready(function(){
    $(".glyphicon-remove").click(function(){
        self = $(this).parents(".orderitemdto");
        childs = $(this).parents(".orderitemdto").children();
        productId = $(childs[1]).attr("productId");
        $.ajax({
            url:"/Ajax?controller=DeleteProductFromCartAjaxController",
            type:"GET",
            data:{"productId":productId},
            dataType:"json",
        }).done(function (data) {
            self.remove();
            if(data["status"]=="ok"){
                notifier = $(".notifier");
                notifier.addClass("notifier-success");
                notifier.html(data["successMessage"]);
                notifier.fadeToggle();
                setTimeout(function(){
                    notifier.fadeToggle();
                }, 1500);
            }else if(data["status"]=="error"){
                notifier = $(".notifier");
                notifier.addClass("notifier-error");
                notifier.html(data["errorMessage"]);
                notifier.fadeToggle();
                setTimeout(function(){
                    notifier.fadeToggle();
                }, 1500);
            }
        }).fail(function(e){
            notifier = $(".notifier");
            notifier.html("Ajax error");
            notifier.fadeToggle();
            setTimeout(function(){
                notifier.fadeToggle();
            }, 1500);
        });
    });
});
</script>
</body>
</html>
