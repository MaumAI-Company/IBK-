<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ include file="/WEB-INF/jsp/include/header.jspf" %>
    <title>사전검증 결과 조회(사전검증 내역 조회) || maum XDC</title>
<style>
.srchArea table tbody tr td {
    padding: 5px 25px 5px 20px;
}
</style>
</head>
<body>
<!-- #wrap -->
<div id="wrap">
    <!-- #container -->
    <div id="container">
        <!-- #snb -->
        <div id="snb" class="open" style="">
		    <div class="topBox">
		        <button type="button" class="btn_ham" title="전체 메뉴">
		            <span class="hide">전체 메뉴</span>
		            <em class="fas fa-bars"></em>
		        </button>
		        <h1 class="logo"><a href="/sso/pre_verification">maum XDC</a></h1>
		    </div>
		    <ul id="subMenuArea" class="nav">
		        <li id="ssoMenu">
		            <h2>
		            	<a href="#none" title="사전검증 결과 조회">
		            		<span>사전검증 결과 조회</span>
		            	</a>
		            </h2>
		            <ul class="sub">
		                <li>
		                	<a id="ssoPreVerification" onclick="fnSubMenuToggleClass(this); return false;" href="/sso/pre_verification">사전검증 내역 조회</a>
		                </li>
		            </ul>
		        </li>
		    </ul>
		</div>
        <!-- //#snb -->

        <!-- #contents -->
        <div id="contents">
            <!-- .content -->
            <div class="content">
				<div class="etcArea">
				    <div class="user_menu">
				    </div>
				</div>

                <div class="titArea">
                    <h3>사전검증 결과 조회<span>사전검증 내역 조회</span></h3>
                </div>

                <div class="srchArea">
					<input type="hidden" id="currentPageNo" name="currentPageNo" value="1"/>    						<!-- 신규 검색시 1 페이지로 이동 -->
					<input type="hidden" id="h_currentPageNo" name="h_currentPageNo" value="${params.currentPageNo}"/>    	<!-- 신규 검색시 1 페이지로 이동 -->
                   	<input type="hidden" id="h_recordsPerPage" name="h_recordsPerPage" value="${params.recordsPerPage}"/> 	<!-- 한 페이지에 보여질 수 -->
                    <input type="hidden" id="h_pageSize" name="h_pageSize" value="${params.pageSize}"/>             		<!-- 페이지 블럭의 수 -->
					<input type="hidden" id="h_searchStartDate" name="h_searchStartDate" value="${params.searchStartDate}"/>           	<!-- 조회기간 S -->
					<input type="hidden" id="h_searchEndDate" name="h_searchEndDate" value="${params.searchEndDate}"/>  	<!-- 조회기간 E -->
					<input type="hidden" id="h_searchType" name="h_searchType" value="${params.searchType}"/>           	<!-- 검색 종류 대상 (부점, 직원, 제목) -->
					<input type="hidden" id="h_searchKeyword" name="h_searchKeyword" value="${params.searchKeyword}"/>  	<!-- 검색 값 -->
					<input type="hidden" id="h_createDtmOrd" name="h_createDtmOrd" value="${params.createDtmOrd}"/>  	<!-- 정렬조건 -->
					<input type="hidden" id="h_searchChannel" name="h_searchChannel" value="${params.searchChannel}"/>  	<!-- 채널 -->
					<input type="hidden" id="h_searchSelect" name="h_searchSelect" value="${params.searchSelect}"/>  	<!-- 발송목적 -->
					<input type="hidden" id="h_searchResult" name="h_searchResult" value="${params.searchResult}"/>  	<!-- 검증결과 -->
					<input type="hidden" id="h_searchFit" name="h_searchFit" value="${params.searchFit}"/>  	<!-- 일치여부 -->
					<input type="hidden" id="h_emCode" name="h_emCode" value="${params.emCode}"/>  	<!-- 일치여부 -->
					 
			
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
                                <th>채널</th>
                                <td>
                                   <select id="searchChannel" class="select">
                                        <option value="all" <c:if test="${empty params.searchChannel or params.searchChannel eq 'all'}">selected="selected"</c:if>>전체</option>
                                        <option value="crm" <c:if test="${params.searchChannel eq 'crm'}">selected="selected"</c:if>>CRM</option>
                                        <option value="center" <c:if test="${params.searchChannel eq 'center'}">selected="selected"</c:if>>IBK메시지센터</option>
                                        <%--<option value="test">테스트</option>--%>
                                    </select>
                                </td>
                                <th>발송목적</th>
                                <td>
                                   <select id="searchSelect" class="select">
                                        <option value="all" <c:if test="${empty params.searchSelect or params.searchSelect eq 'all'}">selected="selected"</c:if>>전체</option>
                                        <option value="select1" <c:if test="${params.searchSelect eq 'select1'}">selected="selected"</c:if>>기존계약유지</option>
                                        <option value="select2" <c:if test="${params.searchSelect eq 'select2'}">selected="selected"</c:if>>고객관리(감사인사,기념일축하)</option>
                                        <option value="select3" <c:if test="${params.searchSelect eq 'select3'}">selected="selected"</c:if>>심의필마케팅(일반,카드)</option>
                                        <option value="select4" <c:if test="${params.searchSelect eq 'select4'}">selected="selected"</c:if>>미심의 광고문자</option>
                                        <%--<option value="test">테스트</option>--%>
                                    </select>
                                </td>
                                <th>검증결과</th>
                                <td>
                                   <select id="searchResult" class="select">
                                        <option value="all" <c:if test="${empty params.searchResult or params.searchResult eq 'all'}">selected="selected"</c:if>>전체</option>
                                        <option value="select1" <c:if test="${params.searchResult eq 'select1'}">selected="selected"</c:if>>기존계약유지</option>
                                        <option value="select2" <c:if test="${params.searchResult eq 'select2'}">selected="selected"</c:if>>고객관리(감사인사,기념일축하)</option>
                                        <option value="select3" <c:if test="${params.searchResult eq 'select3'}">selected="selected"</c:if>>심의필마케팅(일반,카드)</option>
                                        <option value="select4" <c:if test="${params.searchResult eq 'select4'}">selected="selected"</c:if>>미심의 광고문자</option>
                                    </select>
                                </td>                  
                            </tr>
                             <tr>
                                <th>일치여부</th>
                                <td>
                                   <select id="searchFit" class="select">
                                        <option value="all" <c:if test="${empty params.searchFit or params.searchResult eq 'all'}">selected="selected"</c:if>>전체</option>
                                        <option value="fit" <c:if test="${params.searchFit eq 'fit'}">selected="selected"</c:if>>일치</option>
                                        <option value="notfit" <c:if test="${params.searchFit eq 'notfit'}">selected="selected"</c:if>>불일치</option>
                                        <%--<option value="test">테스트</option>--%>
                                    </select>
                                </td>
                                <th>정렬조건<br>(검증일시)</th>
                                <td>
                                   <select id="createDtmOrd" class="select">
									    <option value="asc" <c:if test="${empty params.createDtmOrd or params.createDtmOrd eq 'asc'}">selected="selected"</c:if>>오름차순</option>
									    <option value="desc" <c:if test="${params.createDtmOrd eq 'desc'}">selected="selected"</c:if>>내림차순</option>
                                    </select>
                                </td>
                                <th>대상</th>
                                <td>
                                   <select id="searchType" class="select">
                                        <option value="br" <c:if test="${empty params.searchType or params.searchType eq 'br'}">selected="selected"</c:if>>부점</option>
                                        <option value="title" <c:if test="${params.searchType eq 'title'}">selected="selected"</c:if>>제목</option>
                                    </select>
                                    <input id="searchKeyword" type="text" class="ipt_txt" placeholder="검색어를 입력해주세요." value="${params.searchKeyword}">
                                </td>
                                <td>
                                    <button type="button" class="btn_primary" onclick="javascript:searchMsg();">검색</button> 
                                </td>  
                            </tr>
                        </tbody>
                    </table>
                </div>
                 <div class="btnBox">
                     <button type="button" class="btn_excel fr" onclick="javascript:excelDown();">엑셀 다운로드</button>
                </div>
                <%-- --------------------------------------- --%>

                <div class="tblBox">
                    <table summary="연번, 실행 일시, 구분, 확인 결과, 전행고객번호, 상세보기로 구성">
                        <caption class="hide">진위확인 결과 조회</caption>
                        <thead>
                            <tr>
                                <th scope="col">No.</th>
                                <th scope="col">채널</th>
                                <th scope="col">검증일시</th>
                                <th scope="col">부점</th>
                                <th scope="col">직원</th>
                                <th scope="col">제목</th>
                                <th scope="col">발송목적</th>
                                <th scope="col">검증결과</th>
                                <th scope="col">일치여부</th>
                                <th scope="col">확률(%)</th>
                                <th scope="col">상세보기</th>
                            </tr>
                        </thead>
                        <tbody>
                        	<c:choose>
                        		<c:when test="${empty msgList }">
		                            <tr>
		                                <td class="empty" scope="row" colspan="11">데이터가 없습니다.</td>
		                            </tr>
                        		</c:when>
                        		<c:when test="${!empty msgList }">
                        			<c:forEach var="list" varStatus="status" items="${msgList}" >
			                            <tr>
			                                <!-- ((현재 페이지 번호-1)*페이지별 보여지는 수량) + 현재 순번 -->
			                                <td>
			                                	<c:set var="no" value="" />
			                                	<c:choose>
			                                		<c:when test="${params.createDtmOrd eq 'asc'}">
			                                			${((params.currentPageNo-1)*params.recordsPerPage) + status.count}
			                                			<c:set var="no" value="${((params.currentPageNo-1)*params.recordsPerPage) + status.count}" />
			                                		</c:when>
			                                		<c:when test="${params.createDtmOrd eq 'desc'}">
			                                			${params.paginationInfo.totalRecordCount - ((params.currentPageNo-1) * params.recordsPerPage + status.index) }
			                                			<c:set var="no" value="${params.paginationInfo.totalRecordCount - ((params.currentPageNo-1) * params.recordsPerPage + status.index) }" />
			                                		</c:when>
			                                	</c:choose>
			                                	<input type="hidden" id="msg_${no}" value="${no}">
		                                        <input type="hidden" id="msgContext_${no}" value="${list.msgContext}">
		                                        <input type="hidden" id="msgKeyStartIndex_${no}" value="${list.keyStartIndex}">
		                                        <input type="hidden" id="msgKeyEndIndex_${no}" value="${list.keyEndIndex}">
		                                        <input type="hidden" id="msgSelectLabel_${no}" value="${list.selectLabel}">
		                                        <input type="hidden" id="msgResultLabel_${no}" value="${list.resultLabel}">
		                                        <input type="hidden" id="msgIsMatch_${no}" value="${list.isMatch}">
			                                </td>
			                                <td>
			                                	<c:choose>
			                                		<c:when test="${list.channel eq 'CENTER'}">IBK메시지센터</c:when>
			                                		<c:otherwise>${list.channel}</c:otherwise>
			                                	</c:choose>
		                                	</td>
			                                <td><fmt:formatDate value="${list.createDtm }" pattern="yyyy-MM-dd"/></td><!-- HH:mm:ss -->
			                                <td>${list.brCode }</td>
			                                <td>${list.emCode }</td>
			                                <td>${list.msgTitle }</td>
			                                <td>${list.selectLabel }</td>
			                                <td>${list.resultLabel }</td>
			                                <td>
			                                	<c:choose>
			                                		<c:when test="${list.isMatch eq '0'}"><span class="ft_point_pink">불일치</span></c:when>
			                                		<c:when test="${list.isMatch eq '1'}"><span class="">일치</span></c:when>
			                                		<c:otherwise><span class="">-</span></c:otherwise>
			                                	</c:choose>
		                                	</td>
		                                	<td><fmt:formatNumber value="${list.percentage}" type="percent"/></td>
                                			<td><button type="button" class="btn_view_detail btn_lyr_open" onclick="javascript:msgOpen('${no}')"></button></td>
			                            </tr>
                        			</c:forEach>
                        		</c:when>
                        	</c:choose>
                        </tbody>
                    </table>
                </div>

                 <div id="msgPagingArea" class="pagingArea">
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
							<a href="javascript:movePage('/sso/pre_verification', '${params.makeQueryString(1)}');" class="btn_paging first">
	                            <span class="hide">처음 페이지</span>
	                            <em class="fas fa-angle-double-left"></em>
	                        </a>
							<a href="javascript:movePage('/sso/pre_verification', '${params.makeQueryString(params.paginationInfo.firstPage-1)}');" class="btn_paging prev">
	                            <span class="hide">이전 페이지</span>
	                            <em class="fas fa-angle-left"></em>
	                        </a>
                        </c:if>
                        
                       	<c:forEach begin="${params.paginationInfo.firstPage}" end="${params.paginationInfo.lastPage}" var="pageNo">
                        	<!-- [D] 현재 페이지 인덱스에 addClass('current') 해주면 활성화됨 -->
							<a href="javascript:movePage('/sso/pre_verification', '${params.makeQueryString(pageNo)}');"  class="<c:out value="${params.currentPageNo == pageNo ? 'current' : ''}"/>">${pageNo}</a>
                       	</c:forEach>
						<c:if test="${params.paginationInfo.hasNextPage}">
							<a href="javascript:movePage('/sso/pre_verification', '${params.makeQueryString(params.paginationInfo.lastPage+1)}');" class="btn_paging next">
	                            <span class="hide">다음 페이지</span>
	                            <em class="fas fa-angle-right"></em>
	                        </a>
							<a href="javascript:movePage('/sso/pre_verification', '${params.makeQueryString(params.paginationInfo.totalPageCount)}');" class="btn_paging last">
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
<div id="msg_detail" class="lyrWrap">
<input type="hidden" id="msgId">
<input type="hidden" id="msgData">
	<div id="msg_detail_background" class="lyr_bg" style="display:none;"></div>
    <div class="lyrCont">
        <div class="lyr_top">
            <p class="tit">사전검증 결과</p>
            <button type="button" class="btn_lyr_close"  onclick="javascript:msgClose();"><span class="hide">닫기버튼</span></button>
        </div>
        <div class="lyr_mid">
            <div class="detect_img">
                <!-- [D] 진위확인 결과에 따라 .stateBox p에 addClass('on')해주시면 됩니다. -->
                <div class="stateBox">
                    <p id="msg_detail_title" class="state fake"><%-- 불일치 광고성 문자 --%></p>
                </div>
            </div>
            <div class="result_detail">
                <table summary="분류, paper로 구성">
                    <caption class="hide">사전검증 상세결과</caption>
                    <colgroup>
                        <col width="134px"><col>
                    </colgroup>
                    <tbody>
                        <tr>
                            <th>발송목적</th>
                            <td id="msg_detail_select">
                            	<%-- 기존계약유지관리 --%>
                            </td>
                        </tr>
                        <tr>
                            <th>검증결과</th>
                            <td id="msg_detail_result" >
                            	<%--<span class="ft_point_pink">일반마케팅광고</span> --%>
                            </td>
                        </tr>
                        <tr>
                            <td id="msg_detail_context" colspan="2" class="lyr_con">
                               <%--안녕하세요. 이순신 고객님 IBK 기업은행입니다 .고객님께서 보유 중이신 적금의 만기일이 2021년 9 월 30 일입니다 .지금 사용계획 없으시다면 이번에 새로 출시된 IBK 적금 가입은 어떠실까요 1년 가입 기준 최고 연 2.30% 금리 제공하는 상품입니다. 희망하실 경우 방문 또는 비대면으로 업무 진행 가능하며 , IBK 기업은행 대리 김대리 (02 000 0000) 으로 연락주세요 --%> 
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
			<c:choose>
				<c:when test="${params.createDtmOrd eq 'asc'}">
		            <button type="button" class="btn_prev_result" onclick="javascript:ajaxGetMsgDetail('prev');">
		                <span class="hide">이전 상세결과</span>
		            </button>
		            <button type="button" class="btn_next_result" onclick="javascript:ajaxGetMsgDetail('next');">
		                <span class="hide">다음 상세결과</span>
		            </button>
				</c:when>
				
				<c:when test="${params.createDtmOrd eq 'desc'}">
		            <button type="button" class="btn_prev_result" onclick="javascript:ajaxGetMsgDetail('next');">
		                <span class="hide">이전 상세결과</span>
		            </button>
		            <button type="button" class="btn_next_result" onclick="javascript:ajaxGetMsgDetail('prev');">
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
        $("#ssoMenu").addClass("active");//상위 메뉴
        $("#ssoPreVerification").addClass("active");//하위 메뉴
        
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
	queryString += "&searchChannel=" + $('#h_searchChannel').val();
	queryString += "&searchSelect=" + $('#h_searchSelect').val();
	queryString += "&searchResult=" + $('#h_searchResult').val();
	queryString += "&searchFit=" + $('#h_searchFit').val();
	location.href = uri + queryString;	
}

