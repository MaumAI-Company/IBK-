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

var Popup = {
    /**
     * 팝업창을 띄운다.
     * @param url
     * @param name '-'글자는 ''으로 바뀜
     * @param width if null, 화면 절반 크기
     * @param height if null, 화면 절반 크기
     * @param positionMethod if null, 화면 가운데. Mouse 마우스 위치.
     * @param specs if null, default is DEFAULT_SPECS
     */
    open: function (url, name, width, height, positionMethod, specs, event) {
        //console.log("[popup] Call popup : ", url, name, width, height, positionMethod, specs, event);
        var windowScreenWidth = window.screen.width,
            windowScreenHeight = window.screen.height;
        var left, top;

        name = name.replace(/-/gi, '');
        width = width || windowScreenWidth / 2;
        height = height || windowScreenHeight / 2;

        if (positionMethod && positionMethod === 'Mouse') {
            left = event.screenX;
            top = event.screenY;
        } else {
            left = (windowScreenWidth - width) / 2;
            top = (windowScreenHeight - height) / 2;
        }
        specs = "width=" + width + ",height=" + height + ",left=" + left + ",top=" + top + "," + (specs || DEFAULT_SPECS);

        var popupWindow = window.open(url, name, specs);
        if (popupWindow) {
            popupWindow.focus();
        }
        return popupWindow;
    },
    openByPost: function (url, name, width, height, positionMethod, specs, event) {
        var popupWindow = Popup.open('', name, width, height, positionMethod, specs, event);

        if ($("#popupFrm").length === 0) {
            $("<form>", {
                id: 'popupFrm',
                method: 'post'
            }).appendTo($("body")).css({
                display: 'none'
            });
        }

        $("#popupFrm").attr({
            "target": name,
            "action": url.split(/[?&]/)[0]
        }).empty().append(
            (function () {
                var hiddenInputs = [];
                url.split(/[?&]/).slice(1).map(function (pair) {
                    return pair.split(/=(.+)?/).slice(0, 2);
                }).forEach(function (pairArray, idx) {
                    console.log(idx, pairArray);
                    hiddenInputs.push(
                        $("<input>", {
                            type: 'hidden',
                            name: pairArray[0]
                        }).val(pairArray[1])
                    );
                });
                return hiddenInputs;
            }())
        ).submit();

        return popupWindow;
    }
};

