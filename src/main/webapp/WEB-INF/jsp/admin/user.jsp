<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ include file="/WEB-INF/jsp/include/header.jspf" %>
    <title>Admin (사용자 관리) || maum XDC</title>
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
            <div class="content admin_user">
                <%@ include file="/WEB-INF/jsp/include/etcArea.jspf" %>

                <div class="titArea">
                    <h3>Admin <span>조직 관리 <em class="fas fa-angle-right"></em> 사용자 관리</span></h3>
                </div>
                <div class="srchArea">
					<input type="hidden" id="currentPageNo" name="currentPageNo" value="1"/>    						<!-- 신규 검색시 1 페이지로 이동 -->
					<input type="hidden" id="h_currentPageNo" name="h_currentPageNo" value="${params.currentPageNo}"/>    	<!-- 신규 검색시 1 페이지로 이동 -->
                   	<input type="hidden" id="h_recordsPerPage" name="h_recordsPerPage" value="${params.recordsPerPage}"/> 	<!-- 한 페이지에 보여질 수 -->
                    <input type="hidden" id="h_pageSize" name="h_pageSize" value="${params.pageSize}"/>             		<!-- 페이지 블럭의 수 -->
					<input type="hidden" id="h_searchType" name="h_searchType" value="${params.searchType}"/>           	<!-- 검색 종류 -->
					<input type="hidden" id="h_searchKeyword" name="h_searchKeyword" value="${params.searchKeyword}"/>  	<!-- 검색 값 -->
                    <table summary="조건 선택으로 구성">
                        <caption class="hide">조건 선택</caption>
                        <colgroup>
                            <col width="180px"><col>
                        </colgroup>
                        <tbody>
                            <tr>
                                <th>조건 선택</th>
                                <td>
                                    <select id="searchType" class="select">
                                        <option value="all" <c:if test="${empty params.searchType or params.searchType eq 'all'}">selected="selected"</c:if>>전체</option>
                                        <option value="memName" <c:if test="${params.searchType eq 'memName'}">selected="selected"</c:if>>이름</option>
                                        <option value="memId" <c:if test="${params.searchType eq 'memId'}">selected="selected"</c:if>>아이디</option>
                                        <option value="deptCode" <c:if test="${params.searchType eq 'deptCode'}">selected="selected"</c:if>>부서코드</option>
                                        <option value="deptName" <c:if test="${params.searchType eq 'deptName'}">selected="selected"</c:if>>부서명</option>
                                        <option value="deptEngName" <c:if test="${params.searchType eq 'deptEngName'}">selected="selected"</c:if>>부서명(영문)</option>
                                    </select>
                                    <input id="searchKeyword" type="text" class="ipt_txt" style="width: 240px;" placeholder="검색어를 입력해주세요." value="${params.searchKeyword}">
                                    <button type="button" class="btn_primary" onclick="javascript:searchUser();" >검색</button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="btnBox">
                    <button type="button" class="btn_primary btn_lyr_open" onclick="javascript:addUserOpen();">등록</button><!-- data-lyr-name="add_user" -->
                    <button type="button" class="btn_primary line" onclick="javascript:chkBoxDeleteUser();">삭제</button>
                </div>

                <div class="tblBox icld_chk admin">
                    <table summary="No. 이름, 아이디, 부서ID, 부서명, 부서명(영문), 연락처, 등록일, 수정일로 구성">
                        <caption class="hide">사용자 관리</caption>
                        <colgroup>
                            <col width="50px"><col><col><col><col><col><col><col width="12%"><col width="11%"><col width="11%">
                        </colgroup>
                        <thead>
                            <tr>
                                <th scope="col">
                                    <div class="chkBoxOnly">
                                        <input type="checkbox" class="allChk">
                                    </div>
                                </th>
                                <th scope="col">No.</th>
                                <th scope="col">이름</th>
                                <th scope="col">아이디</th>
                                <th scope="col">부서코드</th>
                                <th scope="col">부서명</th>
                                <th scope="col">부서명(영문)</th>
                                <th scope="col">연락처</th>
                                <th scope="col">등록일</th>
                                <th scope="col">수정일</th>
                            </tr>
                        </thead>
                        <tbody>
                        	<c:choose>
                        		<c:when test="${empty userList }">
		                            <tr>
		                                <td class="empty" scope="row" colspan="10">데이터가 없습니다.</td>
		                            </tr>
                        		</c:when>
                        		<c:when test="${!empty userList }">
                        			<c:forEach var="list" varStatus="status" items="${userList}" >
			                            <tr>
			                                <td scope="row">
			                                    <div class="chkBoxOnly">
			                                        <input name="chkBoxUser" type="checkbox" class="eachChk" value="${list.memId}">
			                                    </div>
			                                </td>
			                                <!-- ((현재 페이지 번호-1)*페이지별 보여지는 수량) + 현재 순번 -->
			                                <td>${((params.currentPageNo-1)*params.recordsPerPage) + status.count}</td>
			                                <td>${list.memName }</td>
			                                <td><a href="javascript:modUserOpen('${list.memId}');"><span class="user_id">${list.memId}</span></a></td>
			                                <td>${list.deptCode }</td>
			                                <td>${list.deptName }</td>
			                                <td>${list.deptEngName }</td>
			                                <td>${list.memPhone }</td>
			                                <td><fmt:formatDate value="${list.memRegDt }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			                                <td><fmt:formatDate value="${list.memModDt }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			                            </tr>
                        			</c:forEach>
                        		</c:when>
                        	</c:choose>
                        </tbody>
                    </table>
                </div>

                <div id="userPagingArea" class="pagingArea">
					<div class="paging">
						<c:if test="${params.paginationInfo.hasPreviousPage}">
							<a href="javascript:movePage('/admin/user', '${params.makeQueryString(1)}');" class="btn_paging first">
	                            <span class="hide">처음 페이지</span>
	                            <em class="fas fa-angle-double-left"></em>
	                        </a>
							<a href="javascript:movePage('/admin/user', '${params.makeQueryString(params.paginationInfo.firstPage-1)}');" class="btn_paging prev">
	                            <span class="hide">이전 페이지</span>
	                            <em class="fas fa-angle-left"></em>
	                        </a>
                        </c:if>
                        
                       	<c:forEach begin="${params.paginationInfo.firstPage}" end="${params.paginationInfo.lastPage}" var="pageNo">
                        	<!-- [D] 현재 페이지 인덱스에 addClass('current') 해주면 활성화됨 -->
							<a href="javascript:movePage('/admin/user', '${params.makeQueryString(pageNo)}');"  class="<c:out value="${params.currentPageNo == pageNo ? 'current' : ''}"/>">${pageNo}</a>
                       	</c:forEach>
						<c:if test="${params.paginationInfo.hasNextPage}">
							<a href="javascript:movePage('/admin/user', '${params.makeQueryString(params.paginationInfo.lastPage+1)}');" class="btn_paging next">
	                            <span class="hide">다음 페이지</span>
	                            <em class="fas fa-angle-right"></em>
	                        </a>
							<a href="javascript:movePage('/admin/user', '${params.makeQueryString(params.paginationInfo.totalPageCount)}');" class="btn_paging last">
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
<!-- 사용자 등록 -->
<div id="add_user" class="lyrWrap modify">
	<div id="add_user_background" class="lyr_bg" style="display:none;"></div>
    <div class="lyrCont">
        <div class="lyr_top">
            <p class="tit">사용자 등록</p>
            <button type="button" class="btn_lyr_close" onclick="addUserClose();"><span class="hide">닫기버튼</span></button>
        </div>
        <div class="lyr_mid">
           <div class="tblBox">
               <table summary="부서 코드, 사용자 ID, 비밀번호, 이름, 성별, 이메일, 생년월일, 직급, 휴대폰, 주소, 사용여부, 부서로 구성">
                    <caption class="hide">사용자 등록</caption>
                    <colgroup>
                        <col width="114px"><col width="253px"><col width="114px"><col>
                    </colgroup>
                    <tbody>
                        <tr>
                            <th rowspan="2">*부서 명/부서 코드</th>
                            <td colspan="3">
                                <input id="addDeptId" name="addDeptId" type="hidden">
                                <input id="addDeptName" name="addDeptName" type="text" class="ipt_txt" readonly="readonly">
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3">
                                <input id="addDeptCode" name="addDeptCode" type="text" class="ipt_txt dptmt_code" readonly="readonly">
                                <button type="button" class="btn_primary line btn_lyr_open" onclick="searchDeptOpen();">조회</button><!-- data-lyr-name="srch_dptmt" -->
                            </td>
                        </tr>
                        <tr>
                            <th>*사용자 ID</th>
                            <td colspan="3">
                                <input id="addMemId" name="addMemId" type="text" class="ipt_txt user_id">
                                <input id="checkUserId" name="checkUserId" type="hidden">
                                <input id="checkUserYN" name="checkUserYN" type="hidden">
                                <button type="button" onclick="addCheckUserId();" class="btn_primary line btn_lyr_open">중복확인</button><!-- data-lyr-name="id_chk" -->
                            </td>
                            <!-- 사용자 등록시에는 비밀번호 자동으로 초기화 되어 등록되도록. 설정
	                           <th>비밀번호</th>
	                           <td>
	                               <button type="button" class="btn_primary line">비밀번호 초기화</button>
	                           </td>
                             -->
                        </tr>
                        <tr>
                            <th>*이름</th>
                            <td>
                                <input id="addMemName" name="addMemName" type="text" class="ipt_txt">
                            </td>
                            <th>성별</th>
                            <td>
                                <div class="radioBox">   
                                    <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->
                                    <input type="radio" name="addMemGender" id="addMan" value="M" checked>
                                    <label for="addMan">남</label>    
                                    <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->                      
                                    <input type="radio" name="addMemGender" id="addWoman" value="W" >
                                    <label for="addWoman">여</label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>이메일</th>
                            <td>
                                <input id="addMemEmail1" name="addMemEmail1" type="text" class="ipt_txt email" style="width:44%;">
                                @
                                <input id="addMemEmail2" name="addMemEmail2" type="text" class="ipt_txt email" style="width:44%;">
                            </td>
                            <th>생년월일</th>
                            <td>
                                <input id="addMemBirthday" name="addMemBirthday" type="text" class="ipt_txt" placeholder="">
                            </td>
                        </tr>
                        <tr>
                            <th>휴대폰</th>
                            <td>
                                <input id="addMemPhone" name="addMemPhone" type="text" class="ipt_txt" placeholder="" maxlength="11">
                            </td>
                            <th>권한</th>
                            <td>
                                <div class="radioBox">   
                                    <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->
                                    <input type="radio" id="addMemRoleUser" name="addMemRoleId" value="USER" checked>
                                    <label for="addMemRoleUser">사용자</label>    
                                    <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->                      
                                    <input type="radio" id="addMemRoleAdmin" name="addMemRoleId" value="ADMIN">
                                    <label for="addMemRoleAdmin">관리자</label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>IP 주소</th>
                            <td>
                                <input id="addMemColumn1" name="addMemColumn1" type="text" class="ipt_txt" placeholder="xxx.xxx.xxx.xxx, ..." onKeyUp="javascript:noEnKo(this)">
                            </td>
                            <th>IP 체크여부</th>
                            <td>
                                <div class="radioBox">   
                                    <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->
                                    <input type="radio" id="addMemColumn2_Y" name="addMemColumn2" value="Y">
                                    <label for="addMemColumn2_Y">사용</label>    
                                    <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->                      
                                    <input type="radio" id="addMemColumn2_N" name="addMemColumn2" value="N" checked>
                                    <label for="addMemColumn2_N">미사용</label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>*사용여부</th>
                            <td>
                                <div class="radioBox">   
                                    <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->
                                    <input type="radio" id="addMemStatUse" name="addMemStat" value="use" checked>
                                    <label for="addMemStatUse">사용</label>    
                                    <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->                      
                                    <input type="radio" id="addMemStatUnused" name="addMemStat" value="unused" >
                                    <label for="addMemStatUnused">미사용</label>
                                </div>
                            </td>
                        </tr>
                    </tbody>
               </table>
           </div>
        </div>
        <div class="lyr_btm">
            <button id="add_user_btn_cancle" type="button" class="btn_primary line btn_lyr_close" onclick="javascript:addUserClose();">취소</button>
            <button type="button" class="btn_primary" onclick="javascript:addUser();">저장</button>
        </div>
    </div>
