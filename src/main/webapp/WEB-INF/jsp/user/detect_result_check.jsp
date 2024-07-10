<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ include file="/WEB-INF/jsp/include/header.jspf" %>
    <title>탐지 및 제어(진위확인 결과 조회) || maum XDC</title>
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
                    <h3>탐지 및 제어 <span>진위확인 결과 조회</span></h3>
                </div>

                <div class="filterArea">
					<input type="hidden" id="currentPageNo" name="currentPageNo" value="1"/>    						<!-- 신규 검색시 1 페이지로 이동 -->
					<input type="hidden" id="h_currentPageNo" name="h_currentPageNo" value="${params.currentPageNo}"/>    	<!-- 신규 검색시 1 페이지로 이동 -->
                   	<input type="hidden" id="h_recordsPerPage" name="h_recordsPerPage" value="${params.recordsPerPage}"/> 	<!-- 한 페이지에 보여질 수 -->
                    <input type="hidden" id="h_pageSize" name="h_pageSize" value="${params.pageSize}"/>             		<!-- 페이지 블럭의 수 -->
					<input type="hidden" id="h_searchType" name="h_searchType" value="${params.searchType}"/>           	<!-- 검색 종류 -->
					<input type="hidden" id="h_searchKeyword" name="h_searchKeyword" value="${params.searchKeyword}"/>  	<!-- 검색 값 -->
					<input type="hidden" id="searchType" name="searchType" value="${params.searchType}"/>           	<!-- 검색 종류 -->
					<input type="hidden" id="createDtmOrd" name="createDtmOrd" value="${params.createDtmOrd}"/>  	<!-- 검색 값 -->
					<input type="hidden" id="h_searchStartDate" name="h_searchStartDate" value="${params.searchStartDate}"/>           	<!-- 조회기간 S -->
					<input type="hidden" id="h_searchEndDate" name="h_searchEndDate" value="${params.searchEndDate}"/>  	<!-- 조회기간 E -->
					<input type="hidden" id="h_searchEdpsCsn" name="h_searchEdpsCsn" value="${params.searchEdpsCsn}"/>  	<!-- 전행고객번호 -->
                    <dl>
                        <dt>조회 기간</dt>
                        <dd>
                            <div class="iptBox date">
                                <input type="text" class="ipt_txt fromDate" id="searchStartDate" name="searchStartDate" value="${params.searchStartDate }" readonly="readonly">
                            </div>                            
                            <em class="tilde">~</em>
                            <div class="iptBox date">
                                <input type="text" class="ipt_txt toDate" id="searchEndDate" name="searchEndDate" value="${params.searchEndDate }" readonly="readonly">
                            </div>
                        </dd>
                    </dl>
                    <dl>
                        <dt>결과 조건</dt>
                        <dd>
							<select id="searchType_new" class="select">
							    <option value="all" <c:if test="${empty params.searchType or params.searchType eq 'all'}">selected="selected"</c:if>>전체</option>
							    <option value="real" <c:if test="${params.searchType eq 'real'}">selected="selected"</c:if>>진본</option>
							    <option value="fake" <c:if test="${params.searchType eq 'fake'}">selected="selected"</c:if>>위조</option>
							</select>
                        </dd>
                    </dl>
                    <dl>
                        <dt>정렬 조건</dt>
                        <dd>
							<select id="createDtmOrd_new" class="select">
							    <option value="asc" <c:if test="${empty params.createDtmOrd or params.createDtmOrd eq 'asc'}">selected="selected"</c:if>>오름차순</option>
							    <option value="desc" <c:if test="${params.createDtmOrd eq 'desc'}">selected="selected"</c:if>>내림차순</option>
							</select>
                        </dd>
                    </dl>
                    <dl>
                        <dt>전행고객번호</dt>
                        <dd>
							<div class="iptBox">
                                <input type="text" class="ipt_txt" style="background: #fff" id="searchEdpsCsn" name="searchEdpsCsn" value="${param.searchEdpsCsn}">
                            </div>
                        </dd>
                    </dl>
                    <dl>
                        <dt></dt>
                        <dd>
		                    <button type="button" class="btn_primary " onclick="javascript:searchScan();">검색</button>    
                        </dd>
                    </dl>
                   	<button type="button" class="btn_primary line fr" onclick="javascript:excelDown();">엑셀 다운로드</button>
                </div>

                <div class="tblBox">
                    <table summary="연번, 실행 일시, 구분, 확인 결과, 전행고객번호, 상세보기로 구성">
                        <caption class="hide">진위확인 결과 조회</caption>
                        <thead>
                            <tr>
                                <th scope="col">No.</th>
                                <th scope="col">실행 일시</th>
                                <th scope="col">탐지 강도</th>
                                <th scope="col">REAL</th>
                                <th scope="col">MONITOR</th>
                                <th scope="col">PAPER</th>
                                <th scope="col">확인 결과</th>
                                <th scope="col">전행고객번호</th>
                                <th scope="col">상세보기</th>
                            </tr>
                        </thead>
                        <tbody>
                        	<!-- 
                            <tr>
                                <td class="empty" scope="row" colspan="6">데이터가 없습니다.</td>
                            </tr>
                            <tr>
                                <td scope="row">1</td>
                                <td>2020-12-12</td>
                                <td>진본</td>
                                <td>00011111</td>
                                <td><button type="button" class="btn_view_detail btn_lyr_open" data-lyr-name="detect_result_detail">상세보기</button></td>
                            </tr>
                             -->
                             
                        	<c:choose>
                        		<c:when test="${empty scanList }">
		                            <tr>
		                                <td class="empty" scope="row" colspan="6">데이터가 없습니다.</td>
		                            </tr>
                        		</c:when>
                        		<c:when test="${!empty scanList }">
                        			<c:forEach var="list" varStatus="status" items="${scanList}" >
			                            <tr>
			                                <!-- ((현재 페이지 번호-1)*페이지별 보여지는 수량) + 현재 순번 -->
			                                <td>
			                                	<c:choose>
			                                		<c:when test="${params.createDtmOrd eq 'asc'}">
			                                			${((params.currentPageNo-1)*params.recordsPerPage) + status.count}
			                                		</c:when>
			                                		<c:when test="${params.createDtmOrd eq 'desc'}">
			                                			${params.paginationInfo.totalRecordCount - ((params.currentPageNo-1) * params.recordsPerPage + status.index) }
			                                		</c:when>
			                                	</c:choose>
			                                </td>
			                                <td><fmt:formatDate value="${list.createDtm }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			                                <td>${list.level }</td>
			                                <td>${list.realCell }</td>
			                                <td>${list.monitorCell }</td>
			                                <td>${list.paperCell }</td>
			                                <td>
			                                	<c:choose>
			                                		<c:when test="${list.detectResult eq 'REAL'}">진본</c:when>
			                                		<c:otherwise><span class="ft_point_pink">위조</span></c:otherwise>
			                                	</c:choose>
			                                </td>
			                                <td>${list.edpsCsn }</td>
                                			<td><button type="button" class="btn_view_detail btn_lyr_open" onclick="javascript:detectResultOpen('${list.id}')";>상세보기</button></td><!--  data-lyr-name="detect_result_detail" -->
                                			<input type="hidden" id="detect_data_${list.id}" name="test" value="${list.detectLog }"/>
                                			<input type="hidden" id="detect_doc_${list.id}" name="test" value="${list.docId }"/>
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
							<a href="javascript:movePage('/user/detect_result_check', '${params.makeQueryString(1)}');" class="btn_paging first">
	                            <span class="hide">처음 페이지</span>
	                            <em class="fas fa-angle-double-left"></em>
	                        </a>
							<a href="javascript:movePage('/user/detect_result_check', '${params.makeQueryString(params.paginationInfo.firstPage-1)}');" class="btn_paging prev">
	                            <span class="hide">이전 페이지</span>
	                            <em class="fas fa-angle-left"></em>
	                        </a>
                        </c:if>
                        
                       	<c:forEach begin="${params.paginationInfo.firstPage}" end="${params.paginationInfo.lastPage}" var="pageNo">
                        	<!-- [D] 현재 페이지 인덱스에 addClass('current') 해주면 활성화됨 -->
							<a href="javascript:movePage('/user/detect_result_check', '${params.makeQueryString(pageNo)}');"  class="<c:out value="${params.currentPageNo == pageNo ? 'current' : ''}"/>">${pageNo}</a>
                       	</c:forEach>
						<c:if test="${params.paginationInfo.hasNextPage}">
							<a href="javascript:movePage('/user/detect_result_check', '${params.makeQueryString(params.paginationInfo.lastPage+1)}');" class="btn_paging next">
	                            <span class="hide">다음 페이지</span>
	                            <em class="fas fa-angle-right"></em>
	                        </a>
							<a href="javascript:movePage('/user/detect_result_check', '${params.makeQueryString(params.paginationInfo.totalPageCount)}');" class="btn_paging last">
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
<!-- 상세보기 -->
<div id="detect_result_detail" class="lyrWrap">
<input type="hidden" id="detectScanId">
<input type="hidden" id="detectScanData">
	<div id="detect_result_detail_background" class="lyr_bg" style="display:none;"></div>
    <div class="lyrCont">
        <div class="lyr_top">
            <p class="tit">진위확인 결과</p>
            <button type="button" class="btn_lyr_close"  onclick="javascript:detectResultClose();"><span class="hide">닫기버튼</span></button>
        </div>
        <div class="lyr_mid">
            <div class="detect_img">
                <!-- [D] 진위확인 결과에 따라 .stateBox p에 addClass('on')해주시면 됩니다. -->
                <div class="stateBox">
                    <p id="state_fake" class="state fake">위조</p>
                    <p id="state_real" class="state real">진본</p>
                </div>
                <ul>
                    <li class="input_img">
                        <img id="detect_image" src="" alt="대상 이미지">
                    </li>
                    <li class="result_img">
                        <!-- [D] 22.01.12 div.detect_cell 영역 추가 해주세요. -->
                        <div id="detect_cell" class="detect_cell">
                            <div id="detect_cell_1"></div>
                            <div id="detect_cell_2"></div>
                            <div id="detect_cell_3"></div>
                            <div id="detect_cell_4"></div>
 
                            <div id="detect_cell_5"></div>
                            <div id="detect_cell_6"></div>
                            <div id="detect_cell_7"></div>
                            <div id="detect_cell_8"></div>
 
                            <div id="detect_cell_9"></div>
                            <div id="detect_cell_10"></div>
                            <div id="detect_cell_11"></div>
                            <div id="detect_cell_12"></div>
                        </div>
                        <img id="detect_image_result" src="" alt="진위 확인 이미지">
                    </li>
                </ul>
            </div>
            <div class="result_detail">
                <table summary="분류, paper로 구성">
                    <caption class="hide">진위확인 상세결과</caption>
                    <colgroup>
                        <col width="134px"><col>
                    </colgroup>
                    <tbody>
                        <tr>
                            <th>분류</th>
                            <th id="detect_result"></th>
                        </tr>
                        <tr>
                            <th>Real</th>
                            <td id="detect_real_cnt">0</td>
                        </tr>
                        <tr>
                            <th>Paper</th>
                            <td id="detect_paper_cnt" class="paper">0</td>
                        </tr>
                        <tr>
                            <th>Monitor</th>
                            <td id="detect_monitor_cnt" class="monitor">0</td>
                        </tr>
                    </tbody>
                </table>
            </div>
			<c:choose>
				<c:when test="${params.createDtmOrd eq 'asc'}">
		            <button type="button" class="btn_prev_result" onclick="javascript:ajaxGetScan('prev');">
		                <span class="hide">이전 상세결과</span>
		            </button>
		            <button type="button" class="btn_next_result" onclick="javascript:ajaxGetScan('next');">
		                <span class="hide">다음 상세결과</span>
		            </button>
				</c:when>
				
				<c:when test="${params.createDtmOrd eq 'desc'}">
		            <button type="button" class="btn_prev_result" onclick="javascript:ajaxGetScan('next');">
		                <span class="hide">이전 상세결과</span>
		            </button>
		            <button type="button" class="btn_next_result" onclick="javascript:ajaxGetScan('prev');">
		                <span class="hide">다음 상세결과</span>
		            </button>
				</c:when>
			</c:choose>
        </div>
    </div>
