/**
 *  util方法
 * @author MrLu
 * @createTime  2020/7/10 9:25
 * @version 1.2
 */
var utils = {
    /**
     * 判断空
     * （字符串empty也算空！）
     * @param str
     * @returns {boolean}
     */
    isEmpty: function (str) {
        if (typeof (str) == 'undefined' || !str || str === "" || str === "empty")
            return true;
        return false;
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
        getQuarterEndMouth:function (quarter) {
             if(!quarter) {
                 quarter =utils.getDate.getQuarter();
             }
             let quarterEndMonth = 0;

             switch (parseInt(quarter)) {
                 case 1:
                     quarterEndMonth=3;break;
                 case 2:
                     quarterEndMonth=6;break;
                 case 3:
                     quarterEndMonth=9;break;
                 case 4:
                     quarterEndMonth=12;break;
                 default:
                     throw new Error('错误的季度！ 季度为 1 2 3 4 ');

             }
             return quarterEndMonth;
        },
        getLastOfMonth(month){
            let date=new Date();
            let nextMonth=++month;
            let nextMonthFirstDay=new Date(date.getFullYear(),nextMonth,1);
            let oneDay=1000*60*60*24;
            return utils.timeFormat.timestampToDate2(new Date(nextMonthFirstDay-oneDay));
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
     /**
     * 判断当前页面是移动版还是pc
     * @author MrLu
     * @createTime  2020/7/20 15:27
     * @version 1.0
      * @return boolean true PC； fasle 移动版
      */
    navigator:function () {
        const sUserAgent = navigator.userAgent;
        if (sUserAgent.indexOf('Android') > -1 || sUserAgent.indexOf('iPhone') > -1 || sUserAgent.indexOf('iPad') > -1 || sUserAgent.indexOf('iPod') > -1 || sUserAgent.indexOf('Symbian') > -1) {
            return false;//移动端
        } else {
            //电脑
            return  true;
        }
    }


}
