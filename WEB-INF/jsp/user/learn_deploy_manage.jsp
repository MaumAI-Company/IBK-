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
                    <h3>학습 관리<span>배포 관리</span></h3>
                </div>

                <div class="srchArea">
					<input type="hidden" id="currentPageNo" name="currentPageNo" value="1"/>    						<!-- 신규 검색시 1 페이지로 이동 -->
					<input type="hidden" id="h_currentPageNo" name="h_currentPageNo" value="${params.currentPageNo}"/>    	<!-- 신규 검색시 1 페이지로 이동 -->
                   	<input type="hidden" id="h_recordsPerPage" name="h_recordsPerPage" value="${params.recordsPerPage}"/> 	<!-- 한 페이지에 보여질 수 -->
                    <input type="hidden" id="h_pageSize" name="h_pageSize" value="${params.pageSize}"/>             		<!-- 페이지 블럭의 수 -->
					<input type="hidden" id="h_searchType" name="h_searchType" value="${params.searchType}"/>           	<!-- 검색 종류 -->
					<input type="hidden" id="h_searchKeyword" name="h_searchKeyword" value="${params.searchKeyword}"/>  	<!-- 검색 값 -->
					<input type="hidden" id="h_searchUse" name="h_searchUse" value="${params.searchUse}"/>  	<!-- 상태 -->
					<input type="hidden" id="h_searchDeploy" name="h_searchDeploy" value="${params.searchDeploy}"/>           	<!-- 배포여부 -->
					<input type="hidden" id="h_deployDtOrd" name="h_deployDtOrd" value="${params.deployDtOrd}"/>  	<!-- 정렬조건 -->
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
                                   <select id="searchUse" class="select">
									    <option value="all" <c:if test="${empty params.searchUse or params.searchUse eq 'all'}">selected="selected"</c:if>>전체</option>
									    <option value="use" <c:if test="${params.searchUse eq 'use'}">selected="selected"</c:if>>사용</option>
									    <option value="stop" <c:if test="${params.searchUse eq 'stop'}">selected="selected"</c:if>>중지</option>
                                    </select>
                                </td>          
                            </tr>
                            <tr>   
                                <th>배포여부</th>
                                <td>
                                   <select id="searchDeploy" class="select">
									    <option value="all" <c:if test="${empty params.searchDeploy or params.searchDeploy eq 'all'}">selected="selected"</c:if>>전체</option>
									    <option value="success" <c:if test="${params.searchDeploy eq 'success'}">selected="selected"</c:if>>성공</option>
									    <option value="fail" <c:if test="${params.searchDeploy eq 'fail'}">selected="selected"</c:if>>실패</option>
                                    </select>
                                </td>    
                                <th>정렬조건</th>
                                <td>
                                   <select id="deployDtOrd" class="select">
									    <option value="asc" <c:if test="${empty params.deployDtOrd or params.deployDtOrd eq 'asc'}">selected="selected"</c:if>>오름차순</option>
									    <option value="desc" <c:if test="${params.deployDtOrd eq 'desc'}">selected="selected"</c:if>>내림차순</option>
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
                    <%--
                    <button type="button" class="btn_primary btn_lyr_open" onclick="javascript:void(0);">배포</button>
                     --%>
                </div>
                <%-- --------------------------------------- --%>

                <div class="tblBox">
                    <table summary="연번, 실행 일시, 구분, 확인 결과, 전행고객번호, 상세보기로 구성">
                        <caption class="hide">배포 이력</caption>
                        <thead>
                            <tr>
                                <th scope="col">NO.</th>
                                <th scope="col">학습모델명</th>
                                <th scope="col">등록자</th>
                                <th scope="col">상태</th>
                                <th scope="col">배포여부</th>
                                <th scope="col">배포일시</th>
                            </tr>
                        </thead>
                        <tbody>
                        	<c:choose>
                        		<c:when test="${empty learnHistoryList }">
		                            <tr>
		                                <td class="empty" scope="row" colspan="6">데이터가 없습니다.</td>
		                            </tr>
                        		</c:when>
                        		<c:when test="${!empty learnHistoryList }">
                        			<c:forEach var="list" varStatus="status" items="${learnHistoryList}" >
			                            <tr>
			                                <!-- ((현재 페이지 번호-1)*페이지별 보여지는 수량) + 현재 순번 -->
			                                <td>
			                                	<c:set var="no" value="" />
			                                	<c:choose>
			                                		<c:when test="${params.deployDtOrd eq 'asc'}">
			                                			${((params.currentPageNo-1)*params.recordsPerPage) + status.count}
			                                			<c:set var="no" value="${((params.currentPageNo-1)*params.recordsPerPage) + status.count}" />
			                                		</c:when>
			                                		<c:when test="${params.deployDtOrd eq 'desc'}">
			                                			${params.paginationInfo.totalRecordCount - ((params.currentPageNo-1) * params.recordsPerPage + status.index) }
			                                			<c:set var="no" value="${params.paginationInfo.totalRecordCount - ((params.currentPageNo-1) * params.recordsPerPage + status.index) }" />
			                                		</c:when>
			                                	</c:choose>
			                                </td>
			                                <td>${list.learnName }</td>
			                                <td>${list.regId }</td>
			                                <td>
			                                	<c:choose>
			                                		<c:when test="${list.useStatus eq '0'}"><span class="ft_point_pink">중지</span></c:when>
			                                		<c:when test="${list.useStatus eq '1'}">사용</c:when>
			                                		<c:otherwise>-</c:otherwise>
			                                	</c:choose>
		                                	</td>
			                                <td>
			                                	<c:choose>
			                                		<c:when test="${list.deployStatus eq '0'}"><span class="ft_point_pink">실패</span></c:when>
			                                		<c:when test="${list.deployStatus eq '1'}">성공</c:when>
			                                		<c:otherwise>-</c:otherwise>
			                                	</c:choose>
			                                </td>
			                                <td><fmt:formatDate value="${list.deployDt }" pattern="yyyy-MM-dd"/></td><!-- HH:mm:ss -->
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
							<a href="javascript:movePage('/user/learn_deploy_manage', '${params.makeQueryString(1)}');" class="btn_paging first">
	                            <span class="hide">처음 페이지</span>
	                            <em class="fas fa-angle-double-left"></em>
	                        </a>
							<a href="javascript:movePage('/user/learn_deploy_manage', '${params.makeQueryString(params.paginationInfo.firstPage-1)}');" class="btn_paging prev">
	                            <span class="hide">이전 페이지</span>
	                            <em class="fas fa-angle-left"></em>
	                        </a>
                        </c:if>
                        
                       	<c:forEach begin="${params.paginationInfo.firstPage}" end="${params.paginationInfo.lastPage}" var="pageNo">
                        	<!-- [D] 현재 페이지 인덱스에 addClass('current') 해주면 활성화됨 -->
							<a href="javascript:movePage('/user/learn_deploy_manage', '${params.makeQueryString(pageNo)}');"  class="<c:out value="${params.currentPageNo == pageNo ? 'current' : ''}"/>">${pageNo}</a>
                       	</c:forEach>
						<c:if test="${params.paginationInfo.hasNextPage}">
							<a href="javascript:movePage('/user/learn_deploy_manage', '${params.makeQueryString(params.paginationInfo.lastPage+1)}');" class="btn_paging next">
	                            <span class="hide">다음 페이지</span>
	                            <em class="fas fa-angle-right"></em>
	                        </a>
							<a href="javascript:movePage('/user/learn_deploy_manage', '${params.makeQueryString(params.paginationInfo.totalPageCount)}');" class="btn_paging last">
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
                                 <input id="epoch" name="epoch" type="text" class="ipt_txt" placeholder="20 (default)" onkeypress="return isNumberKey(event);" onkeyup="this.value=this.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g, '');">
                             </td>
                        </tr>
                        <tr>
                             <td>* Epoch : 학습 데이셋을 학습하는 회수</td>
                         </tr>
                         <tr>
                             <th rowspan="2">Learning Rate</th>
                             <td>
                                <input id="learningRate" name="learningRate" type="text" class="ipt_txt" placeholder="0.02 (default)" onkeypress="return isNumberKey(event);" onkeyup="this.value=this.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g, '');">
                            </td>
                         </tr>
                          <tr>
                             <td>* * Learning Rate : 학습률 , 최적 값을 찾기 위한 기울기의 이동 정도</td>
                         </tr>
                         <tr>
                             <th rowspan="2">Batch Size</th>
                             <td>
                                 <input id="batchSize" name="batchSize" type="text" class="ipt_txt" placeholder="128 (default)" onkeypress="return isNumberKey(event);" onkeyup="this.value=this.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g, '');">
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
        $("#sub_ID00010").addClass("active");//하위 메뉴
        
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
	queryString += "&deployDtOrd=" + $('#h_deployDtOrd').val();
	queryString += "&searchUse=" + $('#h_searchUse').val();// 상태
	queryString += "&searchDeploy=" + $('#h_searchDeploy').val();// 배포여부
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
	
	var uri = "/user/learn_deploy_manage";
	var queryString = "?currentPageNo=" + $('#currentPageNo').val()
			+ "&recordsPerPage=" + $('#recordsPerPage').val() // 현재 선택된 레코드 수로 검색
			+ "&pageSize=" + $('#h_pageSize').val()
			+ "&searchKeyword=" + $('#searchKeyword').val()
			+ "&searchStartDate=" + startDate
			+ "&searchEndDate=" + endDate
			+ "&searchUse=" + $('#searchUse').val()// 상태
			+ "&searchDeploy=" + $('#searchDeploy').val()// 배포여부 
			+ "&searchType=" + $('#searchType').val()// 대상 (학습모델명, 등록자)
			+ "&deployDtOrd=" + $('#deployDtOrd').val();
	
	location.href = uri + queryString;
}
</script>
</body>
</html>