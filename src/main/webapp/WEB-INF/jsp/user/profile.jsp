<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ include file="/WEB-INF/jsp/include/header.jspf" %>
    <title>프로필 || maum XDC</title>
</head>
<body>
<!-- #wrap -->
<div id="wrap">
    <!-- #container -->
    <div id="container">
        <!-- #snb -->
        <%@ include file="/WEB-INF/jsp/include/submenu.jspf" %>
        <!-- //#snb -->

        <!-- #contents -->
        <div id="contents">
            <!-- .content -->
            <div class="content profile">
                <%@ include file="/WEB-INF/jsp/include/etcArea.jspf" %>

                <div class="titArea">
                    <h3>프로필</h3>
                </div>

                <div class="tblBox admin">
                    <table summary="프로필 정보 상세">
                        <caption class="hide">프로필 정보 상세</caption>
                        <colgroup>
                            <col width="180px"><col>
                        </colgroup>
                        <thead>
                            <tr>
                                <th colspan="2">프로필 정보 상세</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <th>사용자 성명</th>
                                <td><c:out value="${memberVO.memName}"/></td>
                            </tr>
                            <tr>
                                <th>ID</th>
                                <td><c:out value="${memberVO.memId}"/></td>
                            </tr>
                            <tr>
                                <th>사용자 그룹</th>
                                <td><c:out value="${deptVO.deptName}"/></td>
                            </tr>
                            <tr>
                                <th>암호변경</th>
                                <td>
                                	<button type="button" class="btn_primary line btn_lyr_open" data-lyr-name="modify_pw"  onclick="javascript:modalOpenClose('open')">암호변경</button>
                                	<span id="pwdMsg" style="display:none;"></span>
                                </td>
                            </tr>
                            <tr style="height:30px;">
                                <th colspan=2>
                                <span style="display:inline;color:red;vertical-align:sub;">*</span>
                                <span style="display:inline;color:grey;vertical-align:sub;font-size:0.9em;">패스워드는 하나 이상의 대문자, 소문자, 숫자, 특수문자를 포함한 8글자에서 12글자 내로 이루어져아합니다. </span>
                                </th>
                                <!-- 
                                <td>
                                	<span id="pwdInfo" style="display:inline;color:red;vertical-align:sub;">ss</span>
                                </td>
                                 -->
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <!-- //.content -->
        </div> 
        <!-- //#contents -->
    </div>
    <!-- //#container -->
</div>
<!-- //#wrap -->

<!---------------------- layer popup ---------------------->
<!-- 암호 변경 -->
<form id="frmProfile" name="frmProfile">
	<input type="hidden" id="RSAModulus" value="${RSAModulus}"/>
	<input type="hidden" id="RSAExponent" value="${RSAExponent}"/>
	<input type="hidden" id="userId" name="userId" value="<c:out value='${sessionMember.memId}' />" />
	
	<div id="modify_pw" class="lyrWrap">
	    <div class="lyrCont">
	        <div class="lyr_top">
	            <p class="tit">암호 변경</p>
	            <button type="button" class="btn_lyr_close" onclick="javascript:modalOpenClose('close')"><span class="hide">닫기버튼</span></button>
	        </div>
	        <div class="lyr_mid">
	            <dl>
	                <dt>기존 암호</dt>
	                <dd><input type="password" id="chkPwd" name="chkPwd" class="ipt_txt" maxlength="12"></dd>
	            </dl>
	            <dl>
	                <dt>새 암호</dt>
	                <dd><input type="password" id="newPwd" name="newPwd" class="ipt_txt" maxlength="12"></dd>
	            </dl>
	            <dl>
	                <dt>새 암호 확인</dt>
	                <dd><input type="password" id="newPwdConfirm" name="newPwdConfirm" class="ipt_txt" maxlength="12"></dd>
	            </dl>
	        </div>
	        <div class="lyr_btm">
	            <button type="button" id="btnCancle" class="btn_primary line btn_lyr_close" onclick="javascript:modalOpenClose('close')">취소</button>
	            <button type="button" id="btnSave" class="btn_primary" onclick="changePassword();">저장</button>
	        </div>
	    </div>
	</div>
</form>
<!-- //암호 변경 -->

