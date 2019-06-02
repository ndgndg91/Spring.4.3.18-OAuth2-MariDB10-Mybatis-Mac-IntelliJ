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
<!DOCTYPE html>
<html lang="ko">
<head>
    <title>Hi</title>
    <script src="/static/vendor/jquery/jquery-3.2.1.min.js"></script>
    <script src="/static/js/common.js"></script>
</head>
<body>
<a href="?lang=en" class="btn btn-sm btn-success">English</a> <a href="?lang=ko" class="btn btn-sm btn-success">한글</a>
<spring:message code="common.welcome"/>
<c:if test="${empty sessionScope.loginUserInfo}"> <!-- sessionScopre.KaKaoAccessToken가 없으면 -->
    <button onclick="location.href='/login';">로그인</button>
    <button onclick="location.href='/join';">회원가입</button>
    <br/>
    <%--    <button onclick="window.open('/login','로그인', 'scrollbars=yes,resizable=no,top=50%,left=50%,width=400,height=800')">로그인</button>--%>
</c:if>
<c:if test="${not empty sessionScope.loginUserInfo }"> <!-- sessionScopre.kakaoUserInfo 있으면 -->
    ${sessionScope.loginUserInfo} 님 방가방가<br/>
    <a href="/logout?loginType=${sessionScope.loginUserInfo.loginType}">로그아웃</a><br/>
    <br/>
    <h1>나와 친구가 되고 싶은 인간들</h1>
    <table>
        <thead>
        <th>사진</th>
        <th>정보</th>
        <th>수락</th>
        </thead>
        <tbody id="applicantList">
        </tbody>
    </table>
    <br/>
    <h1>아직 친구가 아닌 인간들</h1>
    <table>
        <thead>
        <th>이메일</th>
        <th>별명</th>
        <th>이름</th>
        <th>사진</th>
        <th>썸네일</th>
        <th>성별</th>
        <th>나이</th>
        <th>생일</th>
        <th>신청</th>
        </thead>
        <tbody>
        <c:if test="${not empty exceptMeMemberList}">
            <c:forEach var="member" items="${exceptMeMemberList}" begin="0" end="5" step="1" varStatus="status">
                <tr>
                    <td>${member.email}</td>
                    <td>${member.nick}</td>
                    <td>${member.realName}</td>
                    <td><img src="${member.pictureUrl}"></td>
                    <td><img src="${member.thumbnailImageUrl}"></td>
                    <td>${member.gender}</td>
                    <td>${member.age}</td>
                    <td>${member.birth}</td>
                    <td>
                        <button id="applyFriendBtn" data-value="${member.id}"
                                onclick="CommonCtrl.commonAjax('/apply/friend','POST',{'applicantNo':'${sessionScope.loginUserInfo.id}', 'acceptorNo': '${member.id}'}, function(msg) {
                                        alert(msg);
                                        location.reload();
                                        });">
                            친구 신청
                        </button>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <script>
        var getApplicantList = function () {
            CommonCtrl.commonAjax('/applyFor/friend', 'GET', '', function (msg) {
                console.log(msg);
                var html = [];
                var images = [];

                msg.forEach(function (member) {
                    images.push(member.thumbnailImageUrl === '' || member.thumbnailImageUrl === null ? member.pictureUrl : member.thumbnailImageUrl);
                    console.log(member.thumbnailImageUrl === '' || member.thumbnailImageUrl === null ?  member.pictureUrl : member.thumbnailImageUrl);
                    html.push(
                        '<tr>',
                        '<td>',
                        '<img class="memberImg" src=""/>',
                        '</td>',
                        '<td>',
                        member.realName === null ? member.nick : member.realName,
                        '(' + member.email + ') 님이 친구 신청을 하셨습니다!',
                        '</td>',
                        '<td>',
                        '<button onclick="CommonCtrl.commonAjax(\'/accept/friend\',\'POST\',{\'applicantNo\':\'' + member.id + '\', \'acceptorNo\': \'${sessionScope.loginUserInfo.id}\'}, function(msg) {alert(msg); getApplicantList();});">',
                        '친구 수락',
                        '</button>',
                        '</td>',
                        '</tr>');
                    console.log(member);
                });
                $('#applicantList').html(html.join(''));
                images.forEach(function (image) {
                    console.log(image);
                    $('#applicantList .memberImg').attr('src', image);
                })
            });
        };
        getApplicantList();
    </script>
    <button onclick="location.href='/mail/test'">메일 테스트</button>
    <button onclick="location.href='/mail/freemarker'">freemarker 메일 테스트</button>
</c:if>
</body>
</html>

