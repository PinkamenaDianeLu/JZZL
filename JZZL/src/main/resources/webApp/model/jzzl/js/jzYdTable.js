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

    const searchParam = function (typename, recordname, archivetypeid) {
        this.recordname = recordname;
        this.seqid = seqId;
        this.archivetypeid = archivetypeid;
    };

    function getSearchParam() {
        let reS = new searchParam();
        reS.recordname = $('#recordname').val().trim();
        reS.archivetypeid = $('#archivetypeid').val()||null;
        return reS;
    }

     /**
     * 移入
     * @author MrLu
     * @param 
     * @createTime  2020/11/6 16:17
     * @return    |  
      */
     this.moveIn=function(file) {

         //parent.filecode.remove
         //ajax后台改数据
         //$('#dd+record).append(filecodes.foreach)

    }

    function loadTable() {
        tableObject = createTable({
            tableId: 'recordTable',
            searchUrl: '/FileManipulation/selectArchiveRecordPage',
            column: [{
                field: 'typename',
                title: '卷类型'
            }, {
                field: 'recordname',
                title: '文书名称',
                formatter: (value, row) => {
                    return utils.beautifulTitle(value, 13);
                }
            }, {
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    return '<a class="b_but edit" onclick="moveIn('+row+')" >移入</a>';
                }
            }], param: function () {
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
        seqId = seqIdP;
        loadTable();
    }

    _recordsTable.prototype = {
        searchTable
    }
    return _recordsTable;
})()
$(function () {
    const moveObj = parent.recordImgLoad.pValue;
    const moveType = utils.getUrlPar('moveState');//0 单页文书移动  1 整个文书移动  2 文书批量移动
    const seqId = utils.getUrlPar('seqid');
    console.log(moveObj);
    console.log(moveType);


    $.post({
        url: '/ArrangeArchives/getArchiveIndex',
        data: {id: seqId},
        success: (re) => {
            const reV = JSON.parse(re);
            if ('success' === reV.message) {
                //列表带搜索显示所有文书
                let rt = new recordsTable(seqId)
                let Fragment = document.createDocumentFragment();
                for (let thisType of reV.value) {
                    let thisTab = utils.createElement.createElement({
                        tag: 'li', attrs: {
                            id: thisType.id
                        }, arg: '<a data-toggle="tab">' + thisType.archivetypecn + '</a>'
                    });
                    //添加搜索方法
                    thisTab.addEventListener('click', function () {
                        $('.active').removeClass('active');
                        $(this).addClass('active');
                        $('#archivetypeid').val(thisType.id);
                        rt.searchTable();
                    })
                    Fragment.append(thisTab)
                }

                //搜索选项卡、搜索按钮 赋值
                $('#searchBtn,#searchTab').click(function () {
                    $('.active').removeClass('active');
                    $('#searchTab').addClass('active');
                    $('#archivetypeid').val('');
                    rt.searchTable();
                })

                $('#archiveTypeTab').append(Fragment)

            } else {
            }
        }
    });
})