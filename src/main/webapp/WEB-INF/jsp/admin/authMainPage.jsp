<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ include file="/WEB-INF/jsp/include/header.jspf" %>
    <title>Admin (권한 관리) || maum XDC</title>
</head>

<script type="text/javascript">
	function fnMakeMemberMenuTree(seq,memId,roleId){
		var data = new Object();
		data.seq = seq;
		data.memId = memId;
		data.roleId = roleId;
		
		restCall('admin/auth/memberMenu'
				,{
			        method: 'post',
			        data: data,
			        async: false,
			    }
				, function(resp) {
					fnGetMemberMenuSuccessHandler(resp.menuTree);
				} 
				, function(jqXHR, textStatus, errorThrown) {//실패
					console.log('restCall fail'+ '\n jqXHR=', jqXHR, '\n textStatus=', textStatus, '\n errorThrown=', errorThrown);
					alert('메뉴 추가처리에 실패하였습니다.\n관리자에게 문의하세요.');
	            }
		);
	}
	
	function fnGetMemberMenuSuccessHandler(menuTree){
		treeList = menuTree;
		
    	//초기 root data 생성
    	var data = new Array();
    	var tree = new Array();
    	//meta data 생성
    	treeList.forEach(function (obj, idx) {
    	    var menuObj = new Object();
    	    //반복처리 하나의 겟을 만든다.
    	    if (obj.parId === 'system') { 
    	        menuObj.id = obj.menuId;
    	        menuObj.name = obj.menuName;
    	        tree.push(menuObj);
    	    }
    	});

    	$.each(tree, function (idx, obj) {
    	    var menuObj = new Object();
    	    menuObj = fnMakeMenuTreeJson(treeList, obj.id);
    	    data.push(menuObj);
    	});
    	$("#menuList").tree("loadData", data);
	}

	//메뉴 목록 and 상세데이터 로딩처리 Start
	//메뉴 트리를 object로 return 
	function fnMakeMenuTreeJson(treeList, id) {
	    var menuChildrenArr = new Array();
	    var menuChildrenObj = new Object();
	    var obj = new Object();
	    if (treeList.LookUp('parId', id, 'menuId') == '') {
	        obj.id = treeList.LookUp('menuId', id, 'menuId');
	        obj.name = treeList.LookUp('menuId', id, 'menuName');
	        return obj;
	    } else {
	        obj.id = treeList.LookUp('menuId', id, 'menuId');
	        obj.name = treeList.LookUp('menuId', id, 'menuName');	    	
	    	obj.children = fnMakeMenuTreeChildren(treeList, id);	
	        return obj;
	    }
	}

	//상위 메뉴트리가 메타 데이터에서 존재하면, 재귀처리
	function fnMakeMenuTreeChildren(treeList, id) {
	    var arr = new Array();
	    treeList.forEach(function (obj, idx) {
	        if (obj.parId == id) {
	            var menu = new Object();
	            menu.id = obj.menuId;
	            menu.name = obj.menuName;
	            if (treeList.LookUp('parId', obj.menuId, 'menuId') != '') {
	            	menu.children = fnMakeMenuTreeChildren(treeList, obj.menuId);
	            }
	            arr.push(menu);
	        }
	    });
        return arr;
	}
	
	function fnSaveAuthMenuTree(){
		var el = $('#menuList').find('input[type="checkbox"]');
		var arr = new Array();
		$.each(el,function(idx,obj){
			var data = new Object();
			var memSeq = $('#seq').val();
			var memId = $('#memId').val();
			var menuId = $(obj).attr('id').replaceAll('chk_','');
			var roleId = treeList.LookUp('menuId', menuId, 'roleId');
			var menuUse = $(obj).is(':checked') ? 'Y':'N';
			data.memSeq = memSeq;
			data.memId = memId;
			data.menuId = menuId;
			data.roleId = roleId;
			data.menuUse = menuUse;
			arr.push(data);
		});
		console.log(arr);
		if(arr.length > 0){
			var jsonData = new Object();
			var seq = nvl($('#seq').val(),'');
			var memId = nvl($('#memId').val(),'');
			var roleId = nvl($('#roleId').val(),'');
			jsonData.memberMenuList =  arr;
			jsonData.seq = seq;
			jsonData.memId = memId;
			jsonData.roleId = roleId;
			
			restCall('admin/auth/insertMemberMenu'
					,{
				        method: 'post',
				        data: jsonData,
				        async: false,
				    }
					, function(resp) {
						alert('저장 되었습니다.');
						fnGetMemberMenuSuccessHandler(resp.menuTree);
					} 
					, function(jqXHR, textStatus, errorThrown) {//실패
						console.log('restCall fail'+ '\n jqXHR=', jqXHR, '\n textStatus=', textStatus, '\n errorThrown=', errorThrown);
						alert('메뉴 추가처리에 실패하였습니다.\n관리자에게 문의하세요.');
		            }
			);			
		}
	}

		
