<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ include file="/WEB-INF/jsp/include/header.jspf" %>
    <title>Admin (부서 관리) || maum XDC</title>
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
            <div class="content admin_dptmt">
                <%@ include file="/WEB-INF/jsp/include/etcArea.jspf" %>

                <div class="titArea">
                    <h3>Admin <span>조직 관리 <em class="fas fa-angle-right"></em> 부서 관리</span></h3>
                </div>

                <div class="fl">
                    <div class="list_box">
                        <div class="tit">부서 목록</div>
                        <div class="tree_view_wrap prtmt">
                            <%-- 
                            [D] data-section으로 카테고리 분류  
                            <select id="treeSelector" multiple="multiple">
                                <option value="1" data-section="부서 목록">IT팀</option>
                                <option value="1" data-section="부서 목록">심사팀</option>
                                <option value="1" data-section="부서 목록">서비스관리</option>
                                <option value="1" data-section="부서 목록">준법감시팀</option>
                            </select>
                            --%>
	                        <%--  [D] 22.01.25 .tree_view_wrap에 id="menuList"추가와 .treeSelector(기존 트리 구조 마크업) 영역 제거 부탁드립니다. --%>
	                        <div id="deptList" class="tree_view_wrap menu"></div>
                        </div>
                    </div>
                    <div class="btnBox">
                        <button type="buttn" class="btn_primary line fl" onclick="javascript:deleteDept();">삭제</button>
                        <button type="buttn" class="btn_primary" onclick="javascript:addDeptOpen();">부서 추가</button>
                    </div>
                </div>

                <div class="fr">
                    <div class="tblBox admin" id="detail_layout">
                        <table summary="부서코드, 부서명, 부서명(영문), 부서 경로, 사용여부, 등록일, 수정일로 구성">
                            <caption class="hide">부서 정보 상세</caption>
                            <colgroup>
                                <col width="180px"><col>
                            </colgroup>
                            <thead>
                                <tr>
                                    <th colspan="2">부서 정보 상세</th>
                                </tr>
                            </thead>
                            <tbody>
								<input type="hidden" id="detail_deptId" name="detail_deptId" value="">
                                <input type="hidden" id="detail_deptCodePrev" name="detail_deptCodePrev" value="">
                                <tr>
                                    <th>부서코드</th>
                                    <td>
                                        <input type="text" id="detail_deptCode" name="detail_deptCode" class="ipt_txt" value="">
                                    </td>
                                </tr>
                                <tr>
                                    <th>부서명</th>
                                    <td>
                                        <input type="text" id="detail_deptName" name="detail_deptName" class="ipt_txt" value="">
                                    </td>
                                </tr>
                                <tr>
                                    <th>부서명(영문)</th>
                                    <td>
                                        <input type="text" id="detail_deptEngName" name="detail_deptEngName" class="ipt_txt" value="">
                                    </td>
                                </tr>
                                <tr>
                                    <th>부서 경로</th>
                                    <td id="detail_deptFullPath"></td>
                                    <%--
	                                    <c:choose>
		                                    <c:when test="${deptVO.deptPath ne 'nullval'}">
		                                    	<td>${deptVO.deptPath}</td>
		                                    </c:when>
		                                    <c:otherwise>
	                       						<td>IT팀 &gt; 1팀</td>
		                                    </c:otherwise>
	                                    </c:choose>
                                    --%>
                                </tr>
                                <tr>
                                    <th>사용여부</th>
                                    <td>
                                        <div class="radioBox">   
                                            <input type="radio" name="detail_deptStat" id="deptStat1" value="use" >
                                            <label for="deptStat1">사용</label>    
                                            <input type="radio" name="detail_deptStat" id="deptStat2" value="unused" >
                                            <label for="deptStat2">미사용</label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>등록일</th>
                                    <td id="detail_deptRegDt"></td>
                                </tr>
                                <tr>
                                    <th>수정일</th>
                                    <td id="detail_deptModDt"></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="btnBox">
                        <button type="button" class="btn_primary line" onclick="javascript:fnGetRevertDetailLayoutData();">되돌리기</button>
                        <button type="button" class="btn_primary" onclick="javascript:modDept();">수정</button>
                    </div>
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
<!-- 부서 추가 -->
<div id="add_dept" class="lyrWrap modify">
	<div id="add_dept_background" class="lyr_bg" style="display:none;"></div>
    <div class="lyrCont">
        <div class="lyr_top">
            <p class="tit">부서 추가</p>
            <button type="button" class="btn_lyr_close" onclick="addDeptClose();"><span class="hide">닫기버튼</span></button>
        </div>
        <div class="lyr_mid">
           <div class="tblBox">
               <table summary="부서 코드, 사용자 ID, 비밀번호, 이름, 성별, 이메일, 생년월일, 직급, 휴대폰, 주소, 사용여부, 부서로 구성">
                    <caption class="hide">부서 추가</caption>
                    <colgroup>
                        <col width="114px"><col width="253px"><col width="114px"><col>
                    </colgroup>
                    <tbody>
                        <tr>
                            <th rowspan="2">*상위 <br>부서 명/부서 코드</th>
                            <td colspan="3">
                                <input id="addParDeptId" name="addParDeptId" type="hidden">
                                <input id="addParDeptName" name="addParDeptName" type="text" class="ipt_txt" readonly="readonly">
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3">
                                <input id="addParDeptCode" name="addParDeptCode" type="text" class="ipt_txt dptmt_code" readonly="readonly">
                                <button type="button" class="btn_primary line btn_lyr_open" onclick="searchDeptOpen();">조회</button><!-- data-lyr-name="srch_dptmt" -->
                            </td>
                        </tr>
                         <tr>
                             <th>*부서코드</th>
                             <td colspan="3">
                                 <input id="addDeptCode" name="addDeptCode" type="text" class="ipt_txt">
                             </td>
                         </tr>
                         <tr>
                             <th>*부서명</th>
                             <td colspan="3">
                                <input id="addDeptName" name="addDeptName" type="text" class="ipt_txt">
                            </td>
                         </tr>
                         <tr>
                             <th>부서명(영문)</th>
                             <td colspan="3">
                                 <input id="addDeptEngName" name="addDeptEngName" type="text" class="ipt_txt">
                             </td>
                         </tr>
                        <tr>
                            <th>*사용여부</th>
                            <td colspan="3">
                                <div class="radioBox">   
                                    <input type="radio" id="addDeptStatUse" name="addDeptStat" value="use">
                                    <label for="addDeptStatUse">사용</label>    
                                    <input type="radio" id="addDeptStatUnused" name="addDeptStat" value="unused" >
                                    <label for="addDeptStatUnused">미사용</label>
                                </div>
                            </td>
                        </tr>
                    </tbody>
               </table>
           </div>
        </div>
        <div class="lyr_btm">
            <button id="add_dept_btn_cancle" type="button" class="btn_primary line btn_lyr_close" onclick="javascript:addDeptClose();">취소</button>
            <button type="button" class="btn_primary" onclick="javascript:addDept();">저장</button>
        </div>
    </div>
