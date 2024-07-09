<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ include file="/WEB-INF/jsp/include/header.jspf" %>
	<title>Admin (메뉴 관리) || maum XDC</title>
</head>
<script type="text/javascript">	
//////////////////////////메뉴삭제처리 start
	function fnDeleteMenu(){
		var node = $("#menuList").tree("getSelectedNode");
		if(!node){
			alert('잘못된 접근입니다.\n메뉴를 먼저 선택하세요.')
			return false;
		}
		
		var node = $("#menuList").tree("getSelectedNode");
		var nodeName = node.name;
		var nodeChildren = node.children;
		if(nodeChildren.length > 0){
			if(confirm('['+nodeName+'] 메뉴를 삭제하시면,\n['+nodeName+']를 포함한 하위 메뉴도 모두 삭제 됩니다.\n삭제하시겠습니까?')){
				var selectLi = $('input:hidden[name="li_menuId"][value="'+node.id+'"]').parents('li:first');
				var menuIdList = $(selectLi).find('input[name="li_menuId"]');
				
				fnCallDeleteMenu(menuIdList);
			}
		}else{
			if(confirm('['+nodeName+'] 메뉴를 삭제 하시겠습니까?')){
				var selectLi = $('input:hidden[name="li_menuId"][value="'+node.id+'"]').parents('li:first');
				var menuIdList = $(selectLi).find('input[name="li_menuId"]');
				fnCallDeleteMenu(menuIdList);	
			}
		}
	}
	
	function fnCallDeleteMenu(menuIdList){
		var jsondata = new Object();
		var dataArr = new Array();
		$.each(menuIdList,function(idx,obj){
		    var json = new Object();
		    var menuId = nvl($(obj).val(),'');
		    var menuIdEl = $(obj).siblings('input:hidden');
		    var menuSeq = treeList.LookUp('menuId', menuId, 'menuSeq');
		    
		    
		    if(menuId != '' && menuSeq != ''){
		        json.menuId = menuId;
		        json.menuSeq = menuSeq;
		        dataArr.push(json);
		    }
		});		
		jsondata.menuList = dataArr;
		jsondata.menuIdCnt = dataArr.length;
		console.log('jsondata',jsondata);
		
		restCall('admin/menu/deleteMenu'
				,{
			        method: 'post',
			        data: jsondata,
			        async: false,
			    }
				, function(resp) {// 성공
					alert('메뉴가 삭제 되었습니다.');
					console.log(resp);
	                fnDeleteMenuSuccessHandler(resp.menuTree,resp.menuTreeOrderCount);
				} 
				, function(jqXHR, textStatus, errorThrown) {//실패
					console.log('restCall fail'+ '\n jqXHR=', jqXHR, '\n textStatus=', textStatus, '\n errorThrown=', errorThrown);
					alert('메뉴 추가처리에 실패하였습니다.\n관리자에게 문의하세요.');
	            }
		);
	}
	
	function fnDeleteMenuSuccessHandler(menuTree,menuTreeOrderCount){
		treeList = menuTree;
		maxOrder = menuTreeOrderCount;
		
    	fnClearDetailLayout();
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
    	fnClearDetailLayout();
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
            <div class="content admin_dptmt menu_set">
            	<%-- etcArea --%>
				<%@ include file="/WEB-INF/jsp/include/etcArea.jspf" %>
				<%-- //etcArea --%>

                <div class="titArea">
                    <h3>Admin <span>메뉴 관리</span></h3>
                </div>

                <div class="fl">
                    <div class="list_box">
                        <div class="tit">메뉴 목록</div>
                        <!-- [D] 22.01.25 .tree_view_wrap에 id="menuList"추가와 .treeSelector(기존 트리 구조 마크업) 영역 제거 부탁드립니다. -->
                        <div id="menuList" class="tree_view_wrap menu"></div>
                    </div>
                    <div class="btnBox">
                        <button type="buttn" class="btn_primary line fl" onclick="fnDeleteMenu(this); return false;">삭제</button>
                        <button type="buttn" class="btn_primary btn_lyr_open" data-lyr-name="add_menu" onclick="fnOpenModal(this); return false;">메뉴 추가</button>
                    </div>
                </div>

                <div class="fr">
                    <div class="tblBox admin" id="detail_layout">
                        <table summary="메뉴코드, 메뉴명, 메뉴명(영문), 메뉴 경로, 연결 구분, 메뉴 URL, 사용여부, 등록일, 수정일로 구성">
                            <caption class="hide">메뉴 정보 상세</caption>
                            <colgroup>
                                <col width="180px"><col>
                            </colgroup>
                            <thead>
                                <tr>
                                    <th colspan="2">메뉴 정보 상세</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <th>메뉴 코드</th>
                                    <td>
                                        <input type="text" class="ipt_txt" id="detail_menuCode" value="메뉴 코드">
                                    </td>
                                </tr>
                                <tr>
                                    <th>메뉴명</th>
                                    <td>
                                        <input type="text" class="ipt_txt" id="detail_menuName" value="메뉴명">
                                    </td>
                                </tr>
                                <tr>
                                    <th>메뉴명(영문)</th>
                                    <td>
                                        <input type="text" class="ipt_txt" id="detail_menuEngName" value="메뉴명(영문)">
                                    </td>
                                </tr>
                                <tr>
                                    <th>메뉴 경로</th>
                                    <td id="detail_menuFullPath">IT팀 &gt; 1팀</td>
                                </tr>
                                <tr>
                                	<th>순번</th>
                                	<td>
										<select class="select" id="detail_menuMaxOrder">
										</select>
                                	</td>
                                </tr>
                                <tr>
                                	<th>메뉴 이동 유형</th>
                                	<td>
										<select class="select" id="detail_menuMoveOption" onchange="fnMenuMoveOptionChange(this); return false;">
											<%-- <option value="1">상위 레벨</option> --%>
											<option value="2" selected="selected">동등 레벨</option>
											<%-- <option value="3">하위 레벨</option> --%>
										</select>
                                	</td>
                                </tr>
                                <tr>
                                    <th>메뉴 URL</th>
                                    <td>
                                        <input type="text" class="ipt_txt" id="detail_menuUrl" value="메뉴 URL">
                                    </td>
                                </tr>
                                <tr>
                                    <th>사용여부</th>
                                    <td>
                                        <div class="radioBox">   
                                            <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->
                                            <input type="radio" name="detail_menuStat" id="detail_menuStat_use" value="use" checked>
                                            <label for="detail_menuStat_use">사용</label>    
                                            <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->                      
                                            <input type="radio" name="detail_menuStat" id="detail_menuStat_unused" value="unused">
                                            <label for="detail_menuStat_unused">미사용</label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <th>메뉴권한</th>
                                    <td>
                                        <div class="radioBox">   
                                            <input type="radio" name="detail_roleId" id="detail_roleId_ALL" value="ALL" checked>
                                            <label for="detail_roleId_ALL">모든사용자</label>                          
                                            <input type="radio" name="detail_roleId" id="detail_roleId_ADMIN" value="ADMIN">
                                            <label for="detail_roleId_ADMIN">관리자</label>
                                            <input type="radio" name="detail_roleId" id="detail_roleId_USER" value="USER">
                                            <label for="detail_roleId_USER">사용자</label>                                            
                                        </div>
                                    </td>
                                </tr>                                
                                <tr>
                                    <th>등록일</th>
                                    <td id="detail_menuRegDt">2021년 11월 23일 14시 33분 12초</td>
                                </tr>
                                <tr>
                                    <th>수정일</th>
                                    <td id="detail_menuModDt">2021년 11월 23일 14시 33분 12초</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="btnBox">
                        <button type="button" class="btn_primary line" onclick="fnGetRevertDetailLayoutData(); return false;">되돌리기</button>
                        <button type="button" class="btn_primary" onclick="fnUpdateMenuSave(); return false;">수정</button>
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
<!-- 메뉴 추가 -->
<div id="add_menu" class="lyrWrap modify">
    <div class="lyrCont">
        <div class="lyr_top">
            <p class="tit">메뉴 추가</p>
            <button type="button" class="btn_lyr_close" data-lyr-name="add_menu" onclick="fnCloseModal(this); return false;"><span class="hide">닫기버튼</span></button>
        </div>
        <div class="lyr_mid">
            <div class="tblBox" id="add_layout">
                <table summary="메뉴 코드, 메뉴명, 메뉴명(영문), 메뉴 경로, 연결 구분, 메뉴 URL, 사용여부로 구성">
                     <caption class="hide">메뉴 추가</caption>
                     <colgroup>
                         <col width="114px"><col>
                     </colgroup>
                     <tbody>
                        <tr>
                            <th>메뉴 코드</th>
                            <td>
                                <input type="text" class="ipt_txt" id="add_menuCode" value="메뉴 코드">
                            </td>
                        </tr>
                        <tr>
                            <th>메뉴명</th>
                            <td>
                                <input type="text" class="ipt_txt" id="add_menuName" value="메뉴명">
                            </td>
                        </tr>
                        <tr>
                            <th>메뉴명(영문)</th>
                            <td>
                                <input type="text" class="ipt_txt" id="add_menuEngName" value="메뉴명(영문)">
                            </td>
                        </tr>
                        <tr>
                            <th>메뉴 경로</th>
                            <td id="add_menuFullPath"></td>
                        </tr>
                        <tr>
                        	<th>순번</th>
                        	<td>
								<select class="select" id="add_menuMaxOrder"></select>
                        	</td>
                        </tr>
                        <tr>
                        	<th>메뉴 이동 유형</th>
                        	<td>
								<select class="select" id="add_menuMoveOption" onchange="fnAddMenuMoveOptionChange(this); return false;">
									<option value="1">상위 레벨</option>
									<option value="2" selected="selected">동등 레벨</option>
									<option value="3">하위 레벨</option>
								</select>
                        	</td>
                        </tr>
                        <tr>
                            <th>메뉴 URL</th>
                            <td>
                                <input type="text" class="ipt_txt" id="add_menuUrl" value="메뉴 URL" disabled="">
                            </td>
                        </tr>
                        <tr>
                            <th>사용여부</th>
                            <td>
                                <div class="radioBox">   
                                    <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->
                                    <input type="radio" name="add_menuStat" id="add_menuStat_use" value="use" checked>
                                    <label for="add_menuStat_use">사용</label>    
                                    <!-- [D] input[type="radio"]의 id와 label의 for값이 동일해야 합니다. -->                      
                                    <input type="radio" name="add_menuStat" id="add_menuStat_unused" value="unused">
                                    <label for="add_menuStat_unused">미사용</label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <th>메뉴권한</th>
                            <td>
                                <div class="radioBox">   
                                    <input type="radio" name="add_roleId" id="add_roleId_ALL" onchange="fnAddMenuRoleIdChange(this); return false;" value="ALL">
                                    <label for="add_roleId_ALL">모든사용자</label>                          
                                    <input type="radio" name="add_roleId" id="add_roleId_ADMIN" onchange="fnAddMenuRoleIdChange(this); return false;" value="ADMIN">
                                    <label for="add_roleId_ADMIN">관리자</label>
                                    <input type="radio" name="add_roleId" id="add_roleId_USER" onchange="fnAddMenuRoleIdChange(this); return false;" value="USER">
                                    <label for="add_roleId_USER">사용자</label>                                            
                                </div>
                            </td>
                        </tr>
                     </tbody>
                </table>
            </div>
        </div>
        <div class="lyr_btm">
            <button type="button" class="btn_primary line btn_lyr_close" data-lyr-name="add_menu" onclick="fnCloseModal(this); return false;">취소</button>
            <button type="button" class="btn_primary" onclick="fnAddMenuSave(); return false;">저장</button>
        </div>
    </div>
</div>
<!-- //메뉴 추가 -->

<!-- Local Script -->
<script type="text/javascript">
	var treeList = ${menuTree};
	var maxOrder = ${menuTreeOrderCount};	
	
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
	
	//상세보기 메뉴 이동 유형 변경
	function fnMenuMoveOptionChange(obj){
		if(obj != undefined){
			var val = $(obj).val();
			fnMakeMenuOrderSelectBox(val);
		}
	}
	
	//상세보기 메뉴의 순번 select box
	function fnMakeMenuOrderSelectBox(menuMoveOptionVal){
		//선택되 노드데이타
		var node = $("#menuList").tree("getSelectedNode");
		
		if(!node){
			return alert('잘못된 접근입니다.\n메뉴를 먼저 선택하세요.');
		}
		
		var selector = $('#detail_menuMaxOrder');		
		
		if(menuMoveOptionVal == undefined){
			$('#detail_menuMoveOption').attr('selected',true).val('2');
		}
		
		if(menuMoveOptionVal == 1){
			//menuId
			var menuId = node.id;
			//parId
			var parId = treeList.LookUp('menuId', menuId, 'parId');
			if(parId == 'system'){
				alert('최상위 레벨의 메뉴입니다.');
				$('#detail_menuMoveOption').attr('selected',true).val('2');
				return false;
			}				
			
			//상위부서의 parId를 구해온다.
			parId = treeList.LookUp('menuId', parId, 'parId');						
			//maxCnt
			var maxOrderVal = maxOrder.LookUp('parId', parId, 'menuMaxOrder');			
			
			selector.empty();
			
			if(maxOrderVal == ''){
			    var option = '<option value="1">1</option>';
			    selector.append(option);				
			}else{
				for(var i = 0; i <= maxOrderVal; i++){
				    var option = '<option value="'+(i+1)+'">'+(i+1)+'</option>';
				    selector.append(option);
				}	
			}
		}else if(menuMoveOptionVal == 2){
			//menuId
			var menuId = node.id;
			//parId
			var parId = treeList.LookUp('menuId', menuId, 'parId');
			//maxCnt
			var maxOrderVal = maxOrder.LookUp('parId', parId, 'menuMaxOrder');
			
			var menuOrd = treeList.LookUp('menuId', menuId, 'menuOrder');
			menuOrder = Number(menuOrd);
			selector.empty();
			
			if(maxOrderVal == ''){
			    var option = '<option value="1">1</option>';
			    selector.append(option);				
			}else{
				for(var i = 0; i< maxOrderVal; i++){
					var option = '';
					if((i+1) == menuOrd){
						option = '<option value="'+(i+1)+'" selected>'+(i+1)+'</option>';
					}else{
						option = '<option value="'+(i+1)+'">'+(i+1)+'</option>';
					}
				    
				    selector.append(option);
				}
			}			
		}else if(menuMoveOptionVal == 3){
			//menuId
			var menuId = node.id;
			//parId
			var parId = menuId;
			//maxCnt
			var maxOrderVal = maxOrder.LookUp('parId', parId, 'menuMaxOrder');
			
			selector.empty();
			
			if(maxOrderVal == ''){
			    var option = '<option value="1">1</option>';
			    selector.append(option);				
			}else{
				for(var i = 0; i <= maxOrderVal; i++){
				    var option = '<option value="'+(i+1)+'">'+(i+1)+'</option>';
				    selector.append(option);
				}
			}				
		}else{
			alert('menuMoveOptionVal not match');
		}
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
		$('input:radio[name="detail_menuStat"][value="use"]').prop('checked', true);
		$('input:radio[name="detail_menuStat"]').prop('disabled', true);
				
		$('input:radio[name="detail_roleId"][value="ALL"]').prop('checked', true);
		//TODO
		//$('input:radio[name="detail_roleId"]').prop('disabled', true);		
				
		
		//메뉴 이동 유형
		$('#detail_menuMoveOption').attr('selected',true).val('2');
		$('#detail_menuMoveOption').prop('disabled',true);
		
		//순번 초기화
		$('#detail_menuMaxOrder').prop('disabled',true);
		$('#detail_menuMaxOrder').empty();		
		
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
		var node = $("#menuList").tree("getSelectedNode");
		var parId = treeList.LookUp('menuId', node.id, 'parId');
		
		//라디오 지정
		if(id == 'menuStat'){
			$('input:radio[name="detail_menuStat"][value="'+val+'"]').prop('checked', true);
			$('input:radio[name="detail_menuStat"]').prop('disabled', false);
		}
		
		if(id == 'roleId'){
			$('input:radio[name="detail_roleId"][value="'+val+'"]').prop('checked', true);
			/* TODO 초기화하고 일단 진행해봄
			if(parId == 'system'){
				$('input:radio[name="detail_roleId"]').prop('disabled', false);
			}else{
				$('input:radio[name="detail_roleId"]').prop('disabled', true);	
			}
			*/
		}
		
		//input의 text타입을 초기화한다.
		$.each(input,function(idx,obj){
		    var inputType = $(obj).attr('type');
		    var inputId = $(obj).attr('id');
		    if(inputType == 'text' && inputId !== undefined && inputId.replace('detail_','') == id){
		        $(obj).val(val);
		        $(obj).prop('disabled',false);
		    }
		});
		
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
		var node = $("#menuList").tree("getSelectedNode");
		if(node){
        	var menuId = node.id
            var keys = Object.keys(treeList[0]);
            $.each(keys,function(idx,key){
            	var id = key;
				var val = '';
            	if(id == 'menuRegDt'){
            		var menuDt = treeList.LookUp('menuId', menuId, 'menuRegDt');
            		var menuTime = treeList.LookUp('menuId', menuId, 'menuRegTime');
            		val = (DateUtils.convertDateKo(menuDt) + ' ' + DateUtils.convertTimeKo(menuTime));
            	}else if(id == 'menuModDt'){
            		var menuDt = treeList.LookUp('menuId', menuId, 'menuModDt');
            		var menuTime = treeList.LookUp('menuId', menuId, 'menuModTime');
            		val = (DateUtils.convertDateKo(menuDt) + ' ' + DateUtils.convertTimeKo(menuTime));
            	}else{
                	val = treeList.LookUp('menuId', menuId, key);
            	}
            	fnGetDetailLayoutData(id,val);
            });
    		//메뉴 이동 유형
    		$('#detail_menuMoveOption').attr('selected',true).val('2');
    		//$('#detail_menuMoveOption').prop('disabled',false);
    		$('#detail_menuMoveOption').prop('disabled',true);
    		
    		//순번
    		$('#detail_menuMaxOrder').prop('disabled',false);
    		fnMakeMenuOrderSelectBox('2');
		}
	}
	
    $(document).ready(function(){
        // submenu active
        $("#sub_ID00004").addClass("active");//상위 메뉴
        $("#sub_ID00013").addClass("active");//메뉴
        
    	fnClearDetailLayout();
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
        
        var $jqtree = $("#menuList");
        $jqtree.tree({
            data: data,
            onCreateLi: function(node, $li) {
                // Add 'icon' span before title
            	var menuId = node.id
                var keys = Object.keys(treeList[0]);
                $.each(keys,function(idx,key){
                	var elId = 'li_'+key;
                	var elHtml = '<input type="hidden" id="" name="" value="">';
                	elHtml = $(elHtml).attr('id',elId);
                	elHtml = $(elHtml).attr('name',elId);
                	elHtml = $(elHtml).attr('value',treeList.LookUp('menuId', menuId, key));
                	$li.find('.jqtree-title').after(elHtml);
                });
            },
            autoOpen: true,
            usecontextmenu: true
        });
        
        $jqtree.on(
        	    'tree.select',
        	    function(event) {
        	        if (event.node) {
                    	var menuId = event.node.id
                        var keys = Object.keys(treeList[0]);
                        $.each(keys,function(idx,key){
                        	var id = key;
							var val = '';
                        	if(id == 'menuRegDt'){
                        		var menuDt = treeList.LookUp('menuId', menuId, 'menuRegDt');
                        		var menuTime = treeList.LookUp('menuId', menuId, 'menuRegTime');
                        		val = (DateUtils.convertDateKo(menuDt) + ' ' + DateUtils.convertTimeKo(menuTime));
                        	}else if(id == 'menuModDt'){
                        		var menuDt = treeList.LookUp('menuId', menuId, 'menuModDt');
                        		var menuTime = treeList.LookUp('menuId', menuId, 'menuModTime');
                        		val = (DateUtils.convertDateKo(menuDt) + ' ' + DateUtils.convertTimeKo(menuTime));
                        	}else{
                            	val = treeList.LookUp('menuId', menuId, key);
                        	}
                        	fnGetDetailLayoutData(id,val);
                        });
                		//메뉴 이동 유형
                		$('#detail_menuMoveOption').attr('selected',true).val('2');
                		//$('#detail_menuMoveOption').prop('disabled',false);
                		$('#detail_menuMoveOption').prop('disabled',true);
                		
                		//순번
                		$('#detail_menuMaxOrder').prop('disabled',false);
                		fnMakeMenuOrderSelectBox('2');
        	        }else{
        	        	fnClearDetailLayout();
        	        }
        	    }
        	);        
    });    
  //메뉴 목록 and 상세데이터 로딩처리 End
  
/////////////////////////////////////////////////////메뉴 추가처리 start
	function fnOpenModal(obj){
		//선택되 노드데이타
		var node = $("#menuList").tree("getSelectedNode");
		
		if(!node){
			return alert('잘못된 접근입니다.\n메뉴를 먼저 선택하세요.');
		}		
		//modal 데이터 초기화
		fnClearAddMenuModalData();
		fnAddMenuSetModalData();    
	    $('#add_menu').css('display', 'block');
	    $('#add_menu').prepend('<div class="lyr_bg"></div>');			
	}
	
	function fnCloseModal(obj){
		fnClearAddMenuModalData();		 
		$('#add_menu').css('display', 'none'); 
		$('#add_menu').find('.lyr_bg').remove(); 
	}
	
	function fnAddMenuSave(){
		if(fnAddMenuSaveConfirm()){
			if(confirm('추가 하시겠습니까?')){
				fnCallAddMenuSave();	
			}			
		}
	}
	function fnCallAddMenuSave(){
		var node = $("#menuList").tree("getSelectedNode");
		//선택한 메뉴ID
		var orgMemuId = node.id;
		
		var menuCode =  nvl($('#add_menuCode').val(),'');
		//메뉴명
		var menuName = nvl($('#add_menuName').val(),'');
		//메뉴명(영문)
		var menuEngName = nvl($('#add_menuEngName').val(),'');
		//순번
		var menuOrder = nvl($('#add_menuMaxOrder').val(),'');
		//메뉴 이동 유형
		var menuMoveOption = nvl($('#add_menuMoveOption').val(),'');
		
		var parId = '';
		if(menuMoveOption == '1'){
			var menuId = node.id;
			//parId
			var parId = treeList.LookUp('menuId', menuId, 'parId');
			if(parId == 'system'){
				alert('최상위 레벨의 메뉴입니다.');
				$('#add_menuMoveOption').attr('selected',true).val('2');
				return false;
			}
			
			//상위부서의 parId를 구해온다.
			parId = treeList.LookUp('menuId', parId, 'parId');
		}else if(menuMoveOption == '2'){
			//menuId
			var menuId = node.id;
			//parId
			parId = treeList.LookUp('menuId', menuId, 'parId');			
		}else if(menuMoveOption == '3'){
			var menuId = node.id;
			parId = menuId;
		}
		
		//메뉴 URL
		var menuUrl = nvl($('#add_menuUrl').val(),'');
		//사용여부
		var menuStat = nvl( $('input:radio[name="add_menuStat"]:checked').val(),'');
		//메뉴 권한
		var roleId = nvl( $('input:radio[name="add_roleId"]:checked').val(),'');
		
		var data = new Object();
		data.menuCode = menuCode;
		data.menuName = menuName;
		data.menuEngName = menuEngName;
		data.menuOrder = menuOrder;
		data.parId = parId;
		data.menuUrl = menuUrl;
		data.menuStat = menuStat;
		data.roleId = roleId;		
		
		restCall('admin/menu/addMenu'
				,{
			        method: 'post',
			        data: data,
			        async: false,
			    }
				, function(resp) {// 성공
					alert('메뉴가 추가 되었습니다.');
	                fnAddMenuSuccessHandler(resp.menuTree,resp.menuTreeOrderCount);
				} 
				, function(jqXHR, textStatus, errorThrown) {//실패
					console.log('restCall fail'+ '\n jqXHR=', jqXHR, '\n textStatus=', textStatus, '\n errorThrown=', errorThrown);
					alert('메뉴 추가처리에 실패하였습니다.\n관리자에게 문의하세요.');
	            }
		);
	}
	
	function fnAddMenuSuccessHandler(menuTree,menuTreeOrderCount){
		treeList = menuTree;
		maxOrder = menuTreeOrderCount;
		
    	fnClearDetailLayout();
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
    	fnClearAddMenuModalData();
    	fnClearDetailLayout();
    	fnCloseModal();
	}
	
	function fnAddMenuSaveConfirm(){
		var node = $("#menuList").tree("getSelectedNode");
		if(!node){
			alert('잘못된 접근입니다.\n메뉴를 먼저 선택하세요.')
			return false;
		}
		//선택한 메뉴ID
		var orgMemuId = node.id;
		//메뉴코드
		var menuCode =  nvl($('#add_menuCode').val(),'');
		if(menuCode == ''){
			alert('메뉴코드는 필수 입력 정보입니다.');
			return false;
		}		
		//메뉴명
		var menuName = nvl($('#add_menuName').val(),'');
		if(menuName == ''){
			alert('메뉴명은 필수 입력 정보입니다.');
			return false;
		}
		//메뉴명(영문)
		var menuEngName = nvl($('#add_menuEngName').val(),'');
		//순번
		var menuOrder = nvl($('#add_menuMaxOrder').val(),'');		
		if(menuOrder == ''){
			alert('순번은 필수 선택 정보입니다.');
			return false;
		}
		//메뉴 이동 유형
		var menuMoveOption = nvl($('#add_menuMoveOption').val(),'');
		if(menuMoveOption == ''){
			alert('메뉴 이동 유형 필수 선택 정보입니다.');
			return false;
		}
		//메뉴 URL
		var menuUrl = nvl($('#add_menuUrl').val(),'');
		//사용여부
		var menuStat = nvl( $('input:radio[name="add_menuStat"]:checked').val(),'');
		if(menuStat == ''){
			alert('사용여부는 필수 선택 정보입니다.');
			return false;
		}
		//메뉴 권한
		var roleId = nvl( $('input:radio[name="add_roleId"]:checked').val(),'');
		if(roleId == ''){
			alert('메뉴권한은 필수 선택 정보입니다.');
			return false;
		}else{
			//선택한 메뉴ID
			var orgMemuId = node.id;
			var parId = treeList.LookUp('menuId', node.id, 'parId');
			var parRoleId = treeList.LookUp('menuId', parId, 'roleId');
			var checkedRoleId = $('input:radio[name="add_roleId"]:checked').val();
			if(parId != 'system' && parRoleId != checkedRoleId){
				alert('상위부서와 동일한 메뉴권한만 가능합니다.');
				$('input:radio[name="add_roleId"][value="'+parRoleId+'"]').prop('checked', true);
				return false;
			}
		}
		
		return true;
	}
	
	function fnAddMenuRoleIdChange(obj){
		var node = $("#menuList").tree("getSelectedNode");
		if(!node){
			alert('잘못된 접근입니다.\n메뉴를 먼저 선택하세요.')
			return false;
		}
		//선택한 메뉴ID
		var orgMemuId = node.id;
		var parId = treeList.LookUp('menuId', node.id, 'parId');
		var parRoleId = treeList.LookUp('menuId', parId, 'roleId');
		var checkedRoleId = $('input:radio[name="add_roleId"]:checked').val();
		if(parId != 'system' && parRoleId != checkedRoleId){
			alert('상위부서와 동일한 메뉴권한만 가능합니다.');
			$('input:radio[name="add_roleId"][value="'+parRoleId+'"]').prop('checked', true);
			return false;
		}
	}
	
	//메뉴추가 메뉴 이동 유형 변경
	function fnAddMenuMoveOptionChange(obj){
		if(obj != undefined){
			var val = $(obj).val();
			fnMakeAddMenuOrderSelectBox(val);
		}
	}
	
	//메뉴 추가 데이터 세팅
	function fnAddMenuSetModalData(){
		var input = $('#add_layout').find('input');
		var node = $("#menuList").tree("getSelectedNode");
		var parId = treeList.LookUp('menuId', node.id, 'parId');
		
		$.each(input,function(idx,obj){
		    var inputType = $(obj).attr('type');
		    var inputId = $(obj).attr('id');
		    if(inputType == 'text' && inputId !== undefined){
		        $(obj).prop('disabled',false);
		    }
		});
		
		//라디오 초기화
		$('input:radio[name="add_menuStat"]').prop('disabled', false);
		//TODO
		//$('input:radio[name="add_roleId"]').prop('disabled', false);			
		
		//메뉴 이동 유형
		$('#add_menuMoveOption').attr('selected',true).val('2');
		$('#add_menuMoveOption').prop('disabled',false);		
		
		//메뉴권한
		var roleId = treeList.LookUp('menuId', node.id, 'roleId');
		$('input:radio[name="add_roleId"][value="'+roleId+'"]').prop('checked', true);
		//TODO
		//$('input:radio[name="add_roleId"]').prop('disabled', false);
		
		var menuFullPath = treeList.LookUp('menuId', node.id, 'menuFullPath');
		$('#add_menuFullPath').text(menuFullPath);
		//순번
		$('#add_menuMaxOrder').prop('disabled',false);		
		fnMakeAddMenuOrderSelectBox('2');
	}
	
	//메뉴추가 데이터 초기화
	function fnClearAddMenuModalData(){
		var input = $('#add_layout').find('input');
		//input의 text타입을 초기화한다.
		$.each(input,function(idx,obj){
		    var inputType = $(obj).attr('type');
		    if(inputType == 'text'){
		        $(obj).val('');
		        $(obj).prop('disabled',true);
		    }
		});
		//라디오 초기화
		$('input:radio[name="add_menuStat"][value="use"]').prop('checked', true);
		$('input:radio[name="add_menuStat"]').prop('disabled', true);
				
		$('input:radio[name="add_roleId"][value="ALL"]').prop('checked', true);
		//TODO
		//$('input:radio[name="add_roleId"]').prop('disabled', true);		
				
		
		//메뉴 이동 유형
		$('#add_menuMoveOption').attr('selected',true).val('2');
		$('#add_menuMoveOption').prop('disabled',true);
		
		//순번 초기화
		$('#add_menuMaxOrder').prop('disabled',true);
		$('#add_menuMaxOrder').empty();		
		
		//메뉴 정보 상세에서 사용하는 id가 있는 td초기화
		var td = $('#add_layout').find('tr > td');
		//input의 text타입을 초기화한다.
		$.each(td,function(idx,obj){
		    var tdId = $(obj).attr('id');
		    if(tdId !== undefined){
		        $(obj).text('');
		    }    
		});
	}
	
	//메뉴추가 메뉴의 순번 select box
	function fnMakeAddMenuOrderSelectBox(menuMoveOptionVal){
		//선택되 노드데이타
		var node = $("#menuList").tree("getSelectedNode");
		
		if(!node){
			return alert('잘못된 접근입니다.\n메뉴를 먼저 선택하세요.');
		}
		
		var selector = $('#add_menuMaxOrder');		
		
		if(menuMoveOptionVal == undefined){
			$('#add_menuMoveOption').attr('selected',true).val('2');
		}
		
		if(menuMoveOptionVal == 1){
			//menuId
			var menuId = node.id;
			//parId
			var parId = treeList.LookUp('menuId', menuId, 'parId');
			if(parId == 'system'){
				alert('최상위 레벨의 메뉴입니다.');
				$('#add_menuMoveOption').attr('selected',true).val('2');
				return false;
			}
			
			//상위부서의 parId를 구해온다.
			parId = treeList.LookUp('menuId', parId, 'parId');						
			//maxCnt
			var maxOrderVal = maxOrder.LookUp('parId', parId, 'menuMaxOrder');			
			
			selector.empty();
			
			if(maxOrderVal == ''){
			    var option = '<option value="1">1</option>';
			    selector.append(option);				
			}else{
				for(var i = 0; i <= maxOrderVal; i++){
				    var option = '<option value="'+(i+1)+'">'+(i+1)+'</option>';
				    selector.append(option);
				}	
			}
		}else if(menuMoveOptionVal == 2){
			//menuId
			var menuId = node.id;
			//parId
			var parId = treeList.LookUp('menuId', menuId, 'parId');
			//maxCnt
			var maxOrderVal = maxOrder.LookUp('parId', parId, 'menuMaxOrder');
			
			selector.empty();
			
			if(maxOrderVal == ''){
			    var option = '<option value="1">1</option>';
			    selector.append(option);				
			}else{
				for(var i = 0; i <= maxOrderVal; i++){
				    var option = '<option value="'+(i+1)+'">'+(i+1)+'</option>';
				    selector.append(option);
				}
			}
			
			if(parId == 'system'){
				var roleId = treeList.LookUp('menuId', menuId, 'roleId');
				$('input:radio[name="add_roleId"][value="'+roleId+'"]').prop('checked', true);
				$('input:radio[name="add_roleId"]').prop('disabled', false);
			}			
		}else if(menuMoveOptionVal == 3){
			//menuId
			var menuId = node.id;
			//parId
			var parId = menuId;
			//maxCnt
			var maxOrderVal = maxOrder.LookUp('parId', parId, 'menuMaxOrder');
			
			selector.empty();
			
			if(maxOrderVal == ''){
			    var option = '<option value="1">1</option>';
			    selector.append(option);				
			}else{
				for(var i = 0; i <= maxOrderVal; i++){
				    var option = '<option value="'+(i+1)+'">'+(i+1)+'</option>';
				    selector.append(option);
				}
			}
			
			var orgParId = treeList.LookUp('menuId', menuId, 'parId');
			if(orgParId == 'system'){
				var roleId = treeList.LookUp('menuId', menuId, 'roleId');
				$('input:radio[name="add_roleId"][value="'+roleId+'"]').prop('checked', true);
				//TODO
				//$('input:radio[name="add_roleId"]').prop('disabled', true);
			}
			
			
		}else{
			alert('menuMoveOptionVal not match');
		}
	}
/////////////////////////////////////////////////////메뉴 추가처리 End

/////////////////////////////////////수정처리 start	
	function fnUpdateMenuSave(){
		if(fnMenuUpdateConfirm()){
			if(confirm('수정 하시겠습니까?')){
				fnCallUpdateMenuSave();	
			}
		}
	}
	
	function fnCallUpdateMenuSave(){
		var node = $("#menuList").tree("getSelectedNode");
		//선택한 메뉴ID
		var orgMemuId = node.id;
		
		var menuCode =  nvl($('#detail_menuCode').val(),'');
		//메뉴명
		var menuName = nvl($('#detail_menuName').val(),'');
		//메뉴명(영문)
		var menuEngName = nvl($('#detail_menuEngName').val(),'');
		//순번
		var menuOrder = nvl($('#detail_menuMaxOrder').val(),'');
		//메뉴 이동 유형
		var menuMoveOption = nvl($('#detail_menuMoveOption').val(),'');
		
		var parId = '';
		
		var menuId = node.id;
		
		if(menuMoveOption == '1'){			
			//parId
			var parId = treeList.LookUp('menuId', menuId, 'parId');
			if(parId == 'system'){
				alert('최상위 레벨의 메뉴입니다.');
				$('#detail_menuMoveOption').attr('selected',true).val('2');
				return false;
			}
			
			//상위부서의 parId를 구해온다.
			parId = treeList.LookUp('menuId', parId, 'parId');
		}else if(menuMoveOption == '2'){
			//menuId
			//parId
			parId = treeList.LookUp('menuId', menuId, 'parId');			
		}else if(menuMoveOption == '3'){
			parId = menuId;
		}
		
		//메뉴 URL
		var menuUrl = nvl($('#detail_menuUrl').val(),'');
		//사용여부
		var menuStat = nvl( $('input:radio[name="detail_menuStat"]:checked').val(),'');
		//메뉴 권한
		var roleId = nvl( $('input:radio[name="detail_roleId"]:checked').val(),'');
		
		var menuSeq = treeList.LookUp('menuId', menuId, 'menuSeq');
		
		var data = new Object();
		data.menuId = menuId;
		data.menuSeq = menuSeq;
		data.menuCode = menuCode;
		data.menuName = menuName;
		data.menuEngName = menuEngName;
		data.menuOrder = menuOrder;
		data.parId = parId;
		data.menuUrl = menuUrl;
		data.menuStat = menuStat;
		data.roleId = roleId;		
		
		restCall('admin/menu/updateMenu'
				,{
			        method: 'post',
			        data: data,
			        async: false,
			    }
				, function(resp) {// 성공
					alert('수정 되었습니다.');
					fnUpdateMenuSuccessHandler(resp.menuTree,resp.menuTreeOrderCount);
				} 
				, function(jqXHR, textStatus, errorThrown) {//실패
					console.log('restCall fail'+ '\n jqXHR=', jqXHR, '\n textStatus=', textStatus, '\n errorThrown=', errorThrown);
					alert('메뉴 추가처리에 실패하였습니다.\n관리자에게 문의하세요.');
	            }
		);
	}	
	
	function fnMenuUpdateConfirm(){
		var node = $("#menuList").tree("getSelectedNode");
		if(!node){
			alert('잘못된 접근입니다.\n메뉴를 먼저 선택하세요.')
			return false;
		}
		//선택한 메뉴ID
		var orgMemuId = node.id;
		//메뉴코드
		var menuCode =  nvl($('#detail_menuCode').val(),'');
		if(menuCode == ''){
			alert('메뉴코드는 필수 입력 정보입니다.');
			return false;
		}		
		//메뉴명
		var menuName = nvl($('#detail_menuName').val(),'');
		if(menuName == ''){
			alert('메뉴명은 필수 입력 정보입니다.');
			return false;
		}
		//메뉴명(영문)
		var menuEngName = nvl($('#detail_menuEngName').val(),'');
		//순번
		var menuOrder = nvl($('#detail_menuMaxOrder').val(),'');
		if(menuOrder == ''){
			alert('순번은 필수 선택 정보입니다.');
			return false;
		}
		//메뉴 이동 유형
		var menuMoveOption = nvl($('#detail_menuMoveOption').val(),'');
		if(menuMoveOption == ''){
			alert('메뉴 이동 유형 필수 선택 정보입니다.');
			return false;
		}
		//메뉴 URL
		var menuUrl = nvl($('#detail_menuUrl').val(),'');
		//사용여부
		var menuStat = nvl( $('input:radio[name="detail_menuStat"]:checked').val(),'');
		if(menuStat == ''){
			alert('사용여부는 필수 선택 정보입니다.');
			return false;
		}
		//메뉴 권한
		var roleId = nvl( $('input:radio[name="detail_roleId"]:checked').val(),'');
		if(roleId == ''){
			alert('메뉴권한은 필수 선택 정보입니다.');
			return false;
		}else{
			//선택한 메뉴ID
			var orgMemuId = node.id;
			var parId = treeList.LookUp('menuId', node.id, 'parId');
			var parRoleId = treeList.LookUp('menuId', parId, 'roleId');
			var checkedRoleId = $('input:radio[name="detail_roleId"]:checked').val();
			/* TODO 일단은 다 수정가능하게 변경
			if(parId != 'system' && parRoleId != checkedRoleId){
				alert('상위메뉴와 동일한 메뉴권한만 가능합니다.');
				$('input:radio[name="detail_roleId"][value="'+parRoleId+'"]').prop('checked', true);
				return false;
			}
			*/
		}
		return true;
	}
	
	function fnUpdateMenuSuccessHandler(menuTree,menuTreeOrderCount){
		treeList = menuTree;
		maxOrder = menuTreeOrderCount;
		
    	fnClearDetailLayout();
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
    	fnClearDetailLayout();
	}
/////////////////////////////////////수정처리 End
</script>
</body>
</html>