//사용자 목록 조회
function searchMsg(){
	
	var startDate = $('#searchStartDate').val();
	var endDate = $('#searchEndDate').val();
	
	var s = new Date(startDate);
	var e = new Date(endDate);
	
	if (s>e){
		alert('조회기간이 올바르지 않습니다.');
		return;
	}
	
	var uri = "/sso/pre_verification";
	var queryString = "?currentPageNo=" + $('#currentPageNo').val()
			+ "&recordsPerPage=" + $('#recordsPerPage').val()// 현재 선택된 레코드 수로 검색
			+ "&pageSize=" + $('#h_pageSize').val()
			+ "&searchStartDate=" + startDate// 조회기간 시작
			+ "&searchEndDate=" + endDate// 조회 기간 끝
			+ "&searchChannel=" + $('#searchChannel').val()// 채널
			+ "&searchSelect=" + $('#searchSelect').val()// 발송목적
			+ "&searchResult=" + $('#searchResult').val()// 검증결과 
			+ "&searchFit=" + $('#searchFit').val()// 일치여부 
			+ "&searchType=" + $('#searchType').val()// 대상 (부점, 직원, 제목)
			+ "&searchKeyword=" + $('#searchKeyword').val()// 검색어 
			+ "&createDtmOrd=" + $('#createDtmOrd').val();// 정렬조건
	
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
function msgOpen(id){
	modalOpenClose("msg_detail", "open");
	
	// 하이라이트 
	var labelStart = '<span class="highlight">';
	var labelEnd = '</span>';

    // 선택한 값 데이터 셋팅
    var id = $('#msg_'+ id).val();
	if (id == null || id == undefined) {
		alert('해당 페이지의 마지막 정보입니다.');
		return;
	}
    var context = $('#msgContext_'+ id).val();
    var startIndex = $('#msgKeyStartIndex_'+ id).val();
    var endIndex = $('#msgKeyEndIndex_'+ id).val();
    var selectLabel = $('#msgSelectLabel_'+ id).val();
    var resultLabel = $('#msgResultLabel_'+ id).val();
    var isMatch = $('#msgIsMatch_'+ id).val();
    
	// 상세보기
	var title = ''
	if (isMatch=='0') {
		title = '불일치 광고성 문자';
		$('#msg_detail_title').addClass('on');
	} else if (isMatch=='1'){
		title = '일치';
		$('#msg_detail_title').removeClass('on');
	} else {
	}
	
	$('#msgId').val(id);
	$('#msg_detail_title').html(title);
	$('#msg_detail_select').html(selectLabel);
	$('#msg_detail_result').html(resultLabel);

	endIndex = endIndex + labelStart.length;
	
	// 텍스트  하이라이팅 추가 
	var output = '';
		output = [context.slice(0, startIndex), labelStart, context.slice(startIndex)].join('');
		output = [output.slice(0, endIndex), labelEnd, output.slice(endIndex)].join('');
	
	$('#msg_detail_context').html(output);
}

// 모달
function msgClose(){
	modalOpenClose("msg_detail", "close");

	$('#msg_detail_title').val('');
	$('#msg_detail_select').val('');
	$('#msg_detail_result').val('');
	$('#msg_detail_context').val('');
}

// 상세보기 
function ajaxGetMsgDetail(type){
	var order = $('#h_createDtmOrd').val(); // desc 내림차순, asc 오름차순
	var msgId = $('#msgId').val();
	if (type == 'prev'){
		msgId = parseInt(msgId) - 1;
	} else if (type == 'next') {
		msgId = parseInt(msgId) + 1;
	}
	 
	msgOpen(msgId);
}

function excelDown(){
    var newForm = $('<form></form>');
    
    newForm.attr("name", "excelFrm");
    newForm.attr("method", "post");
    newForm.attr("action", "/sso/pre_verification/excel_down");
    newForm.attr("target", "hide_frame");

    newForm.append($('<input/>', {type: 'hidden', name: 'searchType', value: $('#h_searchType').val()}));
    newForm.append($('<input/>', {type: 'hidden', name: 'searchKeyword', value: $('#h_searchKeyword').val()}));
    newForm.append($('<input/>', {type: 'hidden', name: 'searchStartDate', value: $('#h_searchStartDate').val()}));
    newForm.append($('<input/>', {type: 'hidden', name: 'searchEndDate', value: $('#h_searchEndDate').val()}));
    newForm.append($('<input/>', {type: 'hidden', name: 'createDtmOrd', value: $('#h_createDtmOrd').val()}));
    newForm.append($('<input/>', {type: 'hidden', name: 'searchChannel', value: $('#h_searchChannel').val()}));
    newForm.append($('<input/>', {type: 'hidden', name: 'searchSelect', value: $('#h_searchSelect').val()}));
    newForm.append($('<input/>', {type: 'hidden', name: 'searchResult', value: $('#h_searchResult').val()}));
    newForm.append($('<input/>', {type: 'hidden', name: 'searchFit', value: $('#h_searchFit').val()}));
    newForm.append($('<input/>', {type: 'hidden', name: 'emCode', value: $('#h_emCode').val()}));
    
	
    newForm.appendTo('body');

    newForm.submit();
}

// 서브메뉴 선택 이벤트
function fnSubMenuToggleClass(obj){
	var tagName =$(obj).parent().prop('tagName');
	var href = $(obj).attr('href');
	if(tagName == 'H2'){
		$(obj).parent().parent().toggleClass('active');
		location.href = href;
	}else{
		$(obj).toggleClass('active');
		location.href = href;
	}
}
</script>
</body>
</html>