</div>
<!-- //부서 추가 -->

<!-- 부서 명/부서 ID 검색 -->
<div id="srch_dptmt" class="lyrWrap">
	<input type="hidden" id="srch_dptmt_type" name="srch_dptmt"/>
	<div id="srch_dptmt_background" class="lyr_bg" style="display:none;"></div>
	
	<input type="hidden" id="dept_currentPageNo" name="dept_currentPageNo"/> <!-- 신규 검색시 1 페이지로 이동 -->
	<input type="hidden" id="h_dept_currentPageNo" name="h_dept_currentPageNo">    	<!-- 신규 검색시 1 페이지로 이동 -->
	<input type="hidden" id="h_dept_recordsPerPage" name="h_dept_recordsPerPage"/> 	<!-- 한 페이지에 보여질 수 -->
	<input type="hidden" id="h_dept_pageSize" name="h_dept_pageSize"/>             	<!-- 페이지 블럭의 수 -->
	<input type="hidden" id="h_dept_searchType" name="h_dept_searchType" />           	<!-- 검색 종류 -->
	<input type="hidden" id="h_dept_searchKeyword" name="h_dept_searchKeyword" />  	    <!-- 검색 값 -->
    
    <div class="lyrCont">
        <div class="lyr_top">
            <p class="tit">부서 명/부서 코드 검색</p>
            <button type="button" class="btn_lyr_close" onclick="searchDeptClose();"><span class="hide">닫기버튼</span></button>
        </div>
        <div class="lyr_mid">
            <div class="srchArea">
                <select id="dept_searchType" name="dept_searchType" class="select">
                    <!-- option value="deptId">부서 아이디</option> -->
                    <option value="all">전체</option>
                    <option value="deptCode">부서코드</option>
                    <option value="deptName">부서명</option>
                    <option value="deptEngName">영문명</option>
                </select>
                <input id="dept_searchKeyword" name="dept_searchKeyword" type="text" class="ipt_txt" placeholder="검색어를 입력해주세요.">
                <button onclick="javascript:searchDept();" type="button" class="btn_primary line">검색</button>
            </div>

            <div class="tblBox icld_chk admin">
                <table summary="부서 명, 부서 명(영문), 부서 ID로 구성">
                    <caption class="hide">부서 검색 결과</caption>
                    <colgroup>
                        <col width="50px"><col><col><col>
                    </colgroup>
                    <thead>
                        <tr>
                            <th scope="col">
                                <div class="chkBoxOnly">
                                    <input type="checkbox" class="allChk">
                                </div>
                            </th>
                            <th scope="col">부서 코드</th>
                            <th scope="col">부서 명</th>
                            <th scope="col">부서 명(영문)</th>
                        </tr>
                    </thead>
                    <tbody id="srch_dptmt_tbody">
                    </tbody>
                </table>
            </div>
            <div id="deptPagingArea" class="pagingArea"></div>
        </div>
        <div class="lyr_btm">
            <button type="button" class="btn_primary line btn_lyr_close" onclick="javascript:searchDeptClose();">취소</button>
            <button type="button" class="btn_primary" onclick="javascript:chkBoxDeptSave();">저장</button>
        </div>
    </div>
