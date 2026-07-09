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
            <div class="content statistic">
                <%@ include file="/WEB-INF/jsp/include/etcArea.jspf" %>

                <div class="titArea">
                    <h3>통계</h3>
                </div>

                <div class="srchArea">
                    <table summary="조회 기간, 주기, 실행 조건으로 구성">
                        <caption class="hide">조건 선택</caption>
                        <colgroup>
                            <col width="100px"><col width="250px"><col width="100px"><col><col width="100px"><col>
                        </colgroup>
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
                                <td>
                                    <button type="button" class="btn_primary" onclick="javascript:ajaxGetStatistic();">검색</button>
                                </td>
                                <%--
                                <th>주기</th>
                                <td>
                                    <select class="select" id="searchType">
                                        <option value="month">월별</option>
                                        <option value="day">일별</option>
                                    </select>
                                    <button type="button" class="btn_primary" onclick="javascript:ajaxGetStatistic();">검색</button>
                                </td>
                                <th>실행 조건</th>
                                <td>
                                    <select class="select">
                                        <option value="">전체</option>
                                        <option value="">배치</option>
                                        <option value="">즉시</option>
                                    </select>
                                </td>
                                --%>
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
                    <table summary="전체, 진본, 위조로 구성">
                        <caption class="hide">통계정보</caption>
                        <thead id="thead">
                        <%--
                            <tr>
                                <th scope="col">구분</th>
                                <th scope="col">1월</th>
                                <th scope="col">2월</th>
                                <th scope="col">3월</th>
                                <th scope="col">4월</th>
                                <th scope="col">5월</th>
                                <th scope="col">6월</th>
                                <th scope="col">7월</th>
                                <th scope="col">8월</th>
                                <th scope="col">9월</th>
                                <th scope="col">10월</th>
                                <th scope="col">11월</th>
                                <th scope="col">12월</th>
                            </tr>
                        --%>
                        </thead>
                        <tbody id="tbody">
                        <%--
                            <tr>
                                <td class="empty" scope="row" colspan="13">데이터가 없습니다.</td>
                            </tr>
                            <tr>
                                <th scope="row">전체</th>
                                <td>256</td>
                                <td>303</td>
                                <td>256</td>
                                <td>303</td>
                                <td>256</td>
                                <td>303</td>
                                <td>256</td>
                                <td>303</td>
                                <td>256</td>
                                <td>303</td>
                                <td>256</td>
                                <td>303</td>
                            </tr>
                            <tr>
                                <th scope="row">진본</th>
                                <td>256</td>
                                <td>303</td>
                                <td>256</td>
                                <td>303</td>
                                <td>256</td>
                                <td>303</td>
                                <td>256</td>
                                <td>303</td>
                                <td>256</td>
                                <td>303</td>
                                <td>256</td>
                                <td>303</td>
                            </tr>
                            <tr>
                                <th scope="row">위조</th>
                                <td>256</td>
                                <td>303</td>
                                <td>256</td>
                                <td>303</td>
                                <td>256</td>
                                <td>303</td>
                                <td>256</td>
                                <td>303</td>
                                <td>256</td>
                                <td>303</td>
                                <td>256</td>
                                <td>303</td>
                            </tr>
                        --%>
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
        $("#sub_ID00003").addClass("active");//상위 메뉴
        
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
	var mDiff = monthDiff(startDate, endDate);
	
	var s = new Date(startDate);
	var e = new Date(endDate);	
	if (s>e){
		alert('조회기간이 올바르지 않습니다.');
		return;
	}
	
	// 최대 12개월 조회만 가능하도록
	if (mDiff >= 12){
		alert("한번에 최대 12개월까지 조회가 가능합니다.");
		return;
	}
	
	var jsonData = {
		searchType : $('#searchType').val()
		, searchStartDate : startDate
		, searchEndDate : endDate
		, mDiff : mDiff
	};
	$.ajax({
		url : "/user/statistic/search",
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
			
			// Chart 그리기 
			var labelArr = [];
			var allArr = [];
			var realArr = [];
			var fakeArr = [];

			for(var i=0; i<resp.list.length;i++){
			    console.log(resp.list[i]);
			    labelArr[i] = resp.list[i].date + "";
			    allArr[i] = resp.list[i].real + resp.list[i].paper + resp.list[i].monitor + "";
			    realArr[i] = resp.list[i].real + "";
			    fakeArr[i] = resp.list[i].paper + resp.list[i].monitor + "";
			}
			chartDraw(labelArr, allArr, realArr, fakeArr);
			
			tableDraw(labelArr, allArr, realArr, fakeArr);
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
function chartDataCreate(labelArr, allArr, realArr, fakeArr){
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
	         label: '진본',
	         backgroundColor: '#7a7cfb',
	         data: realArr
	     }, {
	         type: 'bar',
	         label: '위조',
	         backgroundColor: '#ec7876',
	         data: fakeArr
	     }]
	 };
 return data;
}

// chart Draw
function chartDraw(labelArr, allArr, realArr, fakeArr){
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
	 
	var data = chartDataCreate(labelArr, allArr, realArr, fakeArr);
	
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

function tableDraw(labelArr, allArr, realArr, fakeArr) {
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
        tbodyHtml += '        <th scope="row">진본</th>';
        for (var i = 0; i < realArr.length; i++) {
            tbodyHtml += '        <td>'+realArr[i]+'</td>';
        }
        tbodyHtml += '    </tr>';
        tbodyHtml += '    <tr>';
        tbodyHtml += '        <th scope="row">위조</th>';
        for (var i = 0; i < fakeArr.length; i++) {
            tbodyHtml += '        <td>'+fakeArr[i]+'</td>';
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
	var mDiff = monthDiff(startDate, endDate);
    var newForm = $('<form></form>');
    
    newForm.attr("name", "excelFrm");
    newForm.attr("method", "post");
    newForm.attr("action", "/user/statistic/excel_down");
    newForm.attr("target", "hide_frame");

    newForm.append($('<input/>', {type: 'hidden', name: 'searchType', value: $('#searchType').val()}));
    newForm.append($('<input/>', {type: 'hidden', name: 'searchStartDate', value: startDate}));
    newForm.append($('<input/>', {type: 'hidden', name: 'searchEndDate', value: endDate}));
    newForm.append($('<input/>', {type: 'hidden', name: 'mDiff', value: mDiff}));

    newForm.appendTo('body');

    newForm.submit();
}

</script>
</body>
</html>