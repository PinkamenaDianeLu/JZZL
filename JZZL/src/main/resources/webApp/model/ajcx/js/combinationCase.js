/**
 * @author Mrlu
 * @createTime 2020/9/25
 * @url webApp.model.ajcx.js
 * @describe 合案
 * @dependence [jquery,layer,utils,bootstrapTableUtil]
 *
 * */


var ajcxTable = (function () {

    let tableObject;
    let appurtenances;//辅案id  Set()
    let caseinfoid;//主案id

    const searchParam = function (ajbh, casename) {
        this.ajbh = ajbh;
        this.casename = casename;
        this.caseinfoid = caseinfoid;
    };

    //搜索内容
    function getSearchParam() {
        let reS = new searchParam();
        //去掉原有的A23
        reS.ajbh =$('#ajbh').val().trim();
        reS.casename = $('#casename').val().trim();
        return reS;
    }

    /**
     * 查看案件
     * @author MrLu
     * @param id 案件信息表id
     * @param casename 案件名称
     * @param ajbh 案件编号
     * @createTime  2020/9/25 9:56
     * @return    |
     */
    this.choiceCase = function (id, casename, ajbh) {
        //判断是否已选中
        if (!appurtenances.has(id)) {
            let caseElement = utils.createElement.createElement({
                tag: 'div', attrs: {
                    class: 'col-sm-12 fua',
                    id: 'aCase' + id
                }, arg: '<span>案件名称：' + casename + '</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;案件编号：<span>' + ajbh + '</span>'
            });
            // 取消按钮
            let cancelCase = utils.createElement.createElement({
                tag: 'i', attrs: {
                    class: 'glyphicon glyphicon-remove i_right'
                }
            });
            //关闭按钮
            cancelCase.addEventListener('click', function () {
                appurtenances.delete(id);
                caseElement.remove();
            });
            caseElement.appendChild(cancelCase);
            $('#appurtenance').append(caseElement);
            appurtenances.add(id)
        } else {
            layer.alert('该案件已被选择，请勿重复选择');

        }
    };


    function loadTable() {
        tableObject = createTable({
            tableId: 'ajcxTable',
            searchUrl: '/CaseManager/selectPeopleCaseForCombinationPage',
            column: [{
                field: 'JQBH',
                title: '警情编号'
            }, {
                field: 'AJBH',
                title: '案件编号',

            }, {
                field: 'CASENAME',
                title: '案件名称',
                formatter: (value, row) => {
                    return utils.beautifulTitle(value, 13);
                }
            }, {
                field: 'BARXM',
                title: '主办人'
            }, {
                title: '操作',
                align: 'center',
                formatter: function (value, row, index) {
                    return '<a class="b_but edit" onclick="choiceCase(\'' + row.CASEINFOID + '\',' + '\'' + row.CASENAME + '\',\'' + row.AJBH + '\')">选择</a>';
                }
            }], param: function () {
                return getSearchParam();
            }
        });
    }

    function getAppurtenances() {
        if (appurtenances.size === 0) {
            layer.alert('您没有选择任何辅案！')
            return false;
        } else {
            return Array.from(appurtenances).join(',');
        }

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

    function _ajcxTable(caseinfoidP) {
        caseinfoid = caseinfoidP;
        appurtenances = new Set();
        loadTable();
    }

    _ajcxTable.prototype = {
        searchTable, getAppurtenances
    };
    return _ajcxTable;
})();

var combination = (function () {
    let caseinfoid;

    function loadSuspects(appurtenances) {
        $.post({
            url: '/CaseManager/selectSuspectByCaseIds',
            data: {
                maincaseinfoid: caseinfoid,
                appurtenances
            },
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    $('#suspectsListA').empty();
                    mainSuspectElementFactory(reV.mainSuspect);
                    suspectElementFactory(reV.appurtenanceSuspect);
                } else {
                    layer.alert('案件无嫌疑人！');
                }
            }
        });

    }

    /**
     * 开始合案
     * @author Mrlu
     * @param appurtenances 被选中的辅案
     * @createTime  2021/3/10 16:22
     * @return    |
     */
    function startCombinationCase(appurtenances) {
        //被选中的嫌疑人们
        let suspectids = new Array();
        for (let thisSelectedSuspect of $('#suspectsListB').find('p')) {
            suspectids[suspectids.length] = thisSelectedSuspect.id;
        }
        if (suspectids.length === 0) {
            layer.confirm('合并的案件没有选择任何嫌疑人，是否继续？', {
                btn: ['取消', '确定']//按钮
            }, function (index) {
                layer.close(index);
                return false;
            });
        }

        layer.confirm('确定合并案件？', {
            btn: [ '确定','取消']//按钮
        }, function (index) {
            alert('已为您开始合并案件，合并进度请注意右上角通知');
            $.post({
                url: '/CaseManager/combinationCase',
                data: {
                    mainCaseId: caseinfoid,
                    suspectids: suspectids.join(','),
                    fcaseids: appurtenances,
                    newcasename: $('#combinationCasename').val()
                },
                success: (re) => {
                    const reV = JSON.parse(re);
                    if ('success' === reV.message) {
                        layer.alert("合案完成")
                        const index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                        parent.layer.close(index);
                    } else {
                        layer.alert('数据异常，无法完成合案');
                    }
                }
            });
            const PIndex = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
            parent.layer.close(PIndex);
        });



    }

    function mainSuspectElementFactory(suspectArrary) {
        let cdf = document.createDocumentFragment();
        for (let thisSuspect of suspectArrary) {
            let thisSuspectEle = utils.createElement.createElement({
                tag: 'p',
                attrs: {id: thisSuspect.id},
                arg: '<span>' + thisSuspect.suspectname + '</span><span>' + thisSuspect.suspectidcard + '</span><span>主案</span>'
            });
            //点击穿梭
            thisSuspectEle.onclick = () => shuttleBoxA(thisSuspectEle);
            $(cdf).append(thisSuspectEle);
        }
        $('#suspectsListA').append(cdf);
    }

    function suspectElementFactory(suspectArrary) {
        let cdf = document.createDocumentFragment();
        for (let thisSuspect of suspectArrary) {
            let caseinfo = thisSuspect.caseinfo;
            let suspectAry = thisSuspect.suspects;
            for (let thisS of suspectAry) {
                let thisSuspectEle = utils.createElement.createElement({
                    tag: 'p',
                    attrs: {id: thisS.id},
                    arg: '<span>' + thisS.suspectname + '</span><span>' + thisS.suspectidcard + '</span>' + '</span><span>' + caseinfo.casename + '</span>'
                });
                //点击穿梭
                thisSuspectEle.onclick = () => shuttleBoxA(thisSuspectEle);
                $(cdf).append(thisSuspectEle);
            }
        }
        $('#suspectsListA').append(cdf);
    }

    /**
     * 穿梭框A的事件内容
     * @author Mrlu
     * @param thisSuspectEle 嫌疑人ELEment
     * @createTime  2021/3/4 16:25
     */
    function shuttleBoxA(thisSuspectEle) {
        $('#suspectsListB').append(thisSuspectEle);//嫌疑人穿梭至B框  已选
        thisSuspectEle.onclick = () => shuttleBoxB(thisSuspectEle);
    }

//穿梭框B的事件内容
    function shuttleBoxB(thisSuspectEle) {
        $('#suspectsListA').append(thisSuspectEle);//嫌疑人穿梭至A框
        thisSuspectEle.onclick = () => shuttleBoxA(thisSuspectEle);
    }

    function _combination(caseinfoidP, appurtenances) {
        caseinfoid = caseinfoidP;
        loadSuspects(appurtenances)
    }

    _combination.prototype = {startCombinationCase};
    return _combination;
})();

$(function () {
    const caseinfoid = utils.getUrlPar('caseinfoid');//主案id
    const casenameP = utils.getUrlPar('casename');//主案案件名称（Base64）
    const caseName = utils.Base64.atou(casenameP);//主案案件名称
    $('#casenameMain').html(caseName);
    $('#combinationCasename').val(caseName + '   ' + '合案');
    let at = new ajcxTable(caseinfoid);
    $('#searchBtn').click(function () {
        at.searchTable();
    });
    //下一步.
    $('#nextBtn').click(function () {

        let appurtenances = at.getAppurtenances();
        if (appurtenances) {
            $('#caseSelect').hide();
            $('#suspectSelect').show();
            let ca = new combination(caseinfoid, appurtenances);
            //开始合案
            $('#startCombinationCase').unbind().click(function () {
                ca.startCombinationCase(appurtenances);
            })
        }

    });
    //关闭按钮
    $('.close_but').click(function () {
        const index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index);
    })
});