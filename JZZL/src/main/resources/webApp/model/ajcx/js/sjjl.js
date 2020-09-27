/**
 * @author Mrlu
 * @createTime 2020/9/25
 * @url webApp.model.ajcx.js
 * @describe
 * @dependence [jquery,layer,utils,bootstrapTableUtil]
 */

var sjjlTable = (function () {

    let tableObject;
    let ajid;

    const searchParam = function () {
        this.id=ajid;
    };

    function getSearchParam() {
        let reS = new searchParam();
        return reS;
    };
    /**
     * 案卷整理
     * @author MrLu
     * @param id 案件关系表id
     * @createTime  2020/9/25 9:56
     * @return    |
     */
    this.submitHistory = function () {
        // let urlP= window.btoa(id+sessionStorage.salt)
        window.open('/model/ajcx/sjjl.html?id=');
    }

    function loadTable() {
        tableObject = createTable({
            tableId: 'sjjlTable',
            searchUrl: '/SFCensorship/selectSFCensorshipPage',
            column: [
                {
                field: 'jqbh',
                title: '卷宗编号'
            }
            /*, {
                field: 'jqbh',
                title: '卷类型',

            }, {
                field: 'jqbh',
                title: '卷名称',
                formatter: (value, row) => {
                    return utils.beautifulTitle(value, 13);
                }
            },{
                field: 'jqbh',
                title: '整理人',
            },
                {
                field: 'createtime',
                title: '生成时间', formatter: (value) => {
                    return utils.timeFormat.timestampToDate2(value)
                }
            }, {
                field: 'jqbh',
                title: '上传时间'
            }, {
                field: 'jqbh',
                title: '状态'
            }, {
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    return '<a class="b_but edit" onclick="submitHistory(\'' + row.id + '\')">送检</a>';
                }
            }*/
            ],param:function (){
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

    function _sjjlTable(ajidP) {
        ajid=ajidP
        loadTable();
    }

    _sjjlTable.prototype = {
        searchTable
    }
    return _sjjlTable;
})()
$(function () {
    //案件的id
    const ajid =utils.getUrlPar('id');
    let st=new sjjlTable(ajid);
    $('#sjjlSearchBtn').click(function () {
        st.searchTable();
    })

    $('#createSFC').on('click', function(){
        layer.open({
            icon: 1,
            type: 2,
            title: '新建卷',
            skin: 'layui-layer-lan',
            maxmin: false,
            shadeClose: true, //点击遮罩关闭层
            area : ['780px' , '300px'],
            content: '/model/ajcx/createSFC_f.html'
        });
    });

})