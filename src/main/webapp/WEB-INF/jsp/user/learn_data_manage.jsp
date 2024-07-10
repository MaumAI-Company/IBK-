<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ include file="/WEB-INF/jsp/include/header.jspf" %>
    <title>학습 관리(데이터 관리) || maum XDC</title>
	<script src="/js/dropzone.js"></script>
	<link rel="stylesheet" href="/css/dropzone.css" type="text/css" />
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
            <div class="content">
                <%@ include file="/WEB-INF/jsp/include/etcArea.jspf" %>

                <div class="titArea">
                    <h3>학습 관리<span>데이터 관리</span></h3>
                </div>

                <div class="srchArea">
					<input type="hidden" id="currentPageNo" name="currentPageNo" value="1"/>    						<!-- 신규 검색시 1 페이지로 이동 -->
					<input type="hidden" id="h_currentPageNo" name="h_currentPageNo" value="${params.currentPageNo}"/>    	<!-- 신규 검색시 1 페이지로 이동 -->
                   	<input type="hidden" id="h_recordsPerPage" name="h_recordsPerPage" value="${params.recordsPerPage}"/> 	<!-- 한 페이지에 보여질 수 -->
                    <input type="hidden" id="h_pageSize" name="h_pageSize" value="${params.pageSize}"/>             		<!-- 페이지 블럭의 수 -->
					<input type="hidden" id="h_searchType" name="h_searchType" value="${params.searchType}"/>           	<!-- 검색 종류 -->
					<input type="hidden" id="h_searchKeyword" name="h_searchKeyword" value="${params.searchKeyword}"/>  	<!-- 검색 값 -->
					<input type="hidden" id="h_learnStatus" name="h_learnStatus" value="${params.learnStatus}"/>           	<!-- 상태 -->
					<input type="hidden" id="h_answerStatus" name="h_answerStatus" value="${params.answerStatus}"/>  	<!-- 학습 가능 유무 -->
					<input type="hidden" id="h_learnFileUploadDtOrd" name="h_learnFileUploadDtOrd" value="${params.learnFileUploadDtOrd}"/>  	<!-- 검색 값 -->
					<input type="hidden" id="h_searchStartDate" name="h_searchStartDate" value="${params.searchStartDate}"/>           	<!-- 조회기간 S -->
					<input type="hidden" id="h_searchEndDate" name="h_searchEndDate" value="${params.searchEndDate}"/>  	<!-- 조회기간 E -->
					
                    <table summary="시스템 사용여부, 조건 선택으로 구성">
                        <caption class="hide">검색조건</caption>
                        <tbody>
                            <tr>
                                <th>조회기간</th>
                                <td>
                                    <div class="iptBox date">
                                        <input type="text" class="ipt_txt fromDate" id="searchStartDate" name="searchStartDate" value="${params.searchStartDate }" readonly="readonly">
                                    </div>                            
                                    <em class="tilde">~</em>
                                    <div class="iptBox date">
                                        <input type="text" class="ipt_txt toDate" id="searchEndDate" name="searchEndDate" value="${params.searchEndDate }" readonly="readonly">
                                    </div>
                                </td>
                                <th>상태</th>
                                <td>
                                   <select id="learnStatus" class="select">
									    <option value="all" <c:if test="${empty params.learnStatus or params.learnStatus eq 'all'}">selected="selected"</c:if>>전체</option>
									    <option value="finish" <c:if test="${params.learnStatus eq 'finish'}">selected="selected"</c:if>>학습완료</option>
									    <option value="learning" <c:if test="${params.learnStatus eq 'learning'}">selected="selected"</c:if>>학습중</option>
									    <option value="regist" <c:if test="${params.learnStatus eq 'regist'}">selected="selected"</c:if>>등록완료</option>
									    <option value="error" <c:if test="${params.learnStatus eq 'error'}">selected="selected"</c:if>>오류</option>
                                    </select>
                                </td> 
                           	</tr>
                            <tr>
                                <th>학습여부</th>
                                <td>
                                   <select id="answerStatus" class="select">
									    <option value="all" <c:if test="${empty params.answerStatus or params.answerStatus eq 'all'}">selected="selected"</c:if>>전체</option>
									    <option value="possible" <c:if test="${params.answerStatus eq 'possible'}">selected="selected"</c:if>>가능</option>
									    <option value="impossible" <c:if test="${params.answerStatus eq 'impossible'}">selected="selected"</c:if>>불가</option>
                                    </select>
                                </td>     
                                <th>정렬조건</th>
                                <td>
                                   <select id="learnFileUploadDtOrd" class="select">
									    <option value="asc" <c:if test="${empty params.learnFileUploadDtOrd or params.learnFileUploadDtOrd eq 'asc'}">selected="selected"</c:if>>오름차순</option>
									    <option value="desc" <c:if test="${params.learnFileUploadDtOrd eq 'desc'}">selected="selected"</c:if>>내림차순</option>
                                    </select>
                                </td>   
                                <th>대상</th>
                                <td>
                                   <select id="searchType" class="select">
                                        <option value="all" <c:if test="${empty params.searchType or params.searchType eq 'all'}">selected="selected"</c:if>>전체</option>
                                        <option value="learn" <c:if test="${params.searchType eq 'learn'}">selected="selected"</c:if>>학습데이터명</option>
                                        <option value="em" <c:if test="${params.searchType eq 'em'}">selected="selected"</c:if>>등록자</option>
                                    </select>
                                    <input id="searchKeyword" type="text" class="ipt_txt" placeholder="검색어를 입력해주세요." value="${params.searchKeyword}">
                                </td>  
                                <td>
                                    <button type="button" class="btn_primary" onclick="javascript:search();">검색</button> 
                                </td>                 
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="btnBox">
                    <button type="button" class="btn_primary btn_lyr_open" onclick="javascript:addLearnOpen();">등록</button>
                    <c:choose>
                    	<c:when test="${learningCount > 0}">
                    		<button type="button" class="btn_primary line" onclick="javascript:ajaxLearnStatusChk();">학습 상태확인</button>
                    		<button type="button" class="btn_primary line" onclick="javascript:ajaxLearnStatusChk('stop');">학습 중지</button>
                    	</c:when>
                    	<c:otherwise>
                    		<button type="button" class="btn_primary btn_lyr_open" onclick="javascript:addAnswerOpen();">학습</button>
                    		<button type="button" class="btn_primary line" onclick="javascript:deleteChk();">삭제</button>
                    	</c:otherwise>
                    </c:choose>
                    <button type="button" class="btn_excel fr" onclick="javascript:templateDown();">템플릿 다운로드</button>
                </div>
                <%-- --------------------------------------- --%>

                <div class="tblBox">
                    <table summary="연번, 실행 일시, 구분, 확인 결과, 전행고객번호, 상세보기로 구성">
                        <caption class="hide">학습 데이터</caption>
                        <thead>
                            <tr>
                                <th scope="col">
                                    <div class="chkBoxOnly">
                                        <input type="checkbox" class="allChk">
                                    </div>
                                </th>
                                <th scope="col">No.</th>
                                <th scope="col">학습데이터명</th>
                                <th scope="col">등록자</th>
                                <th scope="col">상태</th>
                                <th scope="col">학습여부</th>
                                <th scope="col">등록일시</th>
                            </tr>
                        </thead>
                        <tbody>
                            <input type="hidden" id="chkLearnId" name="chkLearnId" value="" />
                        	<c:choose>
                        		<c:when test="${empty learnList }">
		                            <tr>
		                                <td class="empty" scope="row" colspan="7">데이터가 없습니다.</td>
		                            </tr>
                        		</c:when>
                        		<c:when test="${!empty learnList }">
                        			<c:forEach var="list" varStatus="status" items="${learnList}" >
			                            <tr>
			                                <td scope="row">
			                                    <div class="chkBoxOnly">
			                                        <input name="chkBoxLearn" type="checkbox" class="eachChk" value="${list.id}">
			                                        <input type="hidden" id="learnStatus_${list.id}" value="${list.learnStatus}">
			                                        <input type="hidden" id="learnName_${list.id}" value="${list.learnName}">
			                                    </div>
			                                </td>
			                                <!-- ((현재 페이지 번호-1)*페이지별 보여지는 수량) + 현재 순번 -->
			                                <td>${((params.currentPageNo-1)*params.recordsPerPage) + status.count}</td>
			                                <td>${list.learnName }</td>
			                                <td>${list.memName }</td>
			                                <td>
			                                	<c:choose>
			                                		<c:when test="${list.learnStatus eq '4'}"><span class="ft_st_en">학습완료</span></c:when>
			                                		<c:when test="${list.learnStatus eq '3'}"><span class="ft_st_in" id="learnTag_${list.id}">학습중</span></c:when>
			                                		<c:when test="${list.learnStatus eq '2'}"><span class="ft_st_en">등록완료</span></c:when>
			                                		<c:when test="${list.learnStatus eq '1'}"><span class="ft_st_er">오류</span></c:when>
			                                		<c:otherwise>-</c:otherwise>
		                                		</c:choose>
			                                </td>
			                                <td>
			                                	<c:choose>
			                                		<c:when test="${list.learnStatus ne '3' and list.learnStatus ne '0'}">가능</c:when>
			                                		<c:when test="${list.learnStatus eq '3'}"><span class="ft_point_pink">불가</span></c:when>
			                                		<c:otherwise>-</c:otherwise>
		                                		</c:choose>
		                                	</td>
			                                <td><fmt:formatDate value="${list.learnFileUploadDt }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			                            </tr>
                        			</c:forEach>
                        		</c:when>
                        	</c:choose>
                        </tbody>
                    </table>
                </div>

                 <div id="scanPagingArea" class="pagingArea">
                 	<div>
                 		<span style="font-size: 12px;">총 : <font style="color: red;"><c:out value="${params.paginationInfo.totalRecordCount}"/></font> 건 | 페이지당</span>
                 		<select id="recordsPerPage" name="recordsPerPage" class="select" style="min-width: 70px;">
						    <option value="10" <c:if test="${empty params.recordsPerPage or params.recordsPerPage eq '10'}">selected="selected"</c:if>>10</option>
						    <option value="20" <c:if test="${params.recordsPerPage eq '20'}">selected="selected"</c:if>>20</option>
						    <option value="30" <c:if test="${params.recordsPerPage eq '30'}">selected="selected"</c:if>>30</option>
						    <option value="40" <c:if test="${params.recordsPerPage eq '40'}">selected="selected"</c:if>>40</option>
						    <option value="50" <c:if test="${params.recordsPerPage eq '50'}">selected="selected"</c:if>>50</option>
						</select>
					</div>
					<div class="paging">
						<c:if test="${params.paginationInfo.hasPreviousPage}">
							<a href="javascript:movePage('/user/learn_data_manage', '${params.makeQueryString(1)}');" class="btn_paging first">
	                            <span class="hide">처음 페이지</span>
	                            <em class="fas fa-angle-double-left"></em>
	                        </a>
							<a href="javascript:movePage('/user/learn_data_manage', '${params.makeQueryString(params.paginationInfo.firstPage-1)}');" class="btn_paging prev">
	                            <span class="hide">이전 페이지</span>
	                            <em class="fas fa-angle-left"></em>
	                        </a>
                        </c:if>
                        
                       	<c:forEach begin="${params.paginationInfo.firstPage}" end="${params.paginationInfo.lastPage}" var="pageNo">
                        	<!-- [D] 현재 페이지 인덱스에 addClass('current') 해주면 활성화됨 -->
							<a href="javascript:movePage('/user/learn_data_manage', '${params.makeQueryString(pageNo)}');"  class="<c:out value="${params.currentPageNo == pageNo ? 'current' : ''}"/>">${pageNo}</a>
                       	</c:forEach>
						<c:if test="${params.paginationInfo.hasNextPage}">
							<a href="javascript:movePage('/user/learn_data_manage', '${params.makeQueryString(params.paginationInfo.lastPage+1)}');" class="btn_paging next">
	                            <span class="hide">다음 페이지</span>
	                            <em class="fas fa-angle-right"></em>
	                        </a>
							<a href="javascript:movePage('/user/learn_data_manage', '${params.makeQueryString(params.paginationInfo.totalPageCount)}');" class="btn_paging last">
	                            <span class="hide">마지막 페이지</span>
	                            <em class="fas fa-angle-double-right"></em>
	                        </a>
                        </c:if>
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
<!-- 등록-->
<div id="add_user" class="lyrWrap modify">
	<div id="add_user_background" class="lyr_bg" style="display:none;"></div>
    <div class="lyrCont">
        <div class="lyr_top">
            <p class="tit">등록</p>
            <button type="button" class="btn_lyr_close" onclick="javascript:addLearnClose();"><span class="hide">닫기버튼</span></button>
        </div>
        <div class="lyr_mid">
                <div class="workArea">
                    <div class="uploadBox" style="height:240px">
                        <form id="file-upload-form" action="/user/learn_data_fileupload" enctype="multipart/form-data">
                        	<div class="dropzone" style="min-height:240px" id="fileDropzone"></div>                         	
                        	<%--
                            <!-- [D] 파일 선택 시 .upload_form_box에 removeClass('active')해주면 초기 UI 사라짐 -->
                            <!-- [D] 위 내용과 반대로 모든 파일을 삭제 할 경우 addClass('active')해주면 초기 UI 나타남 -->
                            <div class="upload_form_box active">                                           
                                <input id="file-upload" type="file" name="fileUpload" multiple>
                                <label for="file-upload" id="file-drag">
                                    <img src="/images/ico_fileUpload.svg" alt="upload image">
                                    <span class="btn_file_add">업로드</span><br>
                                    ‘업로드’ 버튼을 클릭하여 파일을 선택하거나, Drag & Drop 하세요.
                                </label>
                                <progress id="file-progress" value="0">
                                    <span>0</span>
                                </progress>                                           
                            </div>

                            <output for="file-upload">
                                <!-- [D] 선택 된 파일명이 추가되는 곳 -->
                                <ul id="messages">
                                </ul>
                            </output> 
                            --%>
                        </form>
                    </div>
                </div>
        </div>
        <div class="lyr_btm">
            <button type="button" class="btn_primary line btn_lyr_close" onclick="javascript:addLearnClose();">취소</button>
            <button type="button" class="btn_primary" id="btn-upload-file" >저장</button>
        </div>
    </div>
