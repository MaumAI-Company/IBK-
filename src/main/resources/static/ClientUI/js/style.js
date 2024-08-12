$(document).ready(function(){
	//aside toggle
	$('.btn_toggle_aside').on('click', function(){
		$('body').toggleClass('aside_hide');
	});
	$('#aside nav').mouseenter(function () {
		$(this).addClass('hover');
	}).mouseleave(function () {
		$(this).removeClass('hover');
	});
	$('#aside nav').focusin(function () {
		$(this).addClass('hover');
	}).focusout(function () {
		$(this).removeClass('hover');
	});

	//lnb
	$('#aside .nav li').each(function(){
		if($(this).hasClass('active')){
			$(this).children('ul').show();
		}

		$(this).children('a').on('click', function(){
			if($(this).not(':only-child')){
				$(this).next('ul').stop().slideToggle().parent('li').toggleClass('active').siblings().removeClass('active').children('ul').stop().slideUp();
			}
		});
	});

	//필터 선택
	$('.btn_filter').on('click', function(){
		var btnFilter = $(this),
			filterArea = btnFilter.parent('.filter_area');
		filterArea.toggleClass('active');
		if(filterArea.hasClass('active')){
			btnFilter.addClass('active');
		}else{
			if($('.selected_filter').length === 0 || $('.selected_filter').children().length === 0){
				btnFilter.removeClass('active');
			}
		}
	});
	//필터 영역 제외하고 클릭 시 필터 영역 닫힘
	$(document).on('click', function (e) {
		if(!$(e.target).parents('div').hasClass('filter_area')){
			$('.filter_area').removeClass('active');

			if($('.selected_filter').length === 0 || $('.selected_filter').children().length === 0){
				$('.btn_filter').removeClass('active');
			}
		}
	});

	//상단으로 스크롤
	$('.btn_top').on('click', function(){
		$('html, body').stop().animate({'scrollTop':0},500);
	});

	//input
	inputActive();

	//전체 선택
	checkAll();

	//기간 선택 버튼
	var d = new Date(), week = new Date(), month = new Date(), threeMonth = new Date();
	$('.term_form').each(function(){
		var termForm = $(this);
		termForm.find('.btn').each(function(){
			var btn = $(this);
			btn.on('click', function(){
				termForm.children('.datepicker').eq(1).val(d.toLocaleDateString('fr-CA'));
				if(btn.hasClass('btn_today')){ //오늘
					termForm.children('.datepicker').eq(0).val(d.toLocaleDateString('fr-CA'));
				}else if(btn.hasClass('btn_1week')){ //1주일
					week.setDate(week.getDate() - 7);
					termForm.children('.datepicker').eq(0).val(week.toLocaleDateString('fr-CA'));
				}else if(btn.hasClass('btn_1month')){ //최근 1개월
					month.setMonth(month.getMonth() - 1);
					termForm.children('.datepicker').eq(0).val(month.toLocaleDateString('fr-CA'));
				}else if(btn.hasClass('btn_3month')){ //최근 3개월
					threeMonth.setMonth(threeMonth.getMonth() - 3);
					termForm.children('.datepicker').eq(0).val(threeMonth.toLocaleDateString('fr-CA'));
				}
			});
		});
	});

	//select
	niceSelect();

	//textarea
	$('textarea').each(function () {
		$(this).on('focusin', function() {
			$(this).parents('.scroll-textarea').addClass('active');
		}).on('focusout', function() {
			$(this).parents('.scroll-textarea').removeClass('active');
		});
	});

	//tab
	$('.tab > li').each(function () {
		var tabLi = $(this),
			tabLiIdx = tabLi.index();
		tabLi.find('a').on('click', function () {
			tabLi.addClass('active').siblings().removeClass('active');
			tabLi.parent('.tab').siblings('.tab_cont').children('.cont').eq(tabLiIdx).addClass('active').siblings().removeClass('active');
		});
	});

	//Jquery Scrollbar
	$('textarea, .pop_layer .pop_cont').each(function(){
		if(!$(this).hasClass('noscroll')){
			$(this).scrollbar();
		}
	});

	//Snackbar 닫기
	$('.snackbar').each(function () {
		var snackbar = $(this);
		snackbar.find('.btn_close').on('click', function(){
			snackbar.removeClass('active');
		});
	});

	//레이어 팝업 딤 클릭 시 팝업 닫기
	$('.pop_layer').each(function(){
		$(this).children('.dim').on('click', function(){
			$(this).parent('.pop_layer').removeClass('active');
			$('html,body').removeAttr('style');
		});
	});

	resize(); scroll();

	$(window).scroll(function(){
		scroll();
	});

	$(window).resize(function(){
		resize();
	});
});

