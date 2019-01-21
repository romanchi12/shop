<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    if (request.getAttribute("products") == null || request.getAttribute("categories") == null) {
        response.sendRedirect("Controller?controller=GetProductsPageController");
        return;
    }
%>
<html>
<head>
    <title>Eshop</title>
    <%@include file="WEB-INF/jspf/includes.jspx" %>
</head>
<body>
<div class="page">
    <%@include file="WEB-INF/jspf/navbar.jspx" %>
    <div class="row">
        <div class="hidden-xs col-md-2 col-lg-3">
            <div class="list-group">
                <a href="/Controller?controller=GetProductsPageController"
                   class="list-group-item ${requestScope.categoryId==-1?'active':''}">
                    <fmt:message key="AllProducts"/>
                </a>
                <c:forEach items="${requestScope.categories}" var="category">
                    <a href="/Controller?controller=GetProductsPageController&categoryId=${category.categoryId}"
                       class="list-group-item ${category.categoryId==requestScope.categoryId?'active':''}">
                            ${category.categoryName}
                    </a>
                </c:forEach>
            </div>
        </div>
        <div class="col-xs-12 col-md-8 col-lg-6">

            <div class="row">
                <c:forEach items="${products}" varStatus="" var="product">
                    <div class="col-sm-6 col-md-4">
                        <div class="thumbnail" style="display: inline-block; height: 380px;">
                            <img src="/images/${product.productImgSrc}" alt="${product.productName}">
                            <div class="caption">
                                <h3>${product.productName}</h3>
                                <p>${product.productDescription}</p>
                                <p>
                                    <button productId="${product.productId}"
                                            href="/Ajax?controller=AddProductToCartAjaxController"
                                            class="buy btn btn-success" role="button">${product.productPrice} ₴
                                    </button>
                                    <a href="/Controller?controller=GetProductPageController&productId=${product.productId}"
                                       class="btn btn-primary" role="button"><fmt:message key="Details"/></a>
                                </p>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div class="row">
                <nav aria-label="Page navigation">
                    <ul class="pagination">
                        <li>
                            <a href="/Controller?controller=GetProductsPageController&page=${(requestScope.page-1<0)?0:requestScope.page-1}&categoryId=${requestScope.categoryId}"
                               aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                        <li class="active">
                            <a href="/Controller?controller=GetProductsPageController&page=${(requestScope.page<0)?0:requestScope.page}&categoryId=${requestScope.categoryId}">${requestScope.page + 1 }</a>
                        </li>
                        <li>
                            <a href="/Controller?controller=GetProductsPageController&page=${requestScope.page+1}&categoryId=${requestScope.categoryId}">${requestScope.page + 2}</a>
                        </li>
                        <li>
                            <a href="/Controller?controller=GetProductsPageController&page=${requestScope.page+2}&categoryId=${requestScope.categoryId}">${requestScope.page +3}</a>
                        </li>
                        <li>
                            <a href="/Controller?controller=GetProductsPageController&page=${requestScope.page+3}&categoryId=${requestScope.categoryId}">${requestScope.page + 4}</a>
                        </li>
                        <li>
                            <a href="/Controller?controller=GetProductsPageController&page=${requestScope.page+4}&categoryId=${requestScope.categoryId}">${requestScope.page + 5}</a>
                        </li>
                        <li>
                            <a href="/Controller?controller=GetProductsPageController&page=${requestScope.page+1}&categoryId=${requestScope.categoryId}"
                               aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </ul>
                </nav>
            </div>
        </div>
        <div class="hidden-xs col-md-2 col-lg-3">
            <div class="row">
                <div class="btn-group">
                    <a sort="PRICE_ASC" class="btn btn-default sort ${"PRICE_ASC".equals(sessionScope.sort)?'active':''}" aria-label="Left Align"><span
                            class="glyphicon glyphicon glyphicon-arrow-up" aria-hidden="true"></span></a>
                    <a sort="ID" class="btn btn-default sort ${"ID".equals(sessionScope.sort)?'active':''}" aria-label="Center Align"><span
                            class="glyphicon glyphicon-certificate" aria-hidden="true"></span></a>
                    <a sort="PRICE_DESC" class="btn btn-default sort ${"PRICE_DESC".equals(sessionScope.sort)?'active':''}" aria-label="Right Align"><span
                            class="glyphicon glyphicon glyphicon-arrow-down" aria-hidden="true"></span></a>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
        $(".sort").click(function(){
            $.ajax({
                url: "/Ajax?controller=SetSortingOrderAjaxController",
                type: "GET",
                data: {"sort": $(this).attr("sort")},
                dataType: "json",
            }).done(function(data){
                window.location.reload(false);
            });
        });
        $(".buy").click(function () {
            productId = $(this).attr("productId");
            console.log(productId);
            $.ajax({
                url: "/Ajax?controller=AddProductToCartAjaxController",
                type: "GET",
                data: {"productId": productId},
                dataType: "json",
            }).done(function (data) {
                if (data["status"] == "ok") {
                    notifier = $(".notifier");
                    notifier.addClass("notifier-success");
                    notifier.html(data["successMessage"]);
                    notifier.fadeToggle();
                    setTimeout(function () {
                        notifier.fadeToggle();
                    }, 400);
                } else if (data["status"] == "error") {
                    notifier = $(".notifier");
                    notifier.addClass("notifier-error");
                    notifier.html(data["errorMessage"]);
                    notifier.fadeToggle();
                    setTimeout(function () {
                        notifier.fadeToggle();
                    }, 1500);
                }
            }).fail(function (e) {
                notifier = $(".notifier");
                notifier.html("Ajax error");
                notifier.fadeToggle();
                setTimeout(function () {
                    notifier.fadeToggle();
                }, 1500);
            });
        });
    });
</script>
</body>
</html>