</div>
<!-- //사용자 등록 -->
<!-- 사용자 수정 -->
<div id="mod_user" class="lyrWrap modify">
	<div id="mod_user_background" class="lyr_bg" style="display:none;"></div>
    <div class="lyrCont">
        <div class="lyr_top">
            <p class="tit">사용자 수정</p>
            <button type="button" class="btn_lyr_close" onclick="modUserClose();"><span class="hide">닫기버튼</span></button>
        </div>
        <div class="lyr_mid">
           <div class="tblBox">
               <table summary="부서 코드, 사용자 ID, 비밀번호, 이름, 성별, 이메일, 생년월일, 직급, 휴대폰, 주소, 사용여부, 부서로 구성">
                    <caption class="hide">사용자 등록</caption>
                    <colgroup>
                        <col width="114px"><col width="253px"><col width="114px"><col>
                    </colgroup>
                    <tbody>
                        <tr>
                            <th rowspan="2">*부서 명/부서 코드</th>
                            <td colspan="3">
                                <input id="modDeptId" name="modDeptId" type="hidden">
                                <input id="modDeptName" name="modDeptName" type="text" class="ipt_txt" readonly="readonly">
                            </td>
                        </tr>
                        <tr>
                            <td colspan="3">
                                <input id="modDeptCode" name="modDeptCode" type="text" class="ipt_txt dptmt_code" readonly="readonly">
                                <button type="button" class="btn_primary line btn_lyr_open" onclick="searchDeptOpen();">조회</button><!-- data-lyr-name="srch_dptmt" -->
                            </td>
                        </tr>
                        <tr>
                            <th>*사용자 ID</th>
                            <td>
                                <input id="modMemId" name="modMemId" type="hidden" class="ipt_txt user_id" readonly="readonly">
                                <span id="modMemIdSpan"></span>
                            </td>
                           <th>비밀번호</th>
                           <td>
                               <button type="button" class="btn_primary line" onclick="javascript:resetPassword();">비밀번호 초기화</button>
                           </td>
                        </tr>
                        <tr>
                            <th>*이름</th>
                            <td>
                                <input id="modMemName" name="modMemName" type="hidden" class="ipt_txt" readonly="readonly">
                                <span id="modMemNameSpan"></span>
                            </td>
                            <th>성별</th>
                            <td>
                                <div class="radioBox">   
                                    <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->
                                    <input type="radio" name="modMemGender" id="modMan" value="M">
                                    <label for="modMan">남</label>    
                                    <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->                      
                                    <input type="radio" name="modMemGender" id="modWoman" value="W">
                                    <label for="modWoman">여</label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>이메일</th>
                            <td>
                                <input id="modMemEmail1" name="modMemEmail1" type="text" class="ipt_txt email" style="width:44%;">
                                @
                                <input id="modMemEmail2" name="modMemEmail2" type="text" class="ipt_txt email" style="width:44%;">
                            </td>
                            <th>생년월일</th>
                            <td>
                                <input id="modMemBirthday" name="modMemBirthday" type="text" class="ipt_txt" placeholder="">
                            </td>
                        </tr>
                        <tr>
                            <th>휴대폰</th>
                            <td>
                                <input id="modMemPhone" name="modMemPhone" type="text" class="ipt_txt" placeholder="" maxlength="11">
                            </td>
                            <th>권한</th>
                            <td>
                                <div class="radioBox">   
                                    <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->
                                    <input type="radio" id="modMemRoleUser" name="modMemRoleId" value="USER" >
                                    <label for="modMemRoleUser">사용자</label>    
                                    <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->                      
                                    <input type="radio" id="modMemRoleAdmin" name="modMemRoleId" value="ADMIN">
                                    <label for="modMemRoleAdmin">관리자</label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>IP 주소</th>
                            <td>
                                <input id="modMemColumn1" name="modMemColumn1" type="text" class="ipt_txt" placeholder="xxx.xxx.xxx.xxx, ..." onKeyUp="javascript:noEnKo(this)">
                            </td>
                            <th>IP 체크여부</th>
                            <td>
                                <div class="radioBox">   
                                    <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->
                                    <input type="radio" id="modMemColumn2_Y" name="modMemColumn2" value="Y">
                                    <label for="modMemColumn2_Y">사용</label>    
                                    <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->                      
                                    <input type="radio" id="modMemColumn2_N" name="modMemColumn2" value="N" checked>
                                    <label for="modMemColumn2_N">미사용</label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>*사용여부</th>
                            <td>
                                <div class="radioBox">   
                                    <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->
                                    <input type="radio" id="modMemStatUse" name="modMemStat" value="use" >
                                    <label for="modMemStatUse">사용</label>    
                                    <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->                      
                                    <input type="radio" id="modMemStatUnused" name="modMemStat" value="unused" >
                                    <label for="modMemStatUnused">미사용</label>
                                </div>
                            </td>
                        </tr>
                    </tbody>
               </table>
           </div>
        </div>
        <div class="lyr_btm">
            <button id="mod_user_btn_cancle" type="button" class="btn_primary line btn_lyr_close" onclick="javascript:modUserClose();">취소</button>
            <button type="button" class="btn_primary" onclick="javascript:modUser();">저장</button>
        </div>
    </div>