//nice select
function niceSelect(){
	$('select').niceSelect();

	$('.nice-select').each(function() {
		var niceSelect = $(this),
			select = niceSelect.prev('select');
		//placeholder
		if(niceSelect.find('ul.list li:first-child').hasClass('selected disabled')){
			niceSelect.addClass('placeholder');
		}else{
			niceSelect.removeClass('placeholder');
		}
		select.on('change', function () {
			niceSelect.removeClass('placeholder');
		});

		//list scrollbar
		niceSelect.on('click', function () {
			var list = niceSelect.find('ul.list li'),
				listSelectIdx = niceSelect.find('ul.list li.selected').index(),
				listTop = 0;

			if (listSelectIdx < 3) {
				list.parent('ul.list').animate({
					scrollTop: 0
				}, 1);
			} else {
				setTimeout(function () {
					for (var i = 0;i < listSelectIdx - 2;i++){
						listTop += list.eq(i).outerHeight();
					}
					list.parent('ul.list').animate({
						scrollTop: listTop
					}, 1);
				}, 10)
			}
		});

		//keyboard scroll
		niceSelect.on('keyup keydown',(function(e){
			var key = e.keyCode,
				niceUl = niceSelect.find('.list'),
				niceLi = niceSelect.find('.list li:not(.disabled)'),
				focusLi = niceSelect.find('.list li.focus:not(.disabled)');
			if(niceLi.length > 5){
				var scrollNum = 0
				if(focusLi.index() < 3){
					scrollNum = 0
				}else{
					scrollNum = focusLi.index()-2
				}

				if (key == 40 || key == 38) { // 40 = Down, 38 = Up
					if(key == 40){
						niceUl.stop().animate({
							scrollTop : niceLi.outerHeight() * scrollNum
						},20)
					}
					niceUl.stop().animate({
						scrollTop : niceLi.outerHeight() * scrollNum
					},100)
				}
			}
		}));
	});
}

