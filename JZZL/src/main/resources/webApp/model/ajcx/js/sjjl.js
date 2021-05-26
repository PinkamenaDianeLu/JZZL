/**
 * @author Mrlu
 * @createTime 2020/9/25
 * @url webApp.model.ajcx.js
 * @describe
 * @dependence [jquery,layer,utils,bootstrapTableUtil]
 */

var sjjlTable = (function () {

    let tableObject;
    let ajid;//caseinfoId

    const searchParam = function () {
        this.id = ajid;
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
    this.submitForCensorship = function (id) {
        let urlP = window.btoa(id + sessionStorage.salt)
        window.open('/model/jzzl/jzzl.html?id=' + urlP);
    }

    function loadTable(ajidP) {
        ajid = ajidP;
        // sjjlTable.pValue = ajidP;
        tableObject = createTable({
            tableId: 'sjjlTable',
            searchUrl: '/SFCensorship/selectSFCensorshipPage',
            column: [
                {
                    field: 'jqbh',
                    title: '卷宗编号'
                }
                , {
                    field: 'archivename',
                    title: '卷名称',
                    formatter: (value, row) => {
                        return utils.beautifulTitle(value, 13);
                    }
                }, {
                    field: 'author',
                    title: '创建人',

                }, {
                    field: 'authoridcard',
                    title: '创建人身份证',formatter: (value) => {
                        return '0'===value?'系统整理':value
                    }
                },
                {
                    field: 'createtime',
                    title: '创建时间', formatter: (value) => {
                        return utils.timeFormat.timestampToDate2(value)
                    }
                }, {
                    field: 'archivetype_name',
                    title: '卷类型'
                }, {
                    field: 'issend',
                    title: '是否已发送'
                }, {
                    title: '操作',
                    align: 'center',
                    formatter: function (value, row, index) {
                        let reBtn = '<a class="b_but edit" onclick="submitForCensorship(\'' + row.id + '\')">整理</a>';
                        if (0 !== row.archivetype) {
                            reBtn += '<a class="b_but edit" >发送</a>';
                        }

                        return reBtn;
                    }
                }
            ], param: function () {
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


    function _sjjlTable() {
    }

    _sjjlTable.prototype = {
        loadTable, searchTable
    };
    return _sjjlTable;
})();
/**
 * 进度条相关调度
 * @author MrLu
 * @param  fn  进度条走满之后运行的函数
 * @createTime  2020/11/28 10:06
 * @return    |
 */
var progressBar = (function () {
    let progressLength = 0;//进度条进度
    var overFn;

    function alertProgressBarWindow() {
        //加载进度条
        layer.open({
            id: 10086,
            type: 1,
            title: false,
            shade: [0.8, '#393D49'],
            closeBtn: 0,
            area: ['453px', '170px'],
            content: $('#progressBarDiv')
        });
        //为进度条的弹出框附加一个class 增加透明样式
        $('#10086').parent().addClass('progressBarParent');
    }

    /**
     * 涨进度条
     * @author MrLu
     * @param bfb 百分比进度
     *  @param fn 但百分比超过100触发的事件
     * @createTime  2020/10/27 11:34
     */
    function addProgressBar(bfb) {
        progressLength = progressLength + (+bfb);
        $('#progressBar').css('width', progressLength + '%').html(progressLength + '%');
        if (progressLength > 100) {
            //关闭进度条窗口
            layer.closeAll();
            //初始化进度条
            progressLength = 0;
            $('#progressBar').css('width', 0 + '%').html(0 + '%');
            overFn();
        }
    }

    function _progressBar(fn) {
        overFn = fn;
    }

    _progressBar.prototype = {
        alertProgressBarWindow, addProgressBar
    }
    return _progressBar;

})();
 /**
 * 嫌疑人排序
 * @author MrLu
 * @param ajid 案件id
 * @createTime  2020/12/17 19:24
 * @return    |
  */
var suspectOrder = function (ajid) {
    $.post({
        url: '/SFCensorship/selectSuspectByCaseInfoId',
        data: {
            caseinfoid: ajid
        },
        success: (re) => {
            const reV = JSON.parse(re);
            if ('success' === reV.message) {

                if (!reV.value || 0 === reV.value.length) {
                    alert('该案件没有任何嫌疑人！');
                } else {
                    $('#suspectUl').empty();//清空之前的嫌疑人
                    let suspects = document.createDocumentFragment();
                    //加载选嫌疑人框
                    for (let thisSuspect of reV.value) {
                        let thisSuspectTd = utils.createElement.createElement({
                            tag: 'li',
                            attrs: {class: 'suspectSort', id: 'suspectId' + thisSuspect.id},
                            arg: '<span>' + thisSuspect.suspectname + '</span><span>' + thisSuspect.suspectidcard + '</span>'
                        })
                        suspects.appendChild(thisSuspectTd);
                    };
                    $('#suspectUl').append(suspects);

                    //嫌疑人开启拖拽
                    $("#suspectUl").sortable({
                        delay: 50, cursor: 'move',
                        scroll: true, scrollSensitivity: 10,
                        cancel: "#suspectHead"
                    })
                    //弹出选择嫌疑人优先级窗口
                    layer.open({
                        type: 1,
                        title: '拖拽嫌疑人排序',
                        shade: [0.8, '#393D49'],
                        closeBtn: 1,
                        btn: ['确认顺序']
                        , yes: function (index, layero) {
                            //嫌疑人顺序set
                            let SuspectOrder = new Set();
                            //循环得到拖拽排序后的嫌疑人顺序
                            $('.suspectSort').each(function (index, item) {
                                let thisSuspectId = $(item).attr('id');
                                SuspectOrder.add(thisSuspectId);
                            })
                            //传到后台开始整理
                            layer.close(index);
                            $.post({
                                url: '/SFCensorship/orderArchivesBySuspectOrder',
                                data: {
                                    issuspectorder: Array.from(SuspectOrder).join(',')
                                    , caseinfoid: ajid
                                },
                                success: (re) => {
                                    const reV = JSON.parse(re);
                                    if ('success' === reV.message) {
                                        console.log('嫌疑人排序成功！');
                                    } else {
                                        console.error('嫌疑人排序失败！');
                                    }
                                }
                            });
                            alert('已开始将所有卷按照嫌疑人顺序智能排序，请注意右上角提醒');
                        },
                        area: ['338px', '471px'],
                        content: $('#suspectOrderDiv')
                    });

                }
            } else {
                console.error('该文书关联嫌疑人查询失败！')
            }
        }
    });
}
var st = new sjjlTable();
$(function () {
    $('#userHeart').load('/userHeart.html');
    //案件的id  caseinfo表
    const ajid = utils.getUrlPar('id');
    $.post({
        url: '/SFCensorship/selectCaseInfoById',
        data: {caseinfoid: ajid},
        success: (re) => {
            const reV = JSON.parse(re);
            if ('success' === reV.message) {
                console.log(reV)
                //送检编号
                $('#sfcnumber').html(reV.value.sfcnumber);
                //案件名称
                $('#casename').html(reV.value.casename);
                st.pValue = ajid;//设置pv为caseinfoid
                st.loadTable(ajid);//加载表格
                //查询按钮
                $('#sjjlSearchBtn').click(function () {
                    st.searchTable();
                });
                if (reV.issuspectorder)  {
                    $('#issuspectorder').remove();
                    //新建送检按钮
                    $('#createSFC').on('click', function () {
                        layer.open({
                            icon: 1,
                            type: 2,
                            title: '新建卷',
                            skin: 'layui-layer-lan',
                            maxmin: false,
                            shadeClose: true, //点击遮罩关闭层
                            area: ['1111px', '600px'],
                            content: '/model/ajcx/createSFC_f.html'
                        });
                    });
                }else {
                    $('#issuspectorder').html('您还没有为嫌疑人排序！');
                    //弹出给嫌疑人排序窗口
                    suspectOrder(ajid);
                    //新建功能删除
                    $('#createSFC').remove();
                }
            } else {
                console.error('查询案件信息失败！');
            }
        }
    });
    //嫌疑人排序按钮
    $('#suspectOrder').click(function () {
        suspectOrder(ajid);
    })


})