</div>
<!-- //사용자 수정 -->

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

<!-- 중복확인 -->
<div id="id_chk" class="lyrWrap">
	<div id="id_chk_background" class="lyr_bg" style="display:none;"></div>
    <div class="lyrCont">
        <div class="lyr_top">
            <p class="tit">중복확인</p>
            <button type="button" class="btn_lyr_close" onclick="modalOpenClose('id_chk','close')"><span class="hide">닫기버튼</span></button>
        </div>
        <div class="lyr_mid">
            <p id="id_chk_text" class="info"></p> <!-- <span></span>는 사용 가능한 아이디 입니다. -->
        </div>
        <div class="lyr_btm">
            <button id="id_chk_btn_cancle" type="button" class="btn_primary btn_lyr_close" onclick="modalOpenClose('id_chk','close')">확인</button>
        </div>
    </div>
</div>
<!-- //중복확인 -->

<!-- Local Script -->
<script type="text/javascript">
jQuery.event.add(window,"load",function(){
    $(document).ready(function(){
        // submenu active
        $("#sub_ID00004").addClass("active");//상위 메뉴
        $("#sub_ID00012").addClass("active");//하위 메뉴
        $("#sub_ID00017").addClass("active");//하위 메뉴
    });
});

// 페이지 이동
function movePage(uri, queryString){
	location.href = uri + queryString;	
}