</div>

<!-- 학습 -->
<div id="add_menu" class="lyrWrap modify">
	<div id="add_menu_background" class="lyr_bg" style="display:none;"></div>
    <div class="lyrCont">
        <div class="lyr_top">
            <p class="tit">학습</p>
            <button type="button" class="btn_lyr_close" onclick="javascript:addAnswerClose();"><span class="hide">닫기버튼</span></button>
        </div>
        <div class="lyr_mid">
            <div class="tblBox">
            <form id="file-upload-form2" action="/user/answer_data_fileupload" enctype="multipart/form-data">
                <table summary="">
                     <caption class="hide">학습</caption>
                     <colgroup>
                         <col width="30%"><col>
                     </colgroup>
                     <tbody>
                         <tr>
                             <th rowspan="2">Epoch</th>
                             <td>
                                 <input id="epoch" name="epoch" type="text" class="ipt_txt" placeholder="3 (default)" onkeypress="return isNumberKey(event);" onkeyup="this.value=this.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g, '');">
                             </td>
                        </tr>
                        <tr>
                             <td>* Epoch : 학습 데이터셋을 학습하는 회수</td>
                         </tr>
                         <tr>
                             <th rowspan="2">Learning Rate</th>
                             <td>
                                <input id="learningRate" name="learningRate" type="text" class="ipt_txt" placeholder="0.000001 (default)" onkeypress="return isNumberKey(event);" onkeyup="this.value=this.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g, '');">
                            </td>
                         </tr>
                          <tr>
                             <td>* * Learning Rate : 학습률 , 최적 값을 찾기 위한 기울기의 이동 정도</td>
                         </tr>
                         <tr>
                             <th rowspan="2">Batch Size</th>
                             <td>
                                 <input id="batchSize" name="batchSize" type="text" class="ipt_txt" placeholder="4 (default)" onkeypress="return isNumberKey(event);" onkeyup="this.value=this.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g, '');">
                             </td>
                         </tr>
                          <tr>
                             <td>* * Batch Size : 학습 데이터셋 중 , 몇 개의 데이터를 묶어서 가중치
