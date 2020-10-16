/**
 * @author Mrlu
 * @createTime 2020/10/15
 * @dependence jquery.min.js,layer.js,utils.js
 * @describe  文书图片加载
 *
 */

var recordImgLoad = (function () {
    let recordId;//文书id
    let viewModel;//显示状态 true 下拉图 false平铺图
    let checkFile=new Set();//被选中的文件数组

    /**
     * 查询文书的图片数据
     * @author MrLu
     * @param recordId 文书id
     * @createTime  2020/10/15 18:31
     * @return    |
     */
    function loadFilesByRecord(recordId) {
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
                        loadThumbnail(thisFile, i++);//加载缩略图
                        loadFrontImg(thisFile);//加载平铺图
                    });
                    //切换视图按钮
                    viewModel = false;//下拉显示
                    changeViewModel($('#changeView'));//加载一下下拉展示

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

        $('#frontImg a').click(function () {
            //TODO MrLu 2020/10/16 找美工要个选择样式
            const fileId = $(this).attr('id')
            //判断是否被选中
            if ($(this).hasClass('active')){
                //被选中 -> 取消选中
                $(this).removeClass('active');
                checkFile.delete(fileId);
            }else {
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
        console.log(checkFile);

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
        $('#frontImg a').unbind().removeClass('active');//全部取消被选中的样式
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
        })
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
        let bigImg = utils.createElement.createElement({
            tag: 'img', attrs: {
                id: 'bigImg' + file.id,
                src: file.fileurl,
                class: 'img_text',
                width: '700px', height: '900px'
            }
        });
        bigImg.addEventListener('click', function () {
            //绑定按钮
        })
        return bigImg;
    }

    /**
     * 加载文书图片的缩略图
     * @author MrLu
     * @param file
     * @param index { i:当前图片的数组下标,f:数组长度}
     * @createTime  2020/10/16 9:39
     * @return    |
     */
    function loadThumbnail(file, index) {
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
            $('#thumbnailDiv .active').removeClass('active');
            $('#ImgBigDiv').animate({
                scrollTop: ((+index * 920) + 50)
            }, 300);
            $(this).addClass('active');
        })
        let bigImg = loadImgs(file);
        a.append(thumbnail);
        $('#thumbnailDiv').append(a);
        $('#ImgBigDiv').append(bigImg);

    }

    /**
     * 加载文书的平铺图
     * @author MrLu
     * @param file
     * @createTime  2020/10/16 11:03
     * @return    |
     */
    function loadFrontImg(file) {
        let a = document.createElement('a');
        a.id = 'front' + file.id;
        let front = utils.createElement.createElement({
            tag: 'img', attrs: {
                src: file.fileurl,
                class: 'img_text',
                width: '140px', height: '180px'
            }
        });

        a.append(front)
        $('#frontImg').append(a);
    }

    let _recordImgLoad = function (recordIdP) {
        console.log('开始图片' + recordIdP);
        recordId = recordIdP;
        loadFilesByRecord(recordId)
    }
    _recordImgLoad.prototype = {}
    return _recordImgLoad;

})()