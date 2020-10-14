/**
 * @author Mrlu
 * @createTime 2020/10/8
 * @dependence 依赖js
 * @describe 卷宗整理
 *
 */

var loadArchiveIndex = (function () {
    let seqid;//送检次序id
    /*
    *文书map,因为文书是多卷异步加载的，所以该map中的顺序并不可靠
    *key:dd+文书id value:{recordname(文书名),recordscode(文书代码),archivetypeid(文书类别id)}
    * */
    let recordsMap;//

    let recycleBinObj;//回收站对象

    function loadIndex(id) {
        seqid = id;
        $.post({
            url: '/ArrangeArchives/getArchiveIndex',
            data: {id},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    recordsMap = new Map();
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
        });
        thisTypeEle.addEventListener('click', function () {
            console.log('点一下收缩或展示' + thisType.archivetypecn)
        });
        li.append(thisTypeEle);
        $('#archiveIndex').append(li);
        //加载文书
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
            data: {id: typeId, isDelete: 0},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {

                    //索引信息
                    let indexing = {
                        i: 0,
                        f: reV.value.length
                    };
                    utils.functional.forEach(reV.value, function (thisRecord) {
                        $('#' + liD).append(createRecordDD(thisRecord, indexing));
                        indexing.i++; //此行必须在$('#' + liD).append的后边
                    });
                } else {
                    throw  '该卷文书加载失败：' + typeId;
                }
            }
        });
    }

    /**
     * 创建文书的dd element
     * @author MrLu
     * @param thisRecord 文书对象 数据库
     * @param indexing {i 数组下标,f 数组长度} 可为空
     * @createTime  2020/10/12 9:37
     * @return  HTMLDivElement  |
     */
    function createRecordDD(thisRecord, indexing) {
        if (!thisRecord.id) {
            throw  '文书id未获取，无法加载该文书！！';
        }
        let key = 'dd' + thisRecord.id;
        let dd = utils.createElement.createElement({
            tag: 'dd', attrs: {
                id: key
            }, arg: '<a><p class="recordname">' + thisRecord.recordname + '</p></a>'
        });
        //文书缓存至recordsMap
        recordsMap.set(key,
            {
                recordname: thisRecord.recordname,
                recordscode: thisRecord.recordscode,
                archivetypeid: thisRecord.archivetypeid
            });//缓存信息
        //加载文书
        dd.append(createButtons(key, indexing));
        return dd;
    }

    /**
     * 创建下面那四个按钮
     * @author MrLu
     * @param ddId recordsMap的key
     * @param indexing
     * @createTime  2020/10/10 9:52
     * @return  HTMLDivElement  |
     */
    function createButtons(ddId, indexing) {
        const thisRecord = recordsMap.get(ddId);
        let div = document.createElement('div');
        //判断卷类型
        if (!('ZL001' === thisRecord.recordscode || 'ZL002' === thisRecord.recordscode)) {
            //封皮//封底  没有操作按钮

            div.setAttribute('class', 'tools');
            //用文书代码分辨html还是图片
            div.append(upButton(ddId, indexing));//加载上移按钮
            div.append(downButton(ddId, indexing));//加载下移按钮
            div.append(renameButton(ddId));//加载重命名按钮
            div.append(delButton(ddId));//加载删除按钮
        }
        return div;
    }

    /**
     * 上一位按钮
     * @author MrLu
     * @param ddId
     * @param indexing {i 数组下标,f 数组长度} 可为空
     * @createTime  2020/10/10 14:14
     * @return  HTMLDivElement  |
     */
    function upButton(ddId, indexing = {}) {
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
            let prevOne = $('#' + ddId).prevAll('dd');//获取上一个元素
            //当前面只有一个元素（封皮时） 无法上移
            if (prevOne.length < 2) {
                haveFun = false;
            }
        }
        let up = utils.createElement.createElement({
            tag: 'a', attrs: {
                title: '上移',
            }, arg: '<i class="' + (haveFun ? 'ico' : 'ico01') + ' up"></i>'
        });
        if (haveFun) {
            up.addEventListener('click', function () {
                upFun(ddId);
            })
        }
        return up;
    }

    /**
     * 下一位按钮
     * @author MrLu
     * @param ddId
     * @param indexing {i 数组下标,f 数组长度} 可为空
     * @createTime  2020/10/10 14:14
     * @return  HTMLDivElement  |
     */
    function downButton(ddId, indexing = {}) {
        let haveFun = true;//是否激活方法
        if (indexing.i && indexing.f) {
            //此参数有值说明为首次加载
            if (indexing.f === (indexing.i + 2)) {
                //未除了封皮以外第一个文书
                haveFun = false;
            }
        } else {
            // 当元素已经加载时判断
            //此时已经移动完了  重新加载按钮
            let nextOne = $('#' + ddId).nextAll('dd');//获取下位元素
            //当后面只有一个元素（封底时） 无法下移
            if (nextOne.length < 2) {
                haveFun = false;
            }
        }
        let down = utils.createElement.createElement({
            tag: 'a', attrs: {
                title: '下移',
            }, arg: '<i class="' + (haveFun ? 'ico' : 'ico01') + ' Down"></i>'
        });
        if (haveFun) {
            down.addEventListener('click', function () {
                downFun(ddId);
            })
        }
        return down;
    }

    /**
     * 重命名按钮
     * @author MrLu
     * @param ddId
     * @createTime  2020/10/10 15:13
     * @return  HTMLDivElement  |
     */
    function renameButton(ddId) {
        let haveFun = true;//是否激活方法
        let edit = utils.createElement.createElement({
            tag: 'a', attrs: {
                title: '编辑'
            }, arg: '<i class="' + (haveFun ? 'ico' : 'ico01') + ' aedit"></i>'
        })
        if (haveFun) {
            edit.addEventListener('click', function () {
                renameFun(ddId);
            })
        }
        return edit;

    }

    /**
     * 删除按钮
     * @author MrLu
     * @param ddId
     * @createTime  2020/10/10 15:13
     * @return  HTMLDivElement  |
     */
    function delButton(ddId) {
        let haveFun = true;//是否激活方法
        let del = utils.createElement.createElement({
            tag: 'a', attrs: {
                title: '删除'
            }, arg: '<i class="' + (haveFun ? 'ico' : 'ico01') + ' delete"></i>'
        })
        if (haveFun) {
            del.addEventListener('click', function () {
                delFun(ddId);
            })
        }
        return del;
    }


    /**
     * 上移一位
     * @author MrLu
     * @param ddId
     * @createTime  2020/10/10 11:06
     */
    function upFun(ddId) {
        if (!ddId) {
            throw '未传入需要上传的id！cdd爬';
        }
        let dd = $('#' + ddId);
        let prevDD = dd.prev('dd');//上一个
        prevDD.before(dd);
        reloadButton(dd);//重新加载按钮
        reloadButton(prevDD);//重新加载按钮
    }

    /**
     * 下移一位
     * @author MrLu
     * @param ddId
     * @createTime  2020/10/10 14:11
     */
    function downFun(ddId) {
        if (!ddId) {
            throw '未传入需要上传的id！cdd爬';
        }
        let dd = $('#' + ddId);//要移动的
        let nextDD = dd.next('dd');//下一个
        nextDD.after(dd);//移动顺序
        reloadButton(dd);//重新加载按钮
        reloadButton(nextDD);//重新加载按钮
    }

    /**
     * 重命名
     * @author MrLu
     * @param ddId
     * @createTime  2020/10/10 14:12
     */
    function renameFun(ddId) {
        let thisP = $('#' + ddId).find('.recordname');
        thisP.attr('contenteditable', 'plaintext-only');//该p可编辑
        thisP.addClass('pinput');//更换class更换样式
        thisP.focus();//给予焦点
        //失去焦点事件
        thisP.blur(function () {
            $(this).removeAttr('contenteditable').unbind();//该p不可编辑 解除事件
            let thisOne = recordsMap.get(ddId);
            thisOne.recordname = $(this).html();
            recordsMap.set(ddId, thisOne);//缓存信息
            thisP.removeClass('pinput')
        });
        console.log(recordsMap.get(ddId));
        //
    }

    /**
     * 删除
     * @author MrLu
     * @param ddId
     * @createTime  2020/10/10 14:13
     */
    function delFun(ddId) {
        // try {
        $('#' + ddId).remove();
        //回收站
        recycleBinObj.addRecycleBin(ddId, recordsMap.get(ddId));
        //从recordsMap中删除
        recordsMap.delete(ddId);
        /*  } catch (e) {
              console.log(e.message);
              alert('该页面未加载回收站功能！')
          }*/


    }

    /**
     * 加载回收站对象
     * @author MrLu
     * @param recycleBin 实例化
     * @createTime  2020/10/12 10:08
     * @return    |
     */
    function loadRecycleBin(recycleBin) {
        recycleBinObj = recycleBin;
    }


    /**
     * 重新加载一个文书的按钮列表
     * @author MrLu
     * @param dd 该文书在列表中的dd(注意是一个jquery对象！)
     * @createTime  2020/10/11 12:42
     */
    function reloadButton(dd) {
        dd.find('div').remove();
        dd.append(createButtons(dd.attr('id')));
    }

    //保存数据
    /**
     * 保存文书目录的加载
     * @author MrLu
     * @createTime  2020/10/13 15:44
     */
    function saveData() {

        const archiveTypeList = $('#archiveIndex').find('li');
        if (archiveTypeList.length < 1) {
            alert('未正确加载文书目录，无法保存！');
            throw  '未正确加载文书目录，无法保存！';
        }

        $.post({
            url: '/ArrangeArchives/createNewSeq',
            data: {seqid},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    const newSeqId = reV.value;
                    saveArchiveIndexSortByType(archiveTypeList, newSeqId);
                } else {
                    throw  '未能创建新的整理记录';
                }
            }
        });


    }

    /**
     * 以文书类型保存整理顺序
     * @author MrLu
     * @param archiveTypeList li的list
     * @param newSeqId 新建的整理次序id
     * @createTime  2020/10/13 17:45
     * @return    |
     */
    function saveArchiveIndexSortByType(archiveTypeList, newSeqId) {
        /**
         *
         * @author MrLu
         * @param id 文书id
         * @param name 文书名
         * @param typeid typeId
         * @param order 文书顺序
         * @createTime  2020/10/13 16:12
         */
        const saveD = function (id, name, typeid, order) {
            this.id = id;
            this.name = name;
            this.typeid = typeid;
            this.order = order;
        };
        utils.functional.forEach(archiveTypeList, function (thisType) {
            let saveData = [];//用以存储saveD的对象数组
            const oriTypeId = $(thisType).attr('id').replace('P', '');
            $.each($(thisType).find('dd'), function (i, v) {
                let thisDDid = $(v).attr('id');//获取文书的id  该id也是recordsMap中的key
                let thisRecord = recordsMap.get(thisDDid);//通过key获取该文书的value
                if ('ZL001' === thisRecord.recordscode) {
                    i = -1;//卷宗封皮顺序永远是-1
                } else if ('ZL002' === thisRecord.recordscode) {
                    i = 99999;//封底顺序永远是99999  最后一个
                }
                saveData[saveData.length] = new saveD(thisDDid.replace('dd', ''), thisRecord.recordname, thisRecord.archivetypeid, i);

            })
            //数据保存到后台
            $.post({
                url: '/ArrangeArchives/saveArchiveIndexSortByType',
                data: {
                    saveData: JSON.stringify(saveData),
                    typeid: oriTypeId,
                    seqid: newSeqId
                },
                success: (re) => {
                    const reV = JSON.parse(re);
                    if ('success' === reV.message) {
                        const newTypeId = reV.value;
                        recycleBinObj.saveRecycleIndexSortByType(oriTypeId, newTypeId);
                        console.log('   保存成功')
                    } else {
                    }
                }
            });
        })
    }


    function _loadArchiveIndex() {
        console.log('-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=开始加载-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=')
    }

    _loadArchiveIndex.prototype = {
        loadIndex, loadRecycleBin, createRecordDD, reloadButton, saveData
    }
    return _loadArchiveIndex;
})();


$(function () {
    $('#userHeart').load('/userHeart.html');
    //送检的id
    const sfcId = utils.getUrlPar('id');
    //查询送检最后一次整理的记录
    $.post({
        url: '/ArrangeArchives/selectLastSeqBySfc',
        data: {id: sfcId},
        success: (re) => {
            const reV = JSON.parse(re);
            if ('success' === reV.message) {
                let lai = new loadArchiveIndex();
                lai.loadIndex(reV.value.id);
                let rcb = new recycleBin(lai);
                rcb.loadIndex(reV.value.id);
                lai.loadRecycleBin(rcb);

                //完成整理按钮
                $('#saveData').click(function () {
                    if (confirm('确定完成整理？')) {
                        //加载整理动画
                        lai.saveData();
                    }
                })
            } else {
            }
        }
    });


})