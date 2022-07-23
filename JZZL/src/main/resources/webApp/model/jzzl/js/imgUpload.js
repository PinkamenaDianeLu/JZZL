/**
 * @author Mrlu
 * @createTime 2020/10/31
 * @url webApp.model.jzzl.js
 * @describe 图片上传
 */


var dropUpload = (function () {
    let files = new Map();

    /**
     * 加载拖拽域
     * @author MrLu
     * @param zoneId 拖拽域id
     * @createTime  2020/11/1 15:03
     * @return    |
     */
    function loadDropZone(zoneId) {
        let dz = document.getElementById(zoneId);
        //点击上传区域等于点击了上传文本框
        dz.addEventListener('click', function () {
            document.getElementById('newFiles').click();
        });
        //添加拖拽事件  拖来拖去
        dz.addEventListener('dragover', function (e) {
            this.style.borderColor = 'red';//设置边框颜色
            e.stopPropagation();
            //阻止浏览器默认打开文件的操作
            e.preventDefault();

        });
        dz.ondragleave = function () {
            //恢复边框颜色
            this.style.borderColor = 'gray';
        };
        //释放鼠标
        dz.addEventListener("drop", function (e) {
            this.style.borderColor = 'gray';
            e.stopPropagation();
            //阻止浏览器默认打开文件的操作
            e.preventDefault();
            const files = e.dataTransfer.files;

            //拖文件进来了呢
            createThumbnailZone(files)

        });
        $('#newFiles').change(function () {
            createThumbnailZone($(this)[0].files)
        });
        //加载拖拽域
        $('#thumbnailZone').sortable().disableSelection();
    }

    /**
     * 生成缩略图
     * @author MrLu
     * @param thisImgs file数组
     * @createTime  2020/11/1 17:05
     */
    function createThumbnailZone(thisImgs) {
        if (thisImgs.length > 0) {
            let frag = document.createDocumentFragment();
            // for (let thisImg of thisImgs) {
            for (let i = 0; i < thisImgs.length; i++) {
                let thisImg = thisImgs[i]
                let msg = '';//提示信息
                let isCool = true;//该图片是否符合规范
                let fileSize = +Math.round(thisImg.size * 100 / 1024) / 100;
                if (!/\/(jpg|png|JPG|PNG|jpeg|JPEG)$/.test(thisImg.type)||thisImg.name.indexOf('jpeg')>0) {
                    msg += ' 不是可上传的类型！';
                    isCool = false;
                } else if (fileSize > (5 * 1024)) {
                    msg += '文件过大！请上传小于5M的图片';
                    isCool = false;
                }
                frag.appendChild(createThumbnailDiv(thisImg, isCool, msg));
            }
            $('#thumbnailZone').append(frag);
        }
    }


    function createThumbnailDiv(thisImg, isCool, msg) {
        let key = 'img' + Math.floor(Math.random() * 1000);
        //记录
        if (isCool) {
            thisImg.keyForInput = key;
            files.set(key, thisImg);
        }
        let pDiv = utils.createElement.createElement({
            tag: 'div', attrs: {
                id: key,
                class: isCool ? 'thumbnail' : 'redThumbnail',
            }
        });
        let imgDiv = utils.createElement.createElement({
            tag: 'img', attrs: {
                src: window.URL.createObjectURL(thisImg),
                title: thisImg.name
            }
        });
        let inputDiv = utils.createElement.createElement({
            tag: 'input', attrs: {
                value: thisImg.name
            }
        });
        let msgDiv = utils.createElement.createElement({
            tag: 'span', attrs: {
                style: 'color:red', class: 'thumbnailMsg'
            }, arg: isCool ? '' : msg + '   请删除重传'
        });
        let delDiv = utils.createElement.createElement({
            tag: 'span', attrs: {
                class: 'cos01'
            }, arg: 'X'
        });
        //添加删除方法
        delDiv.addEventListener("click", function () {
            removeOne(key);
        });
        pDiv.appendChild(imgDiv);
        pDiv.appendChild(inputDiv);
        pDiv.appendChild(msgDiv);
        pDiv.appendChild(delDiv);
        return pDiv;
    }

    /**
     * 删除单个
     * @author MrLu
     * @param id
     * @createTime  2020/11/1 17:08
     */
    function removeOne(id) {
        $('#' + id).remove();
        files.delete(id);
    }

    /**
     * 全部清除
     * @author MrLu
     * @createTime  2020/11/1 16:51
     */
    function allRemove() {
        $('#thumbnailZone').empty();
        files.clear();
        $('#newFiles').val('');
    }

    /**
     * 上传
     * @author MrLu
     * @param recordId  要上传的文书id
     * @createTime  2020/11/1 17:12
     * @return    |
     */
    function upload(recordId, maxOrder) {
        if ($('#thumbnailZone').find('.redThumbnail').length > 0) {
            layer.alert('红色边框的图片无法上传，请删除后重试！')
        } else {
            startUpload(recordId)

/*
            const iterator1 = files[Symbol.iterator]();
            let overSize = files.size;
            let thumbnailCDF = $(document.createDocumentFragment());//创建一个存放上传后文件的虚拟dom节点
            let ImgBigCDF = $(document.createDocumentFragment());//创建一个存放上传后文件的虚拟dom节点
            let frontCDF = $(document.createDocumentFragment());//创建一个存放上传后文件的虚拟dom节点
            let newImgAry = [];
            let rlo = parent.lai.getRecordImgLoadObj();//获取当前的文书图片对象
            let pageSum = +parent.$('#pageSum').html().replace('/', '');
            for (const item of iterator1) {
                let formData = new FormData();
                let pDiv = $('#' + item[0]);
                let fileOrder = pDiv.index();//相对于这次上传的顺序
                formData.append('fileName', pDiv.find('input').val());//文件名称
                formData.append('fileOrder', fileOrder);//文件的顺序
                formData.append('newFile', item[1]);//文件本身
                formData.append('recordId', recordId);//文件所属文书的id
                formData.append('maxOrder', maxOrder);//文件所属文书的id
                pDiv.find('.thumbnailMsg').html('正在上传');
                $.post({
                    url: '/FileManipulation/addUpLoadRecordFile',
                    ache: false,
                    processData: false,
                    contentType: false,
                    data: formData,
                    success: (re) => {
                        const reV = JSON.parse(re);
                        if ("success" === reV.message) {
                            pDiv.find('.thumbnailMsg').html('上传成功').css('color', 'green');
                            overSize--;
                            //刷新父页面
                            files.delete(item[1].keyForInput);
                            const newImgObj = reV.value;//得到新创建的文书图片
                            newImgAry[fileOrder] = reV.value;//将上传后的文书放到对应的位置去
                            if (0 === overSize) {
                                layer.msg('全部上传成功！');
                                for (const thisFile of newImgAry) {
                                    if (thisFile) {
                                        //创建对应的组件
                                        thumbnailCDF.append(rlo.loadThumbnail(thisFile, 999));// loadThumbnail 是必须要有的 因为这个方法会将filecode存储到map中  999是假数 这里传的数字大于1即可
                                        ImgBigCDF.append(rlo.loadImgs(thisFile));
                                        frontCDF.append(rlo.loadFrontImg(thisFile));
                                        parent.$('#pageSum').html('/' + (pageSum + 1));// 总页数
                                    }
                                }
                                parent.$('#thumbnailDiv').append(thumbnailCDF);
                                parent.$('#ImgBigDiv').append(ImgBigCDF);
                                parent.$('#frontImg').append(frontCDF);//加载平铺图

                                //自我关闭
                                const index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                parent.layer.close(index);
                            }
                        } else {
                            newImgAry[pDiv.index()] = null;
                            pDiv.find('.thumbnailMsg').html('上传失败');
                        }
                    }
                })

            }*/
        }
    }


    function startUpload(recordId) {
        const iterator1 = files[Symbol.iterator]();
        // let fileNameAry=new Array();
        let formData = new FormData();
        formData.append('recordId', recordId);//文件所属文书的id
        for (const item of iterator1) {
            console.log(item)
            let pDiv = $('#' + item[0]);
            formData.append('newFile', item[1]);//文件本身
            pDiv.find('.thumbnailMsg').html('正在上传');
        }

        $.post({
            url: '/FileManipulation/addUpLoadRecordFileList',
            ache: false,
            processData: false,
            contentType: false,
            data: formData,
            success: (re) => {
                const reV = JSON.parse(re);
                if ("success" === reV.message){
                    const index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                    parent.location.reload();
                    parent.layer.close(index);
                }else {
                    layer.alert("上传失败");
                }

            }
        })
    }


    function _dropUpload(zoneId) {
        loadDropZone(zoneId);
    }

    _dropUpload.prototype = {allRemove, upload};
    return _dropUpload;
})();


$(function () {


    const recordId = utils.getUrlPar('recordId');
    const maxOrder = utils.getUrlPar('maxOrder');
    console.log(maxOrder);
    //拖拽域
    let zl = new dropUpload('dropZone');

    $('#submit-all').click(function () {
        $(this).unbind();//防止重复点击
        zl.upload(recordId, maxOrder);
    });
    $('#removeAll').click(function () {
        zl.allRemove();
    });

});