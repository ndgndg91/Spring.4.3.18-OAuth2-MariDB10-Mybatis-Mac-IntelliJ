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
    <link rel="stylesheet" type="text/css" href="/static/vendor/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/datepicker/datetimepickerstyle.css"/>
    <title>회원 가입</title>
</head>
<body>
<div class="container">
    <h1>회원 가입</h1>
    <form action="/upload/imgFile" method="post">
        <div class="form-group">
            <label for="uEmail">이메일 주소</label>
            <input type="email" class="form-control" id="uEmail" name="uEmail" placeholder="이메일을 입력하세요">
        </div>
        <div class="form-group">
            <label for="uPassword">암 호</label>
            <input type="password" class="form-control" id="uPassword" name="uPassword" placeholder="암호">
        </div>
        <div class="form-group">
            <label for="uPasswordCheck">암 호</label>
            <input type="password" class="form-control" id="uPasswordCheck" name="uPasswordCheck" placeholder="암호 확인">
        </div>
        <div class="form-group">
            <label for="uRealName">이 름</label>
            <input type="password" class="form-control" id="uRealName" name="uRealName" placeholder="이름">
        </div>
        <div class="form-group">
            <label for="uNick">별 명</label>
            <input type="password" class="form-control" id="uNick" name="uNick" placeholder="서비스에서 사용할 닉네임">
        </div>
        <div class="form-group">
            <label>생 일</label>
            <div class='input-group date dateTimePicker' id="datepicker1">
                <input type='text' class="form-control" name="openDate" required="required"/>
                <span class="input-group-addon">
                    <span class="glyphicon glyphicon-calendar"></span>
                </span>
            </div>
        </div>
        <div class="form-group">
            <label class="checkbox-inline">
                <input type="radio" name="uGender" id="uGender1" value="M"> 남자
            </label>
            &nbsp;
            <label class="checkbox-inline">
                <input type="radio" name="uGender" id="uGender2" value="W"> 여자
            </label>
            &nbsp;
            <label class="checkbox-inline">
                <input type="radio" name="uGender" id="uGender3" value="O"> 기타
            </label>
        </div>
        <div class="form-group">
            <label for="uPicture">사 진</label>
            <input type="file" id="uPicture" name="uPicture">
            <div class="imgs_wrap">
            </div>
        </div>
        <button type="submit" class="btn btn-primary">제출</button>
    </form>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.18.0/moment.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.18.0/locale/ko.js"></script>
<script src="/static/datepicker/bootstrap-datetimepicker.js"></script>
<script>
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
        $('#uPicture').on("change",handleImgsFilesSelect);
    });

    function handleImgsFilesSelect(e) {
        $('.imgs_wrap').empty();
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
                $(".imgs_wrap").append(html);
                index++;
            };
            reader.readAsDataURL(f);

        });
    }


    function deleteImageAction(index){
        console.log("index : "+index);
        var imageFileList = Array.from($('#uPicture')[0].files);
        console.log(imageFileList);
        delete imageFileList.splice(index, 1);
        console.log(imageFileList);
        var img_id = "#img_id_" + index;
        $(img_id).remove();
        $('#uPicture').val('');
    }
</script>
</body>
</html>