</div>
<!-- //부서 명/부서 ID 검색 -->
<!-- Local Script -->
<script type="text/javascript">
jQuery.event.add(window,"load",function(){
    $(document).ready(function(){
        // submenu active
        $("#sub_ID00004").addClass("active");//상위 메뉴
        $("#sub_ID00012").addClass("active");//하위 메뉴
        $("#sub_ID00016").addClass("active");//하위 메뉴
		<%--
        // tree multiselect
        $('#treeSelector').treeMultiselect({
            startCollapsed: 'false',
            sectionDelimiter: '-',
        });

        // ie 호환 체크박스
        $('input.section').each(function(i){
            $(this).attr('id', 'chkbox' + i + 1);
            $(this).siblings('label').attr('for', 'chkbox' + i + 1);
        });
        --%>
    });
});
var treeList = ${result};	
//메뉴 트리를 object로 return 
function fnMakeDeptTreeJson(treeList, id) {
    var deptChildrenArr = new Array();
    var deptChildrenObj = new Object();
    var obj = new Object();
    if (treeList.LookUp('parId', id, 'deptId') == '') {
        obj.id = treeList.LookUp('deptId', id, 'deptId');
        obj.name = treeList.LookUp('deptId', id, 'deptName');
        return obj;
    } else {
        obj.id = treeList.LookUp('deptId', id, 'deptId');
        obj.name = treeList.LookUp('deptId', id, 'deptName');	    	
    	obj.children = fnMakeDeptTreeChildren(treeList, id);	
        return obj;
    }
}

//상위 메뉴트리가 메타 데이터에서 존재하면, 재귀처리
function fnMakeDeptTreeChildren(treeList, id) {
    var arr = new Array();
    treeList.forEach(function (obj, idx) {
        if (obj.parId == id) {
            var dept = new Object();
            dept.id = obj.deptId;
            dept.name = obj.deptName;
            if (treeList.LookUp('parId', obj.deptId, 'deptId') != '') {
            	dept.children = fnMakeDeptTreeChildren(treeList, obj.deptId);
            }
            arr.push(dept);
        }
    });
    return arr;
}

//메뉴 정보 상세 초기화
function fnClearDetailLayout(){
	var input = $('#detail_layout').find('input');
	//input의 text타입을 초기화한다.
	$.each(input,function(idx,obj){
	    var inputType = $(obj).attr('type');
	    if(inputType == 'text'){
	        $(obj).val('');
	        $(obj).prop('disabled',true);
	    }
	});
	//라디오 초기화
	$('input:radio[name="detail_deptStat"][value="use"]').prop('checked', true);
	$('input:radio[name="detail_deptStat"]').prop('disabled', true);
	//메뉴 정보 상세에서 사용하는 id가 있는 td초기화
	var td = $('#detail_layout').find('tr > td');
	//input의 text타입을 초기화한다.
	$.each(td,function(idx,obj){
	    var tdId = $(obj).attr('id');
	    if(tdId !== undefined){
	        $(obj).text('');
	    }    
	});
}

