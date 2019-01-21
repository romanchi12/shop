<%@ page import="java.util.logging.Logger" %>
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
    if(request.getAttribute("products")==null){
        response.sendRedirect("/Controller?controller=AdminPageController");
        return;
    }
%>
<html>
<head>
    <%@include file="WEB-INF/jspf/includes.jspx"%>
    <title><fmt:message key="Products"/> | Admin page</title>
</head>
<body>
<div class="page">
    <%@include file="WEB-INF/jspf/navbar.jspx"%>
    <div class="row">
        <div class="hidden-xs col-md-2 col-lg-3">
            <div class="list-group">
                <a href="/Controller?controller=AdminPageController" class="list-group-item active">
                    <fmt:message key="Products"/>
                </a>
                <a href="/Controller?controller=AdminCategoriesController" class="list-group-item"><fmt:message key="Categories"/></a>
            </div>
        </div>
        <div class="col-xs-12 col-md-10 col-lg-9">
            <div class="row" style="position: relative;">
                <div>
                    <a href="/addproduct.jsp">
                        <button type="button" class="btn btn-success"><fmt:message key="AddProduct"/></button>
                    </a>
                </div>
            </div>
            <table class="table table-hover">
                <thead>
                <tr>
                    <th scope="col"><fmt:message key="ProductId"/></th>
                    <th scope="col"><fmt:message key="ProductName"/></th>
                    <th scope="col"><fmt:message key="ProductDescription"/></th>
                    <th scope="col"><fmt:message key="ProductPrice"/></th>
                    <th scope="col"><fmt:message key="Quantity"/></th>
                    <th scope="col"><fmt:message key="Category"/></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${products}" var="productdto">
                    <tr class="product">
                        <th scope="row">${productdto.productId}</th>
                            <td contenteditable="true">${productdto.productName}</td>
                            <td contenteditable="true">${productdto.productDescription}</td>
                            <td contenteditable="true">${productdto.productPrice}</td>
                            <td contenteditable="true">${productdto.warehouseItemQuantity}</td>
                            <td>${productdto.categoryName}</td>
                            <td><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <nav aria-label="Page navigation">
                <ul class="pagination">
                    <li>
                        <a href="/Controller?controller=AdminPageController&page=${(requestScope.page-1<0)?0:requestScope.page-1}" aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                    <li class="active">
                        <a href="/Controller?controller=AdminPageController&page=${(requestScope.page<0)?0:requestScope.page}">${requestScope.page + 1 }</a>
                    </li>
                    <li><a href="/Controller?controller=AdminPageController&page=${requestScope.page+1}">${requestScope.page + 2}</a></li>
                    <li><a href="/Controller?controller=AdminPageController&page=${requestScope.page+2}">${requestScope.page +3}</a></li>
                    <li><a href="/Controller?controller=AdminPageController&page=${requestScope.page+3}">${requestScope.page + 4}</a></li>
                    <li><a href="/Controller?controller=AdminPageController&page=${requestScope.page+4}">${requestScope.page + 5}</a></li>
                    <li>
                        <a href="/Controller?controller=AdminPageController&page=${requestScope.page+1}" aria-label="Next">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </ul>
            </nav>
        </div>
        <div class="hidden-xs hidden-md hidden-lg">

        </div>
    </div>
</div>
<script>
    $(document).ready(function(){
        $(".product").focusout(function(){
            childs = $(this).children();
            productId = $(childs[0]).text();
            productName = $(childs[1]).text();
            productDescription = $(childs[2]).text();
            productPrice = $(childs[3]).text();
            productQuantity = $(childs[4]).text();
            $.ajax({
                url:"/Ajax?controller=UpdateProductAjaxController",
                type:"GET",
                data:{"productName":productName,"productId":productId,
                    "productDecsription":productDescription,
                    "productPrice":productPrice,"productQuantity":productQuantity},
                dataType:"json",
            }).done(function (data) {
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
        $(".glyphicon-remove").click(function(){
            self = $(this).parents(".product");
            childs = $(this).parents(".product").children();
            productId = $(childs[0]).text();
            productName = $(childs[1]).text();
            productDescription = $(childs[2]).text();
            productPrice = $(childs[3]).text();
            productQuantity = $(childs[4]).text();
            $.ajax({
                url:"/Ajax?controller=DeleteProductAjaxController",
                type:"GET",
                data:{"productName":productName,"productId":productId,
                    "productDecsription":productDescription,
                    "productPrice":productPrice,"productQuantity":productQuantity},
                dataType:"json",
            }).done(function (data) {
                if(data["status"]=="ok"){
                    self.remove();
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
