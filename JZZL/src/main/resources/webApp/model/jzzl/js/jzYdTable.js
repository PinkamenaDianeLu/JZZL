/**
 * @author Mrlu
 * @createTime 2020/10/19
 * @dependence 依赖js
 * @describe 卷宗移动
 *
 */
var recordsTable = (function () {
    let moveObj;
    let oriRecordId;
    let tableObject;

    const searchParam = function (typename, recordname, archivetypeid) {
        this.recordname = recordname;
        this.seqid = utils.getUrlPar('seqid');
        this.archivetypeid = archivetypeid;
    };

    function getSearchParam() {
        let reS = new searchParam();
        reS.recordname = $('#recordname').val().trim();
        reS.archivetypeid = $('#archivetypeid').val() || null;
        return reS;
    }

    /**
     * 移入
     * @author MrLu
     * @param recordId 文书id
     * @createTime  2020/11/6 16:17
     * @return    |
     */
    this.moveIn = function (recordId) {
        const seqId = utils.getUrlPar('seqid');
        layer.msg('开始移动，请稍等....');
        $.post({
            url: '/FileManipulation/moveFiles',
            data: {
                fileOrder: moveObj,
                seqId: seqId,
                recordid: recordId,
                orirecordid: oriRecordId
            },
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    let lastFileCount = reV.value.length;//加载文件数量
                    //前台dom更改
                    let Fragment = document.createDocumentFragment();
                    //假的索引使得方法不需要再次迭代dom判断位置
                    let fileIndexing = {
                        i: 999,
                        f: 999
                    };
                    console.log(reV.value)
                    for (let thisFile of reV.value) {
                        //判断是否是最后一个文书
                        lastFileCount--;
                        if (lastFileCount === 0) {
                            //这些写法将不会加载下一个按钮
                            fileIndexing = {
                                i: 991,
                                f: 993
                            };
                        }
                        Fragment.appendChild(parent.lai.createFilesDiv(thisFile, fileIndexing));
                        //将原有的删除
                        parent.$('#front' + thisFile.filecode).remove();
                        parent.$('#bigImg' + thisFile.filecode).remove();
                        parent.$('#thumbnail' + thisFile.filecode).remove();
                        parent.$('#fileIndex' + thisFile.filecode).remove();
                    }
                    // console.log(oriRecordId)
                    //重新计算按钮  因为没有子目录了  以下代码废弃
                    // parent.lai.reloadButton(parent.$('#dd' + oriRecordId).find('.v3').last());
                    // parent.lai.reloadButton(parent.$('#dd' + oriRecordId).find('.v3').first());
                    // const lastFile = parent.$('#dd' + recordId).find('.v3').last();//保存目标文书的原最后一个文书
                    //移动到新目录
                    // parent.$('#dd' + recordId).find('.fileSortZone').append(Fragment);
                    // parent.lai.reloadButton(lastFile);//刷新按钮

                    layer.msg('移动完成！');
                    //自我关闭
                    const index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                    parent.layer.close(index);
                } else {
                    layer.alert('移动失败，请稍后再试');
                }
            }
        });

        //parent.filecode.remove
        //ajax后台改数据

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
                    return oriRecordId !== row.id ? '<a class="b_but edit" onclick="moveIn(' + row.id + ')" >移入</a>' : '<a class="b_but editGreen"  >原文书</a>';
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

    function _recordsTable(moveObjP, oriRecordIdP) {
        oriRecordId = oriRecordIdP;
        moveObj = moveObjP;
        loadTable();
    }

    _recordsTable.prototype = {
        searchTable
    };
    return _recordsTable;
})();
$(function () {
    const moveObj = parent.recordImgLoad.pValue;
    const moveType = utils.getUrlPar('moveState');//0 单页文书移动  1 整个文书移动  2 文书批量移动
    const oriRecordId = utils.getUrlPar('orirecordid');


    $.post({
        url: '/ArrangeArchives/getArchiveIndex',
        data: {id: utils.getUrlPar('seqid')},
        success: (re) => {
            const reV = JSON.parse(re);
            if ('success' === reV.message) {
                //列表带搜索显示所有文书
                let rt = new recordsTable(moveObj, oriRecordId);
                let Fragment = document.createDocumentFragment();
                for (let thisType of reV.value) {
                    let thisTab = utils.createElement.createElement({
                        tag: 'li', attrs: {
                            id: thisType.id
                        }, arg: '<a data-toggle="tab">' + thisType.recordtypecn + '</a>'
                    });
                    //添加搜索方法
                    thisTab.addEventListener('click', function () {
                        $('.active').removeClass('active');
                        $(this).addClass('active');
                        $('#archivetypeid').val(thisType.id);
                        rt.searchTable();
                    });
                    Fragment.appendChild(thisTab)
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
                layer.alert('没有找到可以移动的文书信息！');
                console.error('文书信息加载失败');
            }
        }
    });
})