//메뉴 정보 상세 데이터
function fnGetDetailLayoutData(id,val){
	var input = $('#detail_layout').find('input');
	//input의 text타입을 초기화한다.
	$.each(input,function(idx,obj){
	    var inputType = $(obj).attr('type');
	    var inputId = $(obj).attr('id');
	    if(inputType == 'text' && inputId !== undefined && inputId.replace('detail_','') == id){
	        $(obj).val(val);
	        $(obj).prop('disabled',false);
	    }else if (inputType == 'hidden' && inputId !== undefined && inputId.replace('detail_','') == id){
	        $(obj).val(val);
	        $(obj).prop('disabled',false);
	    }
	});
	//이전 부서코드 지정
	$('#detail_deptCodePrev').val($('#detail_deptCode').val());

	//라디오 지정
	if(id == 'deptStat'){
		console.log('deptStat', val);
		$('input:radio[name="detail_deptStat"][value="'+val+'"]').prop('checked', true);
		$('input:radio[name="detail_deptStat"]').prop('disabled', false);
	}
	
	//메뉴 정보 상세에서 사용하는 id가 있는 td초기화
	var td = $('#detail_layout').find('tr > td');
	//input의 text타입을 초기화한다.
	$.each(td,function(idx,obj){
	    var tdId = $(obj).attr('id');
	    if(tdId !== undefined && (tdId.replace('detail_','') == id)){
	        $(obj).text(val);
	    }    
	});
}

//메뉴 상세정보 되돌리기
function fnGetRevertDetailLayoutData(){
	//선택이 되어 있지 않으면 false를 반환함.
	var node = $("#deptList").tree("getSelectedNode");
	if(node){
    	var deptId = node.id
        var keys = Object.keys(treeList[0]);
        $.each(keys,function(idx,key){
        	var id = key;
			var val = '';
        	if(id == 'deptRegDt'){
        		var deptDt = treeList.LookUp('deptId', deptId, 'deptRegDt');
        		var deptTime = treeList.LookUp('deptId', deptId, 'deptRegTime');
        		val = (DateUtils.convertDateKo(deptDt) + ' ' + DateUtils.convertTimeKo(deptTime));
        	}else if(id == 'deptModDt'){
        		var deptDt = treeList.LookUp('deptId', deptId, 'deptModDt');
        		var deptTime = treeList.LookUp('deptId', deptId, 'deptModTime');
        		val = (DateUtils.convertDateKo(deptDt) + ' ' + DateUtils.convertTimeKo(deptTime));
        	}else{
            	val = treeList.LookUp('deptId', deptId, key);
        	}
        	fnGetDetailLayoutData(id,val);
        });
	}
}

$(document).ready(function(){
	
	fnClearDetailLayout();
	//초기 root data 생성
	var data = new Array();
	var tree = new Array();
	//meta data 생성
	treeList.forEach(function (obj, idx) {
	    var deptObj = new Object();
	    //반복처리 하나의 겟을 만든다.
	    if (obj.parId === 'system') { 
	        deptObj.id = obj.deptId;
	        deptObj.name = obj.deptName;
	        tree.push(deptObj);
	    }
	});

	$.each(tree, function (idx, obj) {
	    var deptObj = new Object();
	    deptObj = fnMakeDeptTreeJson(treeList, obj.id);
	    data.push(deptObj);
	});
    
    var $jqtree = $("#deptList");
    $jqtree.tree({
        data: data,
        onCreateLi: function(node, $li) {
            // Add 'icon' span before title
            console.log('createLi : ',node);
        	var deptId = node.id
            var keys = Object.keys(treeList[0]);
            $.each(keys,function(idx,key){
            	var elId = 'li_'+key;
            	var elHtml = '<input type="hidden" id="" value="">';
            	elHtml = $(elHtml).attr('id',elId);
            	elHtml = $(elHtml).attr('value',treeList.LookUp('deptId', deptId, key));
            	$li.find('.jqtree-title').after(elHtml);
            });
        },
        autoOpen: true,
        usecontextdept: true            
    });
    
    $jqtree.on(
    	    'tree.select',
    	    function(event) {
    	        if (event.node) {
    	        	console.log('tree.select : ',event.node);
                	var deptId = event.node.id
                    var keys = Object.keys(treeList[0]);
                    $.each(keys,function(idx,key){
                    	var id = key;
						var val = '';
                    	if(id == 'deptRegDt'){
                    		var deptDt = treeList.LookUp('deptId', deptId, 'deptRegDt');
                    		var deptTime = treeList.LookUp('deptId', deptId, 'deptRegTime');
                    		val = (DateUtils.convertDateKo(deptDt) + ' ' + DateUtils.convertTimeKo(deptTime));
                    	}else if(id == 'deptModDt'){
                    		var deptDt = treeList.LookUp('deptId', deptId, 'deptModDt');
                    		var deptTime = treeList.LookUp('deptId', deptId, 'deptModTime');
                    		val = (DateUtils.convertDateKo(deptDt) + ' ' + DateUtils.convertTimeKo(deptTime));
                    	}else{
                        	val = treeList.LookUp('deptId', deptId, key);
                    	}
                    	fnGetDetailLayoutData(id,val);
                    });
    	        }else{
    	        	fnClearDetailLayout();
    	        }
    	    }
    	);        
});


