// 공통 js
var agent = navigator.userAgent.toLowerCase(),
    MSIE = 'MSIE',
    EDGE = 'Edge',
    CHROME = 'Chrome',
    FIREFOX = 'Firefox',
    SAFARI = 'Safari',
    browser = /trident/i.test(agent) || /msie/i.test(agent) ? MSIE :
        /edge/i.test(agent) ? EDGE :
            /chrome/i.test(agent) ? CHROME :
                /firefox/i.test(agent) ? FIREFOX :
                    /safari/i.test(agent) ? SAFARI : 'Unknown',
    WINDOWS = 'Windows',
    LINUX = 'Linux',
    MAC = 'Macintosh',
    IPHONE = 'iPhone',
    IPAD = 'iPad',
    ANDROID = 'Android',
    system = /Windows/i.test(agent) ? WINDOWS :
        /linux/i.test(agent) ? LINUX :
            /macintosh/i.test(agent) ? MAC :
                /iphone/i.test(agent) ? IPHONE :
                    /ipad/i.test(agent) ? IPAD :
                        /android/i.test(agent) ? ANDROID : 'Unknown',
    DEFAULT_SPECS = "toolbar=0,location=0,directories=0,titlebar=0,status=0,menubar=0,scrollbars=1,resizable=1",
    PATH = "/",
    COVER_RATIO = 0.6625;

// Ckeditor4 START
function makeFck(objName, callback) {
    try {
        var editor = CKEDITOR.replace(objName, {
            removePlugins: 'easyimage, cloudservices',
            filebrowserUploadUrl: '/api/v1/ckeditor/fileUpload?'
        });

        if (callback && typeof callback === 'function') {
            callback(objName, editor);
        }
    } catch (e) {
        console.log(e);
    }
}

function setEditor(name, editor) {
    contentEditor = editor;
}

// Ckeditor4 END

/* example
 *   icon : 경고 - warning | 채크 - complete | 질문 - message
 *   showAlert('complete', '저장 되었습니다', function() {
 *       // 닫기 버튼 클릭 시 실행할 함수 (생략가능)
 *   });
 */
function showAlert(icon, msg, callbackFunc) {
    let alert = `
        <div id="alert" class="system_alert">
          <div class="dim"></div>
          <div class="popup">
            <div class="pop_cont">
              <i class="ico ico_${icon}"></i>
              <strong class="pop_tit">${msg}</strong>
            </div>
            <div class="pop_foot">
              <a href="javascript:void(0);" class="btn btn_md line_primary btn_close">닫기</a>
            </div>
          </div>
        </div>
    `;

    $('body').append(alert);
    layerShow("alert");

    $('.btn_close').click(function () {
        if (callbackFunc && typeof callbackFunc === 'function') {
            callbackFunc();
        }
        layerHide('alert');
        $('#alert').remove();
    });
}

/* example
 *   icon : 경고 - warning | 채크 - complete | 질문 - message
 *   showConfirm('complete', '저장 하시겠습니까?', function() {
 *       // 확인 버튼 클릭 시 실행할 함수
 *   }, function() {
 *       // 닫기 버튼 클릭 시 실행할 함수 (생략가능)
 *   });
 */
function showConfirm(icon, msg, confirmFunc, dismissFunc) {
    let confirm = `
        <div id="confirm" class="system_alert">
            <div class="dim"></div>
            <div class="popup">
                <div class="pop_cont">
                    <i class="ico ico_${icon}"></i>
                    <strong class="pop_tit">${msg}</strong>
                </div>
                <div class="pop_foot">
                    <a href="javascript:void(0);" id="dismissBtn" class="btn btn_md line_primary">닫기</a>
                    <a href="javascript:void(0);" id="confirmBtn" class="btn btn_md fill_primary">확인</a>
                </div>
            </div>
        </div>
    `;

    $('body').append(confirm);
    layerShow("confirm");

    $('#dismissBtn').click(function () {
        if (dismissFunc && typeof dismissFunc === 'function') {
            dismissFunc();
        }
        layerHide('confirm');
        $('#confirm').remove();
    });

    $('#confirmBtn').click(function () {
        if (confirmFunc && typeof confirmFunc === 'function') {
            confirmFunc();
        }
        layerHide('confirm');
        $('#confirm').remove();
    });
}


