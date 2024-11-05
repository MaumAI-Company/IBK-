$(function() {
	$('.file_form').each(function () {
		if(!$(this).hasClass('view')) {
			var $file = this.querySelector(".input_file");
			var dropZone = this.querySelector(".drop_zone");
			var dropZoneArea = dropZone.querySelector(".area");

			var toggleClass = function (className) {

				console.log("current event: " + className);

				var list = ["dragenter", "dragleave", "dragover", "drop"];

				for (var i = 0; i < list.length; i++) {
					if (className === list[i]) {
						dropZone.parentNode.classList.add(list[i]);
					} else {
						dropZone.parentNode.classList.remove(list[i]);
					}
				}
			}

			var showFiles = function (files) {
				if(!$file.attributes['multiple']){
					dropZoneArea.innerHTML = "";
				}else{
					if(dropZone.parentNode.classList.contains('upload')){
						dropZoneArea.querySelectorAll('.file_add').forEach((elem) => elem.remove());
					}
				}

				for (var i = 0, len = files.length; i < len; i++) {
					var ext = files[i].name.split('.').pop().toLowerCase(),
						size = Math.ceil(files[i].size / 1024) + 'kb';
					dropZoneArea.innerHTML += "<div class='file file_" + ext + " file_add'><p class='file_name'>" + files[i].name + "</p><p class='file_size'>" + size + "</p><button type='button' class='btn_del' data-id='"+i+"'><span class='blind'>파일 삭제</span></button></div>";
					dropZone.parentNode.classList.add('upload');
				}
			}

			var selectFile = function (files) {
				// input file 영역에 드랍된 파일들로 대체
				$file.files = files;
				showFiles($file.files);
			}

			if ($file != null) {
				$file.addEventListener("change", function (e) {
					showFiles(e.target.files);
				});
			}

			if (dropZone != null) {
				// 드래그한 파일이 최초로 진입했을 때
				dropZone.addEventListener("dragenter", function (e) {
					e.stopPropagation();
					e.preventDefault();

					toggleClass("dragenter");

				});

				// 드래그한 파일이 dropZone 영역을 벗어났을 때
				dropZone.addEventListener("dragleave", function (e) {
					e.stopPropagation();
					e.preventDefault();

					toggleClass("dragleave");

				});

				// 드래그한 파일이 dropZone 영역에 머물러 있을 때
				dropZone.addEventListener("dragover", function (e) {
					e.stopPropagation();
					e.preventDefault();

					toggleClass("dragover");

				});

				// 드래그한 파일이 드랍되었을 때
				dropZone.addEventListener("drop", function (e) {
					e.preventDefault();

					toggleClass("drop");

					var files = e.dataTransfer && e.dataTransfer.files;
					console.log(files)

					if (files != null) {
						if (files.length < 1) {
							showAlert("warning", "폴더 업로드 불가");
							return
						}
						selectFile(files);
					} else {
						showAlert("warning", "ERROR");
					}

				});
			}

			//파일 삭제
			$(document).on('click', function (e) {
				if ($(e.target).hasClass('btn_del')) {
					var agent = navigator.userAgent.toLowerCase(),
						btnFileDel = $(e.target),
						fileForm = btnFileDel.parents('.file_form'),
						fileInput = fileForm.find('.input_file');
					btnFileDel.parent('.file').remove();

					if ((navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1)) { //ie
						fileInput.replaceWith(fileInput.clone(true));
					} else {//other browser
						fileInput.val('');
					}
					if(fileForm.find('.drop_zone .area > .file').length == 0){
						fileForm.removeClass('upload data');
					}
				}
			});
		}
	});
});


/**
 * 파일 유효성 검사(멀티)
 * addExt : 배열[] 형태로 추가 검증할 확장자
 * @param files
 * @returns {boolean}
 */
function validMultiFile(files, addExt) {
	let retVal = true;

	// addExt가 null 또는 undefined일 경우 빈 배열로 처리
	addExt = addExt || [];
	let additionalExtensions = addExt.map(ext => ext.replace('.', '')).join('|');
	let allowedExtensions = new RegExp(`\\.(jpg|jpeg|png|${additionalExtensions})$`, 'i'); // 허용되는 파일 확장자
	let allowedMimeTypes = ["image/jpeg", "image/png", "image/jpg"];

	// 경고 메시지에 포함할 확장자 문자열 생성
	let allowedExtMessage = 'jpg, jpeg, png' + (addExt.length > 0 ? ', ' + addExt.join(', ') : '');

	for (let i = 0; i < files.length; i++) {
		let fileObj = files[i];
		let maxSize = 10 * 1024 * 1024; // 10MB

		let filename = fileObj.name;
		let fileType = fileObj.type;
		let fileSize = fileObj.size;

		if (!allowedExtensions.exec(filename) || !allowedMimeTypes.includes(fileType)) {
			showAlert('warning', `허용되지 않는 확장자입니다.<br>${allowedExtMessage} 파일만 첨부 가능합니다.`);
			retVal = false;
		}

		if (fileSize > maxSize) {
			showAlert('warning', '파일사이즈가 10MB가 넘습니다.');
			retVal = false;
		}
	}

	return retVal;
}