function modDept(){
 	// 공백제거
	$('#detail_deptId').val($('#detail_deptId').val().trim()); // 부서 ID
	$('#detail_deptCodePrev').val($('#detail_deptCodePrev').val().trim()); // 이전 부서코드
	$('#detail_deptCode').val($('#detail_deptCode').val().trim()); // 부서코드
	$('#detail_deptName').val($('#detail_deptName').val().trim()); // 부서명
	$('#detail_deptEngName').val($('#detail_deptEngName').val().trim()); // 부서명(영문)
	
	// value Get
	var $detail_deptId = $('#detail_deptId').val();
	var $detail_deptCodePrev = $('#detail_deptCodePrev').val();
	var $detail_deptCode = $('#detail_deptCode').val();
	var $detail_deptName = $('#detail_deptName').val();
	var $detail_deptEngName = $('#detail_deptEngName').val();
	
	 // radio
	 var $detail_deptStat = $("input[name='detail_deptStat']:checked").val(); // 상태 
	 
	 // 변경될 CODE 값 관련
	 var $modDeptCode;
	 if ($detail_deptCode != $detail_deptCodePrev){
		 $modDeptCode = $detail_deptCode;
	 } else {
		 $modDeptCode = null;
	 }
	
	if(confirm("수정하시겠습니까?")){
		var jsonData = {
		    deptId : $detail_deptId
		    , deptCode : $modDeptCode
		    , deptName : $detail_deptName
		    , deptEngName : $detail_deptEngName
		    , deptStat : $detail_deptStat
		    , deptModId : $('#sessionMemId').val()
		};
		// 부서 수정 실행
		ajaxModDept(jsonData);
	}
	return;
}
//부서 수정
function ajaxModDept(jsonData){
	$.ajax({
		url : "/admin/department/mod",
		type : "post",
		async : false,
		data : jsonData,
		dataType: "json",
		success : function(data) {
			if ("SUCCESS" == data.status){
				alert(data.msg);
				location.href = "/admin/department";
			} else if ("EXIST" == data.status){
				alert(data.msg);
			} else if ("FAIL" == data.status){
				alert(data.msg);
			} else {
				alert('실패하였습니다.');
			}
		},
		error : function() {
			console.log("error");
		}
	});
}

function deleteDept(){
 	// 공백제거
	$('#detail_deptId').val($('#detail_deptId').val().trim()); // 부서 ID
	// value Get
	var $deptId = $('#detail_deptId').val();// 현재 선택된 부서를 삭제 하도록
	
	if ($deptId.length ==0 ){
		alert("삭제할 부서를 선택하여 주세요.");
		return;
	}
	
	if(confirm("삭제하시겠습니까?")){
		var jsonData = {
		    deptId : $deptId
		    , deptModId : $('#sessionMemId').val()
		};
		ajaxDeleteDept(jsonData);
	}
	return;
}

//부서 삭제
function ajaxDeleteDept(jsonData){
	$.ajax({
		url : "/admin/department/delete",
		type : "post",
		async : false,
		data : jsonData,
		dataType: "json",
		success : function(data) {
			if ("SUCCESS" == data.status){
				alert(data.msg);
				location.href = "/admin/department";
			} else if ("PARDEPT" == data.status){
				alert(data.msg);
			} else if ("FAIL" == data.status){
				alert(data.msg);
			} else {
				alert('실패하였습니다.');
			}
		},
		error : function() {
			console.log("error");
		}
	});
}


