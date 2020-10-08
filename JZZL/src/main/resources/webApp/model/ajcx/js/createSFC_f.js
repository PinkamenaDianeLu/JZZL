/**
 * @author Mrlu
 * @createTime 2020/9/28
 * @url webApp.model.ajcx.js
 * @dependence [jquery,layer,utils]
 * @describe 新建卷js
 */

var recordsTable = (function () {

    let pid;
    let tableObject;

    const searchParam = function (recordscode) {
        this.pid = pid;
        this.recordscode = recordscode;
    };

    function getSearchParam() {
        let reS = new searchParam();
        reS.recordscode=$('[name=recordscode]:checked').val();
        return reS;
    };

    function loadTable() {
        tableObject = createTable({
            tableId: 'recordsTable',
            searchUrl: '/Records/selectRecordsByJqbhPage',
            column: [{
                field: 'checked',
                radio: true,
                align: 'center',
                valign: 'middle',
            },

                {
                    field: 'jqbh',
                    title: '警情编号'
                }, {
                    field: 'ajbh',
                    title: '案件编号',

                }, {
                    field: 'recordname',
                    title: '文书名称',
                    formatter: (value, row) => {
                        return utils.beautifulTitle(value, 13);
                    }
                }, {
                    field: 'createtime',
                    title: '开具时间（伪）', formatter: (value) => {
                        return utils.timeFormat.timestampToDate2(value)
                    }
                }, {
                    field: 'archivecode',
                    title: '犯罪嫌疑人'
                },], param: function () {
                return getSearchParam();
            }
        });
    }

    /**
     * 查询
     * @author MrLu
     * @createTime  2020/9/25 10:40
     * @return    |
     */
    function searchTable() {
        tableObject.refreshTable();
    }
    //返回表格对象
    function getTable() {
        return tableObject;
    }

    function _recordsTable(idP) {
        pid = idP;
        loadTable();
    }

    _recordsTable.prototype = {
        searchTable,getTable
    };
    return _recordsTable;
})();

var createNewSFC = (function () {
    let peopelcaseid;
    const searchParam = function (recordscode, archivename,recordsId) {
        this.peopelcaseid = peopelcaseid;
        this.recordscode = recordscode;
        this.archivename = archivename;
        this.recordsId=recordsId;
    };

    function getSearchParam() {
        let reS = new searchParam();
        reS.recordscode = $('[name=recordscode]:checked').val();
        reS.archivename = $('#archivename').val().trim();
        reS.recordsId=$('#recordsTable').bootstrapTable('getSelections')[0].id;
        reS.peopelcaseid=peopelcaseid;
        return reS;
    };

     /**
     * 保存内容
     * @author MrLu
     * @createTime  2020/10/7 15:17
      */
    function createNSFC() {
        $.post({
            url: '/SFCensorship/createNewSFCensorship',
            data: {params: JSON.stringify(getSearchParam())},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    layer.msg('成功');
                    window.parent.st.searchTable();//父页面表格刷新
                    const index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                    parent.layer.close(index);
                } else {
                    layer.msg('失败');
                }
            }
        });
    }

    function _createNewSFC(idP) {
        peopelcaseid = idP;
    }

    _createNewSFC.prototype = {createNSFC};
    return _createNewSFC;
})();
$(function () {

    const ajid = parent.st.pValue;//案件表id
    let rt = new recordsTable(ajid);
    //搜索按钮
    $('[name=recordscode]').click(function () {
        rt.searchTable();
    });

    let cnf = new createNewSFC(ajid);
    //保存按钮
    $('#createNewSFC').click(function () {
        cnf.createNSFC();
    });
    //关闭当前页面
    $('#closeWindow').click(function () {
        const index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index);
    })
})