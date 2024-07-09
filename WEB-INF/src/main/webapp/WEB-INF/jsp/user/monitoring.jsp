<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ include file="/WEB-INF/jsp/include/header.jspf" %>
    <title>모니터링 || maum XDC</title>
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
                    <h3>모니터링</h3>
                </div>

                <div class="srchArea">
                    <table summary="시스템 상태로 구성">
                        <caption class="hide">시스템 상태</caption>
                        <colgroup>
                            <col width="180px"><col>
                        </colgroup>
                        <tbody>
                            <tr>
                                <th>시스템 상태</th>
                                <td>
                                    <div class="statusBox">
                                        <!-- [D] 상태에 따라 addClass('active')하면 보여집니다. -->
                                        <p id="statusSuccess" class="success">정상</p>
                                        <p id="statusError" class="error">비정상</p>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <!-- [D] 22.01.21 chart 추가 -->
                <div class="monitoringBox">
                    <div class="stn">
                        <div class="item">
                            <div class="tit">디스크 사용량</div>
                            <div class="chartBox">
                                <canvas id="diskSpace"></canvas>
                            </div>
                        </div>
                        <div class="item">
                            <div class="tit">웹 어플리케이션 CPU 사용량</div>
                            <div class="chartBox">
                                <canvas id="cpuUsage"></canvas>
                            </div>
                        </div>
                    </div>
                    <div class="stn">
                        <div class="item">
                            <div class="tit">JVM MEMORY</div>
                            <div class="chartBox">
                                <canvas id="jvmMemory"></canvas>
                            </div>
                        </div>
                        <div class="item">
                            <div class="tit">업타임</div>
                            <div class="chartBox">
                                <%--<canvas id="processUptime"></canvas> --%>
                                <div  id="processBox" class="statusBox" style="height: 100%;">
                                    <p id="processUptime" class="active" style="height: 100%; line-height: 15;"></p>
                                </div>
                            </div>
                        </div>
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
<!--로딩  -->
<div id="wrap-loading" class="lyr_bg lyrWrap" style="display:none;">
    <img src="/images/loading.gif" alt="Loading..." style="border:0; position:absolute; left:50%; top:50%;" />
</div>
<iframe width=0 height=0 frameborder=0 scrolling=no name="hide_frame" id="hide_frame" style="margin:0"></iframe>
 <!-- //로딩  -->
<!-- Local Script -->

<!-- 이슈, 리스크 진행상태 차트 -->
<script type="text/javascript">
var randomScalingFactor = function() {
    return Math.round(Math.random() * 100);
};
// // // //
$(document).ready(function(){
    // submenu active
    $("#sub_ID00003").addClass("active");//상위 메뉴
    $("#sub_ID00011").addClass("active");//하위 메뉴
    
    // status, 디스크 사용량 조회
    getStatus();

    // process cpu 사용량 조회
    getProcessCpuUsage();
    
    // JVM 조회
    drawJvmMemory();
    
    drawProcessUptime();
});

