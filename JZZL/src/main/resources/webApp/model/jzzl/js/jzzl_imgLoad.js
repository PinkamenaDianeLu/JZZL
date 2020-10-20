/**
 * @author Mrlu
 * @createTime 2020/10/15
 * @dependence jquery.min.js,layer.js,utils.js,jquery-ui.js
 * @describe  文书图片加载
 *
 */

var recordImgLoad = (function () {
    let recordId;//文书id
    let viewModel;//显示状态 true 下拉图 false平铺图
    let checkFile = new Set();//被选中的文件的filecode数组

    /**
     * 查询文书的图片数据
     * @author MrLu
     * @param recordId 文书id
     * @param fileId  指定的文件id 该参数可为空
     * @createTime  2020/10/15 18:31
     * @return    |
     */
    function loadFilesByRecord(recordId,fileId=null) {
        $.post({
            url: '/ArrangeArchives/loadFilesByRecord',
            data: {recordId: recordId},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    $('#ImgBigDiv,#thumbnailDiv').empty().scrollTop(0);
                    $('#frontImg').empty();
                    const files = reV.value;
                    if (!(files && files.length > 0)) {
                        //一页文书都没有还显示个p
                        return;
                    }
                    let i = 0;
                    utils.functional.forEach(files, function (thisFile) {
                        loadThumbnail(thisFile, i++,fileId);//加载缩略图
                        loadFrontImg(thisFile);//加载平铺图
                    });
                    //切换视图按钮
                    viewModel = false;//下拉显示
                    changeViewModel($('#changeView'));//加载一下下拉展示
                    FrontImgSortTable();//加载平铺图的拖拽效果
                    //为移动至按钮添加方法
                    $('#moveToBtn').unbind().click(function () {
                        console.log('移动至方法')
                        moveToFn();
                    })
                    //为切换按钮添加方法
                    $('#changeView').unbind().click(function () {
                        changeViewModel(this);
                    });
                } else {
                }
            }
        });
    }

    /**
     * 平铺图的拖拽控件加载
     * @author MrLu
     * @param
     * @createTime  2020/10/17 11:51
     * @return    |
     */
    function FrontImgSortTable() {

        $('#frontImg').sortable().disableSelection();
    }

    /**
     * 多选按钮
     * @author MrLu
     * @createTime  2020/10/16 14:08
     * @return    |
     */
    function multipleFun(thisBtn) {
        $(thisBtn).find('span').html('取消多选');

        //原有按钮添加取消选中方法
        $(thisBtn).unbind().click(function () {
            cancelMultiple(this)
        })

        //所有图片添加事件
        checkFile = new Set();//被选中值set

        $('#frontImg div').click(function () {
            //TODO MrLu 2020/10/16 找美工要个选择样式
            const fileId = $(this).attr('id')
            //判断是否被选中
            if ($(this).hasClass('active')) {
                //被选中 -> 取消选中
                $(this).removeClass('active');
                checkFile.delete(fileId);
            } else {
                //未被选中 选
                $(this).addClass('active');
                checkFile.add(fileId);//记录被选的文件id
            }

            console.log(checkFile);
        })
    }

    /**
     * 移动至方法
     * @author MrLu
     * @param
     * @createTime  2020/10/16 16:24
     * @return    |
     */
    function moveToFn() {
        //被移动的文书filecode set
        if (!checkFile||checkFile.size===0){
            alert('请选择要移动的文书！');return false;}
        const pString = JSON.stringify(Array.from(checkFile).join(','));

        let url='/model/jzzl/jzYdTable.html?fileCodes='+pString;
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
     * @return    |
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
     * @return    |
     */
    function loadImgs(file) {
        let div = document.createElement('div');
        div.setAttribute('class', 'div_a');
        let bigImg = utils.createElement.createElement({
            tag: 'img', attrs: {
                id: 'bigImg' + file.id,
                src: file.fileurl,
                class: 'img_text'
            }
        });
        bigImg.addEventListener('click', function () {
            //绑定按钮
        });
        div.append(bigImg)
        return div;
    }

    /**
     * 加载文书图片的缩略图
     * @author MrLu
     * @param file
     * @param index { i:当前图片的数组下标,f:数组长度}
     * @param fileId 指定的文件id 可为null
     * @createTime  2020/10/16 9:39
     * @return    |
     */
    function loadThumbnail(file, index,fileId=null) {
        let a = document.createElement('a');
        //创建缩略图
        let thumbnail = utils.createElement.createElement({
            tag: 'img', attrs: {
                id: 'thumbnail' + file.id,
                src: file.fileurl,
                class: 'img_text',
                width: '120px', height: '154px'
            }
        });
        //缩略图跳转图片
        a.addEventListener('click', function () {
            jumpImg(this,index)
        });
        let bigImg = loadImgs(file);
        a.append(thumbnail);
        $('#thumbnailDiv').append(a);
        $('#ImgBigDiv').append(bigImg);
        //如果有指定跳转的图片  跳转
        if (fileId&&file.id===fileId){
            jumpImg(a,index)
        }

    }

     /**
     * 大图区域跳转至指定图片 且缩略图增加被选择样式
     * @author MrLu
     * @param ele 缩略图ele（是个a标签）
      * @param index 相对位置
      * @createTime  2020/10/20 16:53
     * @return    |
      */
    function jumpImg(ele,index) {
        $('#thumbnailDiv .active').removeClass('active');
        $('#ImgBigDiv').animate({
            scrollTop: ((+index * 940) + 50)
        }, 300);
        $(ele).addClass('active');
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
        div.id = file.filecode;
        div.setAttribute('class', 'div_a');
        let front = utils.createElement.createElement({
            tag: 'img', attrs: {
                src: file.fileurl,
                class: 'img_text',
                width: '140px', height: '180px'
            }
        });

        div.append(front)
        $('#frontImg').append(div);
    }

    function getRecordId(){
        return recordId;
    }

    let _recordImgLoad = function (recordIdP,fileId) {
        console.log('开始图片' + recordIdP);
        recordId = recordIdP;
        loadFilesByRecord(recordId,fileId)
    }
    _recordImgLoad.prototype = {getRecordId,jumpImg}
    return _recordImgLoad;

})()