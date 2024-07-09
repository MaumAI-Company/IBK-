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
                    <h3>학습 관리<span>모델 관리</span></h3>
                </div>

                <div class="srchArea">
					<input type="hidden" id="currentPageNo" name="currentPageNo" value="1"/>    						<!-- 신규 검색시 1 페이지로 이동 -->
					<input type="hidden" id="h_currentPageNo" name="h_currentPageNo" value="${params.currentPageNo}"/>    	<!-- 신규 검색시 1 페이지로 이동 -->
                   	<input type="hidden" id="h_recordsPerPage" name="h_recordsPerPage" value="${params.recordsPerPage}"/> 	<!-- 한 페이지에 보여질 수 -->
                    <input type="hidden" id="h_pageSize" name="h_pageSize" value="${params.pageSize}"/>             		<!-- 페이지 블럭의 수 -->
					<input type="hidden" id="h_searchType" name="h_searchType" value="${params.searchType}"/>           	<!-- 검색 종류 -->
					<input type="hidden" id="h_searchKeyword" name="h_searchKeyword" value="${params.searchKeyword}"/>  	<!-- 검색 값 -->
					<input type="hidden" id="h_deployStatus" name="h_deployStatus" value="${params.deployStatus}"/>           	<!-- 상태 -->
					<input type="hidden" id="h_createDtmOrd" name="h_createDtmOrd" value="${params.createDtmOrd}"/>  	<!-- 검색 값 -->
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
                                <td style="float: left;">
                                   <select id="deployStatus" class="select">
									    <option value="all" <c:if test="${empty params.deployStatus or params.deployStatus eq 'all'}">selected="selected"</c:if>>전체</option>
									    <option value="finish" <c:if test="${params.deployStatus eq 'finish'}">selected="selected"</c:if>>배포완료</option>
									    <option value="deploying" <c:if test="${params.deployStatus eq 'deploying'}">selected="selected"</c:if>>배포중</option>
									    <option value="error" <c:if test="${params.deployStatus eq 'error'}">selected="selected"</c:if>>오류</option>
									    <option value="regist" <c:if test="${params.deployStatus eq 'regist'}">selected="selected"</c:if>>배포안됨</option>
                                    </select>
                                </td>
                           	</tr>
                            <tr>
                                <th>정렬조건</th>
                                <td>
                                   <select id="createDtmOrd" class="select">
									    <option value="asc" <c:if test="${empty params.createDtmOrd or params.createDtmOrd eq 'asc'}">selected="selected"</c:if>>오름차순</option>
									    <option value="desc" <c:if test="${params.createDtmOrd eq 'desc'}">selected="selected"</c:if>>내림차순</option>
                                    </select>
                                </td>   
                                <th>대상</th>
                                <td>
                                   <select id="searchType" class="select">
                                        <option value="all" <c:if test="${empty params.searchType or params.searchType eq 'all'}">selected="selected"</c:if>>전체</option>
                                        <option value="learn" <c:if test="${params.searchType eq 'learn'}">selected="selected"</c:if>>학습모델명</option>
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
                    <c:choose>
                    	<c:when test="${deployingCount > 0}">
                    		<button type="button" class="btn_primary line" onclick="javascript:ajaxLearnDeploy();">배포</button>
                    	</c:when>
                    	<c:otherwise>
                    		<button type="button" class="btn_primary btn_lyr_open" onclick="javascript:ajaxLearnDeploy();">배포</button>
                    	</c:otherwise>
                    </c:choose>
                    <span style="font-size: 14px;letter-spacing: -0.19px;vertical-align: center;">Threshold 설정 값 : <c:out value="${threshold.levelStrng }"/></span>
                   	<input type="hidden" id="deployingCount" name="threshold" value="${deployingCount }"/>
                    <input type="hidden" id="threshold" name="threshold" value="${threshold.levelStrng }"/>
                </div>

                <div class="tblBox">
                    <table summary="연번, 실행 일시, 구분, 확인 결과, 전행고객번호, 상세보기로 구성">
                        <caption class="hide">학습 모델</caption>
                        <thead>
                            <tr>
                                <th scope="col">
                                    <div class="chkBoxOnly">
                                        <input type="checkbox" class="allChk">
                                    </div>
                                </th>
                                <th scope="col">NO.</th>
                                <th scope="col">학습모델명</th>
                                <th scope="col">등록자</th>
                                <th scope="col">상태</th>
                                <th scope="col">결과보기</th>
                                <th scope="col">Epoch</th>
                                <th scope="col">Learning Rate</th>
                                <th scope="col">Batch Size</th>
                                <th scope="col">생성일시</th>
                            </tr>
                        </thead>
                        <tbody>
                            <input type="hidden" id="chkLearnId" name="chkLearnId" value="" />
                        	<c:choose>
                        		<c:when test="${empty learnModelList }">
		                            <tr>
		                                <td class="empty" scope="row" colspan="10">데이터가 없습니다.</td>
		                            </tr>
                        		</c:when>
                        		<c:when test="${!empty learnModelList }">
                        			<c:forEach var="list" varStatus="status" items="${learnModelList}" >
			                            <tr>
			                                <td scope="row">
			                                    <div class="chkBoxOnly">
			                                        <input name="chkBoxLearn" type="checkbox" class="eachChk" value="${list.id}">
			                                        <input type="hidden" id="deployStatus_${list.id}" value="${list.deployStatus}">
			                                        <input type="hidden" id="learnName_${list.id}" value="${list.learnName}">
			                                    </div>
			                                </td>
			                                <!-- ((현재 페이지 번호-1)*페이지별 보여지는 수량) + 현재 순번 -->
			                                <td>${((params.currentPageNo-1)*params.recordsPerPage) + status.count}</td>
			                                <td>${list.learnName }</td>
			                                <td>${list.memName }</td>
			                                <td>
			                                	<c:choose>
			                                		<c:when test="${list.deployStatus eq '2'}"><span class="ft_st_en">배포완료</span></c:when>
			                                		<c:when test="${list.deployStatus eq '4'}"><span class="ft_st_in">배포중</span></c:when>
			                                		<c:when test="${list.deployStatus eq '1'}"><span class="ft_st_er">오류</span></c:when>
			                                		<c:when test="${list.deployStatus eq '0'}"><span class="ft_st_en">배포안됨</span></c:when>
			                                		<c:otherwise>-</c:otherwise>
		                                		</c:choose>
			                                </td>
			                                <td>
			                                	<button type="button" class="btn_view_detail btn_lyr_open" onclick="javascript:thresholdOpen('${list.id}')"></button>
			                            		<input type="hidden" id="learningResult_${list.id}" value="${list.learningResult}"> 
			                                </td>
			                                <td>${list.epoch }</td>
			                                <td>${list.learningRate }</td>
			                                <td>${list.batchSize }</td>
			                                <td><fmt:formatDate value="${list.createDtm }" pattern="yyyy-MM-dd "/></td><%-- HH:mm:ss --%>
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
							<a href="javascript:movePage('/user/learn_model_manage', '${params.makeQueryString(1)}');" class="btn_paging first">
	                            <span class="hide">처음 페이지</span>
	                            <em class="fas fa-angle-double-left"></em>
	                        </a>
							<a href="javascript:movePage('/user/learn_model_manage', '${params.makeQueryString(params.paginationInfo.firstPage-1)}');" class="btn_paging prev">
	                            <span class="hide">이전 페이지</span>
	                            <em class="fas fa-angle-left"></em>
	                        </a>
                        </c:if>
                        
                       	<c:forEach begin="${params.paginationInfo.firstPage}" end="${params.paginationInfo.lastPage}" var="pageNo">
                        	<!-- [D] 현재 페이지 인덱스에 addClass('current') 해주면 활성화됨 -->
							<a href="javascript:movePage('/user/learn_model_manage', '${params.makeQueryString(pageNo)}');"  class="<c:out value="${params.currentPageNo == pageNo ? 'current' : ''}"/>">${pageNo}</a>
                       	</c:forEach>
						<c:if test="${params.paginationInfo.hasNextPage}">
							<a href="javascript:movePage('/user/learn_model_manage', '${params.makeQueryString(params.paginationInfo.lastPage+1)}');" class="btn_paging next">
	                            <span class="hide">다음 페이지</span>
	                            <em class="fas fa-angle-right"></em>
	                        </a>
							<a href="javascript:movePage('/user/learn_model_manage', '${params.makeQueryString(params.paginationInfo.totalPageCount)}');" class="btn_paging last">
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
            <p class="tit">Threshold별 결과</p>
            <button type="button" class="btn_lyr_close" onclick="javascript:thresholdClose();"><span class="hide">닫기버튼</span></button>
        </div>
        <div class="lyr_mid">
            <div class="tblBox">
                <table summary="">
                     <caption class="hide">Threshold</caption>
                     <colgroup>
                         <col width="25%"/>
                         <col width="25%"/>
                         <col width="25%"/>
                         <col width="25%"/>
                     </colgroup>
                     <thead>
	                     <tr>
	                         <th scope="col">Threshold</th>
	                         <th scope="col">precision</th>
	                         <th scope="col">recall</th>
	                         <th scope="col">f-1 score</th>
	                     </tr>
                     </thead>
                     <tbody id="tbody">
                     </tbody>
                </table>
            </div>
        </div>
        <div class="lyr_btm">
            <button type="button" class="btn_primary line btn_lyr_close" onclick="javascript:thresholdClose();">닫기</button>
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
        $("#sub_ID00009").addClass("active");//하위 메뉴
        
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
	queryString += "&createDtmOrd=" + $('#h_createDtmOrd').val();
	queryString += "&deployStatus=" + $('#h_deployStatus').val();
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
	
	var uri = "/user/learn_model_manage";
	var queryString = "?currentPageNo=" + $('#currentPageNo').val()
			+ "&recordsPerPage=" + $('#recordsPerPage').val() // 현재 선택된 레코드 수로 검색
			+ "&pageSize=" + $('#h_pageSize').val()
			+ "&searchKeyword=" + $('#searchKeyword').val()
			+ "&searchStartDate=" + startDate
			+ "&searchEndDate=" + endDate
			+ "&searchType=" + $('#searchType').val()
			+ "&deployStatus=" + $('#deployStatus').val()// 상태
			+ "&createDtmOrd=" + $('#createDtmOrd').val();
	
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
function thresholdOpen(id){
	thresholdHtml(id);
	//modalOpenClose("add_user","open");
}