function modalOpenClose(modal, type){
	if (type == "open"){
		$("#"+modal).css("display","block");
		$("#"+modal+"_background").css("display","block");
		$("#"+modal+"_btn_cancle").toggleClass("btn_lyr_close_modal");
	} else if (type == "close"){
		$("#"+modal).css("display","none");
		$("#"+modal+"_background").css("display","none");
		$("#"+modal+"_btn_cancle").toggleClass("btn_lyr_close_modal");
	} 
}

//부서 등록 > 부서 관리 모달 오픈
function addDeptOpen(){
	// 등록 버튼누르면 기존 입력된 데이터 초기화 진행
	addDeptClear();
	$('#srch_dptmt_type').val('add');
	modalOpenClose("add_dept","open");
}

//부서 등록 > 부서 관리 모달 > 취소 (닫기)
function addDeptClose(){
	// 부서 관리 모달에서 취소 버튼누르면 기존 입력된 데이터 초기화 진행
	addDeptClear();
	modalOpenClose("add_dept","close");
}

//사용자 등록 데이터 초기화 
function addDeptClear(){
	// input 초기화
 $('#addParDeptId').val(''); // 부서 ID
 $('#addParDeptName').val('');
 $('#addParDeptCode').val(''); 
 
 $('#addDeptCode').val(''); // 부서코드
 $('#addDeptName').val(''); // 회원 이름
 $('#addDeptEngName').val(''); 
 
 // radio 초기화 - 첫번째 선택지
 $("input[name='addDeptStat'][value='use']").prop("checked", true);
}

function addDept(){
 	// 공백제거
	$('#addParDeptId').val($('#addParDeptId').val().trim()); // 상위 부서 ID
	
	$('#addDeptCode').val($('#addDeptCode').val().trim());
	$('#addDeptName').val($('#addDeptName').val().trim());
	$('#addDeptEngName').val($('#addDeptEngName').val().trim());
	
	// value Get
	var $addParDeptId = $('#addParDeptId').val();
	
	var $addDeptCode = $('#addDeptCode').val();
	var $addDeptName = $('#addDeptName').val();
	var $addDeptEngName = $('#addDeptEngName').val();
	
	 // radio
	 var $addDeptStat = $("input[name='addDeptStat']:checked").val(); // 상태 
	 
	// 필수조건
	if ($addParDeptId.length < 1){
	    alert("상위 부서를 선택해주세요.");
	    return ;
	} 
	if ($addDeptCode.length < 1){
	    alert("부서 코드를 입력해주세요.");
	    $('#addDeptCode').focus();
	    return ;
	}
	if ($addDeptName.length < 1){
	    alert("부서명을 입력해주세요.");
	    $('#addDeptName').focus();
	    return ;
	}
	
	var jsonData = {
	    parId : $addParDeptId
	    , deptCode : $addDeptCode
	    , deptName : $addDeptName
	    , deptEngName : $addDeptEngName
	    , deptStat : $addDeptStat
	    , deptRegId : $('#sessionMemId').val()
	    , deptModId : $('#sessionMemId').val()
	};
	// 부서 추가 실행
	ajaxAddDept(jsonData);
}

//부서 데이터 체크 
function ajaxAddDept(jsonData){
	$.ajax({
		url : "/admin/department/add",
		type : "post",
		async : false,
		data : jsonData,
		dataType: "json",
		success : function(data) {
			if ("SUCCESS" == data.status){
				alert(data.msg);
				addDeptClose();
				location.href = "/admin/department";
			} else if ("FAIL" == data.status){
				alert(data.msg);
			} else {
				alert('실패하였습니다.');
			}
		},
		error : function() {
			console.log("error");
		}
	});
}
//부서 조회창 오픈
function searchDeptOpen(){
	// 모달 오픈
	// 추가될 데이터 초기화
	
	var type = $('#srch_dptmt_type').val();
	
	$("#"+type+"ParDeptId").val("");
	$("#"+type+"ParDeptName").val("");
	$("#"+type+"ParDeptCode").val("");
	
	searchDeptClear(); // 신규 조회시 데이터 초기화
	modalOpenClose("srch_dptmt",'open');

	var jsonData = {
			currentPageNo : $('#dept_currentPageNo').val()
			, recordsPerPage : $('#h_dept_recordsPerPage').val()
			, pageSize : $('#h_dept_pageSize').val()
			, searchType : $('#h_dept_searchType').val()
			, searchKeyword : $('#h_dept_searchKeyword').val()
	};
	ajaxSearchDept(jsonData);
}

