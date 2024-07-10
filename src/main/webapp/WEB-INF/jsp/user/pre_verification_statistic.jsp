<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ include file="/WEB-INF/jsp/include/header.jspf" %>
    <title>통계 || maum XDC</title>
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
                    <h3>통계</h3>
                </div>

                <div class="srchArea">
                    <table summary="조회 기간, 주기, 실행 조건으로 구성">
                        <caption class="hide">조건 선택</caption>
                        <tbody>
                            <tr>
                                <th>조회 기간</th>
                                <td>
                                    <div class="iptBox date">
                                		<input type="text" class="ipt_txt fromDate" id="searchStartDate" name="searchStartDate" value="" readonly="readonly">
                                    </div>                            
                                    <em class="tilde">~</em>
                                    <div class="iptBox date">
                                        <input type="text" class="ipt_txt toDate" id="searchEndDate" name="searchEndDate" value="" readonly="readonly">
                                    </div>
                                </td>
                                <th>주기</th>
                                <td>
                                    <select class="select" id="searchType">
                                        <option value="month">월별</option>
                                        <option value="day">일별</option>
                                    </select>
                                </td>
                                <th>부점</th>
                                <td>
                                    <input id="searchBr" type="text" class="ipt_txt" placeholder="검색어를 입력해주세요." value="${params.searchBr}">
                                    <input id="h_searchBr" type="hidden" class="ipt_txt" placeholder="검색어를 입력해주세요." value="${params.searchBr}">
                                </td>
                                <%--
                                <th>실행 조건</th>
                                <td>
                                    <select class="select" id="searchCondition">
                                        <option value="date">기간</option>
                                        <option value="br">부점</option>
                                        <option value="result">검증목적</option>
                                    </select>
                                </td>
                                 --%>
                                <td>
                                	<button type="button" class="btn_primary" onclick="javascript:ajaxGetStatistic();">검색</button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="btnBox">
                    <button type="button" class="btn_excel" onclick="javascript:excelDown();">통계정보 다운로드</button>
                </div>

                <div class="chartBox" id="myChartArea">
                    <canvas id="myChart"></canvas>
                </div>

                <div class="tblBox admin">
                    <table summary="전체, 일치, 불일치로 구성">
                        <caption class="hide">통계정보</caption>
                        <thead id="thead">
                        </thead>
                        <tbody id="tbody">
                            <tr>
                                <td class="empty" scope="row" colspan="13">데이터가 없습니다.</td>
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
<!-- //로딩  -->
<!-- Local Script -->
<script type="text/javascript">
jQuery.event.add(window,"load",function(){
    $(document).ready(function(){
        // submenu active
        $("#sub_ID00001").addClass("active");//상위 메뉴
        $("#sub_ID00007").addClass("active");//하위 메뉴
        
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
</script>

<script type="text/javascript">

// 화면 그리기
window.onload = function() {
	ajaxGetStatistic();
};
    
function ajaxGetStatistic(){
	var startDate = $('#searchStartDate').val();
	var endDate = $('#searchEndDate').val();
	var searchType = $('#searchType').val();// month, day
	//var searchCondition = $('#searchCondition').val();// date, br, result
	var searchBr = $('#searchBr').val();// 부점
	
	var mDiff = monthDiff(startDate, endDate);
	var dDiff = dayDiff(startDate, endDate);
	
	var s = new Date(startDate);
	var e = new Date(endDate);	
	if (s>e){
		alert('조회기간이 올바르지 않습니다.');
		return;
	}
	

	var jsonData = {
	};
	
	if (searchType == "month"){
		// 최대 12개월 조회만 가능하도록
		if (mDiff >= 12){
			alert("한번에 최대 12개월까지 조회가 가능합니다.");
			return;
		}
		jsonData = {
				searchType : searchType
				, searchStartDate : startDate
				, searchEndDate : endDate
				, diff : mDiff
				, searchBr : searchBr
				//, searchCondition : searchCondition
		};
	} else if (searchType == "day"){
		// 최대 31일간 조회만 가능하도록
		if (dDiff >= 31){
			alert("한번에 최대 31일만 조회가 가능합니다.");
			return;
		}
		jsonData = {
				searchType : searchType
				, searchStartDate : startDate
				, searchEndDate : endDate
				, diff : dDiff
				, searchBr : searchBr
				//, searchCondition : searchCondition
		};
	}
	
	$.ajax({
		url : "/user/pre_verification_statistic/search",
		type : "post",
		async : true,
		data : JSON.stringify(jsonData),
		//dataType: "json",
	    contentType: "application/json",
	    timeout: 60000,// timeout : 60초간 서버와의 통신기다리는 시간.
	    cache: false,
		success : function(resp) {
			console.log(resp);
			
			// 검색조건 세팅
			$('#searchStartDate').val(resp.params.searchStartDate);
			$('#searchEndDate').val(resp.params.searchEndDate);
			$('#searchType').val(resp.params.searchType);
			$('#searchBr').val(resp.params.searchBr);
			$('#h_searchBr').val(resp.params.searchBr);

			// select1 기존계약유지
			// select2 고객관리(감사인사,기념일축하)
			// select3 심의필 마케팅(일반,카드)
			// select4 미심의 광고문자
			var labelArr = [];
			var allArr = [];
			var select1Arr = [];
			var select2Arr = [];
			var select3Arr = [];
			var select4Arr = [];

			for(var i=0; i<resp.list.length;i++){
			    labelArr[i] = resp.list[i].date + "";
			    allArr[i] = resp.list[i].select1 + resp.list[i].select2 + resp.list[i].select3 + resp.list[i].select4 + "";
			    select1Arr[i] = resp.list[i].select1 + "";
			    select2Arr[i] = resp.list[i].select2 + "";
			    select3Arr[i] = resp.list[i].select3 + "";
			    select4Arr[i] = resp.list[i].select4 + "";
			}
			chartDraw2(labelArr, allArr, select1Arr, select2Arr, select3Arr, select4Arr);
			tableDraw2(labelArr, allArr, select1Arr, select2Arr, select3Arr, select4Arr, resp.params.searchBr);
			
			<%-- 검색기준 사용 X
			$('#searchCondition').val(resp.params.searchCondition);// date, br, result
			
			if (resp.params.searchCondition == "date"){
				var labelArr = [];
				var allArr = [];
				var fitArr = [];
				var notfitArr = [];
	
				for(var i=0; i<resp.list.length;i++){
				    labelArr[i] = resp.list[i].date + "";
				    allArr[i] = resp.list[i].fit + resp.list[i].notfit+ "";
				    fitArr[i] = resp.list[i].fit + "";
				    notfitArr[i] = resp.list[i].notfit + "";
				}
				chartDraw(labelArr, allArr, fitArr, notfitArr);
				
				tableDraw(labelArr, allArr, fitArr, notfitArr);
			} else if (resp.params.searchCondition == "br"){
				var labelArr = [];
				var allArr = [];
				var fitArr = [];
				var notfitArr = [];
	
				for(var i=0; i<resp.list.length;i++){
				    labelArr[i] = resp.list[i].brCode + "";
				    allArr[i] = resp.list[i].fit + resp.list[i].notfit+ "";
				    fitArr[i] = resp.list[i].fit + "";
				    notfitArr[i] = resp.list[i].notfit + "";
				}
				chartDraw(labelArr, allArr, fitArr, notfitArr);
				
				tableDraw(labelArr, allArr, fitArr, notfitArr);
			} else if (resp.params.searchCondition == "result"){
				// select1 기존계약유지
				// select2 고객관리(감사인사,기념일축하)
				// select3 심의필 마케팅(일반,카드)
				// select4 미심의 광고문자
				console.log('#############')
				var labelArr = [];
				var allArr = [];
				var select1Arr = [];
				var select2Arr = [];
				var select3Arr = [];
				var select4Arr = [];
	
				for(var i=0; i<resp.list.length;i++){
				    labelArr[i] = resp.list[i].date + "";
				    allArr[i] = resp.list[i].select1 + resp.list[i].select2 + resp.list[i].select3 + resp.list[i].select4 + "";
				    select1Arr[i] = resp.list[i].select1 + "";
				    select2Arr[i] = resp.list[i].select2 + "";
				    select3Arr[i] = resp.list[i].select3 + "";
				    select4Arr[i] = resp.list[i].select4 + "";
				}
				chartDraw2(labelArr, allArr, select1Arr, select2Arr, select3Arr, select4Arr);
				
				tableDraw2(labelArr, allArr, select1Arr, select2Arr, select3Arr, select4Arr);
			}
			--%>
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

// 차트 데이터
function chartDataCreate(labelArr, allArr, fitArr, notfitArr){
   var data = {
	     labels: labelArr,
	     datasets: [{
	         type: 'line',
	         label: '전체',
	         backgroundColor: '#ddd',
	         borderColor: '#555',
	         borderWidth: 2,
	         fill: false,
	         data: allArr
	     }, 
	         {
	         type: 'bar',
	         label: '일치',
	         backgroundColor: '#7a7cfb',
	         data: fitArr
	     }, {
	         type: 'bar',
	         label: '불일치',
	         backgroundColor: '#ec7876',
	         data: notfitArr
	     }]
	 };
 return data;
}

function chartDataCreate2(labelArr, allArr, select1Arr, select2Arr, select3Arr, select4Arr){
	// select1 기존계약유지
	// select2 고객관리(감사인사,기념일축하)
	// select3 심의필 마케팅(일반,카드)
	// select4 미심의 광고문자
   var data = {
	     labels: labelArr,
	     datasets: [{
	         type: 'line',
	         label: '전체',
	         backgroundColor: '#ddd',
	         borderColor: '#555',
	         borderWidth: 2,
	         fill: false,
	         data: allArr
	     }, 
	         {
	         type: 'bar',
	         label: '기존계약유지',
	         backgroundColor: '#7a7cfb',
	         data: select1Arr
	     }, {
	         type: 'bar',
	         label: '고객관리',
	         backgroundColor: '#ec7876',
	         data: select2Arr
	     }, {
	         type: 'bar',
	         label: '심의필 마케팅',
	         backgroundColor: '#1c7876',
	         data: select3Arr
	     }, {
	         type: 'bar',
	         label: '미심의 광고문자',
	         backgroundColor: '#553376',
	         data: select4Arr
	     }]
	 };
 return data;
}

// chart Draw
function chartDraw(labelArr, allArr, fitArr, notfitArr){
	// 기존 canvas 영역이있다면 지우고 다시 추가
	var myChartArea = document.getElementById("myChartArea");
	var oldMyChart = document.getElementById("myChart");
	var newMyChart = document.createElement("canvas");
	newMyChart.id = "myChart";
	
	// 기존 차트와 신규 차트가 겹치는 현상을 방지하기위해 기존 canvas 영역 삭제 후 신규 canvas 생성
	if (oldMyChart == null || oldMyChart == null){
		myChartArea.appendChild(newMyChart);
	}else {
		//oldMyChart.remove();
		var removeChart = oldMyChart;
		removeChart.parentNode.removeChild(removeChart); // ie >> remove() 함수 안되는 부분처리
		myChartArea.appendChild(newMyChart);
	}
	 
	var data = chartDataCreate(labelArr, allArr, fitArr, notfitArr);
	
	var ctx = document.getElementById("myChart").getContext("2d");
 	window.myMixedChart = new Chart(ctx, {
        type: 'bar',
        data: data,
        options: {
            responsive: true,
            title: {
                display: false,
            },
            tooltips: {
                mode: 'index',
                intersect: true
            },
            maintainAspectRatio: false,
            xAxes: [{ 
                categoryPercentage: 0.9, 
                barPercentage: 0.5, 
            }],
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        }
    });
}

function chartDraw2(labelArr, allArr, select1Arr, select2Arr, select3Arr, select4Arr){
	// 기존 canvas 영역이있다면 지우고 다시 추가
	var myChartArea = document.getElementById("myChartArea");
	var oldMyChart = document.getElementById("myChart");
	var newMyChart = document.createElement("canvas");
	newMyChart.id = "myChart";
	
	// 기존 차트와 신규 차트가 겹치는 현상을 방지하기위해 기존 canvas 영역 삭제 후 신규 canvas 생성
	if (oldMyChart == null || oldMyChart == null){
		myChartArea.appendChild(newMyChart);
	}else {
		//oldMyChart.remove();
		var removeChart = oldMyChart;
		removeChart.parentNode.removeChild(removeChart); // ie >> remove() 함수 안되는 부분처리
		myChartArea.appendChild(newMyChart);
	}
	 
	var data = chartDataCreate2(labelArr, allArr, select1Arr, select2Arr, select3Arr, select4Arr);
	
	var ctx = document.getElementById("myChart").getContext("2d");
 	window.myMixedChart = new Chart(ctx, {
        type: 'bar',
        data: data,
        options: {
            responsive: true,
            title: {
                display: false,
            },
            tooltips: {
                mode: 'index',
                intersect: true
            },
            maintainAspectRatio: false,
            xAxes: [{ 
                categoryPercentage: 0.9, 
                barPercentage: 0.5, 
            }],
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        }
    });
}
// 달 차이 계산
function monthDiff(searchStartDate, searchEndDate){
    var input1= searchStartDate.replace(/-/gi,"");
    var input2= searchEndDate.replace(/-/gi,"");
    var d1 = new Date(input1.substr(0,4),input1.substr(4,2)-1,input1.substr(6,2));
    var d2 = new Date(input2.substr(0,4),input2.substr(4,2)-1,input2.substr(6,2));

    var months;
    months = (d2.getFullYear() - d1.getFullYear()) * 12;
    months -= d1.getMonth();
    months += d2.getMonth();
    return months <= 0 ? 0 : months;
}
// 일차이 계산
function dayDiff(searchStartDate, searchEndDate){

	//var searchStartDate = $('#searchStartDate').val();
	//var searchEndDate = $('#searchEndDate').val();
	
    var input1= searchStartDate.replace(/-/gi,"");
    var input2= searchEndDate.replace(/-/gi,"");
    var d1 = new Date(input1.substr(0,4),input1.substr(4,2)-1,input1.substr(6,2));
    var d2 = new Date(input2.substr(0,4),input2.substr(4,2)-1,input2.substr(6,2));
	
    var days;
    days = d2.getTime() - d1.getTime();
    return Math.abs(days/ (1000 * 60 * 60 * 24));
}

function tableDraw(labelArr, allArr, fitArr, notfitArr) {
    var theadHtml = "";
    var tbodyHtml = "";

    if (labelArr.length == 0) {
        
       //theadHtml += '<thead>';
        theadHtml += '    <tr>';
        theadHtml += '        <th scope="col">-</th>';
        theadHtml += '    </tr>';
        //theadHtml += '</thead>';
        
        //tbodyHtml += '<tbody>';
        tbodyHtml += '    <tr>';
        tbodyHtml += '        <td class="empty" scope="row">데이터가 없습니다.</td>';
        tbodyHtml += '    </tr>';
        //tbodyHtml += '</tbody>';
    } else {
        
        //theadHtml += '<thead>';
        theadHtml += '    <tr>';
        theadHtml += '        <th scope="col">구분</th>';
        for (var i = 0; i < labelArr.length; i++) {
            theadHtml += '        <th scope="col">'+labelArr[i]+'</th>';
        }
        theadHtml += '    </tr>';
        //theadHtml += '</thead>';
        
        //tbodyHtml += '<tbody>';
        tbodyHtml += '    <tr>';
        tbodyHtml += '        <th scope="row">전체</th>';
        for (var i = 0; i < allArr.length; i++) {
            tbodyHtml += '        <td>'+allArr[i]+'</td>';
        }
        tbodyHtml += '    </tr>';
        tbodyHtml += '    <tr>';
        tbodyHtml += '        <th scope="row">일치</th>';
        for (var i = 0; i < fitArr.length; i++) {
            tbodyHtml += '        <td>'+fitArr[i]+'</td>';
        }
        tbodyHtml += '    </tr>';
        tbodyHtml += '    <tr>';
        tbodyHtml += '        <th scope="row">불일치</th>';
        for (var i = 0; i < notfitArr.length; i++) {
            tbodyHtml += '        <td>'+notfitArr[i]+'</td>';
        }
        tbodyHtml += '    </tr>';
        //tbodyHtml += '</tbody>';
    }

	$('#thead').html(theadHtml);
	$('#tbody').html(tbodyHtml);
}
function tableDraw2(labelArr, allArr, select1Arr, select2Arr, select3Arr, select4Arr, searchBr) {
	// select1 기존계약유지
	// select2 고객관리(감사인사,기념일축하)
	// select3 심의필 마케팅(일반,카드)
	// select4 미심의 광고문자
	
    var theadHtml = "";
    var tbodyHtml = "";

    if (labelArr.length == 0) {
       //theadHtml += '<thead>';
        theadHtml += '    <tr>';
        theadHtml += '        <th scope="col">-</th>';
        theadHtml += '    </tr>';
        //theadHtml += '</thead>';
        
        //tbodyHtml += '<tbody>';
        tbodyHtml += '    <tr>';
        tbodyHtml += '        <td class="empty" scope="row">데이터가 없습니다.</td>';
        tbodyHtml += '    </tr>';
        //tbodyHtml += '</tbody>';
    } else {
     	var text = ''; 
    	if (searchBr == null || searchBr == undefined || searchBr.length == 0){
    		text = '전체';
    	} else {
    		text = searchBr;
    	}
        //theadHtml += '<thead>';
        theadHtml += '    <tr>';
        theadHtml += '        <th scope="col">';
        theadHtml += '        부점 (' +text+ ')';
        theadHtml += '        </th>';
        for (var i = 0; i < labelArr.length; i++) {
            theadHtml += '        <th scope="col">'+labelArr[i]+'</th>';
        }
        theadHtml += '    </tr>';
        //theadHtml += '</thead>';
        
	// select1 기존계약유지
	// select2 고객관리(감사인사,기념일축하)
	// select3 심의필 마케팅(일반,카드)
	// select4 미심의 광고문자
	
        //tbodyHtml += '<tbody>';
        tbodyHtml += '    <tr>';
        tbodyHtml += '        <th scope="row">전체</th>';
        for (var i = 0; i < allArr.length; i++) {
            tbodyHtml += '        <td>'+allArr[i]+'</td>';
        }
        tbodyHtml += '    </tr>';
        tbodyHtml += '    <tr>';
        tbodyHtml += '        <th scope="row">기존계약유지</th>';
        for (var i = 0; i < select1Arr.length; i++) {
            tbodyHtml += '        <td>'+select1Arr[i]+'</td>';
        }
        tbodyHtml += '    </tr>';
        tbodyHtml += '    <tr>';
        tbodyHtml += '        <th scope="row">고객관리</th>';
        for (var i = 0; i < select2Arr.length; i++) {
            tbodyHtml += '        <td>'+select2Arr[i]+'</td>';
        }
        tbodyHtml += '    </tr>';
        tbodyHtml += '    <tr>';
        tbodyHtml += '        <th scope="row">심의필 마케팅</th>';
        for (var i = 0; i < select3Arr.length; i++) {
            tbodyHtml += '        <td>'+select3Arr[i]+'</td>';
        }
        tbodyHtml += '    </tr>';
        tbodyHtml += '    <tr>';
        tbodyHtml += '        <th scope="row">미심의 광고문자</th>';
        for (var i = 0; i < select4Arr.length; i++) {
            tbodyHtml += '        <td>'+select4Arr[i]+'</td>';
        }
        tbodyHtml += '    </tr>';
        //tbodyHtml += '</tbody>';
    }

	$('#thead').html(theadHtml);
	$('#tbody').html(tbodyHtml);
}

function excelDown(){
	var startDate = $('#searchStartDate').val();
	var endDate = $('#searchEndDate').val();
	var searchType = $('#searchType').val();// month, day
	//var searchCondition = $('#searchCondition').val();// date, br, result
	var searchBr = $('#h_searchBr').val();//부점검색
	var mDiff = monthDiff(startDate, endDate);
	var dDiff = dayDiff(startDate, endDate);
	
	var diff = "0";
	if (searchType == "month"){
		diff = mDiff;
	} else if (searchType == "day"){
		diff = dDiff;
	}
	
    var newForm = $('<form></form>');
    
    newForm.attr("name", "excelFrm");
    newForm.attr("method", "post");
    newForm.attr("action", "/user/pre_verification_statistic/excel_down");
    newForm.attr("target", "hide_frame");

    newForm.append($('<input/>', {type: 'hidden', name: 'searchType', value: searchType}));
    newForm.append($('<input/>', {type: 'hidden', name: 'searchStartDate', value: startDate}));
    newForm.append($('<input/>', {type: 'hidden', name: 'searchEndDate', value: endDate}));
    newForm.append($('<input/>', {type: 'hidden', name: 'diff', value: diff}));
    //newForm.append($('<input/>', {type: 'hidden', name: 'searchCondition', value: searchCondition}));
    newForm.append($('<input/>', {type: 'hidden', name: 'searchBr', value: searchBr}));

    newForm.appendTo('body');

    newForm.submit();
}

</script>
</body>
</html>