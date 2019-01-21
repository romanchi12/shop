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
%>
<html>
<head>
    <%@include file="WEB-INF/jspf/includes.jspx"%>
    <title><fmt:message key="AddProduct"/> | Admin page</title>
</head>
<body>
    <div class="page">
        <%@include file="WEB-INF/jspf/navbar.jspx"%>
        <div class="row">
            <div class="hidden-xs col-md-2 col-lg-3">

            </div>
            <div class="col-xs-12 col-md-8 col-lg-6">
                <form method="post" action="/Controller?controller=AddProductController">

                <div class="input-group">
                    <span class="input-group-addon" id="basic-addon1"><fmt:message key="ProductName" var="ProductNameMsg"/>${ProductNameMsg}</span>
                    <input name="productname" type="text" class="form-control" placeholder="${ProductNameMsg}" aria-describedby="basic-addon1"/>
                </div>
                <br/>
                <div class="input-group">
                    <span class="input-group-addon" id="d-ad"><fmt:message key="ProductQuantity" var="ProductQuantityMsg"/>${ProductQuantityMsg}</span>
                    <input name="productquantity" type="number" class="form-control" placeholder="${ProductQuantityMsg}" aria-describedby="d-ad"/>
                </div>
                <br/>
                <div class="form-group">
                    <label for="productdescribe"><fmt:message key="ProductDescribe" var="ProductDescribeMsg"/>${ProductDescribeMsg}</label>
                    <textarea class="form-control" rows="5"
                              id="productdescribe" name="productdescribe" placeholder="${ProductDescribeMsg}"></textarea>
                </div>
                <br/>
                <div class="form-group">
                    <label for="categorySelection"><fmt:message key="Category" var="Category"/>${Category}:</label>
                    <select class="form-control" id="categorySelection" name="categorySelection">
                    </select>
                </div>
                <br/>
                <div class="input-group">
                    <span class="input-group-addon">₴</span>
                    <input type="number" class="form-control" id="productprice" name="productprice">
                </div>
                <br/>

                <input type="hidden" id="productimagesrc" name="productimagesrc" value=""/>
                <br/>
                <button type="submit" class="btn btn-success"><fmt:message key="AddProduct"/></button>
                </form>
            </div>
            <div class="hidden-xs col-md-2 col-lg-3">
                <form name="photo" id="imageUploadForm" enctype="multipart/form-data" action="/Ajax?controller=ImageUploadAjaxController" method="post">
                    <input type="file" id="ImageBrowse" hidden="hidden" name="image" size="30" accept="image/*" />
                    <br/>
                    <img width="100" style="z-index:1;position: relative; float:left;"  id="thumbnail"/>
                    <span id="error"></span>
                </form>
            </div>
        </div>
    </div>
<script>
$(document).ready(function () {
    $.ajax({
        url:"/Ajax?controller=GetAllCategoriesAjaxController",
        type:"GET",
        dataType:"json",
    }).done(function (data) {
        htmlSelection = "";
        data.forEach(function(category, index){
            htmlSelection += '<option value="' + category.categoryId+ '">' + category.categoryName + '</option>';
        });
        $("#categorySelection").html(htmlSelection);
    }).fail(function(e){
        console.log(e);
    });


    $('#imageUploadForm').on('submit',(function(e) {
        e.preventDefault();
        var formData = new FormData();
        formData.append('file', $('#ImageBrowse')[0].files[0]);
        $.ajax({
            type:'POST',
            url: $(this).attr('action'),
            data:formData,
            cache:false,
            contentType: false,
            processData: false,
            success:function(dataRaw){
                dataJSON = JSON.parse(dataRaw);
                if(dataJSON["imageSrc"]){
                    $("#thumbnail").attr("src","/images/"+dataJSON["imageSrc"]);
                    $("#productimagesrc").attr("value",dataJSON["imageSrc"]);
                    $("#error").html("");
                }else{
                    console.log(dataJSON["status"]);
                    $("#thumbnail").attr("src","");
                    $("#error").html("Error, file must be an image");
                }
            },
            error: function(data){
                notifier = $(".notifier");
                notifier.html("Ajax error");
                notifier.fadeToggle();
                setTimeout(function(){
                    notifier.fadeToggle();
                }, 1500);
            }
        });
    }));

    $("#ImageBrowse").on("change", function() {
        $("#imageUploadForm").submit();
    });
});
</script>
</body>
</html>