//부서 조회(검색)
function searchDept(){
	var jsonData = {
			currentPageNo : $('#dept_currentPageNo').val()
			, recordsPerPage : $('#h_dept_recordsPerPage').val()
			, pageSize : $('#h_dept_pageSize').val()
			, searchType : $('#dept_searchType').val()
			, searchKeyword : $('#dept_searchKeyword').val()
	};
	ajaxSearchDept(jsonData);
}

//부서 조회 페이지 이동
function searchDeptMove(pageNo){
	var jsonData = {
			currentPageNo : pageNo
			, recordsPerPage : $('#h_dept_recordsPerPage').val()
			, pageSize : $('#h_dept_pageSize').val()
			, searchType : $('#h_dept_searchType').val()
			, searchKeyword : $('#h_dept_searchKeyword').val()
	};
	ajaxSearchDept(jsonData);
}

//부서 조회 닫기
function searchDeptClose(){
	// 모달 클로즈
	searchDeptClear(); // 닫을 시 데이터 초기화
	modalOpenClose("srch_dptmt",'close');
}
//부서 목록 조회
function ajaxSearchDept(jsonData){
	// 페이지 및 목록 데이터 초기화
	$("#srch_dptmt_tbody").html('');
	$("#deptPagingArea").html('');
	
	var htmlPagination = '';
	var htmlDeptList = '';
	$.ajax({
		url : "/admin/user/getDeptList",
		type : "post",
		async : false,
		data : jsonData,
		success : function(data) {
			// 페이지 이동을 위한 데이터  세팅
			$('#dept_currentPageNo').val('1');      // 신규 검색시에 무조건 1페이지 부터 보여줌
			$('#h_dept_currentPageNo').val(data.currentPageNo);    // 현재 페이지
			$('#h_dept_recordsPerPage').val(data.recordsPerPage);   // 한 페이지에 보여질 데이터 갯수 5개(Modal)
			$('#h_dept_pageSize').val(data.pageSize);        // 한 페이지에 보여질 하단 페이지 블럭 갯수
			$('#h_dept_searchType').val(data.searchType);     // 검색 종류 : 부서 코드, 부서 명, 부서 영문명 / default : all
			$('#h_dept_searchKeyword').val(data.searchKeyword);     // 검색어 : 
			
			// 부서 목록 - data.deptList | 체크박스, 부서 명, 부서 명(영문), 부서 코드
			if (data.deptList.length <= 0){
				htmlDeptList += '<tr>';
				htmlDeptList += '    <td class="empty" scope="row" colspan="4">데이터가 없습니다.</td>';
				htmlDeptList += '</tr>';
			} else {
				for (var i = 0; i < data.deptList.length; i++){
					htmlDeptList += '<tr>';
					htmlDeptList += '    <td scope="row">';
					htmlDeptList += '        <div class="chkBoxOnly">';
					htmlDeptList += '            <input name="chkBoxDept" type="checkbox" class="eachChk" value="chkDept'+i+'">';
					htmlDeptList += '        </div>';
					htmlDeptList += '    </td>';
					htmlDeptList += '    <td>'+data.deptList[i].deptCode+'</td>';
					htmlDeptList += '    <td>'+data.deptList[i].deptName+'</td>';
					htmlDeptList += '    <td>'+nvl(data.deptList[i].deptEngName,'')+'</td>';
					htmlDeptList += '    <input id="chkDept'+i+'_deptId" type="hidden" value="'+data.deptList[i].deptId+'">';
					htmlDeptList += '    <input id="chkDept'+i+'_deptCode" type="hidden" value="'+data.deptList[i].deptCode+'">';
					htmlDeptList += '    <input id="chkDept'+i+'_deptName" type="hidden" value="'+data.deptList[i].deptName+'">';
					htmlDeptList += '    <input id="chkDept'+i+'_deptEngName" type="hidden" value="'+data.deptList[i].deptEngName+'">';
					htmlDeptList += '</tr>';
				}
			}	
			
			// 부서 페이지네이션 - data.params
	        htmlPagination += '<div class="paging">';
          if(data.hasPreviousPage){
				htmlPagination += '        <a href="javascript:searchDeptMove(1);" class="btn_paging first">';
				htmlPagination += '            <span class="hide">처음 페이지</span>';
				htmlPagination += '            <em class="fas fa-angle-double-left"></em>';
				htmlPagination += '        </a>';
				htmlPagination += '        <a href="javascript:searchDeptMove('+(data.firstPage-1)+');" class="btn_paging prev">';
				htmlPagination += '            <span class="hide">이전 페이지</span>';
				htmlPagination += '            <em class="fas fa-angle-left"></em>';
				htmlPagination += '        </a>';
          }
          for(var i = data.firstPage; i <= data.lastPage; i++){
				if (data.currentPageNo == i){
					htmlPagination += '            <a href="javascript:searchDeptMove('+i+');" class="current">'+i+'</a>';
				} else {
					htmlPagination += '            <a href="javascript:searchDeptMove('+i+');">'+i+'</a>';
				}
          }
          if (data.hasNextPage){
				htmlPagination += '        <a href="javascript:searchDeptMove('+(data.lastPage+1)+');" class="btn_paging next">';
				htmlPagination += '            <span class="hide">다음 페이지</span>';
				htmlPagination += '            <em class="fas fa-angle-right"></em>';
				htmlPagination += '        </a>';
				htmlPagination += '        <a href="javascript:searchDeptMove('+data.totalPageCount+');" class="btn_paging last">';
				htmlPagination += '            <span class="hide">마지막 페이지</span>';
				htmlPagination += '            <em class="fas fa-angle-double-right"></em>';
				htmlPagination += '        </a>';
          }
			htmlPagination += '</div>';
		},
		error : function() {
			console.log("error");
		}
	});
	$("#srch_dptmt_tbody").html(htmlDeptList);
	$("#deptPagingArea").html(htmlPagination);
}