// 현재 프로그램 상태와 디스크 사용량 조회 
function getStatus(){
	$.ajax({
		url : "/management/health",
		type : "get",
		async : false,
        data: {},
		//dataType: "json",
	    contentType: "application/json",
	    timeout: 60000,// timeout : 60초간 서버와의 통신기다리는 시간.
	    cache: false,
		success : function(resp) {
			console.log(resp);
			// 상태
			if ("UP"==resp.status){
				$('#statusSuccess').addClass("active");
				$('#statusError').removeClass("active");
			}else{
				$('#statusSuccess').removeClass("active");
				$('#statusError').addClass("active");
			}

			var diskSpaceDetails = resp.components.diskSpace.details;
			
			// 디스크 사용량 차트 Draw
			drawDiskSpace(diskSpaceDetails);
			
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

// The "recent cpu usage" for the Java Virtual Machine process
function getProcessCpuUsage(){
	$.ajax({
		url : "/management/metrics/process.cpu.usage",
		type : "get",
		async : false,
        data: {},
		//dataType: "json",
	    contentType: "application/json",
	    timeout: 60000,// timeout : 60초간 서버와의 통신기다리는 시간.
	    cache: false,
		success : function(resp) {
			console.log(resp);
			
			var cpuUsage = resp.measurements[0].value;
		    
		    // cpu 사용량
		    drawCpuUsage(cpuUsage);
			
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

// 사용량 그리기
function drawDiskSpace(details){

	var total = details.total;
	var free = details.free;
	
	var type = 'gb';

	var totalSize = convertSize(type, total);
	var freeSize = convertSize(type, free);
	var useSize = totalSize - freeSize;
	
    var config = {
            type: 'horizontalBar',
            data: {
                labels: [
                    '전체 공간',
                    '사용 공간'
                ],
                datasets: [{
                    data: [
                    	totalSize,
                    	useSize
                    ],
                    borderWidth: 0,
                    backgroundColor: [
                        '#7a7cfb',
                        '#4bc0c0',
                    ],
                    label: 'Disk Space 단위 ('+type+')'
                }],
                fontColor: '#f7778a',
            },
            options: {
                scales: {
                    xAxes: [{
                        ticks: {
                            beginAtZero: true
                        }
                    }]
                },
                responsive: true,
                maintainAspectRatio: false,
                legend: {
                    labels: {
                        padding: 30,
    					fontColor: '#002b49',
    					fontSize: 13,
    					fontStyle: 500,
                        boxWidth: 30,
                        cornerRadius: 3,
    				},
                    position: 'top',
                },
                cutoutPercentage: 50,
                animation: {
                    animateScale: true,
                    animateRotate: true
                },
                tooltips: {
                    backgroundColor: '#ffffff',
                    cornerRadius: 3,
                    borderWidth: 1,
                    borderColor: '#cfd5eb',
                    caretSize: 0,
                    titleFontColor: '#cfd5eb',
                    bodyFontColor: '#002b49',
                    bodyFontSize: 14,
                }
            }
        };

        var ctx = document.getElementById('diskSpace').getContext('2d');
        window.myDoughnut = new Chart(ctx, config);
}

// CPU 사용량 그리기
function drawCpuUsage(cpuUsage){
	
	var cpuUsage = (cpuUsage * 100).toFixed(2);// 소숫점 두번째 자리수까지 출력


    var config2 = {
        type: 'line',
        data: {
            labels: ['', '사용량'],
            datasets: [{
                label: "CPU 사용량(%)",
                fill: true,
                backgroundColor: 'rgba(153, 102, 255, 0.5)',
                borderColor: '#9966ff',
                data: [
                	cpuUsage, cpuUsage
                ],
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            legend: {
                labels: {
                    padding: 30,
				},
            },
            tooltips: {
                mode: 'index',
                intersect: false,
            },
            hover: {
                mode: 'nearest',
                intersect: true
            },
            scales: {
                xAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }],
                yAxes: [{
                    ticks: {
                      max: 100,
                      min: 0,
                      stepSize: 10
                    },
                }],
            },
        }
    };

    var ctx2 = document.getElementById("cpuUsage").getContext("2d");
    window.myLine = new Chart(ctx2, config2);
}

// JVM MEMORY 사용량 그리기
function drawJvmMemory(){

	var jvmMemoryMax = getJvmMemoryMax();
	var jvmMemoryUsed = getJvmMemoryUsed();

	var type = 'gb';

	jvmMemoryMax = convertSize(type, jvmMemoryMax);
	jvmMemoryUsed = convertSize(type, jvmMemoryUsed);
	
    var config = {
            type: 'horizontalBar',
            data: {
                labels: [
                    '전체 메모리',
                    '사용중인 메모리'
                ],
                datasets: [{
                    data: [
                    	jvmMemoryMax,
                    	jvmMemoryUsed
                    ],
                    borderWidth: 0,
                    backgroundColor: [
                        '#7a7cfb',
                        '#ff9f40',
                    ],
                    label: 'Memory 단위 ('+type+')'
                }],
                fontColor: '#f7778a',
            },
            options: {
                scales: {
                    xAxes: [{
                        ticks: {
                            beginAtZero: true
                        }
                    }]
                },
                responsive: true,
                maintainAspectRatio: false,
                legend: {
                    labels: {
                        padding: 30,
    					fontColor: '#002b49',
    					fontSize: 13,
    					fontStyle: 500,
                        boxWidth: 30,
                        cornerRadius: 3,
    				},
                    position: 'top',
                },
                cutoutPercentage: 50,
                animation: {
                    animateScale: true,
                    animateRotate: true
                },
                tooltips: {
                    backgroundColor: '#ffffff',
                    cornerRadius: 3,
                    borderWidth: 1,
                    borderColor: '#cfd5eb',
                    caretSize: 0,
                    titleFontColor: '#cfd5eb',
                    bodyFontColor: '#002b49',
                    bodyFontSize: 14,
                }
            }
        };

    var ctx3 = document.getElementById('jvmMemory').getContext('2d');
    window.myDoughnut = new Chart(ctx3, config);
    
}

// AJAX JVM DATA
function getJvmMemoryMax(){
	var result;
	
	$.ajax({
		url : "/management/metrics/jvm.memory.max",
		type : "get",
		async : false,
        data: {},
		//dataType: "json",
	    contentType: "application/json",
	    timeout: 60000,// timeout : 60초간 서버와의 통신기다리는 시간.
	    cache: false,
		success : function(resp) {
			console.log(resp);
			
			result = resp.measurements[0].value;		
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
	
	return result;
}

function getJvmMemoryUsed(){
	var result;
	
	$.ajax({
		url : "/management/metrics/jvm.memory.used",
		type : "get",
		async : false,
        data: {},
		//dataType: "json",
	    contentType: "application/json",
	    timeout: 60000,// timeout : 60초간 서버와의 통신기다리는 시간.
	    cache: false,
		success : function(resp) {
			console.log(resp);
			
			result = resp.measurements[0].value;		
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
	
	return result;
}



// 프로세스 구동중인 시간
function drawProcessUptime(){
	var processUptime;
	$.ajax({
		url : "/management/metrics/process.uptime",
		type : "get",
		async : false,
        data: {},
		//dataType: "json",
	    contentType: "application/json",
	    timeout: 60000,// timeout : 60초간 서버와의 통신기다리는 시간.
	    cache: false,
		success : function(resp) {
			console.log(resp);
			
			processUptime = time(resp.measurements[0].value);		
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
	
	$('#processUptime').html("프로세스 구동중인 시간 :   " + processUptime);
}

// 바이트 형식 변환
function convertSize(type, byteSize){
	var returnSize = 0; 
	
	var kbSize = byteSize / 1024;
	var mbSize = kbSize / 1024;
	var gbSize = mbSize / 1024;
	
	if (type == "kb"){
		returnSize = kbSize;
	} else if (type == "mb"){
		returnSize =  mbSize;
	} else if (type == "gb"){
		returnSize = gbSize;
	} else {
		returnSize = byteSize;
	}
	
	return returnSize.toFixed(2);// 소숫점 두번째 자리수까지 출력
}

function time(seconds) {
	var result;

	//3항 연산자를 이용하여 10보다 작을 경우 0을 붙이도록 처리 하였다.
	var hour = parseInt(seconds/3600) < 10 ? '0'+ parseInt(seconds/3600) : parseInt(seconds/3600); 
	var min = parseInt((seconds%3600)/60) < 10 ? '0'+ parseInt((seconds%3600)/60) : parseInt((seconds%3600)/60);
	var sec = seconds % 60 < 10 ? '0'+ parseInt(seconds) % 60 : parseInt(seconds) % 60;

	//연산한 값을 화면에 뿌려주는 코드
	result = hour+"시간 "+min+"분 " + sec + "초 ";

	return result;
}
</script>
</body>
</html>