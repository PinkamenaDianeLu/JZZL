/**
 * @author Mrlu
 * @createTime 2020/9/17
 * @url webApp.model.ajcx.js
 * @describe 送检记录js
 */

/**
 * 送检记录
 * @author MrLu
 * @createTime  2020/9/17 9:39
 * @return    |
 */
/*var sjjl = (function () {
    /!**
     * 加载列表
     * @author MrLu
     * @param
     * @createTime  2020/9/17 9:50
     * @return    |
     *!/
    function loadTable() {
        console.log('加载了列表')
    }

     /!**
     * 跳转至卷宗整理
     * @author MrLu
     * @param id 整理次序的id
     * @createTime  2020/9/17 10:15
     * @return    |
      *!/
     //TODO MrLu 2020/9/17   id的传递方式有待商酌
    this.jzzl = function (id) {
        window.open('/model/jzzl/jzzl.html?id='+id)

    }

    function _sjjl() {
        loadTable();
    }

    _sjjl.prototype = {}
    return _sjjl;
})()*/

$(function () {


let a=createTable({tableId:'sjjlTable',
    searchUrl:'/CaseSearch/selectPeopleCasePage',
    column :[{
        field: 'jqbh',
        title: '警情编号'
    }, {
        field: 'ajbh',
        title: '案件编号',

    }, {
        field: 'casename',
        title: '案件名称',
        formatter: (value, row) => {
            return utils.beautifulTitle(value, 13);
        }
    },{
        field: 'createtime',
        title: '出卷时间', formatter: (value) => {
            return utils.timeFormat.timestampToDate2(value)
        }
    }, {
        field: 'name',
        title: '办案人'
    }, {
        field: 'idcard',
        title: '办案人身份证号'
    }, {
        title: '操作',
        align: 'center',
        formatter: function (value, row, index) {
            return '<a class="b_but edit" >开始测试</a>';
        }
    }]});
})