</div>
<!-- //상세보기 -->
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
        $("#sub_ID00001").addClass("active");//상위 메뉴
        $("#sub_ID00005").addClass("active");//하위 메뉴
        
        

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
	queryString += "&searchEdpsCsn=" + $('#h_searchEdpsCsn').val();
	queryString += "&recordsPerPage=" + $('#h_recordsPerPage').val();
	queryString += "&createDtmOrd=" + $('#createDtmOrd').val();
	location.href = uri + queryString;	
}

//사용자 목록 조회
function searchScan(){
	
	var startDate = $('#searchStartDate').val();
	var endDate = $('#searchEndDate').val();
	
	var s = new Date(startDate);
	var e = new Date(endDate);
	
	if (s>e){
		alert('조회기간이 올바르지 않습니다.');
		return;
	}
	
	var searchEdpsCsn = $('#searchEdpsCsn').val().trim();
	$('#searchEdpsCsn').val(searchEdpsCsn);
	
	var uri = "/user/detect_result_check";
	var queryString = "?currentPageNo=" + $('#currentPageNo').val()
			+ "&recordsPerPage=" + $('#recordsPerPage').val() // 현재 선택된 레코드 수로 검색
			+ "&pageSize=" + $('#h_pageSize').val()
			+ "&searchType=" + $('#searchType_new').val() // 검색을 누를 경우에만  결과조건
			+ "&searchKeyword=" + $('#searchKeyword').val()
			+ "&searchStartDate=" + startDate
			+ "&searchEndDate=" + endDate
			+ "&createDtmOrd=" + $('#createDtmOrd_new').val()
			+ "&searchEdpsCsn=" + $('#searchEdpsCsn').val();
	
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
// 모달
function detectResultOpen(id, type){
	modalOpenClose("detect_result_detail", "close");
	var $detectData = "";
	// 모달창 상단 id 저장
	$("#detectScanId").val(id);
	
	if (type=="move"){
		$detectData = $("#detectScanData").val();
	} else{
		$detectData = $("#detect_data_"+id).val();		
	}
	//$detectData = $detectData.replaceAll("\'","\"");
	$detectData = $detectData.replace(/\'/gi, "\"");
	
	
	var $detectJsonData =JSON.parse($detectData);
	var patchesArr = $detectJsonData.patches;
	
	//console.log(patchesArr);
	// 진위 확인 셀 초기화
	for (var i=1; i<=12;i++){
		 $("#detect_cell_"+i).removeClass();
	}

	// 진위 확인 셀 별 설정
	var realCnt = 0;
	var paperCnt = 0;
	var monitorCnt = 0;
	
	for (var i=0; i < patchesArr.length; i++){
		if ("REAL" == patchesArr[i].label){
			realCnt++;
		} else if ("PAPER" == patchesArr[i].label){
			paperCnt++;
		} else if ("MONITOR" == patchesArr[i].label){
			monitorCnt++;
		}
	    if (patchesArr[i].top <= 100){
	        if (patchesArr[i].left <= 100 ){
	            $("#detect_cell_1").addClass(patchesArr[i].label.toLowerCase());
	        } else if (patchesArr[i].left <= 400 ){
	            $("#detect_cell_2").addClass(patchesArr[i].label.toLowerCase());
	        } else if (patchesArr[i].left <= 700 ){
	            $("#detect_cell_3").addClass(patchesArr[i].label.toLowerCase());
	        } else if (patchesArr[i].left <= 1000 ){
	            $("#detect_cell_4").addClass(patchesArr[i].label.toLowerCase());
	        } 
	    } else if (patchesArr[i].top <= 300){
	        if (patchesArr[i].left <= 100 ){
	            $("#detect_cell_5").addClass(patchesArr[i].label.toLowerCase());
	        } else if (patchesArr[i].left <= 400 ){
	            $("#detect_cell_6").addClass(patchesArr[i].label.toLowerCase());
	        } else if (patchesArr[i].left <= 700 ){
	            $("#detect_cell_7").addClass(patchesArr[i].label.toLowerCase());
	        } else if (patchesArr[i].left <= 1000 ){
	            $("#detect_cell_8").addClass(patchesArr[i].label.toLowerCase());
	        } 
	    }  else if (patchesArr[i].top <= 620){
	        if (patchesArr[i].left <= 100 ){
	            $("#detect_cell_9").addClass(patchesArr[i].label.toLowerCase());
	        } else if (patchesArr[i].left <= 400 ){
	            $("#detect_cell_10").addClass(patchesArr[i].label.toLowerCase());
	        } else if (patchesArr[i].left <= 700 ){
	            $("#detect_cell_11").addClass(patchesArr[i].label.toLowerCase());
	        } else if (patchesArr[i].left <= 1000 ){
	            $("#detect_cell_12").addClass(patchesArr[i].label.toLowerCase());
	        } 
	    } 
	}
	
	// 진위확인 결과 셋팅
	if ("REAL"==$detectJsonData.label){
		$('#state_real').addClass("on");
		$('#state_fake').removeClass("on");
	}else{
		$('#state_real').removeClass("on");
		$('#state_fake').addClass("on");
	}
	$('#detect_result').html($detectJsonData.label);
	$('#detect_real_cnt').html(realCnt);
	$('#detect_paper_cnt').html(paperCnt);
	$('#detect_monitor_cnt').html(monitorCnt);
	
	var $detectDocId = $("#detect_doc_"+id).val();	
	
	//이미지 다운로드 및 이미지 셋팅
	var jsonData = {
		elementId : $detectDocId
	};
	
   	// 모달 오픈
   	if ($detectDocId == null || $detectDocId == undefined || $detectDocId.length == 0){
   		alert("not exist docId");
	   	modalOpenClose("detect_result_detail", "open");
   	}else {
	   	ajaxGetImage(jsonData);   		
   	}
}
// 모달
function detectResultClose(){
	modalOpenClose("detect_result_detail", "close");
}

// 사용자 이미지 GET
function ajaxGetImage(jsonData){
	$.ajax({
		url : "/mindslab/rest/bprImagePath",
		type : "post",
		async : true,
		data : JSON.stringify(jsonData),
		dataType: "json",
	    contentType: "application/json",
	    timeout: 60000,// timeout : 60초간 서버와의 통신기다리는 시간.
	    cache: false,
		success : function(resp) {
			if (resp.body.docs.length == 1){
				setDetectImage(resp.body.docs[0].imgUrl);
				modalOpenClose("detect_result_detail", "open");
			}else{
				modalOpenClose("detect_result_detail", "close");
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
	             alert("이미지를 다운로드 받지 못하였습니다.");
	        } else {
				console.log("error");
				modalOpenClose("detect_result_detail", "close");
	        }
		}
	});
}

function setDetectImage(imgUrl){
	// 초기화
	$('#detect_image').attr('src', '');
	$('#detect_image_result').attr('src', '');
	
	// 이미지 경로 입력
	$('#detect_image').attr('src', imgUrl);
	$('#detect_image_result').attr('src', imgUrl);
}

//사용자 데이터 체크 - 사용자 아이디 중복체크
function ajaxGetScan(type){
	
	if (type== null || type == undefined){ // type :  next / prev 
		return;
	}
	
	var scanId = $('#detectScanId').val();
	var startDate = $('#h_searchStartDate').val();
	var endDate = $('#h_searchEndDate').val();
	
	var jsonData = {
		scanId : scanId
		, searchType : $('#h_searchType').val()
		, searchKeyword : $('#h_searchKeyword').val()
		, searchStartDate : startDate
		, searchEndDate : endDate
		, createDtmOrd : $('#createDtmOrd').val()
	};
	
	restCall('user/scan/'+type
			,{
		        method: 'post',
		        data: JSON.stringify(jsonData),
		        async: false,
		    }
			, function(resp) {// 성공
				//console.log(resp);
				if (resp.scanInfo != null){
					$("#detectScanData").val(resp.scanInfo.detectLog);
					detectResultOpen(resp.scanInfo.id,"move");
				} else {
					alert('마지막 진위확인 결과 입니다.');
				}
			} 
			, function(jqXHR) {//실패
				console.log("error");
			} 
	);
}

function excelDown(){
    var newForm = $('<form></form>');
    
    newForm.attr("name", "excelFrm");
    newForm.attr("method", "post");
    newForm.attr("action", "/user/scan/excel_down");
    newForm.attr("target", "hide_frame");

    newForm.append($('<input/>', {type: 'hidden', name: 'searchType', value: $('#h_searchType').val()}));
    newForm.append($('<input/>', {type: 'hidden', name: 'searchKeyword', value: $('#h_searchKeyword').val()}));
    newForm.append($('<input/>', {type: 'hidden', name: 'searchStartDate', value: $('#h_searchStartDate').val()}));
    newForm.append($('<input/>', {type: 'hidden', name: 'searchEndDate', value: $('#h_searchEndDate').val()}));
    newForm.append($('<input/>', {type: 'hidden', name: 'createDtmOrd', value: $('#createDtmOrd').val()}));

    newForm.appendTo('body');

    newForm.submit();
}


</script>
</body>
</html>