/****************************************************************************
 * 숫자만 입력 가능하도록 체크
 * input number에 e 입력 방지
 * maxlength 사용 가능
 * input text에 사용
 * ex) onkeyup="fn_numberOnly(this)"
 ***************************************************************************/
function fn_numberOnly(obj) {
    let regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+┼<>@\#$%&\'\"\\\(\=]/gi;	//정규식 구문
    if (regExp.test(obj.value)) {
        // 특수문자 모두 제거
        obj.value = obj.value.replace(regExp, '');
    }
    let num = /[^0-9]/g;
    if (num.test(obj.value)) {
        obj.value = obj.value.replace(num, '');
    }

}


function validFile(selector) {
    let retVal = true;

    $(`#${selector}`).each(function (i) {

        let fileObj = $(this)[0].files[0];
        let maxSize = 10 * 1024 * 1024; // 1MB

        let filename = fileObj.name;
        let fileType = fileObj.type;
        let allowedExtensions = /\.(jpg|jpeg|png|)$/i; // 허용되는 파일 확장자
        let fileSize = fileObj.size;

        if (!allowedExtensions.exec(filename) || !["image/jpeg", "image/png", "image/jpg"].includes(fileType)) {
            showAlert('warning', '유효하지 않은 파일 유형입니다.');
            retVal = false;
        }

        if (fileSize > maxSize) {
            showAlert('warning', '파일사이즈가 10MB가 넘습니다.');
            retVal = false;
        }
    });

    return retVal;
}


/****************************************************************************
 * 숫자에 세자리 마다,넣기                                      *
 ***************************************************************************/
function fn_setCommas(num) {
    if (!isNaN(num))
        return Number(num).toLocaleString('ko-KR')
    else
        return num;
}

/****************************************************************************
 * 숫자에 세자리 마다,넣기
 * input 입력
 ***************************************************************************/
function fn_setCommasOnKeyup(obj) {
    let regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+┼<>@\#$%&\'\"\\\(\=]/gi;	//정규식 구문
    if (regExp.test(obj.value)) {
        // 특수문자 모두 제거
        obj.value = fn_setCommas(obj.value.replace(regExp, ''));
    }
    let num = /[^0-9]/g;
    if (num.test(obj.value)) {
        obj.value = fn_setCommas(obj.value.replace(num, ''));
    }
    obj.value = fn_setCommas(obj.value);
}

/**
 * rangedatepicker 초기화
 * 검색 기간(n일전 ~ 당일)
 *
 */
function fn_resetRangeDatePicker(minusDay) {
    let today = new Date();	// 현재 날짜 및 시간
    let nDaysAgo = new Date(new Date().setDate(new Date().getDate() - minusDay));	// n일전

    let dd = String(today.getDate()).padStart(2, '0');
    let mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    let yyyy = today.getFullYear();
    if (mm === '00') {
        mm = '12';
        yyyy = yyyy - 1;
    }
    today = yyyy + '-' + mm + '-' + dd;
    $('#searchEdDt').val(today);

    dd = String(nDaysAgo.getDate()).padStart(2, '0');
    mm = String(nDaysAgo.getMonth() + 1).padStart(2, '0'); //January is 0!
    yyyy = nDaysAgo.getFullYear();
    if (mm === '00') {
        mm = '12';
        yyyy = yyyy - 1;
    }
    nDaysAgo = yyyy + '-' + mm + '-' + dd;
    $('#searchStDt').val(nDaysAgo);

    var searchDtStr = $('#searchDtStr');
    searchDtStr.val(nDaysAgo + '~' + today);

    $('#allChkRangeDatePicker').prop('checked', false);
    searchDtStr.prop('disabled', false);
}

