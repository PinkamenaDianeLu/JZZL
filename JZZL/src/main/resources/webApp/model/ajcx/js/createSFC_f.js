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
        reS.recordscode = $('[name=recordscode]:checked').val();
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
                    field: 'recordname',
                    title: '文书名称',
                    formatter: (value, row) => {
                        debugger;
                        return utils.beautifulTitle(value, 18);
                    }
                }, {
                    field: 'effectivetime',
                    title: '开具时间',
                    formatter: (value, row) => {
                        return utils.timeFormat.timestampToDate2(value)
                    }
                }, {
                    field: 'suspectname',
                    title: '犯罪嫌疑人',
                    formatter: (value, row) => {
                        return utils.beautifulTitle(value, 16);
                    }
                },], param: function () {
                return getSearchParam();
            }
        });
        //选择后自动生成案卷名称
    $('#recordsTable').on('check.bs.table', function (e, rows) {
        $('#archivename').val(utils.timeFormat.yyyyMMddtoTMD2(rows.issuetime)+' '
            +(rows.suspectname||'')+' '
            +rows.recordname)
        })

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
        searchTable, getTable
    };
    return _recordsTable;
})();

var createNewSFC = (function () {
    let caseinfoid;
    const searchParam = function (recordscode, archivename, recordsId) {
        this.caseinfoid = caseinfoid;
        this.recordscode = recordscode;
        this.archivename = archivename;
        this.recordsId = recordsId;
    };

    function getSearchParam() {
        let reS = new searchParam();
        reS.recordscode = $('[name=recordscode]:checked').val();
        reS.archivename = $('#archivename').val().trim();
        reS.recordsId = $('#recordsTable').bootstrapTable('getSelections')[0].id||null;
        reS.caseinfoid = caseinfoid;
        return reS;
    };

    /**
     * 保存内容
     * @author MrLu
     * @createTime  2020/10/7 15:17
     */
    function createNSFC(loadIndex) {
      let p=  getSearchParam();//参数
        if (p.recordsId){
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

                        window.close();
                    }
                    layer.close(loadIndex);//关闭遮罩层
                }
            });
        }else {
            layer.close(loadIndex);//关闭遮罩层
            layer.alert("请选择文书！");
        }

    }

    function _createNewSFC(idP) {
        caseinfoid = idP;
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
        $('#archivename').val('');//清空名称
    });


    //只有省厅版有换押证
    if (sessionStorage.version !== 'province'||sessionStorage.version === 'provinceTest') {
        $('#recordAS062').remove();
    }
    let cnf = new createNewSFC(ajid);
    //保存按钮
    $('#createNewSFC').click(function () {

        if (!$('#recordsTable').bootstrapTable('getSelections')[0]){
            layer.alert("请选择文书！");
        }else if (!$('#archivename').val()){
            layer.alert("请填写卷宗名称！");
        }else {
            let index = layer.load(1, {
                content:'正在生成中....',
                shade: [0.3,'#fff'] //0.1透明度的白色背景
            });
            cnf.createNSFC(index);
        }

    });
    //关闭当前页面
    $('#closeWindow').click(function () {
        const index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index);
    })
})