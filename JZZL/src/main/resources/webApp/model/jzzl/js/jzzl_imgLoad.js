/**
 * @author Mrlu
 * @createTime 2020/10/15
 * @dependence jquery.min.js,layer.js,utils.js,jquery-ui.js
 * @describe  文书图片加载
 */
var recordImgLoad = (function () {
    let recordId;//文书id
    let viewModel;//显示状态 true 下拉图 false平铺图
    let checkFile = new Set();//复选功能中被选中的文件的filecode数组
    let imgMap = new Map();//文书图片
    let isReadOnly;//是否只读 true 不是  false 只读
    let focusImg;//焦点图 大图模式下正在看的图
    let colorList = ['#FFA500',
        '#40E0D0',
        '#F08080',
        '#FF4500',
        '#1E90FF',
        '#11b121',
        '#4d1f53'];

    /**
     * 查询文书的图片数据
     * @author MrLu
     * @param recordId 文书id
     * @param fileOrder 文书顺序数组
     * @param callback  加载完文书图片后的回调函数
     * @createTime  2020/10/15 18:31
     */
    function loadFilesByRecord(recordId, fileOrder, callback) {
        let url = '/ArrangeArchives/loadFilesByRecord';
        console.log(fileOrder)
        if (fileOrder.length > 0) {
            //客制化
            url = '/ArrangeArchives/loadFilesByFileCodes';
        }
        $.post({
            url: url,
            data: {
                fileOrder: fileOrder.join(','),
                seqId: parent.lai.getSeqId(),
                recordId
            },
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    $('#ImgBigDiv,#thumbnailDiv').empty().scrollTop(0);
                    $('#frontImg,#htmlDiv').empty();
                    const files = reV.value;
                    if (!(files && files.length > 0)) {
                        console.error('该文书下没有任何文件可供显示！'); //一页文书都没有还显示个p
                        return;
                    }
                    if (0 !== files[0].filetype) {
                        //是卷宗封皮、封底、目录
                        $('.recordImgBtn').unbind().hide();//功能区按钮隐藏
                        const thisFile = files[0];//对象拿出
                        let url = '';
                        switch (thisFile.filetype) {
                            case 1://卷封皮
                                url = '/model/jzzl/jzfp.html';
                                break;
                            case 2://卷目录
                                url = '/model/jzzl/jzml.html';
                                break;
                            case 3://卷尾
                                url = '/model/jzzl/jzfd.html';
                                break;
                        }
                        recordImgLoad.pValue = thisFile;//把id加上
                        $('#htmlDiv').show().load(url);
                    } else {
                        $('#htmlDiv').hide();
                        $('.recordImgBtn').show();
                        let i = 0;
                        utils.functional.forEach(files, function (thisFile) {
                            $('#thumbnailDiv').append(loadThumbnail(thisFile, i++));
                            $('#ImgBigDiv').append(loadImgs(thisFile));
                            $('#frontImg').append(loadFrontImg(thisFile));//加载平铺图
                            loadTags(thisFile.filecode);//加载标签

                        });

                        //大图div滚动条事件
                        $('#ImgBigDiv').unbind().scroll(function () {
                            let scrollTopValue = +document.getElementById('ImgBigDiv').scrollTop;
                            let proportionValue = +$('#proportion').val();
                            scrollTopValue = (scrollTopValue - 50) / (1467 * proportionValue * 0.01);
                            let pageCountNow = +scrollTopValue.toFixed(0) + 1;
                            if (pageCountNow > imgMap.size) {
                                pageCountNow = imgMap.size;
                            }
                            $('#jumpToPage').val(pageCountNow);
                            let key = $($('.bigImgClass')[(pageCountNow - 1)]).attr('id').replace('bigImg', '');
                            if (focusImg !== key) {
                                focusImg = key;
                                loadBtn(focusImg);
                                $('#thumbnailDiv').animate({
                                    scrollTop: (((pageCountNow - 1) * 174) + 10)
                                }, 0);
                                $('#thumbnailDiv .active').removeClass('active');
                                $("#thumbnail" + focusImg).addClass('active');
                            }
                        })


                        //跳转至区域
                        $('#pageSum').html('/' + i);// 总页数
                        $('#jumpToPage').val(1);//跳到哪（赋值1 默认显示第一页）
                        /*********************为功能区按钮添加方法******************************/
                        //切换视图按钮
                        viewModel = false;//下拉显示
                        changeViewModel($('#changeView'));//加载一下下拉展示
                        FrontImgSortTable();//加载平铺图的拖拽效果

                        //为切换按钮添加方法
                        $('#changeView').unbind().click(function () {
                            changeViewModel(this);
                        });

                    }
                    callback();//回调方法
                } else {
                    console.error('文书图片加载错误');
                    layer.alert('图片加载失败！');
                }
            }
        });
    }

    //标签对象
    const tags = function (id, updatetime, authorxm, archiveseqid, archivefileid, recordid, filecode, taginfo, tagcolour) {
        this.id = id;
        this.updatetime = updatetime;
        this.authorxm = authorxm;
        this.archiveseqid = archiveseqid;
        this.archivefileid = archivefileid;
        this.recordid = recordid;
        this.filecode = filecode;
        this.taginfo = taginfo;
        this.tagcolour = tagcolour;
    }


    /**
     * 下载按钮
     * @author MrLu
     * @param thisFileCode  传递想要操作的文书的thisFileCode,当！thisFileCode时候将视为直接操作整个文书
     * @createTime  2020/10/31 12:55
     * @return    |
     */
    function loadBtn(thisFileCode) {
        //为下载按钮添加方法
        $('#downLoadBtn').unbind().click(function () {
            if (thisFileCode) {
                utils.createElement.downLoadImg(imgMap.get(thisFileCode).serverip + imgMap.get(thisFileCode).fileurl);
            } else {
                //当操作整个文书对象时  下载文书的所有图片
                const iterator1 = imgMap[Symbol.iterator]();
                for (const item of iterator1) {
                    utils.createElement.downLoadImg(item[1].serverip + item[1].fileurl);
                }
            }
        });
        //新建标签按钮
        $('#newTagBtn').unbind().click(function () {
            if (!isReadOnly) {
                layer.msg('当前案件处于只读状态！1');
                return false;
            }
            if (thisFileCode) {
                //鼠标点击固定区域新建标签  弹开个页 鼠标点哪加哪  保存后刷新该文书的标签方法加载标签 新建个标签表
                layer.prompt({
                    title: '输入标签内容，限制为50字', formType: 2
                }, function (text, index) {
                    let tagsObj = new tags();
                    tagsObj.filecode = thisFileCode;
                    tagsObj.recordid = recordId;
                    tagsObj.taginfo = text;
                    tagsObj.tagcolour = '#11b121';
                    $.post({
                        url: '/FileTags/createNewTags',
                        data: {
                            tag: JSON.stringify(tagsObj)
                        },
                        success: (re) => {
                            const reV = JSON.parse(re);
                            if ('success' === reV.message) {
                                console.log('添加成功');
                                loadTags(thisFileCode);
                            } else {
                                console.log('添加失败');
                            }
                        }
                    });
                    //文件filecode  seqid 文件id
                    layer.close(index);
                })

            } else {
                layer.alert('请选择一张具体的文书图片');
            }
        });
        //重新上传按钮
        $('#reUpLoadBtn').unbind().click(function () {
            if (!isReadOnly) {
                layer.msg('当前案件处于只读状态！2');
                return false;
            }
            if (thisFileCode) {
                layer.open({
                    icon: 1,
                    type: 2,
                    title: '重新上传',
                    skin: 'layui-layer-lan',
                    maxmin: false,
                    shadeClose: false, //点击遮罩关闭层
                    area: ['1000px', '700px'],
                    content: '/model/jzzl/imgReUpload.html?fileId=' + imgMap.get(thisFileCode).id + '&fileCode=' + thisFileCode
                });
            } else {
                layer.alert('请选择需要重新上传的图片');
            }
        });
        //放大按钮
        $('#zoomInBtn').unbind().click(function () {
            //放大倍率
            let proportionValue = +$('#proportion').val();
            proportionValue = proportionValue + 10 > 200 ? 200 : proportionValue + 10;
            $('#proportion').val(proportionValue);
            zoomImg(proportionValue * 0.01)
        })
        //缩小按钮
        $('#zoomOutBtn').unbind().click(function () {
            //放大倍率
            let proportionValue = +$('#proportion').val();
            proportionValue = proportionValue - 10 < 10 ? 10 : proportionValue - 10;
            $('#proportion').val(proportionValue);
            zoomImg(proportionValue * 0.01)
        })
        //更改比率
        $('#proportion').unbind().change(function () {
            let proportionValue = +$(this).val();
            //最大放大200%
            if (proportionValue > 200) {
                proportionValue = 200;
            } else if (proportionValue < 10) {
                //最小10%
                proportionValue = 10;
            }
            $(this).val(proportionValue);
            zoomImg(proportionValue * 0.01)
        })
        //添加上传按钮
        $('#addUploadBtn').unbind().click(function () {
            if (!isReadOnly) {
                layer.msg('当前案件处于只读状态！3');
                return false;
            }
            //直接上传到文书的后面
            recordImgLoad.pValue = recordId;
            let files = $('#ImgBigDiv').children();
            let key = $(files[files.length - 1]).attr('id').replace('bigImg', '');
            layer.open({
                icon: 1,
                type: 2,
                title: '添加上传',
                skin: 'layui-layer-lan',
                maxmin: false,
                shadeClose: false, //点击遮罩关闭层
                area: ['1111px', '600px'],
                content: '/model/jzzl/imgUpload.html?recordId=' + recordId + '&maxOrder=' + imgMap.get(key).thisorder
            });
        });
        //删除按钮
        $('#deleteBtn').unbind().click(function () {
            if (!isReadOnly) {
                layer.msg('当前案件处于只读状态！4');
                return false;
            }
            if (confirm('确认删除？')) {
                if (thisFileCode) {
                    //删除单页文书 recordId
                    parent.lai.delFun('thumbnail' + thisFileCode, recordId);
                } else {
                    //删除整个文书
                    parent.lai.delFun('dd' + recordId, recordId);
                }
            }
        });
        //图片详细按钮
        /**
         * @description
         * @log  2020/11/11 13:56  MrLu  已经没这个功能了
         **/
        $('#imgInfoBtn').unbind().click(function () {
            layer.open({
                icon: 1,
                type: 2,
                title: '图片详细',
                skin: 'layui-layer-lan',
                maxmin: false,
                shadeClose: true, //点击遮罩关闭层
                area: ['1111px', '600px'],
                content: '/model/jzzl/imgInfo.html?recordId=' + recordId + '&fileCode' + thisFileCode
            });
        });
        //为移动至按钮添加方法
        $('#moveToBtn').unbind().click(function () {
            if (thisFileCode) {
                //移动单个图片
                moveToFn(thisFileCode, recordId);
            } else {
                //移动整个文书
                moveToFn(undefined, recordId);
            }

        });
        //跳转至
        $('#jumpToPage').unbind().change(function () {
            let pageNum = +$(this).val();
            let proportionValue = +$('#proportion').val();
            if (pageNum > imgMap.size) {
                pageNum = imgMap.size;
            } else if (pageNum < 1) {
                //最小10%
                pageNum = 1;
            }
            $('#ImgBigDiv').animate({
                scrollTop: (((pageNum - 1) * 1467 * proportionValue * 0.01) + 50)
            }, 300);
            $(this).val(pageNum);

        });
    }


    /**
     * 放大缩小图片比率
     * @author MrLu
     * @param proportionValue 比率值 数值类型
     * @createTime  2020/12/7 17:00
     * @return    |
     */
    function zoomImg(proportionValue) {

        $('.bigImg').attr('width', 957 * proportionValue + 'px');
        $('.bigImg').attr('height', 1467 * proportionValue + 'px');

    }

    /**
     * 加载标签
     * @author MrLu
     * @param fileCode
     * @createTime  2020/12/8 14:29
     * @return    |
     */
    function loadTags(fileCode) {
        if (!isReadOnly) {
            // layer.msg('当前案件处于只读状态！');
            return false;
        }
        if (document.getElementById('bigImg' + fileCode)) {
            $.post({
                url: '/FileTags/selectArchiveTags',
                data: {archiveseqid: parent.lai.getSeqId(), filecode: fileCode},
                success: (re) => {
                    const reV = JSON.parse(re);
                    if ('success' === reV.message) {
                        if (reV.value) {
                            $('#tags' + fileCode).remove();
                            let thisTagsList = utils.createElement.createElement({
                                tag: 'div', attrs: {
                                    class: 'div_a_title',
                                    id: 'tags' + fileCode
                                }
                            });
                            for (let thisTag of reV.value) {
                                let thisTagDivP=document.createElement('p');
                                thisTagDivP.id='tag' + thisTag.id;//标签id
                                let thisTagDiv = utils.createElement.createElement({
                                    tag: 'div',
                                    attrs: {
                                        class: 'a_title',
                                        colour: colorList.indexOf(thisTag.tagcolour),
                                        style: 'background:' + thisTag.tagcolour
                                    },
                                    arg: '' + thisTag.taginfo + '&nbsp;&nbsp;&nbsp;&nbsp;' + thisTag.authorxm + "&nbsp;&nbsp;" + utils.timeFormat.timestampToDate(thisTag.createtime)
                                });
                                //删除标签按钮
                                let delTagBtn = utils.createElement.createElement({
                                    tag: 'a',
                                    attrs: {
                                        color: '#FFF',
                                        'background-color':'#000'
                                    }, arg: 'X'
                                });
                                //删除按钮方法
                                delTagBtn.addEventListener('click', function () {
                                    $.post({
                                        url: '/FileTags/delTag',
                                        data: {id: thisTag.id},
                                        success: (re) => {
                                            const reV = JSON.parse(re);
                                            if ('success' === reV.message) {
                                                $('#tag' + thisTag.id).remove();
                                            } else {
                                                console.error('删除失败')
                                            }
                                        }
                                    });
                                });
                                $(thisTagDiv).append(delTagBtn);
                                thisTagDivP.appendChild(thisTagDiv);
                                //点击标签变色
                                thisTagDiv.addEventListener('click', function () {
                                    //获取当前颜色下标+1
                                    let colorNow = +$(this).attr('colour') + 1;
                                    if (colorNow >= colorList.length) {
                                        colorNow = 0;
                                    }
                                    let color = colorList[colorNow];
                                    $(this).attr({'style': 'background:' + color, colour: colorNow})
                                    $.post({
                                        url: '/FileTags/changeTagColor',
                                        data: {id: thisTag.id,
                                        color:color},
                                        success: (re) => {
                                            const reV = JSON.parse(re);
                                            if ('success' === reV.message) {
                                            } else {
                                                console.error('颜色更改失败')
                                            }
                                        }
                                    });
                                });
                                $(thisTagsList).append(thisTagDivP);
                            }
                            $('#bigImg' + fileCode).append(thisTagsList);
                        }
                    } else {
                        console.error('标签查询错误')
                    }
                }
            });
        } else {
            console.log('加载标签时父图还未加载完成，等待完成。。。。。。。。。。。');
            setTimeout(function () {
                loadTags(fileCode)
            }, 1000);
        }

    }

    /**
     * 平铺图的拖拽控件加载
     * @author MrLu
     * @createTime  2020/10/17 11:51
     */
    function FrontImgSortTable() {
        if (isReadOnly) {
            $('#frontImg').sortable({
                delay: 50, cursor: 'move',
                scroll: true, scrollSensitivity: 10, update: function (event, ui) {//拖拽后位置变化
                    let fileCode = $(ui.item).attr('id').replace('front', '');//获取被挪动的文书的文件代码
                    let fileOrder = $(ui.item).index();
                    let prevFileCode = null;
                    if (fileOrder > 0) {
                        prevFileCode = $(ui.item).prev('.frontDiv').attr('id').replace('front', '');
                    }
                    saveFileOrderOnTime(
                        fileCode, prevFileCode
                        , fileOrder
                    );
                }
            }).disableSelection();
        }

    }

    /**
     * 实时保存顺序
     * @author MrLu
     * @param fileCode 文件代码
     * @param prevFileCode 用于定位的上一个文件代码 当被动到第一个时 该参数为null
     * @param fileOrder 文件的位置
     * @createTime  2021/1/1 13:57
     */
    function saveFileOrderOnTime(fileCode, prevFileCode, fileOrder) {
        const updateObj = function () {
            this.recordId = recordId;//被移动的或被移动到的文书id
            this.fileCode = fileCode;
            this.prevFileCode = prevFileCode;
            this.fileOrder = fileOrder;
            this.seqId = parent.lai.getSeqId();
        }
        let thisObj = new updateObj();
        $.post({
            url: '/FileManipulation/changeFileOrderOnTime',
            data: {paramjson: JSON.stringify(thisObj)},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    //大图、大图的缩略图位置连锁移动
                    orderMove(fileCode, prevFileCode, 'before');
                    console.log('实时保存成功');
                } else {
                    layer.msg('实时保存失败');
                }
            }
        });

    }

    /**
     * 多选按钮
     * @author MrLu
     * @createTime  2020/10/16 14:08
     */
    function multipleFun(thisBtn) {
        if (!isReadOnly) {
            layer.msg('当前案件处于只读状态！6');
            return false;
        }
        $(thisBtn).find('span').html('取消多选');

        //原有按钮添加取消选中方法
        $(thisBtn).unbind().click(function () {
            cancelMultiple(this)
        });

        //所有图片添加事件
        checkFile = new Set();//被选中值set

        $('#frontImg div').click(function () {
            const filecode = $(this).attr('id').replace('front', '');
            //判断是否被选中
            if ($(this).hasClass('active')) {
                //被选中 -> 取消选中
                $(this).removeClass('active');
                checkFile.delete(filecode);
            } else {
                //未被选中 选
                $(this).addClass('active');
                checkFile.add(filecode);//记录被选的文件id
            }
        })
    }

    /**
     * 移动至方法
     * @author MrLu
     * @param fileCode 文件代码
     * @param recordId 文书id
     * @createTime  2020/10/16 16:24
     * @return    |
     */
    function moveToFn(fileCode, recordId) {
        if (!isReadOnly) {
            layer.msg('当前案件处于只读状态！7');
            return false;
        }
        let moveState = 0;
        let pString = '';
        if (viewModel) {
            //下拉图状态下
            if (fileCode) {
                //单页移动至
                pString = fileCode;
            } else {
                layer.alert('请选择要移动的图片，您也可以在平铺模式中多选图片进行批量移动')
                return;
                /*//文书移动至
                moveState = 1;
                pString = recordId;*/

            }
        } else {
            //平面图状态
            //多选移动至
            //被移动的文书filecode set
            if (!checkFile || checkFile.size === 0) {
                alert('请选择要移动的图片！');
                return false;
            }
            pString = Array.from(checkFile).join(',');
            moveState = 2
        }
        recordImgLoad.pValue = pString;//要移动的对象
        layer.open({
            icon: 1,
            type: 2,
            title: '文书移动',
            skin: 'layui-layer-lan',
            maxmin: false,
            shadeClose: true, //点击遮罩关闭层
            area: ['1111px', '600px'],
            content: '/model/jzzl/jzYdTable.html?moveState=' + moveState + '&seqid=' + parent.lai.getSeqId() + '&orirecordid=' + recordId
        });

    }

    /**
     * 初始化多选按钮
     * @author MrLu
     * @param thisBtn 多选按钮element
     * @createTime  2020/10/16 15:52
     */
    function cancelMultiple(thisBtn) {
        $(thisBtn).find('span').html('多选');
        checkFile.clear();//清空被选中的set
        $('#frontImg div').unbind().removeClass('active');//全部取消被选中的样式
        $(thisBtn).unbind().click(function () {
            multipleFun(this);//再点再来
        })
    }

    /**
     * 切换显示的视图
     * @author MrLu
     * @param thisBtn 切换按钮
     * @createTime  2020/10/16 14:18
     */
    function changeViewModel(thisBtn) {
        viewModel = !viewModel;
        //切换视图按钮
        $(thisBtn).unbind().click(function () {
            changeViewModel(thisBtn);
        });
        cancelMultiple($('#multipleBtn'));//初始化多选按钮
        if (viewModel) {
            //下拉图
            $('#multipleBtn,.img_t').hide();//多选按钮、大图显示、移动至按钮隐藏
            $('.img_p').show();
        } else {
            //平铺图
            $('#multipleBtn,.img_t').show();//多选按钮、按钮显示
            $('.img_p').hide();
        }
        $('#viewModel').val(viewModel);
        1
    }

    /**
     * 加载大图
     * @author MrLu
     * @param file
     * @createTime  2020/10/16 10:57
     * @return HTMLElement   |
     */
    function loadImgs(file) {
        let div = document.createElement('div');
        div.id = 'bigImg' + file.filecode;
        div.setAttribute('class', 'div_a bigImgClass');
        const proportion = +$('#proportion').val();
        let width = 957 * (proportion * 0.01);
        let height = 1467 * (proportion * 0.01);
        let bigImg = utils.createElement.createElement({
            tag: 'img', attrs: {
                src: file.serverip + file.fileurl,
                class: 'img_text bigImg',
                width: width + 'px', height: height + 'px',
                onerror: 'this.src="/images/noImage.jpg"'
            }
        });
        bigImg.addEventListener('click', function () {
            //绑定按钮
        });
        div.appendChild(bigImg);
        return div;
    }

    /**
     * 加载文书图片的缩略图
     * @author MrLu
     * @param file
     * @param index {i:当前图片的数组下标,f:数组长度}
     * @createTime  2020/10/16 9:39
     * @return HTMLElement   |
     */
    function loadThumbnail(file, index) {
        let a = document.createElement('a');
        a.id = 'thumbnail' + file.filecode;
        //创建缩略图
        let thumbnail = utils.createElement.createElement({
            tag: 'img', attrs: {
                src: file.serverip + file.fileurl,
                class: 'img_text',
                width: '120px', height: '154px',
                onerror: 'this.src="/images/noImage.jpg"'
            }
        });
        imgMap.set(file.filecode, file);
        //缩略图跳转图片
        a.addEventListener('click', function () {
            jumpImg(this);
            //缩略图点击更改按钮栏 文件级
            loadBtn(file.filecode);
        });
        //为第一张图自动添加事件
        if (0 === index) {
            loadBtn(file.filecode);
        }
        a.appendChild(thumbnail);
        return a;
    }

    /**
     * 大图区域跳转至指定图片 且缩略图增加被选择样式
     * @author MrLu
     * @param ele 缩略图ele（是个a标签）
     * @createTime  2020/10/20 16:53
     * @return    |
     */
    function jumpImg(ele) {
        const proportion = +$('#proportion').val();
        $('#thumbnailDiv .active').removeClass('active');
        $('#ImgBigDiv').animate({
            scrollTop: ((+($(ele).index()) * 1467 * proportion * 0.01) + 50)
        }, 300);
        $(ele).addClass('active');
    }

    /**
     * 移动两个元素的位置
     * @author MrLu
     * @param eleAid 元素A 被移动的
     * @param eleBid 元素B 参照位置
     * @param operation 操作  [before|after]
     * @createTime  2020/10/21 9:34
     * @return    |
     */
    function orderMove(eleAid, eleBid, operation) {
        //要移动的元素有 thumbnail  bigImg
        let thumbnailA = $('#thumbnail' + eleAid),
            bigImgA = $('#bigImg' + eleAid);
        if (eleBid) {
            //不是排在第一个
            $('#bigImg' + eleBid).after(bigImgA);
            $('#thumbnail' + eleBid).after(thumbnailA);
        } else {
            //排在第一个
            $('#ImgBigDiv').prepend(bigImgA);
            $('#thumbnailDiv').prepend(thumbnailA);
        }

        /*
        //这是可以通过左侧文书目录移动的代码
           //要移动的元素有 thumbnail  bigImg front
        //正常开发规范是不允许这么创建变量的，但是 😝
        *    let thumbnailA = $('#thumbnail' + eleAid),
                    bigImgA = $('#bigImg' + eleAid),
                    frontA = $('#front' + eleAid);
                if ('after' === operation) {
                    $('#thumbnail' + eleBid).after(thumbnailA);
                    $('#bigImg' + eleBid).after(bigImgA);
                    $('#front' + eleBid).after(frontA);
                } else if ('before' === operation) {
                    $('#thumbnail' + eleBid).before(thumbnailA);
                    $('#bigImg' + eleBid).before(bigImgA);
                    $('#front' + eleBid).before(frontA);
                } else {
                    console.error('你想干啥啊？CDD')
                }*/
    }

    /**
     * 移出
     * @author MrLu
     * @param fileCode
     * @createTime  2020/10/21 15:59
     */
    function fileMoveOut(fileCode) {
        $('#thumbnail' + fileCode).remove();
        $('#bigImg' + fileCode).remove();
        $('#front' + fileCode).remove();
        checkFile.delete(fileCode);
    }

    /**
     * 移入
     * @author MrLu
     * @param recordid 源文书id
     * @param filecode  被移动的文件代码
     * @param prevFileCode 上一个文书代码 当operation为false时候 值为null
     * @param operation boolean
     * @createTime  2020/10/21 16:00
     */
    function fileMoveIn(recordid, filecode, prevFileCode, operation) {
        $.post({
            url: '/ArrangeArchives/loadFilesByFileCode',
            data: {filecode, recordid},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    const thisFile = reV.value;
                    let thumbnail = loadThumbnail(thisFile);
                    let ImgBig = loadThumbnail(thisFile);
                    let front = loadThumbnail(thisFile);
                    loadTags(thisFile.filecode);//加载标签
                    //如果不是放第一个 调整顺序
                    if (operation) {
                        $('#thumbnail' + prevFileCode).after(thumbnail);
                        $('#bigImg' + prevFileCode).after(ImgBig);
                        $('#front' + prevFileCode).after(front);
                    } else {
                        $('#thumbnailDiv').prepend(thumbnail);
                        $('#ImgBigDiv').prepend(loadImgs(ImgBig));
                        $('#frontImg').prepend(loadFrontImg(front));//加载平铺图
                    }
                } else {
                    console.error('没有找到对应的实体文件');
                }
            }
        });
    }

    /**
     * 加载文书的平铺图
     * @author MrLu
     * @param file
     * @createTime  2020/10/16 11:03
     * @return    |
     */
    function loadFrontImg(file) {
        let div = document.createElement('div');
        div.id = 'front' + file.filecode;
        div.setAttribute('class', 'div_a frontDiv');
        //缩略图
        let front = utils.createElement.createElement({
            tag: 'img', attrs: {
                id: 'frontImg' + file.filecode,
                src: file.serverip + file.fileurl,
                class: 'img_text',
                width: '150px', height: '192px',
                onerror: 'this.src="/images/noImage.jpg"'
            }
        });
        //大图
        let largeWrapper = utils.createElement.createElement({
            tag: 'div', attrs: {
                id: 'preview' + file.filecode,
                style: 'display:none',
                class: 'larimg',

            }, arg: '<img width= "900px" height="1152px" src="' + file.serverip + file.fileurl + '" onerror=\'this.src="/images/noImage.jpg/"\'>'
        });
        /////////放大镜效果
        //生成跟随鼠标的小方框
        let mov = utils.createElement.createElement({
            tag: 'div', attrs: {
                id: 'mov' + file.filecode,
                class: 'movClass',
                style: 'display:none;pointer-events:none'
            }
        })

        front.onmouseover = function () {
            let pervDivLength = $('#front' + file.filecode).prevAll('.frontDiv').length;
            if (0 === pervDivLength % 4 && 0 !== pervDivLength) {
                $(largeWrapper).css('left', '-288px');
            }
            $(mov).show();//显示小框
            $(largeWrapper).show();//方大图显示
        }
        front.onmousemove = function (e) {
            let topY = e.offsetY;
            let topX = e.offsetX;
            mov.style.top = topY + 'px'
            mov.style.left = topX + 'px'
            largeWrapper.scrollLeft = (topX - 25) * 6;
            largeWrapper.scrollTop = (topY - 25) * 6;

        }
        front.addEventListener('mouseleave', function () {
            $(largeWrapper).removeAttr('style');
            $(mov).hide();
            $(largeWrapper).hide();
        })

        div.appendChild(front);
        div.appendChild(mov);
        div.appendChild(largeWrapper);

        return div;
    }

    /**
     * 得到文书的id
     * @author MrLu
     * @createTime  2020/10/21 15:57
     */
    function getRecordId() {
        return recordId;
    }

    let _recordImgLoad = function ({
                                       isReadOnlyP, recordIdP, fileOrder = [], callback = function () {
        }
                                   }) {
        if (this instanceof _recordImgLoad) {
            recordId = recordIdP;
            //重置一些值
            imgMap = new Map();
            checkFile = new Set();
            isReadOnly = isReadOnlyP;
            //查询该文书下的文书图片

            loadFilesByRecord(recordId, fileOrder, callback)
        } else {

            return new _recordImgLoad({
                isReadOnlyP: isReadOnlyP,
                recordIdP: recordIdP,
                fileOrder: fileOrder,
                callback: callback,
            })
        }

    };
    _recordImgLoad.prototype = {
        getRecordId,
        jumpImg,
        orderMove,
        fileMoveOut,
        fileMoveIn,
        loadBtn,
        loadThumbnail,
        loadImgs,
        loadFrontImg
    }
    return _recordImgLoad;

})();