/**
 * 비밀번호 유효성 검사
 * 영문+숫자+특수문자 8자 이상
 *
 * @param pwd
 * @returns {boolean}
 */
function fn_pwdCheck(pwd) {
    let regExp = /^(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9])(?=.*[0-9]).{8,}$/;

    if (!regExp.test(pwd)) {
        return false;
    } else {
        return true;
    }
}

/**
 * hyphen 자동 입력
 *
 * @param ele
 */
function telNoAutoHyphen(ele) {
    ele.value = ele.value.replace(/[^0-9]/g, "").replace(/(^02|^0505|^1[0-9]{3}|^0[0-9]{2})([0-9]{3,4})?([0-9]{4})$/, "$1-$2-$3").replace("--", "-")
}


/**
 * Daum 주소 검색
 *
 * @param prefix
 * prefix 없이 호출한 경우 zip, adres에 값 설정
 */
function fn_openAdresPop(prefix, callback) {
    daum.postcode.load(function () {
        new daum.Postcode({
            oncomplete: function (data) {
                let zip = data.zonecode;

                let adres = '';
                if (data.userSelectedType === 'R') {
                    // 도로명 주소 선택 시
                    adres = data.roadAddress;
                } else {
                    // 지번 주소 선택 시
                    adres = data.jibunAddress;
                }

                if (!prefix) {
                    $('#zip').val(zip);
                    $('#adres').val(adres);
                    $('#dtlAdres').focus();
                } else {
                    $('#' + prefix + 'Zip').val(zip);
                    $('#' + prefix + 'Adres').val(adres);
                    $('#' + prefix + 'DtlAdres').focus()
                }

                // 콜백 함수 호출
                if (callback && typeof callback === 'function') {
                    callback(adres);
                }
            }
        }).open();
    })
}


/**
 * 이메일 주소 검증
 *
 * @param email selector
 * @returns {boolean}
 */
function fn_checkEmail(email) {
    let regExp = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;

    return regExp.test(email);
}

/**
 * URL 주소 검증
 *
 * @param url value
 * @returns {boolean}
 */
function fn_checkUrl(url) {
    const urlRegex = /^(https?:\/\/)?((([\da-z.-]+)\.([a-z.]{2,6}))|(youtu\.be))([\/\w .-]*)*\/?(\?.*)?$/;
    return urlRegex.test(url);
}

Array.prototype.LookUp = function (key, value, column) {
    var result = '';
    var map = this.find((item, index) => item[key] == value);
    if (map == undefined || map == null) {
        result = '';
    } else {
        result = map[column];
    }
    return result;
};

