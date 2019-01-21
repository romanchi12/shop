<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
    if(request.getAttribute("product")==null){
        response.sendRedirect("/Controller?controller=GetProductsPageController");
        return;
    }
%>
<html>
<head>
    <%@include file="WEB-INF/jspf/includes.jspx"%>
    <title>${product.productName}</title>
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
                        <input id="productId" name="productId" type="hidden" value="${product.productId}"/>
                        <h2>${product.productName}</h2>
                        <h4>${product.productDescription}</h4>
                        <hr/>
                        <div class="row">
                            <div class="col-xs-6 col-md-3">
                                <a href="#" class="thumbnail">
                                    <img src="/images/${product.productImgSrc}" alt="${product.productName}"/>
                                </a>
                            </div>
                        </div>
                        <hr/>
                        <div class="input-group">
                            <span class="input-group-addon" id="d-ad"><fmt:message key="Quantity" var="QuantityMsg"/>${QuantityMsg}</span>
                            <input id="productQuantity" name="productQuantity" type="number" class="form-control" placeholder="${QuantityMsg}" aria-describedby="d-ad" value="1"/>
                        </div>
                        <br/>
                        <button href="#" class="btn btn-success" id="buy" role="button">${product.productPrice} ₴</button>
                    </div>
                </div>

        </div>
        <div class="hidden-xs col-md-2 col-lg-3">

        </div>
    </div>
</div>
<script>
$(document).ready(function(){
    $("#buy").click(function(){
        productId = $("#productId").val();
        productQuantity = $("#productQuantity").val();
        $.ajax({
            url:"/Ajax?controller=AddProductToCartAjaxController",
            type:"GET",
            data:{"productId":productId, "productQuantity":productQuantity},
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
                document.location.
                            assign('http://localhost:8080/Controller?controller=GetProductsPageController');

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