값을 갱신할 지 지정하는 단위</td>
                         </tr>
                         <tr>
                            <th>정답셋</th>
                            <td>
                                <%--<button type="button" class="btn_excel ">등록</button>--%>
			                    <div class="uploadBox" style="height:240px; max-width: 400px;">
			                        <%-- <form id="file-upload-form2" action="/user/answer_data_fileupload" enctype="multipart/form-data">--%>
			                        	<div class="dropzone" style="min-height:240px; max-width: 400px;" id="fileDropzone2"></div>
			                        <%-- </form> --%>
			                    </div>
                            </td>
                      
                        </tr>
                     </tbody>
                </table>
            </form>
            </div>
        </div>
        <div class="lyr_btm">
            <button type="button" class="btn_primary line btn_lyr_close" onclick="javascript:addAnswerClose();">취소</button>
            <button type="button" class="btn_primary" id="btn-upload-file2">학습실행</button>
        </div>
    </div>
</div>
<!---------------------- //layer popup ---------------------->
<!-- 로딩  -->
<div id="wrap-loading" class="lyr_bg lyrWrap" style="display:none;">
    <img src="/images/loading.gif" alt="Loading..." style="border:0; position:absolute; left:50%; top:50%;" />
</div>
<iframe width=0 height=0 frameborder=0 scrolling=no name="hide_frame" id="hide_frame" style="margin:0"></iframe>
<!-- //로딩  -->
<!-- Local Script -->
<script type="text/javascript">
jQuery.event.add(window,"load",function(){
    $(document).ready(function(){
        // submenu active
        $("#sub_ID00002").addClass("active");//상위 메뉴
        $("#sub_ID00008").addClass("active");//하위 메뉴
        
        // datepicker
        var thisFromDate = $(this).find('.fromDate'),
            thisToDate = $(this).find('.toDate');

        thisFromDate.datetimepicker({
            format: 'yyyy-mm-dd', 
            minView: 'month',
            pickTime : false, 
            autoclose: 1,
        }).on('changeDate', function(selected){
            var startDate = new Date(selected.date.valueOf());
            thisToDate.datetimepicker('setStartDate', startDate);
        }).on('clearDate', function(selected){
            thisToDate.datetimepicker('setStartDate', null);
        });

        thisToDate.datetimepicker({
            format: 'yyyy-mm-dd', 
            minView: 'month',
            pickTime : false, 
            autoclose: 1,
        }).on('changeDate', function(selected){
            var endDate = new Date(selected.date.valueOf());
            thisFromDate.datetimepicker('setEndDate', endDate);
        }).on('clearDate', function(selected){
            thisFromDate.datetimepicker('setEndDate', null);
        });
                
    });
});


