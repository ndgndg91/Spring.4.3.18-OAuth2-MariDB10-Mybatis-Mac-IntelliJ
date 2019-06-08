<%--
  Created by IntelliJ IDEA.
  User: namdong-gil
  Date: 2019-06-02
  Time: 13:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="/static/vendor/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/datepicker/datetimepickerstyle.css"/>
    <title>회원 가입</title>
</head>
<body>
<div class="container">
    <h1>회원 가입</h1>
    <div class="row">
        <div class="col">
    <form id="joinForm" action="/join" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="uEmail">이메일 주소</label>
            <input type="email" class="form-control" id="uEmail" name="uEmail" placeholder="이메일을 입력하세요" required>
            <label><b style="color: red;" id="validationOfEmailLabel"></b></label>
            <input type="hidden" id="validationOfEmail" value="true"/>
        </div>
        <div class="form-group">
            <label for="uPassword">암 호</label>
            <input type="password" class="form-control" id="uPassword" name="uPassword" placeholder="암호" required>
        </div>
        <div class="form-group">
            <label for="uPasswordCheck">암 호</label>
            <input type="password" class="form-control" id="uPasswordCheck" name="uPasswordCheck" placeholder="암호 확인" required>
            <label><b style="color: red;" id="validationOfPasswordLabel"></b></label>
            <input type="hidden" id="validationOfPassword" value="true"/>
        </div>
        <div class="form-group">
            <label for="uRealName">이 름</label>
            <input type="text" class="form-control" id="uRealName" name="uRealName" placeholder="이름" required>
        </div>
        <div class="form-group">
            <label for="uNick">별 명</label>
            <input type="text" class="form-control" id="uNick" name="uNick" placeholder="서비스에서 사용할 닉네임" required>
        </div>
        <div class="form-group">
            <label>생 일</label>
            <div class='input-group date dateTimePicker' id="datepicker1">
                <input type='text' class="form-control" name="uBirth" required>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
            <label><b style="color: red;" id="validationOfBirthLabel"></b></label>
            <input type="hidden" id="validationOfBirth" value="true"/>
        </div>
        <div class="form-group">
            <label>성 별</label>
            <div class="form-group">
                <label class="checkbox-inline">
                    <input type="radio" name="uGender" id="uGender1" value="M" required> 남자
                </label>
                &nbsp;
                <label class="checkbox-inline">
                    <input type="radio" name="uGender" id="uGender2" value="W" required> 여자
                </label>
                &nbsp;
                <label class="checkbox-inline">
                    <input type="radio" name="uGender" id="uGender3" value="O" required> 기타
                </label>
            </div>
        </div>
        <div class="custom-file">
            <input type="file" class="custom-file-input" id="uPicture" name="uPicture" required>
            <label class="custom-file-label" for="uPicture" id="imageFileName">사 진</label>
        </div>
        <div class="form-group">
            <div class="image_wrap"></div>
        </div>
        <div class="form-group">
            <button type="submit" class="btn btn-primary btn-lg btn-block" id="acceptJoin">가 입</button>
            <button type="button" class="btn btn-dark btn-lg btn-block" id="cancelJoin">취 소</button>
        </div>
    </form>
        </div>
    </div>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.18.0/moment.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.18.0/locale/ko.js"></script>
