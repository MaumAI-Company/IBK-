<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ include file="/WEB-INF/jsp/include/header.jspf" %>
    <title>Login || maum XDC</title>
</head>
<body>
<!-- .loginWrap -->
<div class="loginWrap">
    <form action="" class="loginForm" id="loginForm" name="form-login" method="post">
		<input type="hidden" id="RSAModulus" value="${RSAModulus}"/>
		<input type="hidden" id="RSAExponent" value="${RSAExponent}"/>    
		<input type="hidden" id="userId" name="userId" value=""/>
		<input type="hidden" id="pwd" name="pwd" value=""/>    
        <fieldset>
            <legend>LOGIN</legend>
            <div class="loginBox">
                <h1>maum XDC<span> eXplainable Document Classification</span></h1>
                <div class="formBox">
                    <p class="tit">LOGIN</p>
                    <div class="iptBox">
                        <input type="text" name="userIdChk" id="userIdChk" class="ipt_txt" placeholder="ID" autocomplete="off">
                    </div>
                    <div class="iptBox">
                        <input type="password" name="pwdChk" id="pwdChk" class="ipt_txt" placeholder="Password" autocomplete="off">
                    </div>                   
                    <button name="btn_login" id="btn_login" class="btn_login" onclick="javascript:loginSubmit(event);">LOGIN</button>
                    <p class="error_info" style="margin: 30px 0 0;">
                        <c:if test="${isError}"><span style="color: rgb(255, 0, 0);">${errorMessage}<br /><br /><br /></span></c:if>
                        로그인 오류 발생시 관리자에게 문의 바랍니다.
                    </p>
                </div>
            </div>
        </fieldset>
    </form>
</div>
<!-- //.loginWrap -->
<!-- Local Script -->
<script type="text/javascript">
jQuery.event.add(window,"load",function(){
    $(document).ready(function(){
    });
});

function loginSubmit(e){
	e.preventDefault();
	
	var userIdChk = $('#userIdChk').val().trim();
	var pwdChk = $('#pwdChk').val().trim();
	
	if (userIdChk == null || userIdChk == undefined || userIdChk.length <=0){
		alert("아이디를 입력하여주세요");
		return ;
	}else if (pwdChk == null || pwdChk == undefined || pwdChk.length <=0){
		alert("비밀번호를 입력하여주세요");
		return ;
	}
	

	// rsa 암호화	
	var rsa = new RSAKey();
	rsa.setPublic($('#RSAModulus').val(), $('#RSAExponent').val());

	// 평문전송 방지 ID, PWD RSA 암호화
	$('#userId').val(rsa.encrypt(userIdChk));
	$('#pwd').val(rsa.encrypt(pwdChk));
	
	// 기존 입력데이터 초기화
	$('#userIdChk').val("");
	$('#pwdChk').val("");
	
	// 폼 전달
	var loginFrm = $("#loginForm");
	var actionURL = "/mindslab/loginProcess";
	
	loginFrm.attr("action", actionURL);
	loginFrm.submit();
}
</script>
</body>
</html>