//페이지 이동
function movePage(uri, queryString){
	//queryString += "&recordsPerPage=" + $('#h_recordsPerPage').val();
	queryString += "&learnFileUploadDtOrd=" + $('#h_learnFileUploadDtOrd').val();
	queryString += "&learnStatus=" + $('#h_learnStatus').val();
	queryString += "&answerStatus=" + $('#h_answerStatus').val();
	location.href = uri + queryString;	
}

//사용자 목록 조회
function search(){
	
	var startDate = $('#searchStartDate').val();
	var endDate = $('#searchEndDate').val();
	
	var s = new Date(startDate);
	var e = new Date(endDate);
	
	if (s>e){
		alert('조회기간이 올바르지 않습니다.');
		return;
	}
	
	var uri = "/user/learn_data_manage";
	var queryString = "?currentPageNo=" + $('#currentPageNo').val()
			+ "&recordsPerPage=" + $('#recordsPerPage').val() // 현재 선택된 레코드 수로 검색
			+ "&pageSize=" + $('#h_pageSize').val()
			+ "&searchKeyword=" + $('#searchKeyword').val()
			+ "&searchStartDate=" + startDate
			+ "&searchEndDate=" + endDate
			+ "&searchType=" + $('#searchType').val()
			+ "&learnStatus=" + $('#learnStatus').val()// 상태
			+ "&answerStatus=" + $('#answerStatus').val()// 배포여부 
			+ "&learnFileUploadDtOrd=" + $('#learnFileUploadDtOrd').val();
	
	location.href = uri + queryString;
}