// 학습 파일 등록 닫기
function thresholdClose(){
	modalOpenClose("add_user","close");
}

// Threshold 결과 파싱
function thresholdHtml(id){
    $('#tbody').html('');
	var learningResult = $('#learningResult_'+id).val(); 
	if (learningResult == null || learningResult == undefined || learningResult.length ==0 ){
		alert('학습이 완료된 데이터가 아닙니다.');
		return;
	} 
	learningResult = learningResult.replace(/\'/gi, "\"");
	learningResult = JSON.parse(learningResult);
    
    var arr = learningResult.result;
    arr = arr.sort(function(a, b){
    	return a.threshold - b.threshold;
    })
    
    var threshold_html = '';
    for (i=0; i<arr.length; i++){
	    threshold_html += '<tr>';
	    threshold_html += '	<th>'+arr[i].threshold+'</th>';
	    threshold_html += '	<td style="text-align: center;">'+arr[i].precision+'</td>';
	    threshold_html += '	<td style="text-align: center;">'+arr[i].Recall+'</td>';
	    threshold_html += '	<td style="text-align: center;">'+arr[i].f_1_score+'</td>';
	    threshold_html += '</tr>';
    }
    
	$('#tbody').html(threshold_html);
	modalOpenClose("add_user","open");
}

//학습 배포
function ajaxLearnDeploy(){
	/* 학습중인 데이터가 있다면 학습상태를 확인하고 100 퍼센트 완료된 학습이 있다면 DB 업데이트 이후 화면 전환 */
	var threshold = $('#threshold').val();
	
	// 배포중인건 체크
	var deployingCount = $("#deployingCount").val();
	if (deployingCount>0){
		alert("배포중인 학습 모델이 있습니다.");
		return;
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
			var chkStatus = $('#deployStatus_'+ chkId).val();
			var chkLearnName = $('#learnName_'+ chkId).val();
			var sessionMemId = '<c:out value="${sessionMember.memId}"/>';
			
			if (chkStatus == '4'){
				alert('배포중인 모델입니다.');
				return;
			} else {
				threshold = parseFloat(threshold);
				if (isNaN(threshold)){
					threshold = 0.0
				}
				
				var apiUrl = '<c:out value="${apiUrl}"/>' + '/msg/model_publish';
				var params = {
					model_name : chkLearnName,
					threshold : threshold,
					regId : sessionMemId
				}
				console.log(params);
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
						var useStatus = '0';
						var deployStatus = '0';
						var resultCode = resp.code;
						var resultMsg = resp.message;
						console.log(resp.code, resp.message);
						
						if (resp.code == "200"){
							useStatus = '1';
							deployStatus = '4'; // 배포중 
							alert('배포 요청 성공하였습니다.');
						} else {
							useStatus = '0';
							deployStatus = '1';// 오류 // 배포실패
							alert('배포 요청 실패하였습니다.');
						}

						var params = {
							id : chkId
							, learnName : chkLearnName
							, threshold : threshold
							//, useStatus : useStatus
							, deployStatus : deployStatus
							, resultCode : resultCode
							, resultMsg : resultMsg 
						};
						ajaxDeployStatusUpdate(params);
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

//배포중 상태 업데이트 추가
function ajaxDeployStatusUpdate(params){
	var apiUrl = '/user/deploy_status_update';
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
			location.href = "/user/learn_model_manage";
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
<%-- 
// 배포 이력 추가
function ajaxAddLearnHistory(params){
	var apiUrl = '/user/add_learn_history';
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
--%>
</script>
</body>
</html>