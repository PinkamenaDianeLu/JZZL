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
                        $('#' + liD).append(createRecordDiv(thisRecord));
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
                id: key,
                class: 'v2'
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
        createFileIndex(thisRecord.files, p);
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

    function createFileIndex(files, div) {
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
                callback: function () {
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
            restored(ddId);//调用还原方法
            $(this).remove();//删除该对象
        })
        div.append(a);
        return div;
    }

    /**
     * 添加进回收站
     * @author MrLu
     * @param recordId 被删除的文书的id 是纯的id
     * @param fileCodes[]  被删除的文件的filecode 是纯的filecode 是个数组 方便与批量删除，当fileCodes为undefined时将删除整个文书
     * @createTime  2020/10/12 10:17
     */
    function addRecycleBin(recordId, fileCodes) {
        //判断是删除整个文书还是删除单个文件
        if ('undefined' === typeof fileCodes) {
            //删除整个文书
            $.post({
                url: '/ArrangeArchives/createRecordByRecordId',
                data: {recordid: recordId},
                success: (re) => {
                    const reV = JSON.parse(re);
                    if ('success' === reV.message) {
                        let thisRecord = reV.value;
                        $('#recycleDd' + thisRecord.record.id).remove();//如果有原有的把原有的删除
                        $('#recycleP' + thisRecord.record.archivetypeid).append(createRecordDiv(thisRecord));//整个文书全部挪入回收站
                    } else {
                        throw '文书添加回收站失败';
                    }
                }
            });
        } else {
            //删除文件
            //是否已经拥有此文书了
            if (recycleRecordsMap.has('recycleDd' + recordId)) {
                //有此文书啦
                $.post({
                    url: '/ArrangeArchives/loadFilesByFileCodes',
                    data: {filecode: fileCodes},
                    success: (re) => {
                        const reV = JSON.parse(re);
                        if ('success' === reV.message) {
                            for (let i of reV.value) {
                                let fileDiv = createFilesDiv(i);//生成对应文书
                                $('#recycleDd' + recordId).find('p').append(fileDiv);
                            }
                        } else {
                            throw '文件添加回收站失败';
                        }
                    }
                });
            } else {
                //没有文书 新建文书目录再append
                $.post({
                    url: '/FileManipulation/createRecycleRecordByFiles',
                    data: {filecodes: fileCodes},
                    success: (re) => {
                        const reV = JSON.parse(re);
                        if ('success' === reV.message) {
                            let thisRecord = reV.value;
                            $('#recycleP' + thisRecord.record.archivetypeid).append(createRecordDiv(thisRecord));
                        } else {
                            throw '文件添加回收站失败';
                        }
                    }
                });
            }


        }

        /*  let li = createRecordDiv(delObj)
          $('#recycleP' + delObj.archivetypeid).append(li);*/
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
        let thisRecord = $('#' + ddId);
        let isRecord = thisRecord.hasClass('v2');
        if (isRecord) {
            //查找该文书下的已删除文件
            let fileCodes = utils.functional.map(thisRecord.find('.v3'), function (thisFile) {

                return $(thisFile).attr('id').replace('fileIndex', '');
            })
            //还原整个文书
            archiveIndex.restored(ddId.replace('recycleDd', ''), fileCodes.toString());
            //删除自己
            thisRecord.remove();
            recycleRecordsMap.delete(ddId);
        } else {
            //还原单个文件
            thisRecord = thisRecord.parent().parent('.v2');//变成文书
            console.log(thisRecord);
            //判断是否是最后一张图片 如果是的话也删除文书
            if (thisRecord.find('.v3').length === 1) {
                restored(thisRecord.attr('id'));
            } else {
                //单个文件
                archiveIndex.restored(thisRecord.attr('id').replace('recycleDd', ''), ddId.replace('fileIndex', ''));
                $('#' + ddId).remove();
                recycleRecordsMap.delete(ddId);
            }
        }

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