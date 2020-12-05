/**
 *  util方法
 * @author MrLu
 * @createTime  2020/7/10 9:25
 * @version 1.3
 */
var utils = {
    /**
     * 判断空
     * （字符串empty也算空！）
     * @param str
     * @returns {boolean}
     */
    isEmpty: function (str) {
        return typeof (str) == 'undefined' || !str || str === "" || str === "empty";

    },
    /**
     * 判断非空
     * @param str
     * @returns {boolean}
     */
    isNotEmpty: function (str) {
        return !utils.isEmpty(str);
    },
    /**
     * 获取cookie
     */
    getCookie: function (name) {
        return $.cookie(name);
    },
    /**
     * 获取url中参数
     */
    getUrlPar: function (name) {
        let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        let r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    },
    /**
     * 格式化时间
     */
    timeFormat: {
        /**
         * 时间戳转换成 yyyy-mm-dd hh24:mi:ss
         * @param timestamp
         * @returns {*}
         */
        timestampToDate: function (timestamp) {
            //这里然会undefined而不是null  null值在js中很蠢并不好处理 而undefined可以用typeof完美判断
            if (!timestamp) return undefined;
            let date = new Date(timestamp);
            let y = date.getFullYear(),
                m = date.getMonth() + 1,
                d = date.getDate();
            return y + "-" + (m < 10 ? "0" + m : m) + "-" + (d < 10 ? "0" + d : d) + " " + date.toTimeString().substr(0, 8);
        },
        /**
         * 时间戳转成  yyyy-mm-dd
         * @param timestamp
         * @returns {*}
         */
        timestampToDate2: function (timestamp) {
            //这里然会undefined而不是null  null值在js中很蠢并不好处理 而undefined可以用typeof完美判断
            if (!timestamp) return undefined;
            let date = new Date(timestamp);
            let y = date.getFullYear(),
                m = date.getMonth() + 1,
                d = date.getDate();
            return y + "-" + (m < 10 ? "0" + m : m) + "-" + (d < 10 ? "0" + d : d);
        },
        yyyyMMddToYMD: function (dateStr) {
            if (!dateStr) return undefined;
            let y = dateStr.substr(0, 4) + "年",
                m = dateStr.substr(4, 2) + "月",
                d = dateStr.substr(6, 2) + "日";
            return y + m + d;

        },
        yyyyMMddtoTMD2:function(dateStr){
            if (!dateStr) return '';
            let y = dateStr.substr(0, 4) + "年",
                m = dateStr.substr(5, 2) + "月",
                d = dateStr.substr(8, 2) + "日";
            return y + m + d;
        },
        yyyyMMddhhmissToYMDhms: function (dateStr) {
            if (!dateStr) return undefined;
            let y = dateStr.substr(0, 4) + "年",
                m = dateStr.substr(4, 2) + "月",
                d = dateStr.substr(6, 2) + "日",
                h = dateStr.substr(8, 2) + ":",
                mi = dateStr.substr(10, 2) + ":",
                s = dateStr.substr(12, 2);
            return y + m + d + " " + h + mi + s;

        },
        getYear(timestamp) {
            if (!timestamp) return undefined;
            let date = new Date(timestamp);
            return date.getFullYear();
        }
    },
    /**
     * 获取某种类型的时间
     * @author MrLu
     * @createTime  2020/8/14 14:59
     */
    getDate: {
        //
        /**
         * @author MrLu
         * @param theMonth 月份 不用+1
         * @createTime  2020/5/27 21:13
         * @describe 获取一个月份属于哪个季度的
         * @version 1.0
         */
        getQuarter: function (theMonth) {
            if (!theMonth) {
                let date = new Date;
                theMonth = date.getMonth() + 1;
            }
            let quarter = 0;
            if (theMonth < 4) {
                quarter = 1;
            }
            if (3 < theMonth && theMonth < 7) {
                quarter = 2;
            }
            if (6 < theMonth && theMonth < 10) {
                quarter = 3;
            }
            if (theMonth > 9) {
                quarter = 4;
            }
            return quarter;
        },
        /**
         * @author MrLu
         * @param  quarter 季度  1 2 3 4
         * @createTime  2020/5/27 21:13
         * @describe 获取季度的结束月
         * @version 1.0
         */
        getQuarterEndMouth: function (quarter) {
            if (!quarter) {
                quarter = utils.getDate.getQuarter();
            }
            let quarterEndMonth = 0;

            switch (parseInt(quarter)) {
                case 1:
                    quarterEndMonth = 3;
                    break;
                case 2:
                    quarterEndMonth = 6;
                    break;
                case 3:
                    quarterEndMonth = 9;
                    break;
                case 4:
                    quarterEndMonth = 12;
                    break;
                default:
                    throw new Error('错误的季度！ 季度为 1 2 3 4 ');

            }
            return quarterEndMonth;
        },
        getLastOfMonth(month) {
            let date = new Date();
            let nextMonth = ++month;
            let nextMonthFirstDay = new Date(date.getFullYear(), nextMonth, 1);
            let oneDay = 1000 * 60 * 60 * 24;
            return utils.timeFormat.timestampToDate2(new Date(nextMonthFirstDay - oneDay));
        }

    },

    /**
     * 创建元素标签
     * @author MrLu
     * @createTime  2020/7/20 10:53
     * @version 1.0
     */
    createElement: {
        /**
         * svg用的标签
         * @param tag
         * @param attrs
         * @returns {HTMLElement | SVGAElement | SVGCircleElement | SVGClipPathElement | SVGComponentTransferFunctionElement | SVGDefsElement | SVGDescElement | SVGEllipseElement | SVGFEBlendElement | SVGFEColorMatrixElement | SVGFEComponentTransferElement | SVGFECompositeElement | SVGFEConvolveMatrixElement | SVGFEDiffuseLightingElement | SVGFEDisplacementMapElement | SVGFEDistantLightElement | SVGFEFloodElement | SVGFEFuncAElement | SVGFEFuncBElement | SVGFEFuncGElement | SVGFEFuncRElement | SVGFEGaussianBlurElement | SVGFEImageElement | SVGFEMergeElement | SVGFEMergeNodeElement | SVGFEMorphologyElement | SVGFEOffsetElement | SVGFEPointLightElement | SVGFESpecularLightingElement | SVGFESpotLightElement | SVGFETileElement | SVGFETurbulenceElement | SVGFilterElement | SVGForeignObjectElement | SVGGElement | SVGImageElement | SVGGradientElement | SVGLineElement | SVGLinearGradientElement | SVGMarkerElement | SVGMaskElement | SVGPathElement | SVGMetadataElement | SVGPatternElement | SVGPolygonElement | SVGPolylineElement | SVGRadialGradientElement | SVGRectElement | SVGSVGElement | SVGScriptElement | SVGStopElement | SVGStyleElement | SVGSwitchElement | SVGSymbolElement | SVGTSpanElement | SVGTextContentElement | SVGTextElement | SVGTextPathElement | SVGTextPositioningElement | SVGTitleElement | SVGUseElement | SVGViewElement | SVGElement | Element}
         */
        createElementNS: function (tag, attrs) {
            let el = document.createElementNS("http://www.w3.org/2000/svg", tag);
            for (let k in attrs) {
                el.setAttribute(k, attrs[k]);
            }
            return el;
        },
        /**
         * 普通的元素
         * @param tag
         * @param attrs
         * @param arg
         * @returns {HTMLElement}
         */
        createElement: function ({tag, attrs = {}, arg} = {}) {

            let el = document.createElement(tag);
            for (let k in attrs) {
                el.setAttribute(k, attrs[k]);
            }
            if (arg) {
                el.innerHTML = arg;
                // return el.childNodes;
            }
            return el;
        }, /**
         字符串转换element
         * @author MrLu
         * @param str element字符串
         * @createTime  2020/4/28 12:07
         * @version 1.0
         */
        parseElement: function (str) {
            let o = document.createElement("div");
            o.innerHTML = str;
            return o.childNodes[0];
        },
        downLoadImg:function (imgUrl) {
            let x=new XMLHttpRequest();
            x.open("GET", imgUrl, true);
            x.responseType = 'blob';
            x.onload=function(e){
                let url = window.URL.createObjectURL(x.response)
                let a = document.createElement('a');
                a.href = url;
                a.download = '';
                a.click()
            };
            x.send();
        }

    },
    /**
     * @author MrLu
     * @param  arr 数组
     * @param V 要删除的值
     * @createTime  2020/6/11 9:02
     * @describe 从一个数组中删除某元素
     * @version 1.0
     */
    deleteFromAry: function (arr, V) {
        arr.splice(arr.findIndex(thisV => {
            //这里不能写es6的 ===
            return thisV == V
        }), 1);
        return arr;
    },
     /**
     * 将json对象中的值赋予对应名id的标签
     * @author MrLu
     * @param values json对象
     * @createTime  2020/10/23 16:41
     * @return    |
      */
    setValue: function (values) {
        for (let thisCol in values) {
            let thisNode = document.getElementById(thisCol);
            if (thisNode) {
                if ('INPUT' === thisNode.tagName) {
                    thisNode.value = values[thisCol];
                } else {
                    thisNode.innerHTML = values[thisCol];
                }
            }
        }
    },

    /**
     *  判断element中手否含有某个属性
     * @author MrLu
     * @param el 数组
     * @param name 属性
     * @createTime  2020/8/14 14:57
     * @return  boolean  |
     */
    hasAttr: function (el, name) {
        let attr = el.getAttributeNode && el.getAttributeNode(name);
        return attr ? attr.specified : false;
    },
    /**
     * 截取标题添加.....
     * @author MrLu
     * @param  title 标题
     * @param length  截取长度
     * @createTime  2020/5/2 17:05
     * @return String 截取后的字符串
     */
    beautifulTitle: function (title, length) {

        if (this.isEmpty(title)) return '';
        return '<span>' + (title.substring(0, length) + (title.length > length ? "....." : "") + '</span>');
    },
    convertToChinaNum:function(num){
        const arr1 = ['零', '一', '二', '三', '四', '五', '六', '七', '八', '九'];
        const arr2 = ['', '十', '百', '千', '万', '十', '百', '千', '亿', '十', '百', '千','万', '十', '百', '千','亿'];//可继续追加更高位转换值
        if(!num || isNaN(num)){
            return "零";
        }
        let english = num.toString().split("")
        let result = "";
        for (let i = 0; i < english.length; i++) {
            let des_i = english.length - 1 - i;//倒序排列设值
            result = arr2[i] + result;
            let arr1_index = english[des_i];
            result = arr1[arr1_index] + result;
        }
        //将【零千、零百】换成【零】 【十零】换成【十】
        result = result.replace(/零([千百十])/g, '零').replace(/十零/g, '十');
        //合并中间多个零为一个零
        result = result.replace(/零+/g, '零');
        //将【零亿】换成【亿】【零万】换成【万】
        result = result.replace(/零亿/g, '亿').replace(/零万/g, '万');
        //将【亿万】换成【亿】
        result = result.replace(/亿万/g, '亿');
        //移除末尾的零
        result = result.replace(/零+$/, '')
        //将【零一十】换成【零十】
        //result = result.replace(/零一十/g, '零十');//貌似正规读法是零一十
        //将【一十】换成【十】
        result = result.replace(/^一十/g, '十');
        return result;
    },
    /**
     * 判断当前页面是移动版还是pc
     * @author MrLu
     * @createTime  2020/7/20 15:27
     * @version 1.0
     * @return boolean | true PC； fasle 移动版
     */
    navigator: function () {
        const sUserAgent = navigator.userAgent;
        return !(sUserAgent.indexOf('Android') > -1 || sUserAgent.indexOf('iPhone') > -1 || sUserAgent.indexOf('iPad') > -1 || sUserAgent.indexOf('iPod') > -1 || sUserAgent.indexOf('Symbian') > -1);
    },
    /**
     * 在heart上加载一个js文件并回调方法
     * @author MrLu
     * @param id script标签id
     * @param url script文件路径
     * @param fn  回调方法
     * @createTime  2020/9/25 11:10
     */
    heartJs: function (id, url, fn) {
        let script = document.createElement("script");
        script.type = 'text/javascript';
        script.id = id;
        if (!script.readyState) {
            script.onload = function () {
                fn();
            }
        } else {
            //ie
            script.onreadystatechange = function () {
                if ('loaded' === script.readyState || 'complete' === script.readyState) {
                    script.onreadystatechange = null;
                    fn();
                }
            }
        }
        script.src = url;
        document.getElementsByTagName("head")[0].appendChild(script);

    },
    functional: {
        /**
         * 传递的参数只能运行一次
         * @author MrLu
         * @param fn 方法
         * @createTime  2020/9/27 8:54
         * @return    |
         */
        once: function (fn) {
            let done = false;
            return function () {
                return done ? undefined : (done = true), fn.apply(this, arguments)
            }

        },
        /**
         * 转换函数
         * 常见使用方法 ["1","2","3"].map(utils.unary(parseInt))=>[1,2,3]
         * @author MrLu
         * @param fn 方法
         * @createTime  2020/9/27 9:33
         */
        unary: function (fn) {
            1 === fn.length ? fn : (arg) => fn(arg);

        },
        encryption: function (p) {
            if (sessionStorage.salt) {
                return window.btoa(p + sessionStorage.salt)
            } else {
                throw '未能捕获到加密串，请重新登录或检查服务器连接'
            }

        },
        /**
         * 将数组中的值循环执行某方法
         * @author MrLu
         * @param arrary 数组
         * @param fn 要执行的方法
         * @createTime  2020/10/9 10:15
         */
        forEach: function (arrary, fn) {
            for (const a of arrary)
                fn(a);
        }, map: function (arrary, fn) {
            let results = [];
            for (const a of arrary) {
                //这么写比push快
                results[results.length] = fn(a);
            }
            return results;
        }
    },
     getElementPagePositionX:function(element){
    //计算x坐标
    var actualLeft = element.offsetLeft;
    var current = element.offsetParent;
    while (current !== null){
        actualLeft += current.offsetLeft;
        current = current.offsetParent;
    }

    //返回结果
  return  actualLeft
},
    getElementPagePositionY:function(element){

        //计算y坐标
        var actualTop = element.offsetTop;
        var current = element.offsetParent;
        while (current !== null){
            actualTop += (current.offsetTop+current.clientTop);
            current = current.offsetParent;
        }
        //返回结果
        return actualTop
    }


}