</script>

<script type="text/javascript">

function fnOpenModal(seq,memId,roleId){
	fnClearOpenModal();
	$('#seq').val(seq);
	$('#memId').val(memId);
	$('#roleId').val(roleId);
	fnMakeMemberMenuTree(seq,memId,roleId);
	//modal 데이터 초기화    
    $('#auth_set').css('display', 'block');
    $('#auth_set').prepend('<div class="lyr_bg"></div>');			
}

function fnCloseModal(obj){
	fnClearOpenModal();
	$('#auth_set').css('display', 'none'); 
	$('#auth_set').find('.lyr_bg').remove(); 
}

//페이지 이동
function fnMovePage(pageNo){
	$('#currentPageNo').val(pageNo);
	$('#aform').attr({ action : '/admin/auth/main', method : 'post' }).unbind('submit').submit();
}

//검색결과 조회
function fnSearchMovePage(obj){
	var schAuthType = $('input:radio[name="schAuthType"]:checked').val();
	var schCrtr = $('#schCrtr').val();
	var schText = nvl($('#schText').val());
	$('#currentPageNo').val('1');
	$('#searchAuthType').val(schAuthType);
	$('#searchCriteria').val(schCrtr);
	$('#searchKeyword').val(schText);
	$('#aform').attr({ action : '/admin/auth/main', method : 'post' }).unbind('submit').submit();
}

//초기화
function fnSearchEmpty(obj){
	//사용자 권한 초기화
	$('input:radio[name="schAuthType"][value="all"]').prop('checked', true);
	//조건선택 초기화
	$('#schCrtr').attr('selected',true).val('all');
	//검색어 초기화
	$('#schText').val('');
}

//modal 초기화
function fnClearOpenModal(){
	var data = [];
	$("#menuList").tree("loadData", data);
	$('#seq').val('');
	$('#memId').val('');
	$('#roleId').val('');
}