//모달
function modalOpenClose(modal, type){
	if (type == "open"){
		$("#"+modal).css("display","block");
		$("#"+modal+"_background").css("display","block");
	} else if (type == "close"){
		$("#"+modal).css("display","none");
		$("#"+modal+"_background").css("display","none");
	} 
}


// 학습 파일 등록 열기
function addLearnOpen(){
	modalOpenClose("add_user","open");
}

// 학습 파일 등록 닫기
function addLearnClose(){
	modalOpenClose("add_user","close");
}

//정답셋 파일 등록 열기
function addAnswerOpen(){
	// 체크박스 검사	
	var chkCnt = $("input[name='chkBoxLearn']:checked").length;
	if (chkCnt<=0){
		alert("하나를 선택해주세요.");
		return;
	} else if (chkCnt>1){
		alert("하나를 선택해주세요.");
		return;
	}else if (chkCnt == 1){
		// 하나의 학습 로우가 선택되었음
		$("input[name='chkBoxLearn']:checked").each(function(){
			var chkLearnId = $(this).val();
			$('#chkLearnId').val(chkLearnId);
			modalOpenClose("add_menu","open");
			return;
		});
		
	}
}

//정답셋 파일 등록 닫기
function addAnswerClose(){
	modalOpenClose("add_menu","close");
}

// 기존 엑셀 다운로드 > 템플릿 다운로드로 변경
function excelDown(){
    var newForm = $('<form></form>');
    
    newForm.attr("name", "excelFrm");
    newForm.attr("method", "post");
    newForm.attr("action", "/user/learn_data_manage/excel_down");
    newForm.attr("target", "hide_frame");

    newForm.append($('<input/>', {type: 'hidden', name: 'searchType', value: $('#h_searchType').val()}));
    newForm.append($('<input/>', {type: 'hidden', name: 'searchKeyword', value: $('#h_searchKeyword').val()}));
    newForm.append($('<input/>', {type: 'hidden', name: 'searchStartDate', value: $('#h_searchStartDate').val()}));
    newForm.append($('<input/>', {type: 'hidden', name: 'searchEndDate', value: $('#h_searchEndDate').val()}));
    newForm.append($('<input/>', {type: 'hidden', name: 'learnStatus', value: $('#h_learnStatus').val()}));
    newForm.append($('<input/>', {type: 'hidden', name: 'answerStatus', value: $('#h_answerStatus').val()}));
    newForm.append($('<input/>', {type: 'hidden', name: 'learnFileUploadDtOrd', value: $('#h_learnFileUploadDtOrd').val()}));

    newForm.appendTo('body');

    newForm.submit();
}
</script>
<script>        
//fileDropzone dropzone 설정할 태그의 id로 지정
Dropzone.options.fileDropzone = {
    url: '/user/learn_data_fileupload', //업로드할 url (ex)컨트롤러)
    method: 'post',
    init: function () {
        /* 최초 dropzone 설정시 init을 통해 호출 */
        var submitButton = document.querySelector("#btn-upload-file");
        var myDropzone = this; //closure
        submitButton.addEventListener("click", function () {
            console.log("업로드"); //tell Dropzone to process all queued files
       		if (myDropzone.files.length != 1){
       			alert('파일을 업로드하여 주세요');
       			return false;
       		}
            myDropzone.processQueue();
        });
        this.on("maxfilesexceeded", function(file) { // 파일 한개만 업로드
            this.removeAllFiles();
            this.addFile(file);
        });
    },
    // Controller Response를 받아 분기 처리
    success: function(file, response) {
        if(response.result == 'success') {
            alert('등록 되었습니다.');
            this.removeAllFiles();
            addLearnClose();
            location.href = "/user/learn_data_manage";
        } else {
            alert('등록 실패.')
        }
    },
    acceptedFiles: ".xlsx", // 허용할 파일확장자
    autoProcessQueue: false, // 자동업로드 여부 (true일 경우, 바로 업로드 되어지며, false일 경우, 서버에는 올라가지 않은 상태임 processQueue() 호출시 올라간다.)
    clickable: true, // 클릭가능여부
    thumbnailHeight: 90, // Upload icon size
    thumbnailWidth: 90, // Upload icon size
    maxFiles: 1, // 업로드 파일수
    maxFilesize: 10, // 최대업로드용량 : 10MB
    parallelUploads: 1, // 동시파일업로드 수(이걸 지정한 수 만큼 여러파일을 한번에 컨트롤러에 넘긴다.)
    addRemoveLinks: true, // 삭제버튼 표시 여부
    dictRemoveFile: '삭제', // 삭제버튼 표시 텍스트
    uploadMultiple: false, // 다중업로드 기능
};