//부서 조회 초기화
function searchDeptClear(){
	// 부서 검색 초기화
	$("#dept_searchType").val("all").attr("selected","selected");     // 검색 종류 : 부서 코드, 부서 명, 부서 영문명 / default : all
	$("#dept_searchKeyword").val("");     // 검색 종류 : 부서 코드, 부서 명, 부서 영문명 / default : all
	// 부서 검색 페이지 초기화
	$("#dept_currentPageNo").val("1");      // 신규 검색시에 무조건 1페이지 부터 보여줌
	$("#h_dept_currentPageNo").val("1");    // 현재 페이지
	$("#h_dept_recordsPerPage").val("5");   // 한 페이지에 보여질 데이터 갯수 5개(Modal)
	$("#h_dept_pageSize").val("10");        // 한 페이지에 보여질 하단 페이지 블럭 갯수
	$("#h_dept_searchType").val("all");     // 검색 종류 : 부서 코드, 부서 명, 부서 영문명 / default : all
	$("#h_dept_searchKeyword").val("");     // 검색어 : 
}

//부서 ID 입력
function chkBoxDeptSave(){
	//type : add, mod
	var type = $('#srch_dptmt_type').val();
	
	// 체크박스 검사	
	var chkCnt = $("input[name='chkBoxDept']:checked").length;
	if (chkCnt<=0){
		alert("하나의 부서를 선택해주세요.");
		return;
	} else if (chkCnt>1){
		alert("하나의 부서를 선택해주세요.");
		return;
	}else if (chkCnt == 1){
		// 하나의 부서가 선택되었음
		$("input[name='chkBoxDept']:checked").each(function(){
			var chkDeptId = $(this).val();
			console.log($("#"+chkDeptId+"_deptId").val());
			console.log($("#"+chkDeptId+"_deptCode").val());
			console.log($("#"+chkDeptId+"_deptName").val());
			console.log($("#"+chkDeptId+"_deptEngName").val());
			
			$("#"+type+"ParDeptId").val($("#"+chkDeptId+"_deptId").val());
			$("#"+type+"ParDeptName").val($("#"+chkDeptId+"_deptName").val());
			$("#"+type+"ParDeptCode").val($("#"+chkDeptId+"_deptCode").val());
			
			searchDeptClose();
			return;
		});
		
	}
}

</script>
</body>
</html>