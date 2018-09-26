<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh">
    <head>
        <meta name="viewport" content="initial-scale=1.0, width=device-width, user-scalable=no" />
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
       <!--  <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
        <meta http-equiv="X-UA-Compatible" content="IE=8" /> -->
        <title>springboot用户登录页面</title>
       <!--  <link type="image/x-icon" href="images/favicon.ico" rel="shortcut icon">
        <link rel="stylesheet" href="static/css/main.css" /> -->
    </head>
    <body>
        <div class="wrapper">
            <div class="container">
                <h1>Welcome</h1>
                <form method="post" onsubmit="return false;" class="form">
                    <input type="text" value="cyx" name="username" placeholder="Account"/>
                    <input type="password" value="123456" name="password" placeholder="Password"/>
                    <button type="button" id="login-button">Login</button>
                </form>
            </div>
            <ul class="bg-bubbles">
                <li></li>
                <li></li>
                <li></li>
                <li></li>
                <li></li>
                <li></li>
                <li></li>
                <li></li>
                <li></li>
                <li></li>
            </ul>
        </div>
        <!-- <script type="text/javascript" src="static/js/jquery-1.10.1.min.js" ></script> -->
   		 <script type=text/javascript src="${pageContext.request.contextPath}/js/jquery-1.8.0.js"></script>
        <script type="text/javascript">
            var redirectUrl = "${redirect}"; // 浏览器中返回的URL
            function doLogin() {
                $.post("/userController/userLogin", $(".form").serialize(),function(data){
                    if (data.status == 200) {
                        if (redirectUrl == "") {
                        	alert("登录成功");
                            location.href = "http://localhost:8080//pageController/toIndex";
                        } else {
                            location.href = redirectUrl;
                        }
                    } else {
                    	debugger;
                        alert("登录失败，原因是：" + data.msg);
                    }
                });
            }
            $(function(){
                $("#login-button").click(function(){
                	alert("开始登录")
                    doLogin();
                });
            });
        </script>
    </body>
</html>