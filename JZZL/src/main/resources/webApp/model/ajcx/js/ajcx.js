/**
 * @author Mrlu
 * @createTime 2020/9/17
 * @url webApp.model.ajcx.js
 * @describe 送检记录js
 * @dependence [jquery,layer,utils,bootstrapTableUtil]
 */



var ajcxTable = (function () {

    let tableObject;

    const searchParam = function (ajbh, jqbh) {
        this.ajbh = ajbh;
        this.jqbh = jqbh;
    };

    function getSearchParam() {
        let reS = new searchParam();
        reS.ajbh = $('#ajbh').val().trim();
        reS.jqbh = $('#jqbh').val().trim();
        return reS;
    };
    /**
     * 送检记录
     * @author MrLu
     * @param id 案件关系表id
     * @createTime  2020/9/25 9:56
     * @return    |
     */
    this.submitHistory = function (id) {
        let urlP= window.btoa(id+sessionStorage.salt)
        window.open('/model/ajcx/sjjl.html?id='+urlP);
    }

    function loadTable() {
        tableObject = createTable({
            tableId: 'ajcxTable',
            searchUrl: '/CaseSearch/selectPeopleCasePage',
            column: [{
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
            }, {
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
                field: 'submitcount',
                title: '送检次数'
            },{
                field: 'casetype',
                title: '案件类型'
            }, {
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    return '<a class="b_but edit" onclick="submitHistory(\'' + row.id + '\')">送检</a>';
                }
            }],param:function (){
                return getSearchParam();
            }
        });
    }

     /**
     * 查询
     * @author MrLu
     * @param 
     * @createTime  2020/9/25 10:40
     * @return    |  
      */
    function searchTable() {
        tableObject.refreshTable();
    }

    function _ajcxTable() {
        loadTable();
    }

    _ajcxTable.prototype = {
        searchTable
    }
    return _ajcxTable;
})()
$(function () {
       $.post({
               url: '/Records/getFunArchiveRecordsById',
               data: {id:85},
               success: (re) => {
                   const reV = JSON.parse(re);
                   if ('success' === reV.message) {
                       console.log(reV.value);
                   } else {
                   }
               }
           });
    let at=new ajcxTable();
    $('#ajcxSearchBtn').click(function () {
        at.searchTable();
    })
})