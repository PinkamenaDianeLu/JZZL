/**
 * @author Mrlu
 * @createTime 2020/10/8
 * @dependence 依赖js
 * @describe 卷宗整理
 *
 */

var loadArchiveIndex = (function () {
    let seqid;

    function loadIndex(id) {
        seqid = id;
        $.post({
            url: '/ArrangeArchives/getArchiveIndex',
            data: {id},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    utils.functional.forEach(reV.value, function (thisType) {
                        loadArchiveType(thisType);
                    });
                } else {
                    alert('文书信息无法加载');
                    // window.close();
                }
            }
        });
    }

    /**
     * 加载二级菜单
     * @author MrLu
     * @param thisType
     * @createTime  2020/10/9 11:28
     * @return    |
     */
    function loadArchiveType(thisType) {
        let li = document.createElement('li');
        li.id = 'P' + thisType.id;
        let thisTypeEle = utils.createElement.createElement({
            tag: 'dt', attrs: {
                id: 'typeTitle' + thisType.id
            }, arg: '<i class="u_up"></i><a><p>' + thisType.archivetypecn + '</p>'
        })
        thisTypeEle.addEventListener('click', function () {
            console.log('点一下收缩或展示' + thisType.archivetypecn)
        })
        li.append(thisTypeEle);
        $('#archiveIndex').append(li);
        loadRecords(thisType.id, li.id)

    }

    /**
     * 加载具体文书目录
     * @author MrLu
     * @param typeId
     * @param liD 所属的li的Id
     * @createTime  2020/10/9 11:28
     */
    function loadRecords(typeId, liD) {
        $.post({
            url: '/ArrangeArchives/getRecordsIndex',
            data: {id: typeId},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {

                    let indexing = {
                        i: 0,
                        f: reV.value.length
                    }
                    utils.functional.forEach(reV.value, function (thisRecord) {
                        $('#' + liD).append(createRecordDD(thisRecord, indexing));
                        indexing.i++;
                    })
                } else {
                }
            }
        });
    }

    function createRecordDD(thisRecord, indexing) {
        let dd = utils.createElement.createElement({
            tag: 'dd', attrs: {
                id: 'dd' + thisRecord.id
            }, arg: '<a><p>' + thisRecord.recordname + '</p></a>'
        })
        dd.append(createButtons(thisRecord, indexing));
        return dd;
    }

    /**
     * 创建下面那四个按钮
     * @author MrLu
     * @param thisRecord 文书
     * @param indexing
     * @createTime  2020/10/10 9:52
     * @return  HTMLDivElement  |
     */
    function createButtons(thisRecord, indexing) {
        let div = document.createElement('div')
        //判断卷类型
        if (!('ZL001' === thisRecord.recordscode || 'ZL002' === thisRecord.recordscode)) {
            //封皮//封底  没有操作按钮

            div.setAttribute('class', 'tools');
            //用文书代码分辨html还是图片
            div.append(upButton(thisRecord,indexing));
            div.append(downButton(thisRecord,indexing));
            div.append(renameButton(thisRecord));
            div.append(delButton(thisRecord));
        }

        return div;
    }

    /**
     * 上一位按钮
     * @author MrLu
     * @param thisRecord
     * @param indexing {i 数组下标,f 数组长度} 可为空
     * @createTime  2020/10/10 14:14
     * @return  HTMLDivElement  |
     */
    function upButton(thisRecord, indexing = {}) {
        let haveFun = true;//是否激活方法
        if (indexing.i) {
            //此参数有值说明为首次加载
            if (1 === indexing.i) {
                //未除了封皮以外第一个文书
                haveFun = false;
            }
        } else {
            //当元素已经加载时判断
            //不是封皮封底
            let prevOne = $('#dd' + thisRecord.id).prevAll('dd');//获取上一个元素
            //当前面只有一个元素（封皮时） 无法上移
            if (prevOne.length <= 2) {
                haveFun = false;
            }
        }
        let up = utils.createElement.createElement({
            tag: 'a', attrs: {
                title: '上移',
            }, arg: '<i class="' + (haveFun ? 'ico' : 'ico01') + ' up"></i>'
        })
        if (haveFun) {
            up.addEventListener('click', function () {
                upFun(thisRecord);
            })
        }
        return up;
    }

    /**
     * 下一位按钮
     * @author MrLu
     * @param thisRecord
     * @param indexing {i 数组下标,f 数组长度} 可为空
     * @createTime  2020/10/10 14:14
     * @return  HTMLDivElement  |
     */
    function downButton(thisRecord, indexing = {}) {
        let haveFun = true;//是否激活方法
        if (indexing.i && indexing.f) {
            //此参数有值说明为首次加载
            if (indexing.f === (indexing.i + 2)) {
                //未除了封皮以外第一个文书
                haveFun = false;
            }
        } else {
            //当元素已经加载时判断
            //不是封皮封底
            let nextOne = $('#dd' + thisRecord.id).nextAll('dd');//获取下位元素
            //当后面只有一个元素（封底时） 无法下移
            if (nextOne.length <= 2) {
                haveFun = false;
            }
        }
        let down = utils.createElement.createElement({
            tag: 'a', attrs: {
                title: '下移',
            }, arg: '<i class="' + (haveFun ? 'ico' : 'ico01') + ' Down"></i>'
        })
        if (haveFun) {
            down.addEventListener('click', function () {
                downFun(thisRecord);
            })
        }
        return down;
    }

    /**
     * 重命名按钮
     * @author MrLu
     * @param thisRecord
     * @createTime  2020/10/10 15:13
     * @return  HTMLDivElement  |
     */
    function renameButton(thisRecord) {
        let haveFun = true;//是否激活方法
        let edit = utils.createElement.createElement({
            tag: 'a', attrs: {
                title: '编辑'
            }, arg: '<i class="' + (haveFun ? 'ico' : 'ico01') + ' aedit"></i>'
        })
        if (haveFun) {
            edit.addEventListener('click', function () {
                renameFun(thisRecord);
            })
        }
        return edit;

    }

    /**
     * 删除按钮
     * @author MrLu
     * @param thisRecord
     * @createTime  2020/10/10 15:13
     * @return  HTMLDivElement  |
     */
    function delButton(thisRecord) {
        let haveFun = true;//是否激活方法
        let del = utils.createElement.createElement({
            tag: 'a', attrs: {
                title: '删除'
            }, arg: '<i class="' + (haveFun ? 'ico' : 'ico01') + ' delete"></i>'
        })
        if (haveFun) {
            del.addEventListener('click', function () {
                delFun(thisRecord);
            })
        }
        return del;
    }


    /**
     * 上移一位
     * @author MrLu
     * @param thisRecord
     * @createTime  2020/10/10 11:06
     */
    function upFun(thisRecord) {
        if (!thisRecord) {
            throw '未传入需要上传的id！cdd爬';
        }
        let dd=$('#dd' + thisRecord.id);
        dd.prev('dd').before(createRecordDD(thisRecord));
        dd.remove();
    }

    /**
     * 下移一位
     * @author MrLu
     * @param thisRecord
     * @createTime  2020/10/10 14:11
     */
    function downFun(thisRecord) {
        if (!thisRecord) {
            throw '未传入需要上传的id！cdd爬';
        }
        let dd=$('#dd' + thisRecord.id);
        //获取dd信息
        dd.next('dd').after(createRecordDD(thisRecord));
        dd.remove();
    }

    /**
     * 重命名
     * @author MrLu
     * @param thisRecord
     * @createTime  2020/10/10 14:12
     */
    function renameFun(thisRecord) {

    }

    /**
     * 删除
     * @author MrLu
     * @param thisRecord
     * @createTime  2020/10/10 14:13
     */
    function delFun(thisRecord) {

    }

    function _loadArchiveIndex() {

    }

    _loadArchiveIndex.prototype = {loadIndex}
    return _loadArchiveIndex;
})()

$(function () {
    $('#userHeart').load('/userHeart.html');
    //送检的id
    const seqId = utils.getUrlPar('id');
    let lai = new loadArchiveIndex();
    lai.loadIndex(seqId);

})