Dropzone.options.fileDropzone2 = {
        url: '/user/answer_data_fileupload', //업로드할 url (ex)컨트롤러)
        method: 'post',
        init: function () {
            /* 최초 dropzone 설정시 init을 통해 호출 */
            var submitButton = document.querySelector("#btn-upload-file2");
            var myDropzone = this; //closure

            this.on("sending", function(file, xhr, formData) {
           		var chkLearnId = $('#chkLearnId').val();
           		var epoch = $('#epoch').val().trim();
           		var learningRate = $('#learningRate').val().trim();
           		var batchSize = $('#batchSize').val().trim();

                console.log("sending ## "); //tell Dropzone to process all queued files
                console.log("chkLearnId" , chkLearnId); 
                console.log("epoch" , epoch); 
                console.log("learningRate" , learningRate); 
                console.log("batchSize" , batchSize); 
                
                formData.append("id", chkLearnId);
                formData.append("epoch", epoch);
                formData.append("learningRate", learningRate);
                formData.append("batchSize", batchSize);
            });
            
            submitButton.addEventListener("click", function () {
                console.log("정답셋 업로드"); //tell Dropzone to process all queued files
                
           		var chkLearnId = $('#chkLearnId').val();
           		var epoch = $('#epoch').val().trim();
           		var learningRate = $('#learningRate').val().trim();
           		var batchSize = $('#batchSize').val().trim();

                console.log("chkLearnId" , chkLearnId); 
                console.log("epoch" , epoch); 
                console.log("learningRate" , learningRate); 
                console.log("batchSize" , batchSize); 
				/*
            	var obj = new Object();
            	obj.id = chkLearnId;
            	obj.epoch = epoch;
            	obj.learningRate = learningRate;
            	obj.batchSize = batchSize;
            	
                myDropzone.params = obj; 
                */
           		if (epoch == null || epoch == undefined || epoch.length == 0){
           			/*
           			alert('Epoch를 입력하여 주세요');
           			$('#epoch').focus();
           			return false;
           			*/
           			$('#epoch').val('2');
           		}
           		if (learningRate == null || learningRate == undefined || learningRate.length == 0){
           			/*
           			alert('Rearning Rate를 입력하여 주세요');
           			$('#learningRate').focus();
           			return false;
					*/
           			$('#learningRate').val('0.000001');
           		}
           		if (batchSize == null || batchSize == undefined || batchSize.length == 0){
           			/*
           			alert('Batch Size를 입력하여 주세요');
           		 	$('#batchSize').focus();
           			return false;
           			*/
           			$('#batchSize').val('4');
           		} 
           		
           		if (myDropzone.files.length != 1){
           			alert('정답셋 파일을 업로드하여 주세요');
           			return false;
           		}
                myDropzone.processQueue();
            });
            this.on("maxfilesexceeded", function(file) { // 파일 한개만 업로드
                this.removeAllFiles();
                this.addFile(file);
            });
        },
        // Controller Response를 받아 분기 처리
        success: function(file, response) {
            if(response.result == 'success') {
                // alert('등록 되었습니다.');
                this.removeAllFiles();
                
                console.log(response);
                addAnswerClose();
                ajaxModelTrain(response);
                
                //location.href = "/user/learn_data_manage";
            } else {
                alert('등록 실패.')
            }
        },
        acceptedFiles: ".xlsx", // 허용할 파일확장자
        autoProcessQueue: false, // 자동업로드 여부 (true일 경우, 바로 업로드 되어지며, false일 경우, 서버에는 올라가지 않은 상태임 processQueue() 호출시 올라간다.)
        clickable: true, // 클릭가능여부
        thumbnailHeight: 90, // Upload icon size
        thumbnailWidth: 90, // Upload icon size
        maxFiles: 1, // 업로드 파일수
        maxFilesize: 10, // 최대업로드용량 : 10MB
        parallelUploads: 1, // 동시파일업로드 수(이걸 지정한 수 만큼 여러파일을 한번에 컨트롤러에 넘긴다.)
        addRemoveLinks: true, // 삭제버튼 표시 여부
        dictRemoveFile: '삭제', // 삭제버튼 표시 텍스트
        uploadMultiple: false, // 다중업로드 기능
    };

