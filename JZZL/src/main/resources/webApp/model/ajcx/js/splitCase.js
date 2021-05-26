/**
 * 加载信息
 * @author Mrlu
 * @createTime  2021/3/4 15:12
 * @return    |
 */
var loadMessage = (function () {
    let caseinfoid;//案件表id

    /**
     * 加载案件信息
     * @author Mrlu
     * @createTime  2021/3/4 15:15
     */
    function loadCaseMessage() {
        $.post({
            url: '/CaseManager/selectCaseMessage',
            data: {caseinfoid: caseinfoid},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    console.log(reV.value);
                    $('#casename').val(reV.value.casename);//案件名称
                    $('#jqbh').html(reV.value.jqbh);//案件名称
                    $('#ajbh').html(reV.value.ajbh);//案件名称
                    $('#sfcnumber').html(reV.value.sfcnumber);//案件名称
                } else {
                    layer.alert('案件信息加载失败！');
                    window.close();
                }
            }
        });
    }

    /**
     * 加载案件的嫌疑人
     * @author Mrlu
     * @createTime  2021/3/4 15:49
     * @return    |
     */
    function loadSuspects() {
        $.post({
            url: '/CaseManager/selectSuspectByCaseId',
            data: {caseinfoid: caseinfoid},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    console.log(reV.value);
                    let cdf = document.createDocumentFragment();
                    for (let thisSuspect of reV.value) {
                        let thisSuspectEle = utils.createElement.createElement({
                            tag: 'p',
                            attrs: {id: thisSuspect.id},
                            arg: '<span>' + thisSuspect.suspectname + '</span><span>' + thisSuspect.suspectidcard + '</span>'
                        });
                        //点击穿梭
                        thisSuspectEle.onclick = () => shuttleBoxA(thisSuspectEle);
                        $(cdf).append(thisSuspectEle);
                    }
                    $('#suspectsListA').empty().append(cdf);
                } else {
                    layer.alert('案件无嫌疑人！');
                }
            }
        });

    }


    /**
     * 开始拆案
     * @author Mrlu
     * @param
     * @createTime  2021/3/5 15:41
     * @return    |
     */
    function startSpliCase() {
        //被选中的嫌疑人们
        let suspectids = new Array();
        for (let thisSelectedSuspect of $('#suspectsListB').find('p')) {
            suspectids[suspectids.length] = thisSelectedSuspect.id;
        }
        if (suspectids.length === 0) {
            if (!confirm('拆分的案件没有选择任何嫌疑人，是否继续？')) {
                return false;
            }
        }

        //没被选中的嫌疑人们
        let notsuspectids = new Array();
        for (let thisSelectedSuspect of $('#suspectsListA').find('p')) {
            notsuspectids[notsuspectids.length] = thisSelectedSuspect.id;
        }
        alert('您的案件已经开始拆分，请注意左上角通知！');
        $.post({
            url: '/CaseManager/splitCase',
            data: {
                oricaseid: caseinfoid,
                suspectids: suspectids.join(','),
                notsuspectids: notsuspectids.join(',')
                , newcasename: $('#casename').val()
            },
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    alert('拆案成功！');
                    const index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                    parent.layer.close(index);
                } else {
                    alert('数据异常，案件拆分失败');
                }
            }
        });
        const index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index);

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

    function _loadMessage(idP) {
        caseinfoid = idP;
        //加载被拆案件的一些信息
        loadCaseMessage();
        loadSuspects();
    }

    _loadMessage.prototype = {startSpliCase};
    return _loadMessage;
})();
$(function () {
    //caseinfoid
    const caseinfoid = utils.getUrlPar('caseinfoid');
    //加载
    let lm = new loadMessage(caseinfoid);

    //拆案按钮
    $('#startSplitCase').click(function () {
        lm.startSpliCase();
    })

});