var Random = {
    get: function (start, end) {
        return Math.random() * (end - start) + start;
    },
    getInteger: function (start, end) { // start부터 end사이의 random 정수 반환
        return Math.round(this.get(start, end));
    },
    getHex: function (start, end) {
        return this.getInteger(start, end).toString(16);
    },
    getBoolean: function () {
        return this.getInteger(1, 2) === 1;
    },
    getFont: function (selectedFont) {
        var GOOGLE_FONTAPI = 'https://fonts.googleapis.com/css?family=',
            GOOGLE_WEBFONTS = ['clipregular', 'Bahiana', 'Barrio', 'Caveat Brush', 'Indie Flower', 'Lobster', 'Gloria Hallelujah', 'Pacifico', 'Shadows Into Light', 'Baloo', 'Dancing Script', 'VT323', 'Acme', 'Alex Brush', 'Allura', 'Amatic SC', 'Architects Daughter', 'Audiowide', 'Bad Script', 'Bangers', 'BenchNine', 'Boogaloo', 'Bubblegum Sans', 'Calligraffitti', 'Ceviche One', 'Chathura', 'Chewy', 'Cinzel', 'Comfortaa', 'Coming Soon', 'Cookie', 'Covered By Your Grace', 'Damion', 'Economica', 'Freckle Face', 'Gochi Hand', 'Great Vibes', 'Handlee', 'Homemade Apple', 'Josefin Slab', 'Just Another Hand', 'Kalam', 'Kaushan Script', 'Limelight', 'Lobster Two', 'Marck Script', 'Monoton', 'Neucha', 'Nothing You Could Do', 'Oleo Script', 'Orbitron', 'Pathway Gothic One', 'Patrick Hand', 'Permanent Marker', 'Pinyon Script', 'Playball', 'Poiret One', 'Rajdhani', 'Rancho', 'Reenie Beanie', 'Righteous', 'Rock Salt', 'Sacramento', 'Satisfy', 'Shadows Into Light Two', 'Source Code Pro', 'Special Elite', 'Tangerine', 'Teko', 'Ubuntu Mono', 'Unica One', 'Yellowtail', 'Aclonica', 'Aladin', 'Allan', 'Allerta Stencil', 'Annie Use Your Telescope', 'Arizonia', 'Berkshire Swash', 'Bilbo Swash Caps', 'Black Ops One', 'Bungee Inline', 'Bungee Shade', 'Cabin Sketch', 'Chelsea Market', 'Clicker Script', 'Crafty Girls', 'Creepster', 'Diplomata SC', 'Ewert', 'Fascinate Inline', 'Finger Paint', 'Fontdiner Swanky', 'Fredericka the Great', 'Frijole', 'Give You Glory', 'Grand Hotel', 'Hanuman', 'Herr Von Muellerhoff', 'Italianno', 'Just Me Again Down Here', 'Knewave', 'Kranky', 'Kristi', 'La Belle Aurore', 'Leckerli One', 'Life Savers', 'Love Ya Like A Sister', 'Loved by the King', 'Merienda', 'Merienda One', 'Modak', 'Montez', 'Mountains of Christmas', 'Mouse Memoirs', 'Mr Dafoe', 'Mr De Haviland', 'Norican', 'Oregano', 'Over the Rainbow', 'Parisienne', 'Petit Formal Script', 'Pompiere', 'Press Start 2P', 'Qwigley', 'Raleway Dots', 'Rochester', 'Rouge Script', 'Schoolbell', 'Seaweed Script', 'Slackey', 'Sue Ellen Francisco', 'The Girl Next Door', 'UnifrakturMaguntia', 'Unkempt', 'Waiting for the Sunrise', 'Walter Turncoat', 'Wire One', 'Yesteryear', 'Zeyada', 'Aguafina Script', 'Akronim', 'Averia Sans Libre', 'Bilbo', 'Bungee Hairline', 'Bungee Outline', 'Cedarville Cursive', 'Codystar', 'Condiment', 'Cormorant Upright', 'Dawning of a New Day', 'Delius Unicase', 'Dorsa', 'Dynalight', 'Eagle Lake', 'Engagement', 'Englebert', 'Euphoria Script', 'Faster One', 'Flamenco', 'Glass Antiqua', 'Griffy', 'Henny Penny', 'Irish Grover', 'Italiana', 'Jolly Lodger', 'Joti One', 'Julee', 'Kenia', 'Kite One', 'Kumar One Outline', 'League Script', 'Lemonada', 'Londrina Outline', 'Lovers Quarrel', 'Meddon', 'MedievalSharp', 'Medula One', 'Meie Script', 'Miniver', 'Molle:400i', 'Monofett', 'Monsieur La Doulaise', 'Montserrat Subrayada', 'Mrs Saint Delafield', 'Mystery Quest', 'New Rocker', 'Nosifer', 'Nova Mono', 'Piedra', 'Quintessential', 'Ribeye', 'Ruthie', 'Rye', 'Sail', 'Sancreek', 'Sarina', 'Snippet', 'Sofia', 'Stalemate', 'Sunshiney', 'Swanky and Moo Moo', 'Titan One', 'Trade Winds', 'Tulpen One', 'UnifrakturCook:700', 'Vampiro One', 'Vast Shadow', 'Vibur', 'Wallpoet', 'Almendra Display', 'Almendra SC', 'Arbutus', 'Astloch', 'Aubrey', 'Bigelow Rules', 'Bonbon', 'Butcherman', 'Butterfly Kids', 'Caesar Dressing', 'Devonshire', 'Diplomata', 'Dr Sugiyama', 'Eater', 'Elsie Swash Caps', 'Fascinate', 'Felipa', 'Flavors', 'Gorditas', 'Hanalei', 'Hanalei Fill', 'Jacques Francois Shadow', 'Jim Nightshade', 'Lakki Reddy', 'Londrina Shadow', 'Londrina Sketch', 'Macondo Swash Caps', 'Miltonian', 'Miltonian Tattoo', 'Miss Fajardose', 'Mr Bedfort', 'Mrs Sheppards', 'Nova Script', 'Original Surfer', 'Princess Sofia', 'Ravi Prakash', 'Ribeye Marrow', 'Risque', 'Romanesco', 'Ruge Boogie', 'Sevillana', 'Sirin Stencil', 'Smokum', 'Snowburst One', 'Underdog'],
            selectedFont = selectedFont || GOOGLE_WEBFONTS[this.getInteger(0, GOOGLE_WEBFONTS.length - 1)];

        var link = document.createElement('link');
        link.rel = 'stylesheet';
        link.href = GOOGLE_FONTAPI + selectedFont;
        document.getElementsByTagName('head')[0].appendChild(link);
        return selectedFont;
    },
    getColor: function (alpha) {
        if (alpha)
            return "rgba(" + this.getInteger(0, 255) + "," + this.getInteger(0, 255) + "," + this.getInteger(0, 255) + "," + (alpha === 'r' ? this.get(0, 1) : alpha) + ")";
        else
            return "#" + this.getHex(0, 255).zf(2) + this.getHex(0, 255).zf(2) + this.getHex(0, 255).zf(2);
    }
};