function isNumberKey(evt) {
	/*
	//텍스트박스에 한글 입력 불가(크롬에서 잘 됨)
		<input type="text" onkeypress="return isNumberKey(event);" onkeyup="this.value=this.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g, '');"/>
	*/

    var charCode = (evt.which) ? evt.which : event.keyCode;
    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57))
        return false;
    // Textbox value    
    var _value = event.srcElement.value;    

    // 소수점(.)이 두번 이상 나오지 못하게
    var _pattern0 = /^\d*[.]\d*$/; // 현재 value값에 소수점(.) 이 있으면 . 입력불가
    if (_pattern0.test(_value)) {
        if (charCode == 46) {
            return false;
        }
    }

    // 1000 이하의 숫자만 입력가능
    var _pattern1 = /^\d{3}$/; // 현재 value값이 3자리 숫자이면 . 만 입력가능
    if (_pattern1.test(_value)) {
        if (charCode != 46) {
            alert("1000 미만의 숫자만 입력가능합니다");
            return false;
        }
    }

    // 소수점 여섯째자리까지만 입력가능
    var _pattern2 = /^\d*[.]\d{6}$/; // 현재 value값이 소수점 둘째짜리 숫자이면 더이상 입력 불가
    if (_pattern2.test(_value)) {
        alert("소수점 여섯째자리까지만 입력가능합니다.");
        return false;
    }  
    return true;
}