var DateUtils = {
    ONE_MINUTE: 1000 * 60,
    ONE_HOUR: 1000 * 60 * 60,
    ONE_DAY: 1000 * 60 * 60 * 24,
    PATTERN: 'yyyy.MM.dd HH:mm',
    format: function (input, pattern) {
        var date = DateUtils.getDate(input);
        if (!pattern) {
            pattern = DateUtils.PATTERN;
        }
        return date.format(pattern);
    },
    getDate: function (input) {
        if (typeof input === 'string') {
            input = input.substring(0, 23);
            var a = input.split(/[^0-9]/);
            return new Date(a[0], a[1] - 1, a[2], a[3], a[4], a[5]);
        }
        return new Date(input);
    },
    formatPretty: function (input, pattern) {
        var date = DateUtils.getDate(input);
        var time = date.getTime();
        var now = Date.now();
        var diff = now - time;
        if (diff <= 0) {
            return '지금';
        } else if (diff < DateUtils.ONE_HOUR) { // 1시간 이내
            return Math.round(diff / DateUtils.ONE_MINUTE) + "분전";
        } else if (diff < DateUtils.ONE_DAY) { // 하루이내
            return Math.round(diff / DateUtils.ONE_HOUR) + "시간전";
        } else {
            if (!pattern)
                pattern = DateUtils.PATTERN;
            return date.format(pattern);
        }
    },
    compareToday: function (dateText) {
        var today = new Date();
        var date = DateUtils.getDate(dateText);
        // console.log('today', today, 'date', date, date.getTime(), today.getTime(), date.getTime() - today.getTime());
        return today.getTime() - date.getTime();
    },
    /*
     * 오늘 기준 몇일이 지났는지 계산
     */
    passedToday: function (dateText) {
        var today = new Date();
        today.setHours(0);
        today.setMinutes(0);
        today.setSeconds(0);
        var date = DateUtils.getDate(dateText);
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        return Math.floor((today.getTime() - date.getTime()) / DateUtils.ONE_DAY);
    },
    // 시작일자 기준 몇일이 지났는지 계산
    passeDays: function (sStartDate, sEndDate) {
        var sdate = DateUtils.getDate(sStartDate);
        sdate.setHours(0);
        sdate.setMinutes(0);
        sdate.setSeconds(0);
        var edate = DateUtils.getDate(sEndDate);
        edate.setHours(0);
        edate.setMinutes(0);
        edate.setSeconds(0);
        return Math.floor((sdate.getTime() - edate.getTime()) / DateUtils.ONE_DAY);
    },
    /**
     *  지난달, 전후년 조회 추가
     */
    moveMonth: function (thisId, settingDate) {

        if (thisId == "preMonth") {
            if (settingDate.getMonth() == 0) { //1월에 전달 이동시 (0=월, 11=12월)
                settingDate.setFullYear(settingDate.getFullYear() - 1);
                settingDate.setMonth(11);
            } else {
                settingDate.setMonth((settingDate.getMonth() - 1));
            }
        } else if (thisId == "nextMonth") {
            if (settingDate.getMonth() == 11) { //12월에 다음달 이동시
                settingDate.setFullYear(settingDate.getFullYear() + 1);
                settingDate.setMonth(0);
            } else {
                settingDate.setMonth((settingDate.getMonth() + 1));
            }
        }
        var getDate = settingDate.getFullYear().toString() + "-" + (settingDate.getMonth() >= 9 ? (settingDate.getMonth() + 1).toString() : '0' + (settingDate.getMonth() + 1).toString()) + "-" + (settingDate.getDate() >= 10 ? (settingDate.getDate()).toString() : '0' + (settingDate.getDate()).toString());

        return getDate;
    },
    /**
     *  전후년 조회
     */
    moveYear: function (thisId, settingDate) {

        if (thisId == "prevYear") {
            settingDate.setFullYear(settingDate.getFullYear() - 1);
        } else if (thisId == "nextYear") {
            settingDate.setFullYear(settingDate.getFullYear() + 1);
        }
        var getDate = settingDate.getFullYear().toString() + "-" + (settingDate.getMonth() >= 9 ? (settingDate.getMonth() + 1).toString() : '0' + (settingDate.getMonth() + 1).toString()) + "-" + (settingDate.getDate() >= 10 ? (settingDate.getDate()).toString() : '0' + (settingDate.getDate()).toString());

        return getDate;
    },

    /**
     *  생년월일String Date형변환
     */
    convertDate: function (input) {
        var reg = /(\d{4})(\d{2})(\d{2})/;
        return input.replace(reg, '$1-$2-$3');
    },

    convertDateKo: function (input) {
        var reg = /(\d{4})(\d{2})(\d{2})/;
        return input.replace(reg, '$1년 $2월 $3일');
    },

    convertTimeKo: function (input) {
        var reg = /(\d{2})(\d{2})(\d{2})/;
        return input.replace(reg, '$1시 $2분 $3초');
    },

    /**
     * 월까지만 date 형변환
     * @param input
     * @returns
     */
    convertMonth: function (input) {
        var reg = /(\d{4})(\d{2})/;
        return input.replace(reg, '$1-$2');
    }
}

