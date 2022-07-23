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
    *key:dd+文书id value:record */
    let recordsMap;
    /*key: filecode,value:file*/
    let filesMap;//同样的文书图片map
    let recycleBinObj;//回收站对象
    let recordImgLoadObj;//图片加载对象
    let loadProgress;//加载进度
    let progressLength = 0;//进度条进度
    let viewModel;//显示模式  true 正常 false 只读模式

    function loadIndex(id, viewModelP = true) {
        viewModel = viewModelP;
        seqid = id;
        $.post({
            url: '/ArrangeArchives/getArchiveIndex',
            data: {id},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    recordsMap = new Map();
                    filesMap = new Map();
                    loadProgress = reV.value.length;//共需要加载多少卷
                    utils.functional.forEach(reV.value, function (thisType) {
                        loadArchiveType(thisType);
                    });
                } else {
                    layer.alert('文书信息无法加载');
                }
            }
        });
    }

    function getSeqId() {
        return seqid;
    }

    /**
     * 加载二级菜单 （****卷）
     * @author MrLu
     * @param thisType
     * @createTime  2020/10/9 11:28
     */
    function loadArchiveType(thisType) {
        let div = utils.createElement.createElement({
            tag: 'div', attrs: {
                id: 'P' + thisType.id,
                class: 'v1',
                style: 'display:none'
            }
        });
        let thisTypeEle = utils.createElement.createElement({
            tag: 'p', attrs: {
                id: 'typeTitle' + thisType.id
            }, arg: '<i class="u_up"></i><a>' + thisType.recordtypecn + '</a>'
        });
        thisTypeEle.addEventListener('click', function () {
            $('#P' + thisType.id).slideToggle(300);
        });
        $('#archiveIndex').append(thisTypeEle).append(div);
        //加载文书
        loadRecords(thisType.id);
    }

    /**
     * 加载拖拽控件
     * @author MrLu
     * @createTime  2020/10/17 18:38
     */
    function sortList(zoneClass) {
        if (viewModel) {
            $("." + zoneClass).sortable({
                delay: 50, cursor: 'move',
                scroll: true, scrollSensitivity: 10,
                activate: function (event, ui) {
                    if ($(ui.item).hasClass('v2')) {
                        //在拖拽文书时收起所有文书内的文件目录
                        $('.fileSortZone').hide();
                    }
                },
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

                        let thisFile = filesMap.get($(ui.item).attr('id'));
                        console.log(event.originalPosition)
                        //实时更新文件顺序
                        saveDateOnTime(recordId
                            , recordsMap.get('dd' + recordId).archivetypeid
                            , thisFile.filecode
                            , $(ui.item).prev().attr('id')
                        );
                    } else {
                        console.log('更新文书');
                        let thisRecord = recordsMap.get($(ui.item).attr('id'));
                        //实时更新文书顺序
                        saveDateOnTime(thisRecord.id
                            , $(ui.item).parent().parent('.v1').attr('id')
                            , null, $(ui.item).prev().attr('id'));
                    }
                }, receive: function (event, ui) {
                    //跨文书移动
                    if ('fileSortZone' !== zoneClass) {
                        return;
                    }
                    //判断是其他文书移动至正在看的文书还是正在看的文书移动至其他文书
                    let recordId = $(ui.item).parent().parent().attr('id').replace('dd', '');//文书id
                    let fileObj = filesMap.get($(ui.item).attr('id'));
                    let fileCode = fileObj.filecode;//文件代码
                    if (+recordId === +recordImgLoadObj.getRecordId()) {
                        //其他文书移动至正在看 时
                        let operation = ($(ui.item).index() > 0);//判断是否是第一个
                        let prevFileCode = null;
                        if (operation) {
                            prevFileCode = $(ui.item).prev('.v3').attr('id').replace('fileIndex', '')
                        }

                        recordImgLoadObj.fileMoveIn(fileObj.archiverecordid, fileCode, prevFileCode, operation);
                    } else {
                        //正在看的文书移动至其他文书
                        recordImgLoadObj.fileMoveOut(fileCode)
                    }
                },
                connectWith: "." + zoneClass,//允许跨文书拖拽

            }).disableSelection();
        }
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

                    console.log(reV.value);
                    utils.functional.forEach(reV.value, function (thisRecord) {

                        const thisDD = createRecordDiv(thisRecord, indexing);
                        //判断划分拖拽域
                        if ('ZL001' === thisDD.class || 'ZL003' === thisDD.class|| 'ZL004' === thisDD.class) {
                            //卷首、文书目录 正常加载
                            $('#' + liD).append(thisDD);
                        } else if ('ZL002' === thisDD.class) {
                            //卷尾加载 普通卷+卷尾
                            $('#' + liD).append(sortDiv).append(thisDD);
                        } else {
                            //普通卷放入可拖拽域
                            sortDiv.appendChild(thisDD);
                        }
                        indexing.i++; //此行必须在$('#' + liD).append的后边

                    });

                    loadProgress--;
                    if (0 === loadProgress) {
                        //打开加载第一个文书类型
                        let firstType = $('.v1').first();
                        firstType.slideDown();
                        firstType.find('.v2').first().find('p').click();
                    }

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
        if (!thisRecord.id) {
            throw  '文书id未获取，无法加载该文书！！';
        }
        let record = thisRecord;
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
        //点击文书名收缩或显示文书内的文件
        p.addEventListener('click', function () {
            $(this).next('.fileSortZone').slideToggle(300);
            $('.watching').removeClass('watching');
            $('#'+key).addClass('watching');
        });

        //文书缓存至recordsMap  此行必须在createButtons()方法上
        recordsMap.set(key, record);//缓存信息
        //加载按钮 -> 文书的
        p.appendChild(createButtons(key, indexing));
        div.appendChild(p);
        //加载文书目录
        // div.append(createFileIndex(thisRecord.files));
        //点击显示对应图片
        p.addEventListener('click', function () {
            //加载文书图片 按照子标签的顺序加载
            let fileOrder = utils.functional.map($(div).find('.v3'), function (thisFileIndex) {
                return $(thisFileIndex).attr('id').replace('fileIndex', '');
            });
            recordImgLoadObj = recordImgLoad({
                isReadOnlyP: viewModel,
                recordIdP: record.id,
                fileOrder: fileOrder,
                callback: function () {
                    // recordImgLoadObj.loadBtn();
                }
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
                style: 'display:none',//默认隐藏起来
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
            div.appendChild(createFilesDiv(thisFile, fileIndexing));
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
        filesMap.set(key, thisFile);//缓存信息
        let div = utils.createElement.createElement({
            tag: 'div', attrs: {
                id: key,
                class: 'v3',
                style: (0 === thisFile.filetype||4 === thisFile.filetype) ? '' : 'display:none'
            }
        });
        let p = utils.createElement.createElement({
            tag: 'p', attrs: {}, arg: '<a class="filename">' + thisFile.filename + '</a>'
        });
        // 这里不能使用addEventListener添加事件，否则jquey的unbind无法清除监听
        $(div).click(function () {
            //此时是列表第一次加载时、故图片位置为fileIndexing.i-1  当该元素被拖拽、上下移按钮后 要重新添加事件
            loadFileImg(div, thisFile.archiverecordid);
        });
        div.appendChild(p);
        p.appendChild(createButtons(key, fileIndexing));
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
            recordImgLoadObj.loadBtn(filecode);
        } else {
            let fileOrder = utils.functional.map($('#dd' + recordId).find('.v3'), (thisFileIndex) => {
                return $(thisFileIndex).attr('id').replace('fileIndex', '');
            });
            //点击另一个文书的图片  加载另一个文书
            recordImgLoadObj = recordImgLoad({
                isReadOnlyP: viewModel,
                recordIdP: recordId,
                fileOrder: fileOrder,
                callback: function () {
                    //获取图片缩略
                    let thumbnail = document.getElementById('thumbnail' + filecode);
                    recordImgLoadObj.jumpImg(thumbnail);
                    //
                    recordImgLoadObj.loadBtn(filecode);
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
     * @param indexing  这里 index中的 i<=2 将不会触发向上一个按钮 f=i+2 将不会触发向下一个按钮
     * @createTime  2020/10/10 9:52
     * @return  HTMLDivElement  |
     */
    function createButtons(ddId, indexing) {
        const thisRecord = recordsMap.get(ddId);
        let div = document.createElement('span');
        //判断卷类型
        //thisRecord.recordscode.substr(0,2)
        if (viewModel && ((!thisRecord) || !('ZL' === thisRecord.recordscode.substr(0,2)))) {
            //封皮//封底  没有操作按钮
            div.setAttribute('class', 'tools');
            //用文书代码分辨html还是图片
            div.appendChild(upButton(ddId, indexing));//加载上移按钮
            div.appendChild(downButton(ddId, indexing));//加载下移按钮
            if (1===thisRecord.isazxt&&('AS021' !== thisRecord.recordscode
                &&'AS061' !== thisRecord.recordscode
                &&'AS104' !== thisRecord.recordscode
                &&'AS062' !== thisRecord.recordscode
                &&'AS065' !== thisRecord.recordscode)){
                div.appendChild(renameButton(ddId));//加载重命名按钮
                div.appendChild(delButton(ddId));//加载删除按钮
            }

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
                console.log(indexing.i)
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
                console.log(indexing.f)
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
            //实时更新文件顺序
            saveDateOnTime(thisFile.archiverecordid
                , thisFile.archivetypeid, thisFile.filecode, $('#' + ddId).prev().attr('id'));
        } else {
            let thisRecord = recordsMap.get(ddId);
            //实时更新文书顺序
            saveDateOnTime(thisRecord.id
                , thisRecord.archivetypeid, null, $('#' + ddId).prev().attr('id'));
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
        let index = div.index();
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
            //实时更新文件顺序
            saveDateOnTime(thisFile.archiverecordid
                , thisFile.archivetypeid, thisFile.filecode, $('#' + ddId).prev().attr('id'));
        } else {
            let thisRecord = recordsMap.get(ddId);
            //实时更新文书顺序
            saveDateOnTime(thisRecord.id
                , thisRecord.archivetypeid, null, $('#' + ddId).prev().attr('id'));
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
                saveReNameDateOnTime(ddId, null, thisOne.recordname)
            } else {
                let thisOne = filesMap.get(ddId);
                thisOne.filename = $(this).html();
                filesMap.set(ddId, thisOne);//缓存信息
                saveReNameDateOnTime(thisOne.archiverecordid, thisOne.filecode, thisOne.filename)
            }
            thisP.removeClass('pinput');

        });
    }

    /**
     * 删除方法
     * @author MrLu
     * @param ddId
     * @createTime  2020/10/10 14:13
     */
    function delFun(ddId,recordId) {
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
            saveDeleteDateOnTime(ddId, null);
        } else {
            //是图片
            // thisRecord = thisRecord.parent().parent('.v2');//变成文书
            // let thumbnailList=
            //判断是否是最后一张图片 如果是的话也删除文书
            console.log($('#thumbnailDiv').find('a'))
            if (!$('#thumbnailDiv').find('a').length||$('#thumbnailDiv').find('a').length === 1) {
                layer.confirm('这是该文书的最后一张图片！删除后该文书也会一起删除，是否继续？', {
                    btn: ['确定', '取消']//按钮
                }, function (index) {
                    layer.close(index);
                    delFun('dd'+recordId);//删除文书
                    // filesMap.delete(ddId);
                });
            } else {
                //删除单个文件
                // let thisRecordId = thisRecord.attr('id').replace('dd', '');
                let thisRecordId = recordId;
                let fileCode = ddId.replace('thumbnail', '');
                var  fileArray = fileCode.split(",");
                if(fileCode.split(",").length>1){
                    if (!$('#thumbnailDiv').find('a').length||$('#thumbnailDiv').find('a').length == fileCode.split(",").length) {
                        layer.confirm('您选中了文书中所有图片，删除后该文书也会一起删除，是否继续？确定删除后请刷新页面。', {
                            btn: ['确定', '取消']//按钮
                        }, function (index) {
                            layer.close(index);
                            delFun('dd'+recordId);//删除文书
                            // filesMap.delete(ddId);
                        });
                    }else {
                        for (var i = 0 ;i<fileCode.split(",").length;i++){
                            recycleBinObj.addRecycleBin(thisRecordId, fileArray[i]);
                            saveDeleteDateOnTime(thisRecordId, fileArray[i]);
                        }
                    }
                }else {
                    recycleBinObj.addRecycleBin(thisRecordId, fileCode);


                    // filesMap.delete(ddId);
                    saveDeleteDateOnTime(thisRecordId, fileCode);
                }

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
                data: {fileOrder: fileCodes, seqId: seqid, recordId},
                success: (re) => {
                    const reV = JSON.parse(re);
                    if ('success' === reV.message) {
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
                data: {filecodes: fileCodes, recordid: recordId},
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
     * 实时保存文书顺序
     * @author MrLu
     * @param recordId 被移动的或被移动到的文书id
     * @param typeId 被移动到的文书类型id
     * @param fileCode 文件代码 当移动的是文件时传入的文件代码
     * @param prevId 上一个文件/文书的代码/id
     * @createTime  2020/11/2 18:31
     */
    function saveDateOnTime(recordId, typeId, fileCode, prevId) {

        const updateObj = function () {
            this.recordid = recordId;//被移动的或被移动到的文书id
            this.typeid = typeId;//被移动到的文书类型id
            this.filecode = fileCode;//文件代码 当移动的是文件时传入的文件代码
            this.prevId = prevId ? prevId.replace('fileIndex', '') : null;
            this.seqId = seqid;
        }
        let thisObj = new updateObj();
        $.post({
            url: '/ArrangeArchives/saveDateOnTime',
            data: {paramjson: JSON.stringify(thisObj)},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    console.log('实时保存成功');
                } else {
                    layer.msg('实时保存失败');
                }
            }
        });

    }

    /**
     * 实时保存删除的文书/文件
     * @author MrLu
     * @param recordId
     * @param fileCode
     * @createTime  2020/11/5 9:27
     */
    function saveDeleteDateOnTime(recordId, fileCode) {
        const deleteObj = function () {
            this.recordid = recordId;//被移动的或被移动到的文书id
            this.filecode = fileCode;//文件代码 当移动的是文件时传入的文件代码
            this.seqId = seqid;
        }
        $.post({
            url: '/ArrangeArchives/saveDeleteDateOnTime',
            data: {paramjson: JSON.stringify(new deleteObj())},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    console.log('实时删除成功');
                    //删除后删除图片  并移动至回收站位置
                } else {
                }
            }
        });
    }


    /**
     * 实时保存重命名的数据
     * @author MrLu
     * @param recordId
     * @param fileCode
     * @param name 重命名的名称
     * @createTime  2020/121/22 15:40
     * @return    |
     */
    function saveReNameDateOnTime(recordId, fileCode, name) {
        if(!name){
            layer.alert('文书名称不可为空！');
            return false
        }

        const renameObj = function () {
            this.recordid = recordId;//被移动的或被移动到的文书id
            this.filecode = fileCode;//文件代码 当移动的是文件时传入的文件代码
            this.rename = name.trim();
            this.seqId = seqid;
        }
        $.post({
            url: '/ArrangeArchives/saveReNameDateOnTime',
            data: {paramjson: JSON.stringify(new renameObj())},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    console.log('实时重命名保存成功');
                } else {
                }
            }
        });
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
            layer.alert('未正确加载文书目录，无法保存！');
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
                    // layer.closeAll();
                    // layer.alert('未能成功验证您的身份信息，请重新登录！')
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

            let saveData = getRecordIndexSort(thisType);//获取数据
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
     * @param  hasObj 是否要包含文书完全体对象信息 [true|false]
     * @createTime  2020/10/27 10:07
     * @return  [saveD]  |
     */
    function getRecordIndexSort(thisType, hasObj = false) {
        let saveData = [];//用以存储saveD的对象数组
        $.each($(thisType).find('.v2'), function (i, v) {
            let thisDDid = $(v).attr('id');//获取文书的id  该id也是recordsMap中的key
            let thisRecord = recordsMap.get(thisDDid);//通过key获取该文书的value
            if ('ZL001' === thisRecord.recordscode||'ZL004' === thisRecord.recordscode) {
                i = -9999;//卷宗封皮顺序永远是第一个
            } else if ('ZL003' === thisRecord.recordscode) {
                i = -9900;//文书目录是第二个
            } else if ('ZL002' === thisRecord.recordscode) {
                i = 9999;//封底顺序永远是99999  最后一个
            }
            //获取文书内文件的顺序
            let fileCodes = utils.functional.map($('#' + thisDDid).find('.v3'), function (thisFileEle) {
                return $(thisFileEle).attr('id').replace('fileIndex', '');
            });
            let thisSaveData = new saveD(thisRecord.id, thisRecord.recordname, thisRecord.archivetypeid, fileCodes, i);
            if (hasObj) {
                thisSaveData.recordObj = thisRecord;
            }
            saveData[saveData.length] = thisSaveData;
        });
        return saveData;
    }


    /**
     * 上传新的文书
     * @author MrLu
     * @createTime  2020/12/22 15:49
     * @return    |
     */
    function createNewRecord() {
        layer.open({
            icon: 1,
            type: 2,
            title: '新建',
            skin: 'layui-layer-lan',
            maxmin: false,
            shadeClose: true, //点击遮罩关闭层
            area: ['1111px', '600px'],
            content: '/model/jzzl/createNewRecord.html?seqid=' + seqid,
            end:function () {
                location.reload();
            }
        });
    }

    /**
     * 搜索
     * @author MrLu
     * @param  txt 搜索的文本
     * @createTime  2020/12/28 23:05
     * @return    |
     */
    function searchRecord(txt) {
        $.post({
            url: '/ArrangeArchives/recordNamSearch',
            data: {recordname: txt, seqid: seqid},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {

                    layer.msg((reV.value.length > 0 ? '为您找到' + reV.value.length + '个结果，已用颜色标注！' : '没有符合条件的文书！'));
                    for (const reVElement of reV.value) {
                        $('#dd' + reVElement.id).addClass('search')
                            .click(function () {
                                $(this).removeClass('search');
                            })
                        //父级下拉
                        $('#P' + reVElement.archivetypeid).slideDown(300);
                    }

                    console.log(reV.value)
                } else {
                }
            }
        });

    }

    /**
     * 从基础卷导入新的文书
     * @author MrLu
     * @param
     * @createTime  2020/12/28 19:43
     * @return    |
     */
    function importRecord() {
        layer.open({
            icon: 1,
            type: 2,
            title: '从基础卷导入',
            skin: 'layui-layer-lan',
            maxmin: false,
            shadeClose: true, //点击遮罩关闭层
            area: ['1111px', '600px'],
            content: '/model/jzzl/importRecords.html?seqid=' + seqid
        });
    }

    /**
     * 涨进度条
     * @author MrLu
     * @createTime  2020/10/27 11:34
     */
    function progressBar() {
        progressLength = progressLength + 8.4;
        $('#progressBar').css('width', progressLength + '%').html(progressLength + '%');
        if (progressLength > 100) {
            // layer.closeAll()
            //初始化进度条
            progressLength = 0;
            $('#progressBar').css('width', 0 + '%').html(0 + '%');
        }
    }

    /**
     * 返回当前的文书图片对象
     * @author MrLu
     * @createTime  2021/1/1 14:44
     * @return   Object |  recordImgLoadObj
     */
    function getRecordImgLoadObj() {
        return recordImgLoadObj;
    }

    /**
     * 改变文书左侧目录的字体颜色，并且一碰就没
     * @author MrLu
     * @param recordId 文书id
     * @param colour 颜色
     * @createTime  2021/1/1 16:26
     * @return    |
     */
    function changeRecordNameColour(recordId, colour) {
        $('#dd' + recordId).find('.recordname').mouseover(function () {
            console.log('鼠标移上来惹！');
            $(this).removeAttr('css').unbind('mouseover');
        }).css('color', colour);
    }

    /**
     * 添加新的文书
     * @author MrLu
     * @param thisRecord  新生成的文书
     * @param prevRId  上一个的文书id
     * @createTime  2021/1/1 17:03
     * @return    |
     */
    function addRecord(thisRecord, prevRId) {

        const prevRecord= recordsMap.get('dd' + prevRId);
        let recordSortZone=$('#P' + thisRecord.archivetypeid).find('.recordSortZone');
        if ('ZL003'===prevRecord.recordscode){
            //上一个是卷目录  是该卷类型的第一个呢  牙白得死内！
            let indexing = {
                i: 1,
                f: 999,
            };
            const thisDD = createRecordDiv(thisRecord,indexing);
            let firstRecord = recordSortZone.find('.v2').first();//取出原第一个
            recordSortZone.prepend(thisDD);
            reloadButton(firstRecord);//将原第一个元素的按钮重新加载
        }else {
            //此时只需要主意自己和上一个即可
            let indexing = {
                i: 3,
                f: 5,
            };
            //创建一个上一个和下一个都有的
            const thisDD = createRecordDiv(thisRecord,indexing);
            let prev=$('#dd' + prevRId);
            prev.after(thisDD);
            let lastRecord =recordSortZone.find('.v2').last();
            reloadButton(prev);//将上一个元素的按钮重新加载
            reloadButton(lastRecord);//将最后一个元素的按钮重新加载
        }
        changeRecordNameColour(thisRecord.id,'#36ff25');



  /*      const thisDD = createRecordDiv(thisRecord);
        console.log(thisDD);
        //我用id选择器选出来个数组？   太迷幻了吧
        changeRecordNameColour(thisRecord.id,'#36ff25');
        let lastRecord = $('#P' + thisRecord.archivetypeid).find('.recordSortZone').find('.v2').last();
        let firstRecord = $('#P' + thisRecord.archivetypeid).find('.recordSortZone').find('.v2').first();
        console.log(firstRecord)
        console.log(lastRecord)
        console.log(prev)
        reloadButton(lastRecord);//将最后一个元素的按钮重新加载
        reloadButton(firstRecord);//将第一个元素的按钮重新加载
        reloadButton(prev);//将上一个元素的按钮重新加载
*/


        /*
        * 
        *    let thisRecord = reV.value;
                        const typeId = thisRecord.record.archivetypeid;
                        let lastRecord = $('#P' + typeId).find('.recordSortZone').find('.v2').last()
                        let thisRecordDiv = createRecordDiv(thisRecord, fileIndexing)//createRecordIndex方法接受数组 所以要把该文书对象放入一个数组中
                        lastRecord.after(thisRecordDiv)
                        reloadButton(lastRecord);//重新加载最后一个文书的按钮
                        reloadButton($(thisRecordDiv));

        * */
    }


    function _loadArchiveIndex() {
        console.log('-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=开始加载-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=')
    }

    _loadArchiveIndex.prototype = {
        loadIndex, loadRecycleBin, reloadButton,
        saveData, restored, getSeqId,
        getRecordIndexSort, progressBar, delFun,
        createFilesDiv, createNewRecord, importRecord, searchRecord,
        getRecordImgLoadObj, changeRecordNameColour, addRecord
    };
    return _loadArchiveIndex;
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

var lai = new loadArchiveIndex();
$(function () {
    $('#userHeart').load('/userHeart.html');
    //回收站切换效果
    $('#aded').click(function () {
        if ($('.textt').is(':hidden')) {
            $('.delt').hide();
            $('.textt').show();
        } else {
            layer.msg('回收站');
            $('.textt').hide();
            $('.delt').show();

        }
    });
    //送检的id
    const sfcId = utils.getUrlPar('id');
    //查询送检最后一次整理的记录selectLastSeqBySfc
    $.post({
        url: '/ArrangeArchives/selectLastSeqSfcBySfc',
        data: {id: sfcId},
        success: (re) => {
            const reV = JSON.parse(re);
            if ('success' === reV.message) {
                const seq = reV.value.seq;//最新的整理记录id
                const sfc = reV.value.sfc;//送检卷信息
                $('#recordStatusMessage').html(reV.value.casename);
                const caseStatus = reV.value.caseStatus;//送检卷信息
                let viewModel = true;
                if ('未发送' !== sfc.issend&&'发送失败' !== sfc.issend) {
                    viewModel = false;
                    layer.msg('该卷当前状态为：'+sfc.issend+'！当前卷处于只读状态！');
                }else if ("0" !== caseStatus) {
                    viewModel = false;
                    layer.msg('正由其它用户操作，当前窗口处于只读状态！');
                    $('#recordStatusMessage').html('当前案件处于只读状态！正在由用户：' + caseStatus.xm + "对《" + caseStatus.sfcname + "》进行编辑")
                    let unLock = utils.createElement.createElement({
                        tag: 'a',
                        attrs: {
                            style: 'color: #0b3786; background: #fff;line-height: 20px; display: inline-block; padding: 2px 10px; margin-left: 10px; border-right: 5px;'
                        },
                        arg: '解锁'
                    })
                    //解锁
                    unLock.addEventListener('click', function () {
                        layer.confirm('此操作将强制关闭对方页面，是否继续？', {
                            btn: ['确定', '取消']//按钮
                        }, function (index) {
                            layer.close(index);
                            $.post({
                                url: '/ArrangeArchives/unlockReocrd',
                                data: {
                                    sfcid: caseStatus.sfcid,
                                    username: caseStatus.username,
                                    caseinfoid: caseStatus.caseinfid
                                },
                                success: (re) => {
                                    const reV = JSON.parse(re);
                                    if ('success' === reV.message) {
                                        location.reload();
                                    } else {
                                        layer.alert('解锁失败');
                                    }
                                }
                            });
                        })
                    })
                    $('#recordStatusMessage').append(unLock);


                } else {
                    //非只读状态 此时后台已经将该页面记载在redis上了  此时订阅websocket
                    let rwc = new recordWebSocket(sessionStorage.username, sfc.id);
                }
                const isSuspectOrder = 0 === +reV.value.issuspectorder;//基础卷是否为已为嫌疑人排序

                function loadArchives(viewModel) {
                    lai.loadIndex(seq.id, viewModel);//开始加载目录
                    let rcb = new recycleBin(lai, viewModel);//加载回收站部分
                    rcb.loadIndex(seq.id);//回收站目录加载
                    lai.loadRecycleBin(rcb);
                }

                //拍摄快照按钮
                $('#snapshot').click(function () {
                    layer.confirm('确定完成整理？', {
                        btn: ['确定', '取消']//按钮
                    }, function (index) {
                        layer.close(index);
                        lai.saveData();
                    });
                });
                //新创建文书
                $('#createRecord').click(function () {
                    if (viewModel) {
                        lai.createNewRecord();
                    } else {
                        layer.msg('当前案件处于只读状态！');
                    }
                })

                function search(mine) {
                    $('.noSearch').hide();
                    $('.Search').show();
                    $(mine).unbind().click(function () {
                        let txt = $('#searchRecordInput').val();
                        if (txt) lai.searchRecord(txt);
                    })
                }

                //文书搜索
                $('#searchRecord').click(function () {
                    //当隐藏时 展示搜索框
                    search(this);
                })
                //取消搜索按钮
                $('#closeSearch').click(function () {
                    $('.Search').hide();
                    $('#searchRecordInput').val('');
                    $('.noSearch').show();
                    // $('.search').removeClass('search');
                    $('#searchRecord').unbind().click(function () {
                        search(this);
                    })
                })

                //从基础卷导入文书/图片
                $('#importRecord').click(function () {
                    if (viewModel) {
                        if (0 === sfc.archivetype) {
                            layer.alert('当前文书为基础卷！勿需导入');
                        } else {
                            lai.importRecord();
                        }
                    } else {
                        layer.msg('当前案件处于只读状态！');
                    }
                })

                if (isSuspectOrder) {
                    layer.alert('您还没有为嫌疑人整理顺序，或智能排序未完成，请留意页面右上角通知区域或刷新页面重试');
                    window.close();
                } else {
                    //不需要选人了 直接加载好了
                    loadArchives(viewModel)
                }
            } else {
                console.error('未能获取到创建卷信息！');
            }
        }
    });
});