// 사용자 목록 조회
function searchUser(){
	var uri = "/admin/user";
	var queryString = "?currentPageNo=" + $('#currentPageNo').val()
			+ "&recordsPerPage=" + $('#h_recordsPerPage').val()
			+ "&pageSize=" + $('#h_pageSize').val()
			+ "&searchType=" + $('#searchType').val()
			+ "&searchKeyword=" + $('#searchKeyword').val();
	
	location.href = uri + queryString;
}

// 사용자 등록 > 사용자 관리 모달 오픈
function addUserOpen(){
	// 등록 버튼누르면 기존 입력된 데이터 초기화 진행
	addUserClear();
	$('#srch_dptmt_type').val('add');
	modalOpenClose("add_user","open");
}

//사용자 등록 > 사용자 관리 모달 > 취소 (닫기)
function addUserClose(){
	// 사용자 관리 모달에서 취소 버튼누르면 기존 입력된 데이터 초기화 진행
	addUserClear();
	modalOpenClose("add_user","close");
}

// 사용자 등록 > 사용자 관리 모달 오픈
function modUserOpen(memId){
	// 수정 버튼누르면 기존 입력된 데이터 초기화 진행
	modUserClear();
	$('#srch_dptmt_type').val('mod');
	ajaxGetUserInfo(memId);
	modalOpenClose("mod_user","open");
}

