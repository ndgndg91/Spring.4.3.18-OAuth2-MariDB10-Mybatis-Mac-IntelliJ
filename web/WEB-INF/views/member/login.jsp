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
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/static/images/icons/favicon.ico"/>
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/vendor/bootstrap/css/bootstrap.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/fonts/font-awesome-4.7.0/css/font-awesome.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/fonts/iconic/css/material-design-iconic-font.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/vendor/animate/animate.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/vendor/css-hamburgers/hamburgers.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/vendor/animsition/css/animsition.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/vendor/select2/select2.min.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/vendor/daterangepicker/daterangepicker.css">
    <!--===============================================================================================-->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/util.css">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/main.css">
    <!--============================================================================================style="background-image: url('/static/images/bg-01.jpg');"===-->
</head>
<body>
<div class="container-login100">
    <div class="wrap-login100 p-l-55 p-r-55 p-t-80 p-b-30">
				<span class="login100-form-title p-b-37">
					Sign In
				</span>

            <div class="wrap-input100 validate-input m-b-20" data-validate="Enter username or email">
                <input class="input100" type="text" name="username" id="username" placeholder="username or email">
                <span class="focus-input100"></span>
            </div>

            <div class="wrap-input100 validate-input m-b-25" data-validate = "Enter password">
                <input class="input100" type="password" name="pass" id="pass" placeholder="password">
                <span class="focus-input100"></span>
            </div>

            <div class="container-login100-form-btn">
                <button class="login100-form-btn" id="loginBtn">
                    Sign In
                </button>
            </div>

            <div class="text-center p-t-57 p-b-20">
					<span class="txt1">
						Or login with
					</span>
            </div>

            <div class="flex-c p-b-112">
                    <%-- 페이스북은 redirect는 무조건 https 프로토콜만 지원하고 있음.   --%>
<%--                <a href="${facebook_url}}" class="login100-social-item">--%>
<%--                    <i class="fa fa-facebook-f"></i>--%>
<%--                </a>--%>
                <a href="${kakao_url}" class="login100-social-item">
                    <img src="${pageContext.request.contextPath}/static/images/icons/kakaolink_btn_medium/kakaolink_btn_medium.png" alt="KAKAO"/>
                </a>
                <a href="${google_url}" class="login100-social-item">
                    <img src="${pageContext.request.contextPath}/static/images/icons/icon-google.png" alt="GOOGLE"/>
                </a>
                <a href="${naver_url}" class="login100-social-item">
                    <img src="http://static.nid.naver.com/oauth/small_g_in.PNG" alt="NAVER"/>
                </a>
            </div>

            <div class="text-center">
                <a href="#" class="txt2 hov1">
                    Sign Up
                </a>
            </div>
    </div>
</div>



<div id="dropDownSelect1"></div>

<!--===============================================================================================-->
<script src="${pageContext.request.contextPath}/static/vendor/jquery/jquery-3.2.1.min.js"></script>
<!--=========/static/======================================================================================-->
<script src="${pageContext.request.contextPath}/static/vendor/animsition/js/animsition.js"></script>
<!--===============================================================================================-->
<script src="${pageContext.request.contextPath}/static/vendor/bootstrap/js/popper.js"></script>
<script src="${pageContext.request.contextPath}/static/vendor/bootstrap/js/bootstrap.min.js"></script>
<!--===============================================================================================-->
<script src="${pageContext.request.contextPath}/static/vendor/select2/select2.min.js"></script>
<!--===============================================================================================-->
<script src="${pageContext.request.contextPath}/static/vendor/daterangepicker/moment.min.js"></script>
<script src="${pageContext.request.contextPath}/static/vendor/daterangepicker/daterangepicker.js"></script>
<!--===============================================================================================-->
<script src="${pageContext.request.contextPath}/static/vendor/countdowntime/countdowntime.js"></script>
<!--===============================================================================================-->
<script src="${pageContext.request.contextPath}/static/js/common.js"></script>
<script>
    $(function () {
        $('#loginBtn').on('click', function () {
            CommonCtrl.commonAjax('${pageContext.request.contextPath}/login', 'POST', {'username' : $('#username').val(), 'pass': $('#pass').val()}, function(msg){checkLoginProcessResult(msg)});
        });
    });

    function checkLoginProcessResult(result) {
        switch (result) {
            case 'loginSuccess':
                location.href="/";
                break;
            case 'notExistEmail':
                alert('존재 하지 않는 이메일 입니다!');
                break;
            case 'incorrectPassword':
                alert("비밀 번호가 일치 하지 않습니다!");
                break;
            default:
                break;
        }
    }

</script>
</body>
</html>
