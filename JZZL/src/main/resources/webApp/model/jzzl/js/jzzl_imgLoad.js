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

    /**
     * 查询文书的图片数据
     * @author MrLu
     * @param recordId 文书id
     * @param fileOrder 文书顺序数组
     * @param callback  加载完文书图片后的回调函数
     * @createTime  2020/10/15 18:31
     */
    function loadFilesByRecord(recordId, fileOrder, callback) {
        $.post({
            url: '/ArrangeArchives/loadFilesByFileCodes',
            data: {fileOrder: fileOrder.join(','),recordid:recordId},
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

                        });
                        /*********************为功能区按钮添加方法******************************/
                        //切换视图按钮
                        viewModel = false;//下拉显示
                        changeViewModel($('#changeView'));//加载一下下拉展示
                        FrontImgSortTable();//加载平铺图的拖拽效果
                        //为移动至按钮添加方法
                        $('#moveToBtn').unbind().click(function () {
                            console.log('移动至方法');
                            moveToFn();
                        });
                        //为切换按钮添加方法
                        $('#changeView').unbind().click(function () {
                            changeViewModel(this);
                        });

                    }
                    callback();//回调方法
                } else {
                }
            }
        });
    }


     /**
     * 加载按钮
     * @author MrLu
      * @param thisFileCode  传递想要操作的文书的thisFileCode,当！thisFileCode时候将视为直接操作整个文书
     * @createTime  2020/10/31 12:55
     * @return    |
      */
    function loadBtn(thisFileCode) {
        console.log('操作对象为：'+(thisFileCode||'整个文书'))
         //为下载按钮添加方法
         $('#downLoadBtn').unbind().click(function () {
             if (thisFileCode){
                 utils.createElement.downLoadImg(imgMap.get(thisFileCode).fileurl);
             }else {
                 //当操作整个文书对象时  下载文书的所有图片
                 const iterator1 = imgMap[Symbol.iterator]();
                 for (const item of iterator1) {
                     utils.createElement.downLoadImg(item[1].fileurl);
                 }
             }
         });
         //新建标签按钮
         $('#newTagBtn').unbind().click(function () {
             if (thisFileCode){
                 //鼠标点击固定区域新建标签  弹开个页 鼠标点哪加哪  保存后刷新该文书的标签方法加载标签 新建个标签表
             }else {
                layer.alert('请选择一张具体的文书图片');
             }
         });
         //重新上传按钮
         $('#reUpLoadBtn').unbind().click(function () {
             if (thisFileCode){
                 //上传至该文书后面
             }else {
                 //上传至整个文书的最后
                 layer.alert('请选择需要重新上传的图片');
             }
         });
         //添加上传按钮
         $('#addUploadBtn').unbind().click(function () {
             if (thisFileCode){
                 //上传至该文书后面
             }else {
                 //上传至整个文书的最后
             }
             recordImgLoad.pValue=recordId;
             layer.open({
                 icon: 1,
                 type: 2,
                 title: '新建卷',
                 skin: 'layui-layer-lan',
                 maxmin: false,
                 shadeClose: true, //点击遮罩关闭层
                 area: ['1111px', '600px'],
                 content: '/model/jzzl/imgUpload.html?recordId='+recordId
             });
         });
         //删除按钮
         $('#deleteBtn').unbind().click(function () {

         });
         //图片详细按钮
         $('#imgInfoBtn').unbind().click(function () {

         });
    }

    /**
     * 平铺图的拖拽控件加载
     * @author MrLu
     * @createTime  2020/10/17 11:51
     */
    function FrontImgSortTable() {

        $('#frontImg').sortable().disableSelection();
    }

    /**
     * 多选按钮
     * @author MrLu
     * @createTime  2020/10/16 14:08
     */
    function multipleFun(thisBtn) {
        $(thisBtn).find('span').html('取消多选');

        //原有按钮添加取消选中方法
        $(thisBtn).unbind().click(function () {
            cancelMultiple(this)
        });

        //所有图片添加事件
        checkFile = new Set();//被选中值set

        $('#frontImg div').click(function () {
            const filecode = $(this).attr('id')
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
            console.log(checkFile);
        })
    }

    /**
     * 移动至方法
     * @author MrLu
     * @createTime  2020/10/16 16:24
     * @return    |
     */
    function moveToFn() {
        //被移动的文书filecode set
        if (!checkFile || checkFile.size === 0) {
            alert('请选择要移动的文书！');
            return false;
        }
        const pString = JSON.stringify(Array.from(checkFile).join(','));
        let url = '/model/jzzl/jzYdTable.html?fileCodes=' + pString;
        console.log(url);
        layer.open({
            icon: 1,
            type: 2,
            title: '新建卷',
            skin: 'layui-layer-lan',
            maxmin: false,
            shadeClose: true, //点击遮罩关闭层
            area: ['1111px', '600px'],
            content: url
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
        div.setAttribute('class', 'div_a');
        let bigImg = utils.createElement.createElement({
            tag: 'img', attrs: {
                src: file.fileurl,
                class: 'img_text'
            }
        });
        bigImg.addEventListener('click', function () {
            //绑定按钮
        });
        div.append(bigImg);
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
                src: file.fileurl,
                class: 'img_text',
                width: '120px', height: '154px'
            }
        });
        imgMap.set(file.filecode,file);
        //缩略图跳转图片
        a.addEventListener('click', function () {
            jumpImg(this);
            //缩略图点击更改按钮栏 文件级
            loadBtn(file.filecode);
        });
        a.append(thumbnail);
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
        $('#thumbnailDiv .active').removeClass('active');
        $('#ImgBigDiv').animate({
            scrollTop: ((+($(ele).index()) * 940) + 50)
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
        //要移动的元素有 thumbnail  bigImg front
        //正常开发规范是不允许这么创建变量的，但是 😝
        let thumbnailA = $('#thumbnail' + eleAid),
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
        }

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
     * @param recordid 文书id
     * @param filecode
     * @param prevFileCode 上一个文书代码 当operation为false时候 值为null
     * @param operation boolean
     * @createTime  2020/10/21 16:00
     */
    function fileMoveIn(recordid,filecode, prevFileCode, operation) {
        $.post({
            url: '/ArrangeArchives/loadFilesByFileCode',
            data: {filecode,recordid},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    const thisFile = reV.value;
                    let thumbnail = loadThumbnail(thisFile);
                    let ImgBig = loadThumbnail(thisFile);
                    let front = loadThumbnail(thisFile);
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
        div.setAttribute('class', 'div_a');
        let front = utils.createElement.createElement({
            tag: 'img', attrs: {
                src: file.fileurl,
                class: 'img_text',
                width: '140px', height: '180px'
            }
        });

        div.append(front)
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
                                       recordIdP, fileOrder = [], callback = function () {
        }
                                   }) {
        if (this instanceof _recordImgLoad) {
            recordId = recordIdP;
            loadFilesByRecord(recordId, fileOrder, callback)
        } else {

            return new _recordImgLoad({
                recordIdP: recordIdP,
                fileOrder: fileOrder,
                callback: callback,
            })
        }

    };
    _recordImgLoad.prototype = {getRecordId, jumpImg, orderMove, fileMoveOut, fileMoveIn,loadBtn}
    return _recordImgLoad;

})();