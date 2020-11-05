/**
 * @author Mrlu
 * @createTime 2020/10/19
 * @dependence 依赖js
 * @describe 卷宗移动
 *
 */
var recordsTable = (function () {

    let tableObject;
    let seqId;

    const searchParam = function (typename, recordname) {
        this.typename = typename;
        this.recordname = recordname;
        this.seqid=seqId;
    };

    function getSearchParam() {
        let reS = new searchParam();
        reS.typename = $('#typename').val().trim();
        reS.recordname = $('#recordname').val().trim();
        return reS;
    };


    function loadTable() {
        tableObject = createTable({
            tableId: 'recordTable',
            searchUrl: '/CaseSearch/selectPeopleCasePage',
            column: [{
                field: 'jqbh',
                title: '卷类型'
            }, {
                field: 'ajbh',
                title: '文书名称',
                formatter: (value, row) => {
                    return utils.beautifulTitle(value, 13);
                }
            },  {
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    return '<a class="b_but edit" >移动至</a>';
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

    function _recordsTable(seqIdP) {
        seqId=seqIdP;
        loadTable();
    }

    _recordsTable.prototype = {
        searchTable
    }
    return _recordsTable;
})()
$(function () {
    const moveObj=  parent.recordImgLoad.pValue;
    const  moveType=utils.getUrlPar('moveState');//0 单页文书移动  1 整个文书移动  2 文书批量移动
    const  seqId=utils.getUrlPar('seqid');
    console.log(moveObj);
    console.log(moveType);
    //列表带搜索显示所有文书
    let rt=new recordsTable(seqId)
})