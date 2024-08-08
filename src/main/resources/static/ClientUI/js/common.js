// 공통 js

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
    } catch(e) {
        console.log(e);
    }
}

function setEditor(name, editor) {
    contentEditor = editor;
}
// Ckeditor4 END

/* example
 *   icon : 경고 - exclamation | 채크 - check | 질문 - question
 *   showAlert('check', '저장 되었습니다', function() {
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

    $('.btn_close').click(function() {
        if (callbackFunc && typeof callbackFunc === 'function') {
            callbackFunc();
        }
        layerHide('alert');
        $('#alert').remove();
    });
}

/* example
 *   icon : 경고 - exclamation | 채크 - check | 질문 - question
 *   showConfirm('check', '저장 하시겠습니까?', function() {
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

    $('#dismissBtn').click(function() {
        if (dismissFunc && typeof dismissFunc === 'function') {
            dismissFunc();
        }
        layerHide('confirm');
        $('#confirm').remove();
    });

    $('#confirmBtn').click(function() {
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

    $(`#${selector}`).each(function(i) {

        let fileObj = $(this)[0].files[0];
        let maxSize = 10 * 1024 * 1024; // 1MB

        let filename = fileObj.name;
        let fileType = fileObj.type;
        let allowedExtensions = /\.(jpg|jpeg|png|)$/i; // 허용되는 파일 확장자
        let fileSize = fileObj.size;

        if (!allowedExtensions.exec(filename) || !["image/jpeg", "image/png", "image/jpg"].includes(fileType)) {
            showAlert('alert', '유효하지 않은 파일 유형입니다.');
            retVal =  false;
        }

        if (fileSize > maxSize) {
            showAlert('alert', '파일사이즈가 10MB가 넘습니다.');
            retVal =  false;
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

    if(!regExp.test(pwd)) {
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
function fn_openAdresPop(prefix, callback){
    daum.postcode.load(function (){
        new daum.Postcode({
            oncomplete: function(data) {
                let zip = data.zonecode;

                let adres = '';
                if (data.userSelectedType === 'R'){
                    // 도로명 주소 선택 시
                    adres = data.roadAddress;
                } else {
                    // 지번 주소 선택 시
                    adres = data.jibunAddress;
                }

                if (!prefix){
                    $('#zip').val(zip);
                    $('#adres').val(adres);
                    $('#dtlAdres').focus();
                } else {
                    $('#'+prefix+'Zip').val(zip);
                    $('#'+prefix+'Adres').val(adres);
                    $('#'+prefix+'DtlAdres').focus()
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