function ajaxModelTrain(response){
    // 정답셋 파일 등록 완료 후 학습 API 호출
	var apiUrl = '<c:out value="${apiUrl}"/>' + '/msg/model_train';
	var sessionMemId = '<c:out value="${sessionMember.memId}"/>';

	var infoEpoch = parseInt(response.info.epoch);
	var infoRate = parseFloat(response.info.learningRate);
	var infoBatchSize = parseInt(response.info.batchSize);

	if (isNaN(infoEpoch)){
		infoEpoch = 0;
	}
	if (isNaN(infoRate)){
		infoRate = 0.0;
	}
	if (isNaN(infoBatchSize)){
		infoBatchSize = 0;
	}
	    
    var params = {
  		  "learning_id": response.info.id + '',
		  "model_name": response.info.learnName + '',
		  "epoch": infoEpoch,
		  "rate": infoRate,
		  "batch_size": infoBatchSize,
		  "train_db_key": response.info.learnFileUrl + '',
		  "test_db_key": response.info.answerFileUrl + '',
		  "regId" : sessionMemId + ''
		  
    }
	$.ajax({
		url : apiUrl,
		type : "post",
		async : true,
		data : JSON.stringify(params),
		dataType: "json",
	    contentType: "application/json",
	    timeout: 60000,// timeout : 30초간 서버와의 통신기다리는 시간.
	    cache: false,
		success : function(resp) {
			console.log(resp);
			 var params = {
					id :  response.info.id,
					learnStatus : "3"
		    }
			ajaxLearnStatusUpdate(params);
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

function ajaxLearnStatusUpdate(params){
	/* type 1: 오류, 2: 등록완료, 3: 학습중, 4: 학습완료
		var params = {
			id :  response.info.id,
			learnStatus : "3"
		}
	*/
	
    // 정답셋 파일 등록 완료 후 학습 API 호출
    var apiUrl = '/user/learning_status_update';
	
	$.ajax({
		url : apiUrl,
		type : "post",
		async : true,
		data : JSON.stringify(params),
		dataType: "json",
	    contentType: "application/json",
	    timeout: 60000,// timeout : 30초간 서버와의 통신기다리는 시간.
	    cache: false,
		success : function(resp) {
			console.log(resp);
			alert('요청이 완료되었습니다.');
			location.href = "/user/learn_data_manage";
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

// 학습상태확인
function ajaxLearnStatusChk(type){
	/* 학습중인 데이터가 있다면 학습상태를 확인하고 100 퍼센트 완료된 학습이 있다면 DB 업데이트 이후 화면 전환 */
	if (type == null || type == undefined || type.leangth == 0){
		type = "status";
	}
	
	// 체크박스 검사	
	var chkCnt = $("input[name='chkBoxLearn']:checked").length;
	if (chkCnt<=0){
		alert("하나를 선택해주세요.");
		return;
	} else if (chkCnt>1){
		alert("하나를 선택해주세요.");
		return;
	}else if (chkCnt == 1){
		// 하나의 학습 로우가 선택되었음
		$("input[name='chkBoxLearn']:checked").each(function(){
			var chkId = $(this).val();
			var chkStatus = $('#learnStatus_'+ chkId).val();
			var chkLearnName = $('#learnName_'+ chkId).val();
			
			if (chkStatus != '3'){
				alert('학습중인 데이터를 선택해주세요');
				return;
			} else {
				var apiUrl = '<c:out value="${apiUrl}"/>' + '/msg/model_status';
				var params = {
					model_name : chkLearnName + ''
				}
				$.ajax({
					url : apiUrl,
					type : "post",
					async : true,
					data : JSON.stringify(params),
					dataType: "json",
				    contentType: "application/json",
				    timeout: 60000,// timeout : 30초간 서버와의 통신기다리는 시간.
				    cache: false,
					success : function(resp) {
						console.log(resp);
						if (resp.code == "200"){
							var per = parseInt(parseInt(resp.train_step) / parseInt(resp.total_step) * 100) ;
							if (resp.total_step == resp.train_step){
								// 학습완료
								console.log('학습이 완료되었습니다');
								if(type=="status") {								
									$('#learnTag_'+ chkId).html('학습완료<br>('+ parseInt(per) +'%)');
								}else if(type=="stop") {
									$('#learnTag_'+ chkId).html('학습완료<br>('+ parseInt(per) +'%)');
									alert('학습이 완료된 데이터는 중지할 수 없습니다.');
								}
							}if (resp.total_step > resp.train_step){
								// 학습중
								console.log('학습중 입니다');
								if(type=="status") {
									$('#learnTag_'+ chkId).html('학습중<br>('+ parseInt(per) +'%)');
								}else if(type=="stop") {
									$('#learnTag_'+ chkId).html('학습중<br>('+ parseInt(per) +'%)');
									ajaxLearnStopChk(chkId, chkLearnName);
								}
							}	
						} else {
							console.log(resp.code, resp.message);
						}
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
		});
	}
}

// 학습 중지
function ajaxLearnStopChk(chkId, chkLearnName){
	var apiUrl = '<c:out value="${apiUrl}"/>' + '/msg/model_train_stop';
	var params = {
		model_name : chkLearnName + ''
	}
	$.ajax({
		url : apiUrl,
		type : "post",
		async : true,
		data : JSON.stringify(params),
		dataType: "json",
	    contentType: "application/json",
	    timeout: 60000,// timeout : 30초간 서버와의 통신기다리는 시간.
	    cache: false,
		success : function(resp) {
			console.log(resp);
			if (resp.code == "200"){

				alert('학습 중지가 완료되었습니다.');
				 var params = {
						id :  chkId,
						learnStatus : "2"
			    }
				ajaxLearnStatusUpdate(params);
			} else {
				console.log(resp.code, resp.message);
			}
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

// 삭제
function deleteChk(){
	// 체크박스 검사	
	var chkCnt = $("input[name='chkBoxLearn']:checked").length;
	if (chkCnt<=0){
		alert("하나를 선택해주세요.");
		return;
	} else if (chkCnt>1){
		alert("하나를 선택해주세요.");
		return;
	}else if (chkCnt == 1){
		// 하나의 학습 로우가 선택되었음
		$("input[name='chkBoxLearn']:checked").each(function(){
			var chkLearnId = $(this).val();
			$('#chkLearnId').val(chkLearnId);
			if(confirm('삭제하시겠습니까?')){
				ajaxDeleteLearn();
			}
			return;
		});
		
	}
}
// 삭제
function ajaxDeleteLearn(){
	
    // 삭제 API
    var apiUrl = '/user/delete_learn';
    
    var params = {
		id : $('#chkLearnId').val()
	}
	
	$.ajax({
		url : apiUrl,
		type : "post",
		async : true,
		data : JSON.stringify(params),
		dataType: "json",
	    contentType: "application/json",
	    timeout: 60000,// timeout : 30초간 서버와의 통신기다리는 시간.
	    cache: false,
		success : function(resp) {
			console.log(resp);
			if (resp.result == 'success'){
				alert('삭제 성공하였습니다.');

	            location.href = "/user/learn_data_manage";
			}
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

// 템플릿다운로드
function templateDown(){
	location.href = "/sample/sample.xlsx"
}

</script>
</body>
</html>