/**
 * nvl 체크
 * @param obj : 검사값
 * @param val : 변경값
 * @returns
 */
function nvl(obj, val) {
    if (obj == null || obj == "" || obj === undefined) {
        // 두번째 매개변수가 없을경우 빈값으로 설정
        return val === undefined ? null : val;
    } else {
        return obj;
    }
}

/**
 * Rest Service
 * ex)	restCall('', {}, function(resp) {}, function(jqXHR) {});
 */
var restCall = function (url, args, callback, failCallback) {
    var DEFAULTS = {
        method: "GET",
        contentType: "application/json",
        async: true,
        cache: false,
        title: "progress ...",
        loadingDelay: 30
    };

    var settings = $.extend({}, DEFAULTS, args);

    // json object stringify
    if (typeof settings.data === 'object') {
        settings.data = JSON.stringify(settings.data);
    }
    // console.log('restCall', settings.method, url, settings.data);

    var isCompleted = false;
    var timeout = setTimeout(function () {
        !isCompleted && loading.on(settings.title);
    }, settings.loadingDelay);

    $.ajax(PATH + url, settings).done(function (data) {
        if (callback) {
            callback(data);
        }

        clearTimeout(timeout);
        try {
            loading.off();
        } catch (ignore) {
        }
    }).fail(function (jqXHR, textStatus, errorThrown) {
        console.log("restCall fail", url, '\n jqXHR=', jqXHR, '\n textStatus=', textStatus, '\n errorThrown=', errorThrown);
        if (failCallback) {
            failCallback(jqXHR, textStatus, errorThrown);
        } else {
            var errMsg = "";
            if (jqXHR.getResponseHeader('error')) {
                errMsg = 'Message: ' + jqXHR.getResponseHeader('error.message') + "<br>" +
                    'Cause: ' + jqXHR.getResponseHeader('error.cause');
            } else if (jqXHR.responseJSON) {
                errMsg = 'Error: ' + jqXHR.responseJSON.error + '<br>' +
                    'Message: ' + jqXHR.responseJSON.message + '<br>' +
                    'Status: ' + jqXHR.responseJSON.status + '<br>' +
                    'Path: ' + jqXHR.responseJSON.path;
            } else {
                errMsg = 'Error:<br>' + textStatus + "<br>" + errorThrown;
            }
            console.log("errMsg", errMsg);

            var $errorBody = $("<div>", {
                'class': 'overlay-error-body'
            }).append(errMsg);
            loading.on($errorBody);
        }
    }).always(function (data_jqXHR, textStatus, jqXHR_errorThrown) {
        isCompleted = true;
    });
};

function fnTableToExcel(tableId, fileNm, callback) {
    var table = $("#" + tableId);
    if (table && table.length) {
        $("#" + tableId + " strong.th").remove();
        var preserveColors = (table.hasClass('table2excel_with_colors') ? true : false);
        $(table).table2excel({
            exclude: ".noExl",
            name: "Excel Document Name",
            filename: fileNm + '.xls',
            fileext: ".xls",
            exclude_img: true,
            exclude_links: true,
            exclude_inputs: true,
            preserveColors: preserveColors
        });
        if (callback && typeof callback === 'function') {
            callback();
        }
    } else {
        showAlert("warning", "존재하지 않는 테이블 입니다.");
        return false;
    }
}

function getCookie(cookieName) {
    let cookieValue = null;
    if (document.cookie) {
        let array = document.cookie.split((escape(cookieName) + '='));
        if (array.length >= 2) {
            let arraySub = array[1].split(';');
            cookieValue = unescape(arraySub[0]);
        }
    }
    return cookieValue;
}

function fn_KorEngOnly(obj) {
    obj.value = obj.value.replace(/[^가-힣a-zA-Z\s]/g, '');
}