//사용자 등록 > 사용자 관리 모달 > 취소 (닫기)
function modUserClose(){
	// 사용자 관리 모달에서 취소 버튼누르면 기존 입력된 데이터 초기화 진행
	modUserClear();
	modalOpenClose("mod_user","close");
}

//사용자 추가 
function addUser(){
 	// 공백제거
	$('#addDeptId').val($('#addDeptId').val().trim()); // 부서 ID
	$('#addDeptName').val($('#addDeptName').val().trim());
	$('#addDeptCode').val($('#addDeptCode').val().trim());
	$('#addMemId').val($('#addMemId').val().trim()); // 회원 ID
	$('#addMemName').val($('#addMemName').val().trim()); // 회원 이름
	$('#addMemEmail1').val($('#addMemEmail1').val().trim());
	$('#addMemEmail2').val($('#addMemEmail2').val().trim());
	$('#addMemBirthday').val($('#addMemBirthday').val().trim()); // 생년월일 yyyymmdd
	$('#addMemPhone').val($('#addMemPhone').val().trim()); // 전화번호
	$('#checkUserId').val($('#checkUserId').val().trim());
	$('#checkUserYN').val($('#checkUserYN').val().trim());
	
	$('#addMemColumn1').val($('#addMemColumn1').val().trim());//IP 주소
	
	// value Get
	var $addDeptId = $('#addDeptId').val();
	var $addDeptName = $('#addDeptName').val();
	var $addDeptCode = $('#addDeptCode').val();
	var $addMemId = $('#addMemId').val();
	var $addMemName = $('#addMemName').val();
	var $addMemEmail1 = $('#addMemEmail1').val();
	var $addMemEmail2 = $('#addMemEmail2').val();
	var $addMemBirthday = $('#addMemBirthday').val();
	var $addMemPhone = $('#addMemPhone').val();
	var $addMemEmail = $addMemEmail1 + "@" + $addMemEmail2;

	var $addMemColumn1 = $('#addMemColumn1').val();
	
	 // radio
	 var $addMemGender = $("input[name='addMemGender']:checked").val(); // 성별
	 var $addMemStat = $("input[name='addMemStat']:checked").val(); // 상태 
	 var $addMemRoleId = $("input[name='addMemRoleId']:checked").val(); // 상태 
	 
	 var $addMemColumn2 = $("input[name='addMemColumn2']:checked").val(); // IP 체크여부 
	 
	 // 아이디 중복 관련
	 var $checkUserId = $('#checkUserId').val();
	 var $checkUserYN = $('#checkUserYN').val();

	// 필수조건
	if ($addDeptId.length < 1){
	    alert("부서 정보를 입력해주세요.");
	    return ;
	} 
	if ($addMemId.length < 1){
	    alert("사용자 아이디를 입력해주세요.");
	    $('#addMemId').focus();
	    return ;
	} else {
		if ("N" == $checkUserYN){
			alert("사용자 ID 중복확인이 필요합니다.");
			$('#addMemId').focus();
		    return ;
		} else {
			if ($addMemId!=$checkUserId){
				alert("사용자 ID 중복확인이 필요합니다.");
				$('#addMemId').focus();
			    return ;
			}
		}
	} 
	if ($addMemName.length < 1){
	    alert("이름을 입력해주세요.");
	    $('#addMemName').focus();
	    return ;
	}
	
	if ("@" == $addMemEmail){
		$addMemEmail = "";
	}
	
	var jsonData = {
	    deptId : $addDeptId
	    , memId : $addMemId
	    , memName : $addMemName
	    , memBirthday : $addMemBirthday
	    , memPhone : $addMemPhone
	    , memGender : $addMemGender
	    , memStat : $addMemStat
	    , memEmail : $addMemEmail
	    , roleId : $addMemRoleId
	    , memColumn1 : $addMemColumn1
	    , memColumn2 : $addMemColumn2
	    , memRegId : $('#sessionMemId').val()
	    , memModId : $('#sessionMemId').val()
	};
	// 사용자 추가 실행
	ajaxAddUser(jsonData);
}