//input
function inputActive(){
	$('.input, .search').each(function () {
		var divInp = $(this),
			inp = divInp.find('input');

		//input에 텍스트 입력 시 삭제 버튼 활성화
		inp.bind('keyup focusin', function () {
			if (inp.val() != '') {
				divInp.addClass('active');
			} else {
				divInp.removeClass('active');
			}
		}).on('focusout', function () {
			divInp.removeClass('active');
		});

		if (divInp.hasClass('success') || divInp.hasClass('error')) {
			inp.bind('keyup', function () {
				divInp.removeClass('success error');
			});
		}

		//삭제 버튼 focus 안되도록
		divInp.find('.btn_del').attr('tabindex','-1');

		//삭제 버튼 클릭 시 input 내용 클리어
		divInp.on('mousedown', '.btn_del', function () {
			divInp.removeClass('active').find('input').val('');
		});
	});

	//input number comma
	$('.comma').inputmask('numeric', {
		autoGroup: true,
		groupSeparator: ',',
	});
	$('.comma').keyup(function(){
		var commaNum = $(this).prop('value');
		if(commaNum.match('-')){
			$(this).prop('value','');
		}
	});

	//input datepicker form
	/*$('.datepicker').inputmask({
		mask: '9999.99.99',
		showMaskOnHover: false,
		showMaskOnFocus: false,
	});*/

	//input 한글만
	$(document).on("keyup", "input.only_kor", function() {
		var pattern = /[a-z0-9]|[ \[\]{}()<>?|`~!@#$%^&*-_+=,.;:\"'\\]/g;
		this.value = this.value.replace(pattern, '');
	});

	//input 영어만
	$(document).on("keyup", "input.only_eng", function() {
		var pattern =  /[^A-Za-z\s]/ig;
		this.value = this.value.replace(pattern, '');
	});
}

//checkbox all
function checkAll(){
	$('.all_chk').each(function () {
		var allChk = $(this),
			chkId = allChk.attr('name'),
			chk = $('input[id^='+chkId+']:not([disabled])'),
			chkNum = chk.length,
			idx = 0;
		allChk.on('change', function () {
			if($(this).is(':checked')){
				idx = chkNum;
				chk.prop('checked',true);
			}else{
				idx = 0;
				chk.prop('checked',false);
			}
		});
		chk.on('change',function(){
			if($(this).is(':checked')){
				idx++;
				if(idx == chkNum){
					allChk.prop('checked',true);
				}
			}else{
				idx--;
				allChk.prop('checked',false);
			}
		});
	});
}

function scroll(){
	var sct = $(window).scrollTop();

	if(sct > 0){
		$('#header').addClass('fixed');
	}else{
		$('#header').removeClass('fixed');
	}
}

function resize(){
	var winW = $(window).outerWidth(),
		winH = $(window).outerHeight();

	if(winW >= 1024){ //pc
	}else{ //tablet & mobile
	}

}

//개발용 common js 에 있는 소스(실 작업시 중복될 수 있음) : s
function fn_setDatePicker(selector, option) {
	if (!option) {
		option = {};
	}
	option.timepicker = false;
	option.format = 'Y.m.d';
	option.scrollMonth = false;
	option.scrollInput = false;
	$(selector).datetimepicker(option);
}
function fn_setDateTimePicker(selector, option) {
	if (!option) {
		option = {};
	}
	option.format = 'Y.m.d H:i';
	option.scrollMonth = false;
	$(selector).datetimepicker(option);
}
//개발용 common js 에 있는 소스 : e

//파일 다운로드
function downloadURI(uri, name){
	var link = document.createElement("a");
	link.setAttribute('download', name);
	link.href = uri;
	document.body.appendChild(link);
	link.click();
	link.remove();
}

//글자수 제한
function maxLengthCheck(object){
	if(object.value.length > object.maxLength){
		object.value = object.value.slice(0, object.maxLength);
	}
}

//레이어 팝업 열기
function layerShow(ele,obj){
	var winH = $(window).height(),
		popCont = $('#'+ele).not('.system_alert').find('.pop_cont').not('.noscroll');
	$('html, body').css({'overflow':'hidden'});
	$('#'+ele).addClass('active');

	//Focus
	if($('#'+ele).hasClass('pop_layer')){
		$(obj).attr('data-focus','on');
	}else{
		$(obj).attr('data-focus','alert');
	}
	$('#'+ele).attr('tabindex','0').focus();

	//팝업 열때 콘텐츠 스크롤바 추가
	/*if(popCont.parent('div').hasClass('w1280')){
		popCont.scrollbar();
	}*/

	popCont.find('.tbl_scrollbar').each(function () {
		if(!$(this).hasClass('noscroll')){
			$(this).scrollbar();
		}
	});
}
//레이어 팝업 닫기
function layerHide(ele){
	$('#'+ele).removeClass('active');
	if(!$('.pop_layer, .system_alert').hasClass('active')){
		$('html, body').removeAttr('style');
	}

	//Focus
	if($('#'+ele).hasClass('pop_layer')){
		$('[data-focus~=on]').focus();
		window.setTimeout(function(){
			$('[data-focus~=on]').removeAttr('data-focus');
		});
	}else{
		$('[data-focus~=alert]').focus();
		window.setTimeout(function(){
			$('[data-focus~=alert]').removeAttr('data-focus');
		});
	}
	$('#'+ele).removeAttr('tabindex','0');
}

//윈도우 팝업
function winPop(url, title, w, h){
	var top = (screen.height/2)-(h/2),
		left = (screen.width/2)-(w/2);
	return window.open(url, title, 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width='+w+', height='+h+', top='+top+', left='+left);
}

//Toast & Snackbar
function systemMsg(ele){
	$('#'+ele).addClass('active');

	//Toast 3초 뒤 숨김
	var toast = setTimeout(function () {
		if($('#'+ele).hasClass('toast')){
			$('#'+ele).removeClass('active');
			clearTimeout(toast);
		}
	}, 3000);
}

function  fn_setDatePickerTime(selector) {
	$(selector).daterangepicker({
		"singleDatePicker": true,
		"timePicker": true,
		"timePickerIncrement": 10,
		"locale": {
			format: 'YYYY-MM-DD HH:mm:ss',
			separator: ' ~ ',
			applyLabel: '확인',
			cancelLabel: '닫기',
			daysOfWeek: ['일', '월', '화', '수', '목', '금', '토'],
			monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
			firstDay: 1,
		},
		"drops": "auto",
		"timePickerIncrement": 1,
	});
}