function fn_mapToJson(map) {
    return JSON.stringify(
        Array.from(map.entries(), ([key, value]) =>
            value instanceof Map ? [key, Array.from(value)] : [key, value]
        )
    );
}

function fn_jsonToMap(json) {
    return new Map(
        JSON.parse(json).map(([key, value]) =>
            Array.isArray(value) ? [key, fn_jsonToMap(JSON.stringify(value))] : [key, value]
        )
    );
}

function fn_settingChip(searchJson) {
    let tags = '';
    let searchTypeTags = '';

    let startDate = $('#searchStartDate').val();
    let endDate = $('#searchEndDate').val();
    let target = $('[name=searchTarget]:checked').val();

    if (searchJson) {
        let searchMap = fn_jsonToMap(searchJson);

        if (searchMap.get("searchStartDate") || searchMap.get("searchStartDate")) {
            startDate = searchMap.get("searchStartDate");
            endDate = searchMap.get("searchStartDate");
        }

        if (searchMap.get("searchTarget")) {
            target = searchMap.get("searchTarget");
        }

        if (searchMap.get("searchType")) {
            searchMap.get("searchType").forEach((value, key) => {
                let keyNm = $('.' + key).text();
                searchTypeTags += `
                    <div class="chip">
                        <div class="chip_${key}">${keyNm} : ${value}</div>
                        <button type="button" class="btn_del" onclick="fn_removeChip(this, true)">
                            <span class="blind">삭제</span>
                        </button>
                    </div>
                `;
            });
        }
    }

    if (startDate || endDate) { //템플릿은 기간이 없기때문에 체크
        tags += `
                    <div class="chip">
                        <div>예산집행년월 : ${startDate} ~ ${endDate}</div>
                        <button type="button" class="btn_del" onclick="fn_removeChip(this)">
                            <span class="blind">삭제</span>
                        </button>
                    </div>
                `;
    }
    tags += `
                <div class="chip">
                    <div>대상 : ${target == '1' ? '본부' : '영업점'}</div>
                    <button type="button" class="btn_del" onclick="fn_removeChip(this)">
                        <span class="blind">삭제</span>
                    </button>
                </div>
            `;

    tags += searchTypeTags;

    $('.selected_filter').html(tags);
}

function fn_removeChip(obj, srchTyAt) {
    if (srchTyAt) {
        let findKey = $(obj).siblings('div').attr('class');

        let searchJsonMap = fn_jsonToMap($('#searchJson').val());
        searchJsonMap.get("searchType").delete(findKey.replaceAll('chip_', ''));

        let searchJsonString = fn_mapToJson(searchJsonMap);
        $('#searchJson').val(searchJsonString);
    }

    $(obj).parents('.chip').remove();
    fn_search();
}

/*
    default column {
     예산집행년월 : searchStartDate / searchEndDate
     대상 : searchTarget
     검색어타입 : searchKeyword (enum 사용)
    }

    json 문자열 id : searchJson

    * chip & and 조건 필요 시 사용
    1. 공통 함수명 및 id명 통일
    2. form 내부에 input id=searchJson 태그 생성
 */
function fn_searchConditionSet() {
    //검색조건 json
    //map 값 세팅 또는 업데이트 후 다시 json 문자열로 변환해서 submit

    let searchJsonMap = $('#searchJson').val() ? fn_jsonToMap($('#searchJson').val()) : new Map();

    //default condition
    if ($.trim($('#searchStartDate').val()) || $.trim($('#searchEndDate').val())) {
        searchJsonMap.set("searchStartDate", $('#searchStartDate').val());
        searchJsonMap.set("searchEndDate", $('#searchEndDate').val());
    }

    if ($.trim($('[name=searchTarget]:checked').val())) {
        searchJsonMap.set("searchTarget", $('[name=searchTarget]:checked').val());
    }

    //검색어가 있는 경우
    if ($.trim($('#searchKeyword').val())) {
        let searchTypeMap = new Map();
        if (searchJsonMap.get('searchType')) {
            searchTypeMap = searchJsonMap.get('searchType');
        }
        searchTypeMap.set($('#searchType option:selected').attr('class'), $('#searchKeyword').val());
        searchJsonMap.set("searchType", searchTypeMap);

        $('#searchKeyword').val('');
    }
    //필요 시 코드 수정 및 추가 ...

    let searchJsonString = fn_mapToJson(searchJsonMap);
    $('#searchJson').val(searchJsonString);

    //searchType은 따로 또 전달.
    if (searchJsonMap.get('searchType')) {
        let searchTypeJsonString = fn_mapToJson(searchJsonMap.get('searchType'));
        $('#searchTypeJson').val(searchTypeJsonString);
    }
}

