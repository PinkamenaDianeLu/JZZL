/**
 * @author Mrlu
 * @createTime 2020/10/8
 * @dependence jquery.min.js,layer.js,utils.js,jzzl_imgLoad.js,jzzl_recycleBin.js,jquery-ui.js
 * @describe 卷宗整理
 *
 * 恭喜您接手此部分代码 GGHF！ 👏
 *
 */

var loadArchiveIndex = (function () {
    let seqid;//送检次序id
    /*文书map,因为文书是多卷异步加载的，所以该map中的顺序并不可靠
    *key:dd+文书id value:{id(文书id),recordname(文书名),recordscode(文书代码),archivetypeid(文书类别id)}* */
    let recordsMap;
    /*key: filecode,value:{id: file的id,filename: 文件名,fileurl: 文件图片链接,archiverecordid: 文件所属文书id}*/
    let filesMap;//同样的文书图片map
    let recycleBinObj;//回收站对象
    let recordImgLoadObj;//图片加载对象

    let progressLength=0;

    function loadIndex(id) {
        seqid = id;
        $.post({
            url: '/ArrangeArchives/getArchiveIndex',
            data: {id},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    recordsMap = new Map();
                    filesMap = new Map();
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
     * 加载二级菜单 （****卷）
     * @author MrLu
     * @param thisType
     * @createTime  2020/10/9 11:28
     */
    function loadArchiveType(thisType) {
        let div = document.createElement('div');
        div.id = 'P' + thisType.id;
        div.setAttribute('class', 'v1');
        let thisTypeEle = utils.createElement.createElement({
            tag: 'p', attrs: {
                id: 'typeTitle' + thisType.id
            }, arg: '<i class="u_up"></i><a>' + thisType.archivetypecn + '</a>'
        });
        thisTypeEle.addEventListener('click', function () {
            console.log('点一下收缩或展示' + thisType.archivetypecn);
        });
        div.append(thisTypeEle);
        $('#archiveIndex').append(div);
        //加载文书
        loadRecords(thisType.id)
    }

    /**
     * 加载拖拽控件
     * @author MrLu
     * @createTime  2020/10/17 18:38
     */
    function sortList(zoneClass) {
        $("." + zoneClass).sortable({
            // revert: true,//整个动画
            update: function (event, ui) {//拖拽后位置变化
                //重新加载四个按钮
                let PDiv = ui.item.siblings();
                reloadButton(PDiv.first());
                reloadButton(PDiv.last());
                reloadButton(ui.item);
                //判断被拖拽的文件类型
                if ('fileSortZone' === zoneClass) {
                    //如果是文件图片
                    let recordId = $(ui.item).parent().parent().attr('id').replace('dd', '');//文书id
                    let eleB;//文件代码
                    let operation = ($(ui.item).index() > 0);//判断是否是第一个
                    if (operation) {
                        eleB = $(ui.item).prev('.v3');
                        operation = 'after';
                    } else {
                        eleB = $(ui.item).next('.v3');
                        operation = 'before';
                    }
                    //对应的图片也移动位置
                    imgOrderMove(recordId, $(ui.item), eleB, operation);
                    // 被拖拽后重新加载点击事件
                    $(ui.item).unbind().click(function () {
                        loadFileImg(this, $(this).parent().parent().attr('id').replace('dd', ''));
                    })
                }
            }, receive: function (event, ui) {
                //跨文书移动
                //判断是其他文书移动至正在看的文书还是正在看的文书移动至其他文书
                let recordId = $(ui.item).parent().parent().attr('id').replace('dd', '');//文书id
                let fileCode = $(ui.item).attr('id').replace('fileIndex', '');//文件代码
                if (+recordId === +recordImgLoadObj.getRecordId()) {
                    //其他文书移动至正在看 时
                    let operation = ($(ui.item).index() > 0);//判断是否是第一个
                    let prevFileCode = null;
                    if (operation) {
                        prevFileCode = $(ui.item).prev('.v3').attr('id').replace('fileIndex', '')
                    }
                    recordImgLoadObj.fileMoveIn(recordId,fileCode, prevFileCode, operation);
                } else {
                    //正在看的文书移动至其他文书
                    recordImgLoadObj.fileMoveOut(fileCode)
                }
            },
            connectWith: ".fileSortZone",//允许跨文书拖拽

        }).disableSelection();
    }

    /**
     * 加载具体文书目录
     * @author MrLu
     * @param typeId
     * @createTime  2020/10/9 11:28
     */
    function loadRecords(typeId) {
        $.post({
            url: '/ArrangeArchives/getRecordsIndex',
            data: {id: typeId, isDelete: 0},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    const liD = 'P' + typeId;
                    //索引信息
                    let indexing = {
                        i: 0,
                        f: reV.value.length,
                    };
                    //拖拽域
                    let sortDiv = utils.createElement.createElement({
                        tag: 'div', attrs: {
                            class: 'recordSortZone',
                        }
                    });
                    //循环文书

                    utils.functional.forEach(reV.value, function (thisRecord) {
                        const thisDD = createRecordDiv(thisRecord, indexing);
                        //判断划分拖拽域
                        if ('ZL001' === thisDD.class || 'ZL003' === thisDD.class) {
                            //卷首、文书目录 正常加载
                            $('#' + liD).append(thisDD);
                        } else if ('ZL002' === thisDD.class) {
                            //卷尾加载 普通卷+卷尾
                            $('#' + liD).append(sortDiv).append(thisDD);
                        } else {
                            //普通卷放入可拖拽域
                            sortDiv.append(thisDD);
                        }
                        indexing.i++; //此行必须在$('#' + liD).append的后边


                    });
                    sortList('recordSortZone');
                } else {
                    throw  '该卷文书加载失败：' + typeId;
                }
            }
        });
    }

    /**
     * 创建文书的div element
     * @author MrLu
     * @param thisRecord 文书对象{files,record}
     * @param indexing {i 数组下标,f 数组长度} 可为空
     * @createTime  2020/10/12 9:37
     * @return  HTMLDivElement  |
     */
    function createRecordDiv(thisRecord, indexing) {
        if (!thisRecord.record.id) {
            throw  '文书id未获取，无法加载该文书！！';
        }
        let record = thisRecord.record;
        let key = 'dd' + record.id;
        let div = utils.createElement.createElement({
            tag: 'div', attrs: {
                id: key,
                class: 'v2',
            }
        });
        div.class = record.recordscode;
        let p = utils.createElement.createElement({
            tag: 'p', attrs: {}, arg: '<a class="recordname">' + record.recordname + '</a>'
        });

        //文书缓存至recordsMap  此行必须在createButtons()方法上
        recordsMap.set(key, {
            id: record.id,
            recordname: record.recordname,
            recordscode: record.recordscode,
            archivetypeid: record.archivetypeid
        });//缓存信息
        //加载按钮 -> 文书的
        p.append(createButtons(key, indexing));
        div.append(p);
        //加载文书目录
        div.append(createFileIndex(thisRecord.files));
        //点击显示对应图片
        p.addEventListener('click', function () {
            //加载文书图片 按照子标签的顺序加载
            let fileOrder = utils.functional.map($(div).find('.v3'), function (thisFileIndex) {
                return $(thisFileIndex).attr('id').replace('fileIndex', '');
            });
            recordImgLoadObj = recordImgLoad({
                recordIdP: record.id,
                fileOrder: fileOrder
            });
        });
        return div;
    }

    /**
     * 创建文书内具体图片的目录
     * @author MrLu
     * @param files
     * @createTime  2020/10/20 15:23
     * @return  HTMLDivElement  |
     */
    function createFileIndex(files) {
        let div = utils.createElement.createElement({
            tag: 'div', attrs: {
                class: 'fileSortZone',
            }
        });
        //索引信息 不从0开始是为了在上下按钮计算时能与文书通用 i:n f:files.length + (n+1)
        let fileIndexing = {
            i: 2,
            f: files.length + 3,
        };
        for (let thisFile of files) {
            //加载第三级别文书图片目录
            //只有文书图片需要加载内容、文书封皮封底目录不需要子目录
            div.append(createFilesDiv(thisFile, fileIndexing));
            fileIndexing.i++;
        }
        sortList('fileSortZone');//加载拖拽域
        return div;
    }

    /**
     * 加载文书内图片目录
     * @author MrLu
     * @param thisFile
     *  @param fileIndexing
     * @createTime  2020/10/20 11:43
     * @return  HTMLElement  |
     */
    function createFilesDiv(thisFile, fileIndexing) {
        const key = 'fileIndex' + thisFile.filecode;
        filesMap.set(key, {
            filecode: thisFile.filecode,
            filename: thisFile.filename,
            fileurl: thisFile.fileurl,
            archiverecordid: thisFile.archiverecordid
        });//缓存信息
        let div = utils.createElement.createElement({
            tag: 'div', attrs: {
                id: key,
                class: 'v3',
                style: 0 === thisFile.filetype ? '' : 'display:none'
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
        div.append(p);
        p.append(createButtons(key, fileIndexing));
        return div;
    }

    /**
     * 点击加载图片
     * @author MrLu
     * @param indexDiv 文件目录的div对象
     * @param recordId 文件file对应的文书id
     * @createTime  2020/10/20 16:01
     */
    function loadFileImg(indexDiv, recordId) {
        let filecode = $(indexDiv).attr('id').replace('fileIndex', '');//文件代码
        //判断是否还在原本的文文书内
        if (+recordId === +recordImgLoadObj.getRecordId()) {
            //同文书点击
            //获取文书order
            let thumbnail = document.getElementById('thumbnail' + filecode);
            recordImgLoadObj.jumpImg(thumbnail);
        } else {
            let fileOrder = utils.functional.map($('#dd' + recordId).find('.v3'), (thisFileIndex) => {
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
     * 移动右侧显示中的图片位置
     * @author MrLu
     * @param recordId 文书id
     * @param  eleA 左侧列表中的元素A
     * @param  eleB 左侧列表中的元素B
     * @param operation 操作  [before|after]
     * @createTime  2020/10/24 17:14
     */
    function imgOrderMove(recordId, eleA, eleB, operation) {
        //判断是否还在原本的文文书内
        if (+recordId === +recordImgLoadObj.getRecordId()) {
            let aid = eleA.attr('id').replace('fileIndex', '');
            let bid = eleB.attr('id').replace('fileIndex', '');
            recordImgLoadObj.orderMove(aid, bid, operation);
        } else {
            //其它目录的文书位置移动不管
        }
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
        let div = document.createElement('span');
        //判断卷类型
        if ((!thisRecord) || !('ZL001' === thisRecord.recordscode || 'ZL003' === thisRecord.recordscode || 'ZL002' === thisRecord.recordscode)) {
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
     * 判断并加载上一位按钮
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
            if (2 >= indexing.i) {
                //未除了封皮和卷目录以外第一个文书
                haveFun = false;
            }
        } else {
            //当元素已经加载时判断
            //不是封皮封底
            let prevOne = $('#' + ddId).prevAll('div');//获取上一个元素
            //因为拖拽域的关系 上一个是检测不到同类元素的
            if (prevOne.length < 1) {
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
     * 判断并加载下一位按钮
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
            let nextOne = $('#' + ddId).nextAll('div');//获取下位元素
            //当后面只有一个元素（封底时） 无法下移
            //因为拖拽域的关系 下一个是检测不到同类元素的
            if (nextOne.length < 1) {
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
     * 判断并加载重命名按钮
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
     * 判断并加载删除按钮
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
     * 上移一位方法
     * @author MrLu
     * @param ddId
     * @createTime  2020/10/10 11:06
     */
    function upFun(ddId) {
        event.stopPropagation();
        if (!ddId) {
            throw '未传入需要上传的id！cdd爬';
        }
        let div = $('#' + ddId);
        let prevDD = div.prev('div');//上一个
        prevDD.before(div);
        if (div.hasClass('v3')) {
            //判断为文书图片元素 重赋事件
            //判断当前位置
            let thisFile = filesMap.get(ddId);
            //右侧位置跟随变化
            imgOrderMove(thisFile.archiverecordid, div, prevDD, 'before');
            div.unbind().click(function () {
                //重赋事件
                loadFileImg(div, thisFile.archiverecordid)
            })
            prevDD.unbind().click(function () {
                loadFileImg(prevDD, thisFile.archiverecordid)
            })
        }
        reloadButton(div);//重新加载按钮
        reloadButton(prevDD);//重新加载按钮
    }

    /**
     * 下移一位方法
     * @author MrLu
     * @param ddId
     * @createTime  2020/10/10 14:11
     */
    function downFun(ddId) {
        event.stopPropagation();
        if (!ddId) {
            throw '未传入需要上传的id！cdd爬';
        }

        let div = $('#' + ddId);//要移动的
        let nextDiv = div.next('div');//下一个
        nextDiv.after(div);//移动顺序
        if (div.hasClass('v3')) {
            //判断为文书图片元素 重赋事件
            //判断当前位置
            let thisFile = filesMap.get(ddId);
            //右侧位置跟随变化
            imgOrderMove(thisFile.archiverecordid, div, nextDiv, 'after');
            div.unbind().click(function () {
                //重赋事件
                loadFileImg(div, thisFile.archiverecordid)
            })
            nextDiv.unbind().click(function () {
                loadFileImg(nextDiv, thisFile.archiverecordid)
            })
        }
        reloadButton(div);//重新加载按钮
        reloadButton(nextDiv);//重新加载按钮
    }

    /**
     * 重命名方法
     * @author MrLu
     * @param ddId
     * @createTime  2020/10/10 14:12
     */
    function renameFun(ddId) {
        event.stopPropagation();
        let thisEle = $('#' + ddId);//获取当前element对象
        let thisP, level;
        //判断当前文书对象是文书还是文件
        if (thisEle.hasClass('v2')) {
            //是2级 是文书
            thisP = thisEle.find('.recordname');
            level = 2;
        } else {
            //是三级 是文书图片
            thisP = thisEle.find('.filename');
            level = 3;
        }

        thisP.attr('contenteditable', 'plaintext-only');//该p可编辑
        thisP.addClass('pinput');//更换class更换样式
        thisP.focus();//给予焦点
        //失去焦点事件
        thisP.blur(function () {
            $(this).removeAttr('contenteditable').unbind();//该p不可编辑 解除事件
            if (2 === level) {
                let thisOne = recordsMap.get(ddId);
                thisOne.recordname = $(this).html();
                recordsMap.set(ddId, thisOne);//缓存信息
                console.log(recordsMap.get(ddId));
            } else {
                let thisOne = filesMap.get(ddId);
                thisOne.filename = $(this).html();
                filesMap.set(ddId, thisOne);//缓存信息
                console.log(filesMap.get(ddId));
            }
            thisP.removeClass('pinput')
        });
    }

    /**
     * 删除方法
     * @author MrLu
     * @param ddId
     * @createTime  2020/10/10 14:13
     */
    function delFun(ddId) {
        event.stopPropagation();
        //判断级别 v2为文书 v3为图片
        let thisRecord = $('#' + ddId);
        let isRecord = thisRecord.hasClass('v2');
        if (isRecord) {
            //是文书
            //直接全部删除整个文书
            recycleBinObj.addRecycleBin(ddId.replace('dd', ''), undefined);
            thisRecord.remove();
            recordsMap.delete(ddId);
        } else {
            //是图片
            thisRecord = thisRecord.parent().parent('.v2');//变成文书
            //判断是否是最后一张图片 如果是的话也删除文书
            if (thisRecord.find('.v3').length === 1) {
                if (confirm('这是该文书的最后一张图片！删除后该文书也会一起删除，是否继续？')) {
                    delFun(thisRecord.attr('id'));
                    filesMap.delete(ddId);
                }
            } else {
                //删除单个文件
                recycleBinObj.addRecycleBin(thisRecord.attr('id').replace('dd', ''), ddId.replace('fileIndex', ''));
                $('#' + ddId).remove();
                filesMap.delete(ddId);
            }
        }
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
     * 从回收站还原
     * @author MrLu
     @param recordId 要还原的文书的id 是纯的id
     * @param fileCodes  要还原的文件的filecode 是纯的filecode 是个数组 方便批量还原
     * @createTime  2020/10/26 15:33
     * @return    |
     */
    function restored(recordId, fileCodes) {
        //通过传递一个假的indexing  使createFilesDiv方法不需要再查找dom元素 降低损耗
        let fileIndexing = {
            i: 999,
            f: 999
        };
        //判断未删除的列表中是否有这个文书
        if (recordsMap.has('dd' + recordId)) {
            //阳间还有这个文书
            $.post({
                url: '/ArrangeArchives/loadFilesByFileCodes',
                data: {fileOrder: fileCodes,recordid:recordId},
                success: (re) => {
                    const reV = JSON.parse(re);
                    if ('success' === reV.message) {
                        console.log(reV.value);

                        let lastFile = $('#dd' + recordId).find('.v3').last()
                        utils.functional.forEach(reV.value, function (thisFile) {
                            $('#dd' + recordId).find('.fileSortZone').append(createFilesDiv(thisFile, fileIndexing));
                        })
                        reloadButton(lastFile);//重新加载最后一个文件的按钮
                        reloadButton($('#dd' + recordId).find('.v3').last());
                    } else {
                        throw '文书还原失败';
                    }
                }
            });
        } else {
            //阳间没有这个文书了
            //还原文件
            $.post({
                url: '/FileManipulation/createRecycleRecordByFiles',
                data: {filecodes: fileCodes,recordid:recordId},
                success: (re) => {
                    const reV = JSON.parse(re);
                    if ('success' === reV.message) {
                        let thisRecord = reV.value;
                        const typeId = thisRecord.record.archivetypeid;
                        let lastRecord = $('#P' + typeId).find('.recordSortZone').find('.v2').last()
                        let thisRecordDiv = createRecordDiv(thisRecord, fileIndexing)//createRecordIndex方法接受数组 所以要把该文书对象放入一个数组中
                        lastRecord.after(thisRecordDiv)
                        reloadButton(lastRecord);//重新加载最后一个文书的按钮
                        reloadButton($(thisRecordDiv));
                    } else {
                        throw '文件还原失败';
                    }
                }
            });
        }
    }

    /**
     * 重新加载一个文书的按钮列表
     * @author MrLu
     * @param dd 该文书在列表中的dd(注意是一个jquery对象！)
     * @createTime  2020/10/11 12:42
     */
    function reloadButton(dd) {
        dd.children('span').remove();
        dd.children('p').append(createButtons(dd.attr('id')));
    }

    //保存数据
    /**
     * 保存文书目录的加载
     * @author MrLu
     * @createTime  2020/10/13 15:44
     */
    function saveData() {
        const archiveTypeList = $('#archiveIndex').find('.v1');
        if (archiveTypeList.length < 1) {
            alert('未正确加载文书目录，无法保存！');
            throw  '未正确加载文书目录，无法保存！';
        }

//创建新的整理次序
        $.post({
            url: '/ArrangeArchives/createNewSeq',
            data: {seqid},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    const newSeqId = reV.value;
                    saveArchiveIndexSortByType(archiveTypeList, newSeqId);
                } else {
                    layer.closeAll();
                    layer.alert('未能成功验证您的身份信息，请重新登录！')
                    throw  '未能创建新的整理记录';
                }
            }
        });


    }

    /**
     * 以文书类型保存整理顺序
     * @author MrLu
     * @param archiveTypeList 文书类型的list
     * @param newSeqId 新建的整理次序id
     * @createTime  2020/10/13 17:45
     * @return    |
     */
    function saveArchiveIndexSortByType(archiveTypeList, newSeqId) {

        utils.functional.forEach(archiveTypeList, function (thisType) {

            let saveData= getRecordIndexSort(thisType);//获取数据
            console.log(saveData);

            const oriTypeId = $(thisType).attr('id').replace('P', '');//文书类型id
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
                        progressBar();
                        recycleBinObj.saveRecycleIndexSortByType(oriTypeId, newTypeId);
                        console.log('   保存成功')
                    } else {
                    }
                }
            });
        })
    }
    /**
     * @author MrLu
     * @param id 文书id
     * @param name 文书名
     * @param typeid typeId
     * @param filecodes 文书内的文件
     * @param order 文书顺序
     * @createTime  2020/10/13 16:12
     */
    const saveD = function (id, name, typeid, filecodes, order) {
        this.id = id;
        this.name = name;
        this.typeid = typeid;
        this.filecodes = filecodes.toString();
        this.order = order;

    };
     /**
     * 通过文书类型的ele获取该文书类型中所有文书的顺序数据
     * @author MrLu
     * @param thisType ($('#archiveIndex').find('.v1'))
     * @createTime  2020/10/27 10:07
     * @return  [saveD]  |
      */
    function getRecordIndexSort(thisType) {
        let saveData = [];//用以存储saveD的对象数组
        $.each($(thisType).find('.v2'), function (i, v) {
            let thisDDid = $(v).attr('id');//获取文书的id  该id也是recordsMap中的key
            let thisRecord = recordsMap.get(thisDDid);//通过key获取该文书的value
            if ('ZL001' === thisRecord.recordscode) {
                i = -9999;//卷宗封皮顺序永远是第一个
            } else if ('ZL003' === thisRecord.recordscode) {
                i = -9900;//文书目录是第二个
            } else if ('ZL002' === thisRecord.recordscode) {
                i = 9999;//封底顺序永远是99999  最后一个
            }
            //获取文书内文件的顺序
            let fileCodes = utils.functional.map($('#' + thisDDid).find('.v3'), function (thisFileEle) {
                return $(thisFileEle).attr('id').replace('fileIndex','');
            });
            saveData[saveData.length] = new saveD(thisRecord.id, thisRecord.recordname, thisRecord.archivetypeid,fileCodes, i);
        });
        return saveData;
    }

     /**
     * 涨进度条
     * @author MrLu
     * @createTime  2020/10/27 11:34
      */
    function progressBar() {
        progressLength=progressLength+8.4;
        $('#progressBar').css('width',progressLength+'%').html(progressLength+'%');
        if (progressLength > 100) {
            layer.closeAll()
            //初始化进度条
            progressLength=0;
            $('#progressBar').css('width',0+'%').html(0+'%');
        }
    }

    function _loadArchiveIndex() {
        console.log('-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=开始加载-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=')
    }

    _loadArchiveIndex.prototype = {
        loadIndex, loadRecycleBin, reloadButton, saveData, restored,getRecordIndexSort,progressBar
    };
    return _loadArchiveIndex;
})();

var lai = new loadArchiveIndex();
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
                lai.loadIndex(reV.value.id);
                let rcb = new recycleBin(lai);
                rcb.loadIndex(reV.value.id);
                lai.loadRecycleBin(rcb);

                //完成整理按钮
                $('#saveData').click(function () {

                    if (confirm('确定完成整理？')) {
                        //加载进度条
                        layer.open({
                            id:10086,
                            type: 1,
                            title: false,
                            shade: [0.8, '#393D49'],
                            closeBtn: 0,
                            area: ['453px', '170px'],
                            content: $('#progressBarDiv')
                        });
                        lai.saveData();
                    }
                })
            } else {
            }
        }
    });
});