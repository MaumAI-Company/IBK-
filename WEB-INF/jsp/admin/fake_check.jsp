<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ include file="/WEB-INF/jsp/include/header.jspf" %>
    <title>Admin (사전검증 수준 설정) || maum XDC</title>
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
            <div class="content fake_chk">
                <%@ include file="/WEB-INF/jsp/include/etcArea.jspf" %>

                <div class="titArea">
                    <h3>Admin <span>사전검증 수준(Threshold) 설정</span></h3>
                </div>

                <div class="srchArea"><!-- srchArea 20220512 추가, tblBox admin 클래스 제거 -->
                    <table summary="탐지 Level 설정">
                        <caption class="hide">탐지 Level 설정</caption>
                        <colgroup>
                            <col>
                        </colgroup>
                        <thead>
                        </thead>
                        <tbody>
                            <tr>
                            	<th>
                            		사전검증 수준(Threshold) 설정
                            	</th>
                                <td>
                                	<c:forEach var="list" varStatus="status" items="${detectionLevelList}" >
	              						<c:if test="${list.useStatus eq '0'}">
	                       					<input type="hidden" id="prevLevel" name="prevLevel" value="${list.levelStrng}" />
	              						</c:if>
              						</c:forEach>
                                    <select id="levelSelect" class="select">
                                    	<c:forEach var="list" varStatus="status" items="${detectionLevelList}" >
                                    		<c:choose>
                                   				<c:when test="${list.levelStrng eq '0.0'}"></c:when>
                                   				<c:when test="${list.levelStrng eq '0.6'}"></c:when>
                                   				<c:when test="${list.levelStrng eq '0.7'}"></c:when>
                                   				<c:when test="${list.levelStrng eq '0.8'}"></c:when>
                                   				<c:when test="${list.levelStrng eq '0.9'}"></c:when>
                                   				<c:when test="${list.levelStrng eq '1.0'}"></c:when>
                                   				<c:otherwise>
	                                    			<option value="${list.levelStrng}" <c:if test="${list.useStatus eq '0'}">selected="selected"</c:if>>
                                    					${list.levelStrng}
   	                                 				</option>
                                   				</c:otherwise>
                                   			</c:choose>
                                    	</c:forEach>
                                    </select>
                                </td>
                                <td>
                                	<button type="button" class="btn_primary fr" style="margin: 0;" onclick="javascript:ajaxSetLevel();">설정 저장</button>
                                </td>
                            </tr>
                            <tr>
                            	<th colspan="2">
			                        <span style="color: rgb(160, 160, 160);">
	                            		<br>사전검증 수준이 0.3인 경우, ‘미심의 광고문자’ 확률이 0.3 이상인 문자메시지를 검증 처리 합니다.
		                        		<br>부가 설명: 확률이 0.5이상인 경우는 threshold와 상관 없이 미심의 광고 문자로 처리됨 
			                        </span>
                            	</th>
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

<!-- 로딩  -->
<div id="wrap-loading" class="lyr_bg lyrWrap" style="display:none;">
    <img src="/images/loading.gif" alt="Loading..." style="border:0; position:absolute; left:50%; top:50%;" />
</div>
<iframe width=0 height=0 frameborder=0 scrolling=no name="hide_frame" id="hide_frame" style="margin:0"></iframe>
<!-- Local Script -->
<script type="text/javascript">
jQuery.event.add(window,"load",function(){
    $(document).ready(function(){
        // submenu active
        $("#sub_ID00004").addClass("active");//상위 메뉴
        $("#sub_ID00015").addClass("active");//하위 메뉴

        // 선택 Level에 따른 row 활성화 효과
        /*
        $('input[name="fake_chk_set"]').on('click', function(){
            $('input[name="fake_chk_set"]').parents('tr').removeClass('active');
            $(this).parents('tr').addClass('active');
        });
        */
    });
});

/* request parameter
	String prevSeq = params.get("prevSeq") == null ? "": (String) params.get("prevSeq") ;
	String changeSeq = params.get("changeSeq") == null ? "": (String) params.get("changeSeq") ;
	String sessionMemId = params.get("sessionMemId") == null ? "": (String) params.get("sessionMemId") ;
*/

//사용자 이미지 GET
function ajaxSetLevel(jsonData){
	var prevLevel = $('#prevLevel').val();
	//var changeLevel = $('input[name=fake_chk_set]:checked').val();
	var changeLevel = $('#levelSelect').val()
	
	if (prevLevel == null || prevLevel == undefined || prevLevel.length==0){// 이전 seq 값이 없다면 끔.
		prevLevel = "0.1";
	}
	
	if (prevLevel == changeLevel){
		alert('탐지 Level 변경 후 저장을 눌러주세요.');
		return;
	}  
	
	var jsonData = {
		prevLevel : prevLevel
		, changeLevel : changeLevel
		, sessionMemId : $('#sessionMemId').val()
	};
	$.ajax({
		url : "/admin/detection_level/change",
		type : "post",
		async : true,
		data : JSON.stringify(jsonData),
		dataType: "json",
	    contentType: "application/json",
	    timeout: 60000,// timeout : 60초간 서버와의 통신기다리는 시간.
	    cache: false,
		success : function(resp) {
			console.log("resp :: ", resp);
			if (changeLevel == 0){
				alert("위조탐지 기능이 중지 되었습니다.");
			}else {
				alert("위조탐지 레벨이 "+changeLevel+"로 설정되었습니다.");
			}
				
			location.href= "/admin/fake_check";
		},
		beforeSend: function() { //로딩이미지 보여주기
		    $("#wrap-loading").show();
		},
		complete: function() { //로딩이미지 숨기기
		    $("#wrap-loading").hide();
		},
		error : function(jqXHR, textStatus) {
	        if(textStatus === 'timeout'){     
	            console.log('timeout : 60 sec');
	        } else {
				console.log("error");
	        }
		}
	});
}

</script>
</body>
</html>