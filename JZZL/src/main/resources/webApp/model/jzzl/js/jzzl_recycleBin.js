/**
 * @author Mrlu
 * @createTime 2020/10/12
 * @dependence jquery.min.js,layer.js,utils.js,jzzl_imgLoad.js
 * @describe 卷宗整理 回收站
 *
 */
var recycleBin = (function () {
    let seqid;//送检次序id
    let recycleRecordsMap;//
    let archiveIndex;//loadArchiveIndex实例
    let recordImgLoadObj;//图片加载对象
    function loadIndex(id) {
        seqid = id;
        $.post({
            url: '/ArrangeArchives/getArchiveIndex',
            data: {id},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    recycleRecordsMap = new Map();
                    utils.functional.forEach(reV.value, function (thisType) {
                        loadArchiveType(thisType);
                    });
                } else {
                    alert('文书信息无法加载');
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
        let div = document.createElement('div');
        div.id = 'recycleP' + thisType.id;
        let thisTypeEle = utils.createElement.createElement({
            tag: 'div', attrs: {
                id: 'recycleTypeTitle' + thisType.id
            }, arg: '<i class="u_up"></i><a><p>' + thisType.archivetypecn + '</p>'
        });
        thisTypeEle.addEventListener('click', function () {
            console.log('点一下收缩或展示' + thisType.archivetypecn)
        });
        div.append(thisTypeEle);
        $('#recycleBinArchiveIndex').append(div);
        //加载文书
        loadRecords(thisType.id, div.id)

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
            data: {id: typeId, isDelete: 1},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {

                    utils.functional.forEach(reV.value, function (thisRecord) {
                        $('#'+liD).append(createRecordDiv(thisRecord));
                    });
                } else {
                    throw  '(回收站)该卷文书加载失败：' + typeId;
                }
            }
        });
    }

    function createRecordDiv(thisRecord) {
        let record = thisRecord.record;
        if (!record) {
            throw  '(回收站)无文书信息，无法加载该文书！！';
        }
        let key = 'recycleDd' + record.id;
        let div = utils.createElement.createElement({
            tag: 'div', attrs: {
                id: key
            }
        });
        let p = utils.createElement.createElement({
            tag: 'p', attrs: {}, arg: '<a class="recordname">' + record.recordname + '</a>'
        });
        //文书缓存至recordsMap
        recycleRecordsMap.set(key, {
            id: record.id,
            archivetypeid: record.archivetypeid,
            recordname: record.recordname,
            recordscode: record.recordscode
        });//缓存信息
        p.append(createButtons(key));//加载按钮
        //加载文书目录
        createFileIndex(thisRecord.files,p);
        //点击显示对应图片
        p.addEventListener('click', function () {
            //加载文书图片 按照子标签的顺序加载
            let fileOrder = utils.functional.map($(div).find('.v3'), function (thisFileIndex) {
                return $(thisFileIndex).attr('id').replace('fileIndex', '');
            });
            //点击显示对应图片
            recordImgLoadObj = recordImgLoad({
                recordIdP: record.id,
                fileOrder: fileOrder
            });

        });
        //加载文书
        div.append(p);
        return div;
    }
    function createFileIndex(files,div) {
      /*  let a=   utils.functional.map(files,createFilesDiv);
        console.log(Array.from(a))
        return Array.from(a);*/
        for (let thisFile of files) {
            div.append(createFilesDiv(thisFile));
        }
    }

    function createFilesDiv(thisFile) {
        const key = 'fileIndex' + thisFile.filecode;
        recycleRecordsMap.set(key, {
            filecode: thisFile.filecode,
            filename: thisFile.filename,
            fileurl: thisFile.fileurl,
            archiverecordid: thisFile.archiverecordid
        });//缓存信息
        let div = utils.createElement.createElement({
            tag: 'div', attrs: {
                id: key,
                class: 'v3',
            }
        });
        let p = utils.createElement.createElement({
            tag: 'p', attrs: {}, arg: '<a class="filename">' + thisFile.filename + '</a>'
        });
        // 这里不能使用addEventListener添加事件，否则jquey的unbind无法清除监听
        $(div).click(function () {
            //此时是列表第一次加载时、故图片位置为fileIndexing.i-1  当该元素被拖拽、上下移按钮后 要重新添加事件
            loadFileImg(div, thisFile.archiverecordid);
        })
        p.append(createButtons(key));
        div.append(p);
        console.log(div)
        return div;
    }
    function loadFileImg(indexDiv, recordId) {
        let filecode = $(indexDiv).attr('id').replace('fileIndex', '');//文件代码
        //判断是否还在原本的文文书内
        if (+recordId === +recordImgLoadObj.getRecordId()) {
            //同文书点击
            //获取文书order
            let thumbnail = document.getElementById('thumbnail' + filecode);
            recordImgLoadObj.jumpImg(thumbnail);
        } else {
            let fileOrder = utils.functional.map($('#dd' + recordId).find('.v3'), function (thisFileIndex) {
                return $(thisFileIndex).attr('id').replace('fileIndex', '');
            })
            //点击另一个文书的图片  加载另一个文书
            recordImgLoadObj = recordImgLoad({
                recordIdP: recordId,
                fileOrder: fileOrder,
                callback:function () {
                    //获取图片缩略
                    let thumbnail = document.getElementById('thumbnail' + filecode);
                    recordImgLoadObj.jumpImg(thumbnail);
                }
            });
        }
    }
    /**
     * 创建还原按钮
     * @author MrLu
     * @param ddId
     * @createTime  2020/10/12 14:40
     * @return    |
     */
    function createButtons(ddId) {
        // const thisRecord = recycleRecordsMap.get(ddId);
        let div = document.createElement('div');
        div.setAttribute('class', 'hy_a');
        let a = document.createElement('a');
        a.innerHTML = '还原';
        a.addEventListener('click', function () {
            restored(ddId)
            $(this).remove();
        })
        div.append(a);
        return div;
    }

    /**
     * 添加进回收站
     * @author MrLu
     * @param ddId 被删除的文书的ddid也是recordsMap的key
     * @param delObj loadArchiveIndex中的recordsMap中对应ddid的value
     * @createTime  2020/10/12 10:17
     * @return    |
     */
    function addRecycleBin(ddId, delObj) {
        console.log(delObj)
        delObj.id = ddId.replace('dd', '');
        let li = createRecordDiv(delObj)
        $('#recycleP' + delObj.archivetypeid).append(li);
    }

    /**
     * 还原、恢复
     * @author MrLu
     * @param ddId
     * @createTime  2020/10/12 14:36
     * @return    |
     */
    function restored(ddId) {
        //获取保存的文书对象
        const thisRecord = recycleRecordsMap.get(ddId);
        let typeFd = $('#P' + thisRecord.archivetypeid + ' dd').last();//该类型文书的封底
        let lastRecord = typeFd.prev('dd');//该类型文书的最后一个文书
        //重返阳间
        //通过传递一个假的indexing  使createRecordDiv方法不需要再查找dom元素 降低损耗
        let indexing = {
            i: 0,
            f: 0
        };
        let reArchive = archiveIndex.createRecordDiv(thisRecord, indexing);//重塑肉身
        typeFd.before(reArchive);//返回人界
        console.log(reArchive)
        archiveIndex.reloadButton($(reArchive));//重新加载按钮
        archiveIndex.reloadButton(lastRecord);//重新加载按钮
        //把回收站的删除
        $('#' + ddId).remove();
        recycleRecordsMap.delete(ddId);
        //重新加载被还原文书的上一个文书的按钮
    }

    /**
     * 根据不同的文书类型保存回收站的文书信息
     * @author MrLu
     * @param  oriTypeId 原有的typeid
     * @param  newTypeId 新建的typeid
     * @createTime  2020/10/14 16:08
     * @return    |
     */
    function saveRecycleIndexSortByType(oriTypeId, newTypeId) {
        const recycleRecordsList = $('#recycleP' + oriTypeId).find('dd');
        if (recycleRecordsList.length > 0) {
            //该类型回收站有东西
            const saveD = function (id, name, typeid, order) {
                this.id = id;
                this.name = name;
                this.typeid = typeid;
                this.order = order;
            };
            let saveData = [];//用以存储saveD的对象数组
            let i = 1;
            utils.functional.forEach(recycleRecordsList, function (thisRecord) {
                let key = $(thisRecord).attr('id');
                let thisRecycleRecord = recycleRecordsMap.get(key);
                saveData[saveData.length] = new saveD(
                    key.replace('recycleDd', ''),
                    thisRecycleRecord.recordname,
                    newTypeId,//
                    i++);
                //.replace('recycleDd', '');
            })
            $.post({
                url: '/ArrangeArchives/saveRecycleIndexSortByType',
                data: {
                    saveData: JSON.stringify(saveData),
                    newTypeid: newTypeId,
                },
                success: (re) => {
                    const reV = JSON.parse(re);
                    if ('success' === reV.message) {
                        console.log('回收站保存成功')
                    } else {
                    }
                }
            });
        }
    }

    let _recycleBin = function (lai) {
        if (!lai) {
            throw '未传入loadArchiveIndex实例，回收站功能无法加载！';
        }
        console.log('-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=开始回收站-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=')
        archiveIndex = lai;
    }
    _recycleBin.prototype = {
        loadIndex, addRecycleBin, saveRecycleIndexSortByType
    }
    return _recycleBin;
})();