<!-- Local Script -->
<script type="text/javascript">
jQuery.event.add(window,"load",function(){
    $(document).ready(function(){
        // submenu active
        //$("#").addClass("active");//상위 메뉴
        //$("#").addClass("active");//하위 메뉴
        
    	// 암호변경 레이어
    	// 오픈 class : id=modify_pw || display:block;
    	// 닫힘 class : id=modify_pw || display:none;
    });
    
});

function modalOpenClose(type){
	if (type == 'open' ){
        $('#modify_pw').css('display', 'block');
        $('#modify_pw').prepend('<div class="lyr_bg"></div>');
	} else if (type == 'close'){
        $('#modify_pw').css('display', 'none'); 
        $('#modify_pw').find('.lyr_bg').remove(); 
	}
}

function changePassword(){
	var $userId = $('#userId').val();
	var $chkPwd = $('#chkPwd').val();
	var $newPwd = $('#newPwd').val();
	var $newPwdConfirm = $('#newPwdConfirm').val();

	// 1. 암호 규칙 체크 필요
	
	// 2. 변경할 암호가 일치하는지 확인필요
	
	// 입력 안한 경우 체크
	if ($chkPwd.length <= 0){
		alert('기존 암호를 입력해주세요');
		$('#chkPwd').focus();
		return;
	} else if ($newPwd.length <= 0){
		alert('새 암호를 입력해주세요');
		$('#newPwd').focus();
		return;
	} else if ($newPwdConfirm.length <= 0){
		alert('새 암호 확인을 입력해주세요');
		$('#newPwdConfirm').focus();
		return;
	}
	
	var passMin = 8;
	var passMax = 12; 
	
	// 입력한 경우 새로운 비밀번호는 8자리 이상인지 12자리 이하인지 체크.
	// 패스워드는 하나 이상의 대문자, 소문자, 숫자, 특수문자를 포함한 8글자에서 12글자 내로 이루어져아합니다.
	if (!($newPwd.length >= passMin && $newPwd.length <= passMax)){
		alert('새 암호의 길이는 '+passMin+'글자 이상 '+passMax+'이하로 이루어져야합니다.');
		$('#newPwd').focus();
		return;
	} else if (!($newPwdConfirm.length >= passMin && $newPwdConfirm.length <= passMax)){
		alert('새 암호의 길이는 '+passMin+'글자 이상 '+passMax+'이하로 이루어져야합니다.');
		$('#newPwdConfirm').focus();
		return;
	}
	
	// 입력한 경우 체크 동일 암호 체크	
	if ($newPwd != $newPwdConfirm){
		alert('암호가 일치하지 않습니다');
		$('#newPwdConfirm').focus();
		return;
	} else {
		// 3. 암호 변경 Ajax 요청 수행
		
	    // rsa 암호화	
		var rsa = new RSAKey();
		rsa.setPublic($('#RSAModulus').val(), $('#RSAExponent').val());

		var jsonData = {
				"userId" : rsa.encrypt($userId)
				, "chkPwd" : rsa.encrypt($chkPwd)
				, "newPwd" : rsa.encrypt($newPwd)
		};
		
		if (confirm('암호를 변경하시겠습니까?')){
			ajaxChangePassword(jsonData);
			// 모달창 close 및 입력한 정보 초기화
			$('#modify_pw').css('display','none');
			$('#chkPwd').val('');
			$('#newPwd').val('');
			$('#newPwdConfirm').val('');
		}
	}
}

function ajaxChangePassword(jsonData){
	// 암호 변경 Ajax
	var result = "";

	$.ajax({
		url : "/user/changeUserPassword",
		type : "post",
		async : false,
		data : JSON.stringify(jsonData),
		dataType: "json",
        contentType: "application/json",
		success : function(data) {
			console.log(data);
			result = data;
			if ("SUCCESS" == data.status){
				$("#pwdMsg").css("color", "green");
				$("#pwdMsg").css("vertical-align", "sub");
				$("#pwdMsg").css("display", "inline");
				$('#pwdMsg').html(data.msg);
			} else {
				$("#pwdMsg").css("color", "red");
				$("#pwdMsg").css("vertical-align", "sub");
				$("#pwdMsg").css("display", "inline");
				$('#pwdMsg').html(data.msg);
			}
		},
		error : function() {
			console.log("error");
		}
	});
	return result;
}

</script>
</body>
</html>