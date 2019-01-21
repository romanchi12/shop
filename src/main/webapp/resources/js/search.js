$(document).ready(function () {
    $("#search").focusout(function(){
        console.log("focusout");

        $(".search-box>ul").fadeToggle();
    });
    $("#search").focusin(function(){
        console.log("focusin");
        $(".search-box>ul").fadeToggle();
    });
    $("#search").keyup(function(eventObject){
        query = $(this).val();
        $.ajax({
            url:"/Ajax?controller=SearchProductsAjaxController",
            type:"GET",
            data:{"query":query},
            dataType:"json"
        }).done(function (data) {
            appendix = "";
            for(var i = 0; i<data.length;i++){
            appendix+="<li><img class='search-img' src='/images/"+data[i].productImgSrc+"'/> <a href='/Controller?controller=GetProductPageController&productId="+data[i].productId+ "'>"+data[i].productName+"</a></li>";
            }
            $(".search-box>ul").html(appendix);
        }).fail(function(e){
            console.log(e);
            notifier = $(".notifier");
            notifier.html("Ajax error");
            notifier.fadeToggle();
            setTimeout(function(){
                notifier.fadeToggle();
            }, 1500);
        });
    });
});