<script src="/static/datepicker/bootstrap-datetimepicker.js"></script>
<script>
    var $uPicture;
    var $imageWrap;
    var $imageFileName;
    var $uEmail;
    var $validationOfEmailLabel;
    var $validationOfEmail;
    var $uPassword;
    var $uPasswordCheck;
    var $validationOfPasswordLabel;
    var $validationOfPassword;
    var $uBirth;
    var $validationOfBirthLabel;
    var $validationOfBirth;
    $(function () {
        $('#datepicker1').datetimepicker({
            format: 'YYYY-MM-DD',
            locale:'ko',
            icons: {
                time: "fa fa-clock-o",
                date: "fa fa-calendar",
                up: "fa fa-arrow-up",
                down: "fa fa-arrow-down"
            }
        });

        $uEmail = $('#uEmail');
        $validationOfEmailLabel = $('#validationOfEmailLabel');
        $validationOfEmail = $('#validationOfEmail');
        $uPassword = $('#uPassword');
        $uPasswordCheck = $('#uPasswordCheck');
        $validationOfPasswordLabel = $('#validationOfPasswordLabel');
        $validationOfPassword = $('#validationOfPassword');
        $uBirth = $('#uBirth');
        $validationOfBirthLabel = $('#validationOfBirthLabel');
        $validationOfBirth = $('#validationOfBirth');
        $imageWrap = $('.image_wrap');
        $uPicture = $('#uPicture');
        $imageFileName = $('#imageFileName');
        $uPicture.on("change",handleImgsFilesSelect);
        $('#cancelJoin').on('click', function () {
           history.back();
        });

        $uEmail.on('input', function () {
            if (!emailIsValid($(this).val())) {
                $validationOfEmailLabel.text('이메일 형식이 아닙니다.');
                $validationOfEmail.val('false');
                return;
            }
            $validationOfEmail.val('true');
            $validationOfEmailLabel.text('');
        });

        $uEmail.on('blur', function () {
            if (!emailIsValid($(this).val())) {
                $validationOfEmailLabel.text('이메일 형식이 아닙니다.');
                return;
            }
            $validationOfEmailLabel.text('');
        });

        $uPasswordCheck.on('input', function () {
           var uPassword = $uPassword.val();
           var uPasswordCheck = $uPasswordCheck.val();
           if (uPassword !== uPasswordCheck){
               $validationOfPasswordLabel.text('비밀번호가 일치하지 않습니다.');
               $validationOfPassword.val('false');
               return;
           }

           $validationOfPasswordLabel.text('');
           $validationOfPassword.val('true');
        });

        $uPasswordCheck.on('blur', function () {
            var uPassword = $uPassword.val();
            var uPasswordCheck = $uPasswordCheck.val();
            if (uPassword !== uPasswordCheck){
                $validationOfPasswordLabel.text('비밀번호가 일치하지 않습니다.');
                $validationOfPassword.val('false');
                return;
            }

            $validationOfPasswordLabel.text('');
            $validationOfPassword.val('true');
        });

        $uBirth.on('input', function () {
            if(birthIsValid($(this).val())){
                $validationOfBirth.val('true');
                $validationOfBirthLabel.text('');
                return;
            }

            $validationOfBirth.val('false');
            $validationOfBirthLabel.text('날짜 형식이 아닙니다.');
        });

        $uBirth.on('blur', function () {
            if(birthIsValid($(this).val())){
                $validationOfBirth.val('true');
                $validationOfBirthLabel.text('');
                return;
            }

            $validationOfBirth.val('false');
            $validationOfBirthLabel.text('날짜 형식이 아닙니다.');
        });
    });

    function emailIsValid (email) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)
    }

    function birthIsValid(birth) {
        return /^\d{4}-\d{2}-\d{2}$/.test(birth);
    }

    function handleImgsFilesSelect(e) {
        $imageWrap.empty();
        var files = e.target.files;
        var filesArr = Array.prototype.slice.call(files);

        var index = 0;
        filesArr.forEach(function(f) {
            if(!f.type.match("image.*")){
                alert("확장자는 이미지 확장자만 가능합니다.");
                return;
            }

            var reader = new FileReader();
            reader.onload = function(e) {
                var html = "<a href=\"javascript:void(0);\" onclick=\"deleteImageAction("+index+")\" id=\"img_id_"+index+"\"><img src=\"" + e.target.result +"\" data-file='"+f.name+"' class='selProductFile' alt='클릭 시 삭제' title='Click to remove' width=200px height=200px></a>";
                /* var img_html = "<img src=\"" + e.target.result + "\" width=200px height=200px />";
                $(".imgs_wrap").append(img_html); */
                $imageWrap.append(html);
                index++;
            };
            reader.readAsDataURL(f);
        });
        $imageFileName.text($uPicture.val());
    }


    function deleteImageAction(index){
        console.log("index : "+index);
        var imageFileList = Array.from($uPicture[0].files);
        console.log(imageFileList);
        delete imageFileList.splice(index, 1);
        console.log(imageFileList);
        var img_id = "#img_id_" + index;
        $(img_id).remove();
        $uPicture.val('');
        $imageFileName.text('사 진');
    }
</script>
</body>
</html>