//사용자 데이터 체크 - 사용자 아이디 중복체크
function ajaxAddUser(jsonData){
	$.ajax({
		url : "/admin/user/add",
		type : "post",
		async : false,
		data : jsonData,
		dataType: "json",
		success : function(data) {
			if ("SUCCESS" == data.status){
				alert(data.msg);
				addUserClose();// 사용자 관리 추가 모달 CLOSE
				searchUser();
			} else if ("FAIL" == data.status){
				alert(data.msg);
				addUserClose();// 사용자 관리 추가 모달 CLOSE
			} else {
				alert('실패하였습니다.');
				addUserClose();// 사용자 관리 추가 모달 CLOSE
			}
		},
		error : function() {
			console.log("error");
		}
	});
}

function resetPassword(){
	var $modMemId = $('#modMemId').val();

	var jsonData = {
		memId : $modMemId
	};
	if(confirm('비밀번호 초기화를 하시겠습니까?')){
		ajaxResetPassword(jsonData);
	}
}

// 사용자 비밀번호 초기화
function ajaxResetPassword(jsonData){
	$.ajax({
		url : "/admin/user/resetPassword",
		type : "post",
		async : false,
		data : jsonData,
		dataType: "json",
		success : function(data) {
			if ("SUCCESS" == data.status){
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

function deleteUser(memId){
	var jsonData = {
	    memId : memId
	    , memModId : $('#sessionMemId').val()
	};
	// 사용자 추가 실행
	ajaxDeleteUser(jsonData);
}

//사용자 비밀번호 초기화
function ajaxDeleteUser(jsonData){
	$.ajax({
		url : "/admin/user/delete",
		type : "post",
		async : false,
		data : jsonData,
		dataType: "json",
		success : function(data) {
			if ("SUCCESS" == data.status){
				alert(data.msg);
				searchUser();
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

// 사용자 등록 데이터 초기화 
function addUserClear(){
	// input 초기화
    $('#addDeptId').val(''); // 부서 ID
    $('#addDeptName').val('');
    $('#addDeptCode').val(''); 
    $('#addMemId').val(''); // 회원 ID
    $('#addMemName').val(''); // 회원 이름
    $('#addMemEmail1').val(''); 
    $('#addMemEmail2').val('');
    $('#addMemBirthday').val(''); // 생년월일 yyyymmdd
    $('#addMemPhone').val(''); // 전화번호
    $('#addMemColumn1').val(''); // IP주소
    
    // radio 초기화 - 첫번째 선택지
    $("input[name='addMemGender'][value='M']").prop("checked", true);
    $("input[name='addMemStat'][value='use']").prop("checked", true);
    $("input[name='addMemColumn2'][value='N']").prop("checked", true); 
    
    // 아이디 중복 관련
    $('#checkUserId').val(''); // 중복체크된 아이디
    $('#checkUserYN').val('N'); // 중복체크된 아이디
}

//사용자 추가 
function modUser(){
	// 공백제거
	$('#modDeptId').val($('#modDeptId').val().trim()); // 부서 ID
	$('#modDeptName').val($('#modDeptName').val().trim());
	$('#modDeptCode').val($('#modDeptCode').val().trim());
	$('#modMemId').val($('#modMemId').val().trim()); // 회원 ID
	$('#modMemName').val($('#modMemName').val().trim()); // 회원 이름
	$('#modMemEmail1').val($('#modMemEmail1').val().trim());
	$('#modMemEmail2').val($('#modMemEmail2').val().trim());
	$('#modMemBirthday').val($('#modMemBirthday').val().trim()); // 생년월일 yyyymmdd
	$('#modMemPhone').val($('#modMemPhone').val().trim()); // 전화번호

	$('#modMemColumn1').val($('#modMemColumn1').val().trim());//IP 주소

	// value Get
	var $modDeptId = $('#modDeptId').val();
	var $modDeptName = $('#modDeptName').val();
	var $modDeptCode = $('#modDeptCode').val();
	var $modMemId = $('#modMemId').val();
	var $modMemName = $('#modMemName').val();
	var $modMemEmail1 = $('#modMemEmail1').val();
	var $modMemEmail2 = $('#modMemEmail2').val();
	var $modMemBirthday = $('#modMemBirthday').val();
	var $modMemPhone = $('#modMemPhone').val();
	var $modMemEmail = $modMemEmail1 + "@" + $modMemEmail2;
	
	var $modMemColumn1 = $('#modMemColumn1').val();
	
	// radio
	var $modMemGender = $("input[name='modMemGender']:checked").val(); // 성별
	var $modMemStat = $("input[name='modMemStat']:checked").val(); // 상태 
	var $modMemRoleId = $("input[name='modMemRoleId']:checked").val(); // 상태 
	 
	var $modMemColumn2 = $("input[name='modMemColumn2']:checked").val(); // IP 체크여부 

	// 필수조건
	if ($modDeptId.length < 1){
	    alert("부서 정보를 입력해주세요.");
	    return ;
	} 
	if ("@" == $modMemEmail){
		$modMemEmail = "";
	}
	
	var jsonData = {
	    deptId : $modDeptId
	    , memId : $modMemId
	    , memBirthday : $modMemBirthday
	    , memPhone : $modMemPhone
	    , memGender : $modMemGender
	    , memStat : $modMemStat
	    , memEmail : $modMemEmail
	    , roleId : $modMemRoleId
	    , memColumn1 : $modMemColumn1
	    , memColumn2 : $modMemColumn2
	    , memModId : $('#sessionMemId').val()
	};
	// 사용자 추가 실행
	ajaxModUser(jsonData);
}

//사용자 데이터 체크 - 사용자 아이디 중복체크
function ajaxModUser(jsonData){
	$.ajax({
		url : "/admin/user/mod",
		type : "post",
		async : false,
		data : jsonData,
		dataType: "json",
		success : function(data) {
			if ("SUCCESS" == data.status){
				alert(data.msg);
				modUserClose();// 사용자 관리 추가 모달 CLOSE
				searchUser();
			} else if ("FAIL" == data.status){
				alert(data.msg);
				modUserClose();// 사용자 관리 추가 모달 CLOSE
			} else {
				alert('실패하였습니다.');
				modUserClose();// 사용자 관리 추가 모달 CLOSE
			}
		},
		error : function() {
			console.log("error");
		}
	});
}

// 사용자 수정 데이터 초기화 
function modUserClear(){
	// input 초기화
    $('#modDeptId').val(''); // 부서 ID
    $('#modDeptName').val('');
    $('#modDeptCode').val(''); 
    $('#modMemId').val(''); // 회원 ID
    $('#modMemName').val(''); // 회원 이름
    $('#modMemIdSpan').html(''); // 회원 ID
    $('#modMemNameSpan').html(''); // 회원 이름
    $('#modMemEmail1').val(''); 
    $('#modMemEmail2').val('');
    $('#modMemBirthday').val(''); // 생년월일 yyyymmdd
    $('#modMemPhone').val(''); // 전화번호
    $('#modMemColumn1').val(''); // IP주소
    
    // radio 초기화 - 첫번째 선택지
    $("input[name='modMemGender']").prop("checked", false);
    $("input[name='modMemStat']").prop("checked", false);
	$("input[name='modMemRoleId']").prop("checked", false); 
	$("input[name='modMemColumn2']").prop("checked", false); 

}

// 추가할 사용자 ID 중복 확인
function addCheckUserId(){
    // 좌우 공백 제거
    $("#addMemId").val($("#addMemId").val().trim());
    
	var checkUserId = $("#addMemId").val();
	
	if (checkUserId.length == 0){
		alert("사용자 ID를 입력해주세요.");
		return;
	}
	// 수행
	ajaxCheckUserId(checkUserId);
}

// 사용자 중복확인 모달
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

//사용자 데이터 체크 - 사용자 아이디 중복체크
function ajaxCheckUserId(checkUserId){
	var text = "";
	var result = "";
	// 사용자 아이디  검사
	var jsonData = {
			"userId" : checkUserId
	};
	
	$.ajax({
		url : "/admin/user/checkUserId",
		type : "post",
		async : false,
		data : jsonData,
		dataType: "json",
		success : function(data) {
			result = data.cnt; // 조회된 사용자 건수 반환
			if (result == 0){
				// 조회된 사용자가 없으므로 사용가능
				text = "<b>"+ checkUserId +"</b> 는 사용 가능한 아이디 입니다.";
    			$("#id_chk_text").html(text);
    			$("#checkUserId").val(checkUserId); // 사용 가능 ID 입력
    			$("#checkUserYN").val("Y"); // 사용 가능 상태  Y
			} else if (result > 0) {
				// 조회된 사용자가 있으므로 사용불가
				text = "<b>"+ checkUserId +"</b> 는 사용중인 아이디 입니다.";
    			$("#id_chk_text").html(text);
			    $("#addMemId").val(""); // 중복 확인 정보 초기화
			    $("#checkUserId").val(""); // 중복 확인 정보 초기화
			    $("#checkUserYN").val("N"); // 중복된 사용자 : N, 중복되지 않아서 사용가능한 사용자 : Y
			} else {
			}
			// modal OPEN
			modalOpenClose("id_chk","open");
		},
		error : function() {
			console.log("error");
		}
	});
	return result;
}

//사용자 정보 조회 및 수정 화면 값 입력
function ajaxGetUserInfo(memId){
	var text = "";
	var result = "";
	// 사용자 아이디  검사
	var jsonData = {
			"userId" : memId
	};
	
	$.ajax({
		url : "/admin/user/getUserInfo",
		type : "post",
		async : false,
		data : jsonData,
		dataType: "json",
		success : function(data) {
			//console.log(data);
			result = data;
			
			var info = data.userInfo;
			var email = info.memEmail;
			var email1 = '';
			var email2 = '';
			if (email != null && email != undefined ){
				var emailArr = email.split('@');
				
				if (emailArr.length == 1){
					email1 = emailArr[0]; 
				} else if (emailArr.length == 2){
					email1 = emailArr[0];
					email2 = emailArr[1]; 
				}
			}
			
			// input 값 입력
		    $('#modDeptId').val(info.deptId); // 부서 ID
		    $('#modDeptName').val(info.deptName);
		    $('#modDeptCode').val(info.deptCode); 
		    $('#modMemId').val(info.memId); // 회원 ID
		    $('#modMemIdSpan').html(info.memId); // 회원 ID
		    $('#modMemName').val(info.memName); // 회원 이름
		    $('#modMemNameSpan').html(info.memName); // 회원 이름
		    $('#modMemEmail1').val(email1); 
		    $('#modMemEmail2').val(email2);
		    $('#modMemBirthday').val(info.memBirthday); // 생년월일 yyyymmdd
		    $('#modMemPhone').val(info.memPhone); // 전화번호
		    $('#modMemColumn1').val(info.memColumn1); // IP
		    
		    // radio 값 선택
		    var memGender;
		    var memStat;
		    var roleId;
		    var memColumn2;
		    
		    if (info.memGender == null || info.memGender == ""){
		    	memGender = "M";
		    } else {
		    	memGender = info.memGender;
		    }
		    if (info.memStat == null || info.memStat == ""){
		    	memStat = "use";
		    } else {
		    	memStat = info.memStat;
		    }
		    if (info.roleId == null || info.roleId == ""){
		    	roleId = "USER";
		    } else {
		    	roleId = info.roleId;
		    }
		    if (info.memColumn2 == null || info.memColumn2 == ""){
		    	memColumn2 = "N";
		    } else {
		    	memColumn2 = info.memColumn2;
		    }
		    $("input[name='modMemGender'][value='"+memGender+"']").prop("checked", true);
		    $("input[name='modMemStat'][value='"+memStat+"']").prop("checked", true);
		    $("input[name='modMemRoleId'][value='"+roleId+"']").prop("checked", true); 
		    $("input[name='modMemColumn2'][value='"+memColumn2+"']").prop("checked", true); 
		},
		error : function() {
			console.log("error");
		}
	});
	return result;
}

//부서 조회창 오픈
function searchDeptOpen(){
	// 모달 오픈
	// 추가될 데이터 초기화
	
	var type = $('#srch_dptmt_type').val();
	
	$("#"+type+"DeptId").val("");
	$("#"+type+"DeptName").val("");
	$("#"+type+"DeptCode").val("");
	
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

// 부서 조회 페이지 이동
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

// 부서 조회 초기화
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
			
			$("#"+type+"DeptId").val($("#"+chkDeptId+"_deptId").val());
			$("#"+type+"DeptName").val($("#"+chkDeptId+"_deptName").val());
			$("#"+type+"DeptCode").val($("#"+chkDeptId+"_deptCode").val());
			
			searchDeptClose();
			return;
		});
		
	}
}

// 사용자 삭제 체크 확인
function chkBoxDeleteUser(){
		
	// 체크박스 검사	
	var chkCnt = $("input[name='chkBoxUser']:checked").length;
	if (chkCnt<=0){
		alert("하나의 사용자를 선택해주세요.");
		return;
	} else if (chkCnt>1){
		alert("하나의 사용자를 선택해주세요.");
		return;
	}else if (chkCnt == 1){
		// 하나의 부서가 선택되었음
		$("input[name='chkBoxUser']:checked").each(function(){
			var chkMemId = $(this).val();

			if(confirm("삭제하시겠습니까?")){
				deleteUser(chkMemId);
			}
			return;
		});
		
	}
}
// IP 주소 입력..
function noEnKo(obj){
	console.log(obj);
	
	var id = obj.id;
	var val = obj.value;
	
    var str = val.split(".");
    str[0] = str[0].replace(/[^-\.0-9]/g,'').replace(/(.)(?=(\d{3})+$)/g,'$1,');
    var fmStr = str.join('.');
    
    var result = fmStr.replace(/[`~!@#$%^&*()_|+\-=?;:'"<>\{\}\[\]\\\|ㄱ-ㅎ|ㅏ-ㅣ-ㅢ|가-힣|a-z|A-Z]/g,'');
    
    $('#'+id).val(result);
    //return result
}
</script>
</body>
</html>