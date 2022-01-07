/**
 * @author Mrlu
 * @createTime 2020/12/25
 * @url webApp.model.jzzl.js
 * @describe 图片重新上传
 */


$(function () {
    const fileId = utils.getUrlPar('fileId');
    const fileCode = utils.getUrlPar('fileCode');

    $.post({
        url: '/FileManipulation/selectFileByFileId',
        data: {
            fileid: fileId
        },
        success: (re) => {
            const reV = JSON.parse(re);
            if ('success' === reV.message) {
                $('#oldImg').attr('src', reV.value.serverip + reV.value.fileurl);
            } else {
                layer.alert('加载该图片失败！');
            }
        }
    });


    let dz = document.getElementById('uploadZone');
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
        createThumbnailZone(files[0])

    });
    $('#newFiles').change(function () {
        createThumbnailZone($(this)[0].files[0])
    });

    function createThumbnailZone(thisImg) {
        if (thisImg) {
            let isCool = true;//该图片是否符合规范
            let fileSize = +Math.round(thisImg.size * 100 / 1024) / 100;
            if (!/\/(jpg|jpeg|png|JPG|PNG)$/.test(thisImg.type)) {
                layer.alert(' 不是可上传的类型！');
                isCool = false;
            } else if (fileSize > (5 * 1024)) {
                layer.alert('文件过大！请上传小于5M的图片');
                isCool = false;
            }
            if (isCool) {
                $('#newImg').attr('src', window.URL.createObjectURL(thisImg));
                $('#jia').remove();
                $('#newImg').show();
                $('#reUploadBtn').unbind().click(function () {
                    console.log(thisImg);
                    let formData = new FormData();
                    formData.append('newFile', thisImg);//文件本身
                    formData.append('fileId', fileId);//文件所属文书的id
                    $.post({
                        url: '/FileManipulation/reUpLoadFile',
                        ache: false,
                        processData: false,
                        contentType: false,
                        data: formData,
                        success: (re) => {
                            const reV = JSON.parse(re);
                            if ("success" === reV.message) {
                                layer.msg('替换成功！');
                                //替换父页面
                                // parent.$('#frontImg'+fileCode).attr('src',reV.value);
                                parent.$('#front'+fileCode).find('img').attr('src',reV.value);
                                parent.$('#thumbnail'+fileCode).find('img').attr('src',reV.value);
                                parent.$('#bigImg'+fileCode).find('img').attr('src',reV.value);
                                //自我关闭
                                const index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                                parent.layer.close(index);
                            } else {
                                layer.msg('该图片替换失败');
                            }
                        }
                    })
                })
            }
        }
    }


});