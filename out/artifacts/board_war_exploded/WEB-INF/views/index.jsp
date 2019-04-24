<%--
  Created by IntelliJ IDEA.
  User: namdong-gil
  Date: 2019-04-16
  Time: 15:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
  <head>
    <title>Hi</title>
  </head>
  <body>
  <a href="?lang=en" class="btn btn-sm btn-success">English</a> <a href="?lang=ko" class="btn btn-sm btn-success">한글</a>
  <spring:message code="common.welcome"/>
  <c:if test="${empty sessionScope.KaKaoAccessToken && empty sessionScope.googleAccessToken}"> <!-- sessionScopre.KaKaoAccessToken가 없으면 -->
      <button onclick="location.href='/login';">로그인</button>
<%--    <button onclick="window.open('/login','로그인', 'scrollbars=yes,resizable=no,top=50%,left=50%,width=400,height=800')">로그인</button>--%>
  </c:if>
  <c:if test="${not empty sessionScope.kakaoUserInfo }"> <!-- sessionScopre.kakaoUserInfo 있으면 -->
    ${sessionScope.kakaoUserInfo} 님 방가방가<br/>
    <a href="/auth/kakao/logout">로그아웃</a><br/>
  </c:if>

  <c:if test="${not empty sessionScope.googleEmail}">
    ${sessionScope.googleName} 님 방가방가<br/>
    <a href="/auth/google/logout">로그아웃</a>
  </c:if>
  <button onclick="location.href='/mail/test'">메일 테스트</button>
  <button onclick="location.href='/mail/freemarker'">freemarker 메일 테스트</button>
  </body>
</html>

