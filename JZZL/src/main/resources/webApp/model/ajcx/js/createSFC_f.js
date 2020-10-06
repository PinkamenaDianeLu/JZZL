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

    const searchParam = function () {
        this.pid = pid;
    };

    function getSearchParam() {
        let reS = new searchParam();
        return reS;
    };

    function loadTable() {
        tableObject = createTable({
            tableId: 'recordsTable',
            searchUrl: '/Records/selectRecordsByJqbhPage',
            column: [{
                field: 'checked',
                checkbox: true,
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
                    title: '文件代码'
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
    const searchParam = function (archivetype, archivename) {
        this.peopelcaseid = peopelcaseid;
        this.archivetype = archivetype;
        this.archivename = archivename;
    };

    function getSearchParam() {
        let reS = new searchParam();
        reS.archivetype = $('[name=archivetype]:checked').val();
        reS.archivename = $('#archivename').val().trim();
        return reS;
    };

    function createNSFC() {
        $.post({
            url: '/SFCensorship/createNewSFCensorship',
            data: {params: JSON.stringify(getSearchParam())},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    alert('成功')
                } else {
                    alert('失败')
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
    const ajid = parent.sjjlTable.pValue;//案件表id
    let rt = new recordsTable(ajid);
    $('#recordsSearchBtn').click(function () {
        rt.searchTable();
    });
    let cnf = new createNewSFC(ajid);
    $('#createNewSFC').click(function () {
        let tableObject=rt.getTable().getSelections();
        console.log(tableObject)
        cnf.createNSFC();
    });
})