/**
 * @author Mrlu
 * @createTime 2020/10/28
 * @dependence 依赖js
 * @describe 卷宗目录
 *
 */



var recordCoverIndex = (function () {
    let PEle;
    let file;
    /**
     * 此页面标准数据格式
     * @author MrLu
     * @createTime  2020/10/29 10:01
     * @return    |
     */
    let recordObj = function (name, pagenumber, index, author, recorddate, wh, comment, recordid) {
        this.name = name;//文书名
        this.pagenumber = pagenumber;//页号
        this.index = index;//序号
        this.author = author;//责任人
        this.recorddate = recorddate;//文书开局日期
        this.wh = wh;//文书文号
        this.comment = comment;//注释
        this.recordid = recordid;//文书id
    };

    /**
     * 创建表格内的一行
     * @author MrLu
     * @createTime  2020/10/29 10:00
     * @return    |
     */
    function createIndexTr({
                               index, wh, zrz, tm, rq, pagenumber, comment
                           }) {
        let thisTr = document.createElement('tr');
        thisTr.setAttribute('class', 'indexTr');
        let thisIndexSpan = utils.createElement.createElement({
            tag: 'td',
            arg: '<input class="indexValue" name="index" value="' + index + '"/>'
        });
        let thisWhInput = utils.createElement.createElement({
            tag: 'td',
            arg: '<input  class="indexValue" name="wh" value="' + (wh || '') + '" />'
        });
        let thisZrzInput = utils.createElement.createElement({
            tag: 'td',
            arg: '<input class="indexValue" name="zrz"  value="' + zrz + '" />'
        });
        let thisTmInput = utils.createElement.createElement({
            tag: 'td',
            arg: '<input class="indexValue" name="tm"  value="' + tm + '" />'
        });
        let thisRqInput = utils.createElement.createElement({
            tag: 'td',
            arg: '<input class="indexValue" name="rq"  value="' + rq + '" />'
        });
        let thisPagenumberInput = utils.createElement.createElement({
            tag: 'td',
            arg: '<input class="indexValue" name="pagenumber"  value="' +(pagenumber>0?pagenumber:'')  + '" />'
        });
        let thisCommentInput = utils.createElement.createElement({
            tag: 'td',
            arg: '<input class="indexValue" name="comment"  value="' + comment + '" />'
        });
        thisTr.appendChild(thisIndexSpan);
        thisTr.appendChild(thisWhInput);
        thisTr.appendChild(thisZrzInput)
        ;thisTr.appendChild(thisTmInput);
        thisTr.appendChild(thisRqInput);
        thisTr.appendChild(thisPagenumberInput)
        ;thisTr.appendChild(thisCommentInput);
        return thisTr;
    }

    /**
     * 加载表格
     * @author MrLu
     * @param records 文书数组
     * @createTime  2020/10/29 10:33
     * @return    |
     */
    function loadTable(records) {
        let trs = [];
        for (let thisRecord of records) {
            trs[trs.length] = createIndexTr({
                index: thisRecord.index,
                wh: thisRecord.wh,
                zrz: thisRecord.author,
                tm: thisRecord.name,
                rq: thisRecord.recorddate||'-',
                pagenumber: thisRecord.pagenumber,
                comment: thisRecord.comment || ''
            })
        }
        $('.indexTr').remove();
        $('#indexTable').append(trs);
    }

    /**
     * 查询一个文书有多少页
     * @author Mrlu
     * @param
     * @createTime  2021/2/23 15:38
     * @return    |
     */
    function getPageNumByRecordId() {

    }

    /**
     * 一键生成按钮
     * @author MrLu
     * @createTime  2020/10/29 9:48
     * @return    |
     */
    let oneKeyGo = () => {
        const typeDate = parent.lai.getRecordIndexSort(PEle, true);
        console.log(typeDate);
        //循环生成
        let i = 1;//文书序号
        let lastPageCount = 1;
        let records = [];
        for (let thisRecord of typeDate) {

            $.post({
                url: '/FileManipulation/getRecordPageNum',
                async: false,
                data: {recordid: thisRecord.id},
                success: (re) => {
                    const reV = JSON.parse(re);
                    if ('success' === reV.message) {
                        let isXtws=thisRecord.recordObj.recordscode.indexOf('ZL')>-1;
                        records[records.length] = new recordObj(
                            thisRecord.name,
                            (isXtws?0:lastPageCount),
                            i++,
                            thisRecord.recordObj.author,
                            utils.timeFormat.timestampToDate(thisRecord.recordObj.effectivetime||''),
                            thisRecord.recordObj.recordwh,
                            ''
                        );
                        // lastPageCount += thisRecord.filecodes.split(',').length;
                        if (!isXtws){
                            //非系统文书 页码增加
                            lastPageCount += reV.value;
                        }

                    } else {
                        console.error('查询失败:' + thisRecord.id + ':' + thisRecord.name);
                    }
                }
            });

        }
        loadTable(records);
    }

    /**
     * 保存文书目录信息
     * @author MrLu
     * @param indexId
     * @createTime  2020/10/29 10:55
     * @return    |
     */
    function saveIndexInfo(indexId) {
        let recordInfo = [];
        utils.functional.forEach($('#indexTable').find('.indexTr'), function (thisTr) {

            let trValue = utils.functional.map($(thisTr).find('.indexValue'), function (thisIndex) {
                return $(thisIndex).val();
            })
            //name, pagenumber, index, author, recorddate, wh, comment
            console.log(trValue)
            recordInfo[recordInfo.length] = new recordObj(
                trValue[3],
                trValue[5],
                trValue[0],
                trValue[2],
                trValue[4],
                trValue[1],
                trValue[6]);
        });
        console.log(JSON.stringify(recordInfo));
        console.log(file);
        $.post({
            url: '/FileManipulation/saveFunArchiveRecordindex',
            data: {
                fileid: file.id,
                indexinfo: JSON.stringify(recordInfo),
                indexid: indexId

            },
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    getIndexInfo();
                    layer.msg('保存成功');
                } else {
                    layer.alert('保存失败，请重新登录再试');
                }
            }
        });
    }

    /**
     * 查询文书目录信息
     * @author MrLu
     * @param
     * @createTime  2020/10/29 16:03
     * @return    |
     */
    function getIndexInfo() {
        $.post({
            url: '/FileManipulation/selectFunArchiveRecordindexByType',
            data: {
                archiveseqid: file.archiveseqid,
                archivetypeid: file.archivetypeid
            },
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    if (reV.value) {
                        console.log(reV.value);
                        //重新加载目录信息
                        loadTable(JSON.parse(reV.value.indexinfo));
                    }
//为保存按钮上方法
                    $('#saveIndexInfo').unbind().click(function () {
                        if (!reV.value) {
                            saveIndexInfo(-1);
                        } else {
                            saveIndexInfo(reV.value.id);
                        }
                    })

                } else {
                }
            }
        });

    }

    function _recordCover(PEleP, fileP) {
        PEle = PEleP;
        file = fileP;
    }

    _recordCover.prototype = {getIndexInfo, oneKeyGo}

    return _recordCover;
})()


$(function () {
    const thisFile = parent.recordImgLoad.pValue;
    //得到该文书类型对象
    let PEle = document.getElementById('P' + thisFile.archivetypeid);
    if (PEle) {
        let rc = new recordCoverIndex(PEle, thisFile);
        /**
         * 一键生成按钮
         * @author MrLu
         * @createTime  2020/10/29 10:11
         */
        $('#oneKeyGo').click(function () {
            rc.oneKeyGo();
        });

        rc.getIndexInfo();


    } else {
        layer.alert('该文书信息无法获取，请刷新页面重试！');
    }


})