</script>
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
                    <h3>Admin <span>권한 관리</span></h3>
                </div>
				<input type="hidden" id="seq" name="seq" value=""/>
				<input type="hidden" id="memId" name="memId" value=""/>
				<input type="hidden" id="roleId" name="roleId" value=""/>
				
				<form role="form" id="aform" method="post" enctype="multipart/form-data" class="form-horizontal">
					<!-- 신규 검색시 1 페이지로 이동 -->
					<input type="hidden" id="currentPageNo" name="currentPageNo" value="1"/>
					<!-- org 검색시 1 페이지로 이동 -->
					<input type="hidden" id="currentPageNo" name="currentPageNo" value="${params.currentPageNo}"/>
					<!-- org 페이지에 보여질 수 -->
                   	<input type="hidden" id="recordsPerPage" name="recordsPerPage" value="${params.recordsPerPage}"/>
                   	<!-- org 페이지 블럭의 수 -->
                    <input type="hidden" id="pageSize" name="pageSize" value="${params.pageSize}"/>
                    <!-- org 검색 종류 -->
					<input type="hidden" id="searchType" name="searchType" value="${params.searchType}"/>
					<!-- org 검색 값 -->
					<input type="hidden" id="searchKeyword" name="searchKeyword" value="${params.searchKeyword}"/>					
					<!-- 추가 조회 컬럼 -->
					<input type="hidden" id="searchAuthType" name="searchAuthType" value="${params.searchAuthType}"/>
					<input type="hidden" id="searchCriteria" name="searchCriteria" value="${params.searchCriteria}"/>
					
				</form>
                <div class="srchArea">
                    <table summary="시스템 사용여부, 조건 선택으로 구성">
                        <caption class="hide">검색조건</caption>
                        <colgroup>
                            <col width="140px"><col><col width="140px"><col>
                        </colgroup>
                        <tbody>
                            <tr>
                                <th>사용자 권한</th>
                                <td>
                                    <div class="radioBox">   
                                        <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->
                                        <input type="radio" name="schAuthType" id="radioAll" value="all" <c:if test="${empty params.searchAuthType or params.searchAuthType eq 'all'}">checked</c:if>>
                                        <label for="radioAll">모두</label>
                                        <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->                      
                                        <input type="radio" name="schAuthType" id="schUser" value="user" <c:if test="${empty params.searchAuthType or params.searchAuthType eq 'user'}">checked</c:if>>
                                        <label for="schUser">사용자</label>
                                        <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->
                                        <input type="radio" name="schAuthType" id="schAdmin" value="admin" <c:if test="${empty params.searchAuthType or params.searchAuthType eq 'admin'}">checked</c:if>>
                                        <label for="schAdmin">관리자</label>
                                    </div>
                                </td>
                                <th>조건 선택</th>
                                <td>
                                    <select class="select" id="schCrtr">
                                    	<option value="all" <c:if test="${empty params.searchCriteria or params.searchCriteria eq 'all'}">selected</c:if>>전체</option>
                                        <option value="memId" <c:if test="${empty params.searchCriteria or params.searchCriteria eq 'memId'}">selected</c:if>>사용자명</option>
                                        <option value="memName" <c:if test="${empty params.searchCriteria or params.searchCriteria eq 'memName'}">selected</c:if>>아이디</option>
                                        <option value="deptName" <c:if test="${empty params.searchCriteria or params.searchCriteria eq 'deptName'}">selected</c:if>>부서명</option>
                                        <option value="memPhone" <c:if test="${empty params.searchCriteria or params.searchCriteria eq 'memPhone'}">selected</c:if>>연락처</option>
                                    </select>
                                    <input type="text" class="ipt_txt" id="schText" value="${params.searchKeyword}" placeholder="검색어를 입력해주세요." onkeyup="if (window.event.keyCode == 13) {fnSearchMovePage(this); return false;}">
                                    <button type="button" class="btn_primary" onclick="fnSearchMovePage(this); return false;">검색</button>
                                    <button type="button" class="btn_primary" onclick="fnSearchEmpty(this); return false;">초기화</button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
				<%--
                <div class="btnBox">
                    <button type="button" class="btn_primary">등록</button>
                </div>
 				--%>
                <div class="btnBox"></div>
                 				
                <div class="tblBox admin">
                    <table>
                        <caption class="hide">메뉴목록 권한 관리</caption>
                        <colgroup>
                            <col><col><col><col><col><col><col>
                        </colgroup>
                        <thead>
                            <tr>
                                <th scope="col">No.</th>
                                <th scope="col">이름</th>
                                <th scope="col">아이디</th>
                                <th scope="col">부서명</th>
                                <th scope="col">연락처</th>
                                <th scope="col">등록일</th>
                                <th scope="col">권한관리</th>
                            </tr>
                        </thead>
                        <tbody>
                        	<c:choose>
                        		<c:when test="${empty userList }">
		                            <tr>
		                                <td class="empty" scope="row" colspan="7">데이터가 없습니다.</td>
		                            </tr>
                        		</c:when>
                        		<c:when test="${!empty userList }">
                        			<c:forEach var="list" varStatus="status" items="${userList}" >
			                            <tr>
			                                <!-- ((현재 페이지 번호-1)*페이지별 보여지는 수량) + 현재 순번 -->
			                                <td>${((params.currentPageNo-1)*params.recordsPerPage) + status.count}</td>
			                                <td>${list.memName }</td>
			                                <td>${list.memId }</td>
			                                <td>${list.deptName }</td>
			                                <td>${list.memPhone }</td>
			                                <td><fmt:formatDate value="${list.memRegDt }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			                                <td>
			                                    <button type="button" class="btn_auth_set btn_lyr_open" onclick="fnOpenModal('${list.memSeq}','${list.memId}','${list.roleId}'); return false;">
			                                        <span class="hide">권한관리</span>
			                                        <em class="fas fa-user-cog"></em>
			                                    </button>
			                                </td>
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
							<a href="javascript:fnMovePage('1');" class="btn_paging first">
	                            <span class="hide">처음 페이지</span>
	                            <em class="fas fa-angle-double-left"></em>
	                        </a>
							<a href="javascript:fnMovePage('${params.paginationInfo.firstPage-1}');" class="btn_paging prev">
	                            <span class="hide">이전 페이지</span>
	                            <em class="fas fa-angle-left"></em>
	                        </a>
                        </c:if>
                        
                       	<c:forEach begin="${params.paginationInfo.firstPage}" end="${params.paginationInfo.lastPage}" var="pageNo">
                        	<!-- [D] 현재 페이지 인덱스에 addClass('current') 해주면 활성화됨 -->
							<a href="javascript:fnMovePage('${pageNo}');"  class="<c:out value="${params.currentPageNo == pageNo ? 'current' : ''}"/>">${pageNo}</a>
                       	</c:forEach>
						<c:if test="${params.paginationInfo.hasNextPage}">
							<a href="javascript:fnMovePage('${params.paginationInfo.lastPage+1}');" class="btn_paging next">
	                            <span class="hide">다음 페이지</span>
	                            <em class="fas fa-angle-right"></em>
	                        </a>
							<a href="javascript:fnMovePage('${params.paginationInfo.totalPageCount}');" class="btn_paging last">
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
<!-- 권한 관리 -->
<div id="auth_set" class="lyrWrap">
    <div class="lyrCont">
        <div class="lyr_top">
            <p class="tit">권한 관리</p>
            <button type="button" class="btn_lyr_close" onclick="fnCloseModal(this); return false;"><span class="hide">닫기버튼</span></button>
        </div>
        <div class="lyr_mid">
            <p class="desc">사용할 시스템(메뉴)를 선택하세요.</p>
            <!-- [D] 22.01.25 .tree_view_wrap에 id="menuList"추가와 .treeSelector(기존 트리 구조 마크업) 영역 제거 부탁드립니다. -->
            <div id="menuList" class="tree_view_wrap menu"></div>
        </div>
        <div class="lyr_btm">
            <button type="button" class="btn_primary line btn_lyr_close" onclick="fnCloseModal(this); return false;">취소</button>
            <button type="button" class="btn_primary" onclick="fnSaveAuthMenuTree(); return false;">저장</button>
        </div>
    </div>
