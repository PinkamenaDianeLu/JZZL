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

    /**
     * 发送打包
     * @author MrLu
     * @param id 案件关系表id
     * @createTime  2021/5/26 15:07
     * @return    |
     */
    this.sendArchive = function (id) {
        $.post({
            url: '/SFCensorship/sendArchive',
            data: {id},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    layer.alert("打包程序调用成功");
                    searchTable();
                }else if ('issended' === reV.message){
                    layer.alert("已为您发送，请不要重复点击");
                } else {
                    layer.alert("打包程序调用失败");
                }
            }
        });

        layer.alert("打包程序已在后台运行，请关注右上角通知");
    }

    /**
     * 审批卷
     * @author MrLu
     * @createTime  2021/6/16 15:00
     * @return    |
     * @param id
     */
    this.approvalArchive = function (id) {
        $.post({
            url: '/SFCensorship/approvalArchive',
            data: {id},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    layer.msg("审批成功");
                } else {
                    layer.alert("审批程序调用失败");
                }
            }
        });
    }

    function loadTable(ajidP) {
        ajid = ajidP;
        // sjjlTable.pValue = ajidP;
        tableObject = createTable({
            tableId: 'sjjlTable',
            searchUrl: '/SFCensorship/selectSFCensorshipPage',
            column: [
                {
                    field: 'ajbh',
                    title: '案件编号'
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
                    title: '创建人身份证', formatter: (value) => {
                        return '0' === value ? '系统整理' : value
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
                            console.log(row)
                            //省厅办调用打包发送
                            if ((sessionStorage.version === 'province' || sessionStorage.version === 'provinceTest') &&
                                (sessionStorage.js === 'fz') && row.issend === '未发送') {
                                //

                                reBtn += '<a class="b_but edit" onclick="sendArchive(\'' + row.id + '\')">发送接收机关</a>';
                            }
                            if (sessionStorage.groupcode === '2306' && sessionStorage.version === 'city') {
                                //大庆地市
                                reBtn += '<a class="b_but edit" onclick="approvalArchive(\'' + row.id + '\')">审批通过</a>';
                            }
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
                    layer.alert('该案件没有任何嫌疑/行为人！');
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
                    }

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
                            layer.alert('已开始将所有卷按照嫌疑人顺序智能排序，请注意右上角提醒');
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
                //确定当前案件类别
                if (reV.value.casetype === '行政') {
                    $('#createSFC').remove();//行政案件只有一个基础卷
                    $('#suspectOrder').html('行为人排序');//行政叫行为人
                }
                if (reV.value.casestate !== '案综案件'){
                    $('#recordSync').remove();//合案拆案没有同步文书按钮  因为这个已经不是案综的案件了
                }
                if (reV.issuspectorder) {
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
                } else {
                    $('#issuspectorder').html('案卷未自动排序，现已为您提前排序，请稍后刷新网页');
                    //弹出给嫌疑人排序窗口
                    // suspectOrder(ajid);
                    $.post({
                        url: '/ManualSort/manualSort',
                        data: {caseId: reV.value.id},
                        success: (re) => {
                            const reV = JSON.parse(re);
                            if ('success' === reV.message) {
                                layer.msg("整理成功");
                                location.reload();
                            } else {
                                layer.alert("整理失败");
                            }
                        }
                    })
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



    //文书同步按钮
    $('#recordSync').click(function () {
        layer.alert('已为您快速同步文书，请稍后...');
        $.post({
            url: '/tempRemedial/supplementaryRecordsByAjid',
            data: {
                ajid: ajid
            }, success: function (re) {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    layer.alert('同步成功！')
                } else {
                }
            }
        })
    })

    //操作说明
    $('#promptAlert').click(function () {
        layer.open({
            icon: 1,
            type: 2,
            title: '流程提示',
            skin: 'layui-layer-lan',
            maxmin: false,
            shadeClose: true, //点击遮罩关闭层
            area: ['1111px', '600px'],
            content: '/model/ajcx/prompt.html'
        });
    })

    //完整性校验
    $('#integralityCheck').click(function () {
        layer.alert('已开始为所有卷进行完整性校验，请稍后...');
        $.post({
            url: '/tempRemedial/integralityCheck',
            data: {
                ajid: ajid
            }, success: function (re) {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {

                    document.getElementById('integralityCheckMessgae').innerHTML=reV.value
                    layer.closeAll();
                    layer.open({
                        id: 86016,
                        type: 1,
                        title: '校验结果',
                        skin: 'layui-layer-lan',
                        maxmin: false,
                        shadeClose: true, //点击遮罩关闭层
                        shade: [0.8, '#393D49'],
                        closeBtn: 1,
                        area: ['1000px', '600px'],
                        content: $('#integralityCheckDiv')
                    });
                } else {
                    layer.alert('校验失败');
                }
            }
        })
    })
})