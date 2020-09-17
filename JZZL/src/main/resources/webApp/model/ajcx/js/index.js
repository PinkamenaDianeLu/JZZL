/**
 * @author Mrlu
 * @createTime 2020/9/17
 * @url webApp.model.ajcx.js
 * @describe 送检记录js
 */

/**
 * 送检记录
 * @author MrLu
 * @param
 * @createTime  2020/9/17 9:39
 * @return    |
 */
var sjjl = (function () {
    /**
     * 加载列表
     * @author MrLu
     * @param
     * @createTime  2020/9/17 9:50
     * @return    |
     */
    function loadTable() {
        console.log('加载了列表')
    }

     /**
     * 跳转至卷宗整理
     * @author MrLu
     * @param id 整理次序的id
     * @createTime  2020/9/17 10:15
     * @return    |
      */
     //TODO MrLu 2020/9/17   id的传递方式有待商酌
    this.jzzl = function (id) {
        window.open('/model/jzzl/jzzl.html?id='+id)

    }

    function _sjjl() {
        loadTable();
    }

    _sjjl.prototype = {}
    return _sjjl;
})()

$(function () {

})