var LocalStorageItem = {
    set: function (itemName, itemValue) {
        try {
            typeof (Storage) !== "undefined" && localStorage.setItem(itemName, itemValue);
        } catch (ignored) {}
    },
    get: function (itemName, notfoundDefault) {
        if (typeof (Storage) !== "undefined") {
            try {
                var itemValue = localStorage.getItem(itemName);
                if (itemValue !== "undefined") {
                    return itemValue;
                } else {
                    return notfoundDefault;
                }
            } catch (ignored) {}
        } else {
            return notfoundDefault;
        }
    },
    getInteger: function (itemName, notfoundDefault) {
        return parseInt(this.get(itemName, notfoundDefault));
    },
    getBoolean: function (itemName, notfoundDefault) {
        if (notfoundDefault) {
            return this.get(itemName, notfoundDefault.toString()) === 'true';
        } else {
            return this.get(itemName) === 'true';
        }
    }
};

Date.prototype.format = function (f) { // http://stove99.tistory.com/46
    if (!this.valueOf()) return " ";

    var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
    var d = this;

    return f.replace(/(yyyy|yy|MM|dd|E|HH|hh|mm|ss|a\/p)/gi, function ($1) {
        switch ($1) {
            case "yyyy":
                return d.getFullYear();
            case "yy":
                return (d.getFullYear() % 1000).zf(2);
            case "MM":
                return (d.getMonth() + 1).zf(2);
            case "dd":
                return d.getDate().zf(2);
            case "E":
                return weekName[d.getDay()];
            case "HH":
                return d.getHours().zf(2);
            case "hh":
                return ((h = d.getHours() % 12) ? h : 12).zf(2);
            case "mm":
                return d.getMinutes().zf(2);
            case "ss":
                return d.getSeconds().zf(2);
            case "a/p":
                return d.getHours() < 12 ? "am" : "pm";
            default:
                return $1;
        }
    });
};
String.prototype.string = function (len) {
    var s = '',
        i = 0;
    while (i++ < len) {
        s += this;
    }
    return s;
};
String.prototype.zf = function (len) {
    return "0".string(len - this.length) + this;
};
Number.prototype.zf = function (len) {
    return this.toString().zf(len);
};

Number.prototype.toBlank = function () {
    return this == 0 ? "" : this;
};
Number.prototype.withComma = function () {
    return this.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};
