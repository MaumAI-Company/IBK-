$(document).ready(function(){
	//aside toggle
	$('.btn_toggle_aside').on('click', function(){
		$('body').toggleClass('aside_hide');
	});

	//lnb
	$('#aside .nav li').each(function(){
		if($(this).hasClass('active')){
			$(this).children('ul').show();
		}

		$(this).children('a').on('click', function(){
			if($(this).not(':only-child')){
				$('body').removeClass('aside_hide');
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

	//input
	inputActive();

	//전체 선택
	checkAll();

	//select
	niceSelect();

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
	var winH = $(window).height();

	$('html, body').css({'overflow':'hidden'});
	$('#'+ele).addClass('active');

	//Focus
	if($('#'+ele).hasClass('pop_layer')){
		$(obj).attr('data-focus','on');
	}else{
		$(obj).attr('data-focus','alert');
	}
	$('#'+ele).attr('tabindex','0').focus();
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