</div>
<!-- //권한 관리 -->

<!-- Local Script -->
<script type="text/javascript">
	var treeList;
	
	function fnMenuChecked(obj){
		
		var chkId = $(obj).attr('id');
		var topMenuId = fnGetTopMenuId(chkId.replaceAll('chk_',''),chkId);			
		var el = $('#chk_'+topMenuId).parent().parent().find('input[type="checkbox"]');	
		var checkedCnt = 0;
		
		$.each(el,function(idx,obj){
			var id = 'chk_' + topMenuId;
		    if($(obj).attr('id') != id && $(obj).is(':checked')){
		    	checkedCnt++;
		    }		        
		});
		
		if(checkedCnt == 0){
			if(el.length > 1){
				$('#chk_'+ topMenuId).parent().parent().find('input[type="checkbox"]').prop('checked',false);	
			}
		}else{
			$('#chk_'+ topMenuId).prop('checked',true);
		}				
	}	
	
	function fnGetTopMenuId(id,orgId) {
	    var topId = id;
	    var orgChecked = $('#'+orgId).is(':checked');
	    
	    var subEl = $('#'+orgId).parent().parent().find('input[type="checkbox"]');
	    if(subEl.length > 1){
	    	$(subEl).prop('checked',orgChecked);
	    }
	    
	    treeList.forEach(function (obj, idx) {
	    	if(obj.menuId == id){
		    	if(treeList.LookUp('menuId', id, 'parId') != 'system'){
		    		$('#chk_'+id).prop('checked',orgChecked);		    		
		    		topId = fnGetTopMenuId(treeList.LookUp('menuId', obj.menuId, 'parId'),orgId);
		    		fnSubMenuChecked(treeList.LookUp('menuId', obj.menuId, 'parId'));
		    	}else{
		    		if(treeList.LookUp('menuId', id, 'parId') != 'system'){		    			
		    			$('#chk_'+obj.menuId).prop('checked',orgChecked);
		    			fnSubMenuChecked(treeList.LookUp('menuId', id, 'parId'));
		    		}
		    		topId = obj.menuId;
		    	}
	    	}
	    });
        return topId;
	}
	
	function fnSubMenuChecked(menuId){
		var checkedCnt = 0;
		var el = $('#chk_'+menuId).parent().parent().find('input[type="checkbox"]');
		$.each(el,function(idx,obj){
			var id = 'chk_' + menuId;
		    if($(obj).attr('id') != id && $(obj).is(':checked')){
		    	checkedCnt++;
		    }
		});
		if(checkedCnt == 0){
			$('#chk_'+ menuId).parent().parent().find('input[type="checkbox"]').prop('checked',false);
		}else{
			$('#chk_'+ menuId).prop('checked',true);
		}
		console.log('checkedCnt :: ' ,checkedCnt);
	}

	
    $(document).ready(function(){
        $("#sub_ID00004").addClass("active");//상위 메뉴
        $("#sub_ID00014").addClass("active");//메뉴
        

        var data = [];
        var $jqtree = $("#menuList");

        $jqtree.tree({
            data: data,
            onCreateLi: function(node, li) {
            	if(treeList != undefined && treeList.length > 0){
                	var menuId = node.id
                    var keys = Object.keys(treeList[0]);
                    $.each(keys,function(idx,key){
                    	var elId = 'li_'+key;
                    	var elHtml = '<input type="hidden" id="" name="" value="">';
                    	elHtml = $(elHtml).attr('id',elId);
                    	elHtml = $(elHtml).attr('name',elId);
                    	elHtml = $(elHtml).attr('value',treeList.LookUp('menuId', menuId, key));
                    	li.find('.jqtree-title').after(elHtml);
                    });
                    var elChkId = 'chk_'+menuId;
                    var authMenuUse = treeList.LookUp('menuId', menuId, 'authMenuUse');
                    var elChk = '';
                    if(authMenuUse == 'N'){
                    	elChk = '<input type="checkbox" id="'+elChkId+'" onchange="fnMenuChecked(this); return false;"><label for="'+elChkId+'"></label>';
                    }else{
                    	elChk = '<input type="checkbox" id="'+elChkId+'" onchange="fnMenuChecked(this); return false;" checked><label for="'+elChkId+'"></label>';	
                    }
                    li.find('.jqtree-title').before(elChk);
                    // ie 호환 체크박스
                    /*
                    $('.jqtree-tree input[type=checkbox]').each(function(i){
                        $(this).attr('id', 'chk_'+menuId);
                        $(this).siblings('label').attr('for', 'chk_'+menuId);
                    });
                    */
            	}
            },
            autoOpen: true,
            usecontextmenu: true,
            selectable: true,
            onCanSelectNode: function(node) {
            	
            }
        });
    });
</script>
</body>
</html>