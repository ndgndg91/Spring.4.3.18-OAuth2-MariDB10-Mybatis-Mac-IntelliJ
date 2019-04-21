<%--
  Created by IntelliJ IDEA.
  User: namdong-gil
  Date: 2019-04-16
  Time: 17:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!--===============================================================================================-->
    <link rel="icon" type="image/png" href="/static/images/icons/favicon.ico"/>
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="/static/vendor/bootstrap/css/bootstrap.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="/static/fonts/font-awesome-4.7.0/css/font-awesome.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="/static/fonts/iconic/css/material-design-iconic-font.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="/static/vendor/animate/animate.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="/static/vendor/css-hamburgers/hamburgers.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="/static/vendor/animsition/css/animsition.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="/static/vendor/select2/select2.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="/static/vendor/daterangepicker/daterangepicker.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="/static/css/util.css">
    <link rel="stylesheet" type="text/css" href="/static/css/main.css">
    <!--============================================================================================style="background-image: url('/static/images/bg-01.jpg');"===-->
</head>
<body>
<div class="container-login100">
    <div class="wrap-login100 p-l-55 p-r-55 p-t-80 p-b-30">
        <form class="login100-form validate-form">
				<span class="login100-form-title p-b-37">
					Sign In
				</span>

            <div class="wrap-input100 validate-input m-b-20" data-validate="Enter username or email">
                <input class="input100" type="text" name="username" placeholder="username or email">
                <span class="focus-input100"></span>
            </div>

            <div class="wrap-input100 validate-input m-b-25" data-validate = "Enter password">
                <input class="input100" type="password" name="pass" placeholder="password">
                <span class="focus-input100"></span>
            </div>

            <div class="container-login100-form-btn">
                <button class="login100-form-btn">
                    Sign In
                </button>
            </div>

            <div class="text-center p-t-57 p-b-20">
					<span class="txt1">
						Or login with
					</span>
            </div>

            <div class="flex-c p-b-112">
                <a href="${kakao_url}" class="login100-social-item">
<%--                    <i class="fa fa-facebook-f"></i>--%>
                    <img src="/static/images/icons/kakaolink_btn_medium/kakaolink_btn_medium.png"/>
                </a>

                <a href="${google_url}" class="login100-social-item">
                    <img src="/static/images/icons/icon-google.png" alt="GOOGLE">
                </a>
            </div>

            <div class="text-center">
                <a href="#" class="txt2 hov1">
                    Sign Up
                </a>
            </div>
        </form>


    </div>
</div>



<div id="dropDownSelect1"></div>

<!--===============================================================================================-->
<script src="/static/vendor/jquery/jquery-3.2.1.min.js"></script>
<!--=========/static/======================================================================================-->
<script src="/static/vendor/animsition/js/animsition.js"></script>
<!--===============================================================================================-->
<script src="/static/vendor/bootstrap/js/popper.js"></script>
<script src="/static/vendor/bootstrap/js/bootstrap.min.js"></script>
<!--===============================================================================================-->
<script src="/static/vendor/select2/select2.min.js"></script>
<!--===============================================================================================-->
<script src="/static/vendor/daterangepicker/moment.min.js"></script>
<script src="/static/vendor/daterangepicker/daterangepicker.js"></script>
<!--===============================================================================================-->
<script src="/static/vendor/countdowntime/countdowntime.js"></script>
<!--===============================================================================================-->
<script src="/static/js/main.js"></script>
</body>
</html>