Number.prototype.toDate = function (pattern) {
    return DateUtils.getDate(this).format(pattern);
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

var StringUtils = {
    isBlank: function (str) {
        return str == null || $.trim(str) === '';
    },
    isNotBlank: function (str) {
        return !StringUtils.isBlank(str);
    },
    defaultIfBlank: function (str, def) {
        return StringUtils.isBlank(str) ? def : str;
    },
    concatIfNotBlank: function (str1, sep, str2) {
        if (StringUtils.isBlank(str1) && StringUtils.isBlank(str2)) {
            return '';
        }
        return str1 + sep + str2;
    },
    /**
     *  금액 fommat
     */
    setComma: function (value) {
        var reg = /(^[+-]?\d+)(\d{3})/;
        var n = value;
        while (reg.test(n)) {
            n = n.toString().replace(reg, '$1' + ',' + '$2');
        }
        return n;
    }
}

var KB = 1024,
    MB = KB * KB,
    GB = MB * KB,
    TB = GB * KB;
var File = {
    formatSize: function (length, unit, digits) {

        if (unit) {
            if (typeof digits === 'undefined')
                digits = 1;
            if (unit === 'MB') {
                return (length / MB).toFixed(digits) + " MB";
            } else if (unit === 'GB') {
                return (length / GB).toFixed(digits) + " GB";
            } else if (unit === 'TB') {
                return (length / TB).toFixed(digits) + " TB";
            }
        } else {
            if (length < KB)
                return length + " B";
            else if (length < MB)
                return (length / KB).toFixed(0) + " kB";
            else if (length < GB)
                return (length / MB).toFixed(1) + " MB";
            else if (length < TB)
                return (length / GB).toFixed(1) + " GB";
            else
                return (length / TB).toFixed(2) + " TB";
        }
    }
};

var reqParam = location.search.split(/[?&]/).slice(1).map(function (paramPair) {
    return paramPair.split(/=(.+)?/).slice(0, 2);
}).reduce(function (obj, pairArray) {
    obj[pairArray[0]] = pairArray[1];
    return obj;
}, {});

$.urlParam = function (name) {
    var results = new RegExp('[\?&amp;]' + name + '=([^&amp;#]*)').exec(window.location.href);
    return results ? results[1] || "" : "";
}



function Loading() {
    $("body").append(
        $("<div>", {
            id: 'wrap_overlay'
        }).append(
            $("<div>", {
                id: 'overlay'
            }).append(
                $("<div>", {
                    class: 'spinner-grow'
                }),
                $("<div>", {
                    id: 'overlay_body'
                })
            ).css({
                position: 'absolute',
                top: '50%',
                left: '50%',
                transform: 'translate(-50%, -50%)',
                textAlign: 'center'
            })
        ).css({
            position: 'fixed',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundColor: 'rgba(0, 0, 0, .25)',
            display: 'none'
        })
    );

    $("html").on("click", "#wrap_overlay", function () {
        loading.off();
    });
    console.log('Loading.init');
};
Loading.prototype = {
    on: function (body) {
        $("#wrap_overlay").show();
        $("#overlay_body").empty().append(body);
        //			console.log('Loading.on', body);
    },
    off: function () {
        $("#wrap_overlay").hide();
        $("#overlay_body").empty();
        //			console.log('Loading.off');
    }
};

var loading;
$(function () {
    loading = new Loading();
});

$.fn.serializeObject = function () {
    "use strict"
    var result = {};
    var extend = function (i, element) {
        var node = result[element.name];
        if ('undefined' !== typeof node && node !== null) {
            if ($.isArray(node)) {
                node.push(element.value);
            } else {
                result[element.name] = [node, element.value];
            }
        } else {
            result[element.name] = element.value;
        }
    }

    $.each(this.serializeArray(), extend);
    return result;
};

$.isBlank = function (str) {
    if (str == null) {
        return true;
    } else if (str === '') {
        return true;
    } else if ($.trim(str) === '') {
        return true;
    }
    return false;
}

function validateAndGetFormObject(selector, requiredFieldnames) {
    var formObject = $(selector).serializeObject();
    // console.log('formObject', formObject);

    if (!$.isEmptyObject(requiredFieldnames)) {
        $.each(requiredFieldnames, function (i, name) {
            console.log(i, name, formObject[name], $.isBlank(formObject[name]));
            if ($.isBlank(formObject[name])) {
                $(selector).find("[name='" + name + "']").addClass('border-danger');
                throw new Error(errorTitle + ' ' + errorMsg);
            }
        });
    }

    return formObject;
}

function makeToast(title, message) {
    return $("<div>", {
        class: 'toast',
        'data-autohide': 'false'
    }).append(
        $("<div>", {
            class: 'toast-header'
        }).append(
            $("<strong>", {
                class: 'mr-auto text-danger'
            }).html(title),
            $("<button>", {
                type: 'button',
                class: 'ml-2 mb-1 close',
                'data-dismiss': 'toast'
            }).html('&times;')
        ),
        $("<div>", {
            class: 'toast-body'
        }).html(message)
    ).toast('show');
}

var equalsIgnoreCase = function (str1, str2) {
    if (str1 == null || str2 == null) {
        return false;
    }
    return str1.toLowerCase() === str2.toLowerCase();
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
        } catch (ignore) {}
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

/**
 * replaceAll 추가
 */
String.prototype.replaceAll = function (searchStr, replaceStr) {
    return this.split(searchStr).join(replaceStr);
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

// 순차 정렬
function fnSortAscChg(items, field) {
    items.sort(function (a, b) {
        return (a[field] < b[field]) ? -1 : (a[field] > b[field]) ? 1 : 0;
    });
    return items;
}

//역순차 정렬
function fnSortDescChg(items, field) {
    items.sort(function (a, b) {
        return (a[field] > b[field]) ? -1 : (a[field] < b[field]) ? 1 : 0;
    });
    return items;
}

//목록리스트 페이지로 이동
function fnMoveMainPage() {
    location.href = '/uws/html/isis/isis_index.html';
}

//문자길이 byte체크
function fnGetByteLengthb(str) {
    var byte = 0;
    for (var i = 0; i < str.length; ++i) {
        // 한글,특수문자 2byte || 영문, 숫자, 기호 1byte
        (str.charCodeAt(i) > 127) ? byte += 2: byte++;
    }
    return byte;
}

//공통 fail CallBack
function fnFailCallback(jqXHR, textStatus, errorThrown) {
    var failLoading = new Loading();
    var errMsg = '대상시스템에 요청이 많아 응답이 지연되고 있습니다.<br/>';
    errMsg += '잠시 후 다시 시도해주세요.<br/>';
    if (jqXHR.responseJSON.returnMsg[0].ERROR != null && jqXHR.responseJSON.returnMsg[0].ERROR.length != 3) {
        alert(jqXHR.responseJSON.returnMsg[0].ERROR);
        location.reload();
    } else {
        var $errorBody = $("<div>", {
            'class': 'overlay-error-body'
        }).append(errMsg);
        var header = $('.conts-head');
        $('.contents').empty();
        $('.contents').append(header);
        failLoading.on($errorBody);
    }
}

//공통 fail CallBack 페이지 유지 에러메세지만 출력
function fnFailCallbackHoldingView(jqXHR, textStatus, errorThrown) {
    var failLoading = new Loading();
    var errMsg = '대상시스템에 요청이 많아 응답이 지연되고 있습니다.<br/>';
    errMsg += '잠시 후 다시 시도해주세요.<br/>';
    if (jqXHR.responseJSON.returnMsg[0].ERROR != null && jqXHR.responseJSON.returnMsg[0].ERROR.length != 3) {
        alert(jqXHR.responseJSON.returnMsg[0].ERROR);
    } else {
        var $errorBody = $("<div>", {
            'class': 'overlay-error-body'
        }).append(errMsg);
        var header = $('.conts-head');
        $('.contents').empty();
        $('.contents').append(header);
        failLoading.on($errorBody);
    }
}

/** string/number에 맞는 기본 포멧세팅
 * @param type
 * @param id
 * @param rpc
 * @returns
 */
function fnConvertFormmat(type, id, rpc, def) {
    var result;
    if (type == 'string') {
        if ($('#' + id).val() != null || $('#' + id).val() == undefined) {
            result = nvl(String($('#' + id).val().replaceAll(rpc, '').trim()), def);
        } else {
            result = def
        }
    } else if (type == 'number') {
        if ($('#' + id).val() != null || $('#' + id).val() == undefined) {
            result = nvl(Number($('#' + id).val().replaceAll(rpc, '').trim()), def);
        } else {
            result = def
        }
    } else {
        result = '';
    }
    return result;
}

/**
 * form hidden 동기화
 * @param id
 * @param value
 * @returns
 */
function fnSyncFormSetValue(id, value) {
    $('#' + id).val(value);
}

function printBndno(no) {
    return no.replace(/([0-9]{3})([0-9]{3})([0-9]+)?/, "$1-$2-$3");
}

function printComma(no) {
    return no.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

//array.LookUp(매칭값key,매칭값,반환값key)
Array.prototype.LookUp =  function (key, value, column) {
	var result = '';
	var map = this.find((item, index) => item[key] == value);
	if (map == undefined || map == null) {
		result = '';
	} else {
		result = map[column];
	}
	return result;
};