function fn_setMonthPicker(st, ed) {
    st = st ? st : '#searchStartDate';
    ed = ed ? ed : '#searchEndDate';
    let currentYear = (new Date()).getFullYear();
    let startYear = currentYear - 10;
    let options = {
        startYear: startYear,
        finalYear: currentYear,
        pattern: 'yyyy-mm',
        monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
        onSelect: function (month, year) {
            let endDate = $('#searchEndDate').monthpicker('getDate');
            let selectedDate = new Date(year, month - 1);

            if (endDate && endDate < selectedDate) {
                $('#searchStartDate').monthpicker('setValue', {
                    selectedYear: year,
                    selectedMonth: month
                });
            }
        }
    };

    $(st).monthpicker(options);
    $(ed).monthpicker(options);

    fn_stopPropagation();

    $(st).on('monthpicker-click-month', function (e, month) {
        const year = $(this).monthpicker('getDate').getFullYear();
        fn_validateDateRange(new Date(year, month - 1), true);
    });

    $(ed).on('monthpicker-click-month', function (e, month) {
        const year = $(this).monthpicker('getDate').getFullYear();
        fn_validateDateRange(new Date(year, month - 1), false);
    });
}

function fn_stopPropagation() {
    $('.ui-datepicker').on('click', function (e) {
        e.stopPropagation();
    })
}

function fn_validateDateRange(selectedDate, isStart) {
    const startDate = isStart ? selectedDate : $('#searchStartDate').monthpicker('getDate');
    const endDate = isStart ? $('#searchEndDate').monthpicker('getDate') : selectedDate;

    if (!startDate || !endDate) {
        return;
    }

    if (isStart && startDate > endDate) {
        // 시작월이 종료월보다 이후인 경우
        $('#searchStartDate').monthpicker('setValue', {
            dateSeparator: '-',
            pattern: 'yyyy-mm',
            selectedYear: endDate.getFullYear(),
            selectedMonth: endDate.getMonth() + 1,
        });
    } else if (!isStart && startDate > endDate) {
        // 종료월이 시작월보다 이전인 경우
        $('#searchEndDate').monthpicker('setValue', {
            dateSeparator: '-',
            pattern: 'yyyy-mm',
            selectedYear: startDate.getFullYear(),
            selectedMonth: startDate.getMonth() + 1
        });
    }

    // 선택된 날짜 기준으로 disabled 처리
    //fn_updateDisabledMonths(startDate, endDate);
}

/*function fn_updateDisabledMonths(startDate, endDate) {
    // 시작월 선택 시 해당 연도의 종료월 이후의 월들을 disabled
    const startDisabled = [];
    const endDisabled = [];

    const currentYear = startDate.getFullYear();

    if (currentYear === endDate.getFullYear()) {
        // 같은 연도인 경우
        for (let i = 1; i <= 12; i++) {
            if (i > endDate.getMonth() + 1) startDisabled.push(i);
            if (i < startDate.getMonth() + 1) endDisabled.push(i);
        }
    }

    $('#searchEndDate').monthpicker('disableMonths', startDisabled);
    $('#searchStartDate').monthpicker('disableMonths', endDisabled);
}*/