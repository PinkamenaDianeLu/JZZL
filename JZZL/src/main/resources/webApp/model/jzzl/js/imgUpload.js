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
            for (let thisImg of thisImgs) {
                let msg = '';//提示信息
                let isCool = true;//该图片是否符合规范
                let fileSize = +Math.round(thisImg.size * 100 / 1024) / 100;
                if (!/\/(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(thisImg.type)) {
                    msg += ' 不是可上传的类型！';
                    isCool = false;
                } else if (fileSize > (5 * 1024)) {
                    msg += '文件过大！请上传小于5M的图片';
                    isCool = false;
                }
                frag.append(createThumbnailDiv(thisImg, isCool, msg));
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
            }, arg: isCool ? '' : msg + ',请删除重传'
        });
        let delDiv = utils.createElement.createElement({
            tag: 'span', attrs: {
                class: 'cos01'
            }, arg: 'X'
        });
        //添加删除方法
        delDiv.addEventListener("click", function () {
            removeOne(key);
        })
        pDiv.append(imgDiv);
        pDiv.append(inputDiv);
        pDiv.append(msgDiv);
        pDiv.append(delDiv);
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
    function upload(recordId) {
        if ($('#thumbnailZone').find('.redThumbnail').length > 0) {
            layer.alert('红色边框的图片无法上传，请删除后重试！')
        } else {
            const iterator1 = files[Symbol.iterator]();
            for (const item of iterator1) {
                let formData = new FormData();
                let pDiv = $('#' + item[0]);
                formData.append('fileName', pDiv.find('input').val());//文件名称
                formData.append('fileOrder', pDiv.index());//文件的顺序
                formData.append('newFile', item[1]);//文件本身
                formData.append('recordId', recordId);//文件所属文书的id
                console.log(pDiv.find('input').val());
                console.log(pDiv.index());
                console.log(item[1]);
                pDiv.find('.thumbnailMsg').html('正在上传');
                $.post({
                    url: '/FileManipulation/upLoadRecordFiles',
                    ache: false,
                    processData: false,
                    contentType: false,
                    data: formData,
                    success: (re) => {
                        const reV = JSON.parse(re);
                        if ("success" === reV.message) {
                            pDiv.find('.thumbnailMsg').html('上传成功');
                            //刷新父页面
                            // parent.dqfxpgbgOut.reFreshTk();
                            // layer.msg('提交成功');
                            files.delete(item[1].keyForInput);
                            //自我关闭
                         /*   const index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                            parent.layer.close(index);*/
                        }
                    }
                })

                /*         if (files.size === 0) {
                             layer.alert('上传完成');
                             const index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                             parent.layer.close(index);
                         }*/
                //如果有上传失败的话
                /*
                /  pDiv.find('.thumbnailMsg').html('上传失败');
                  layer.alert('有部分文件未能完成上传，请检查文件并刷新后重试');
                   const index = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(index);
                / */
            }
        }
    }


    function _dropUpload(zoneId) {
        loadDropZone(zoneId);
    }

    _dropUpload.prototype = {allRemove, upload};
    return _dropUpload;
})();


$(function () {
    console.log(utils.getUrlPar('recordId'));
    const recordId = utils.getUrlPar('recordId');
    //拖拽域
    let zl = new dropUpload('dropZone');

    $('#submit-all').click(function () {
        $(this).unbind();//防止重复点击
        zl.upload(recordId);
    });
    $('#removeAll').click(function () {
        zl.allRemove();
    });

})