/**
 * @author Mrlu
 * @createTime 2020/10/31
 * @url webApp.model.jzzl.js
 * @describe 图片上传
 */

$(function () {
    console.log(parent.recordImgLoad.pValue);
    console.log(utils.getUrlPar('recordId'));
    $('.js-uploader__box').uploader({
        //配置参数
    });
    $('#submitFiles').click(function () {
        console.log($("#fileinput"));
    })
/*    let formData = new FormData();


    formData.append('newFile', $("#newFiles")[0].files[0]);
    $.post({
        url: '/FileManipulation/upLoadRecordFiles',
        ache: false,
        processData: false,
        contentType: false,
        data: formData,
        success: (re) => {
            const reV = JSON.parse(re);
            if ('success' === reV.message) {
                if ("success" === reV.message) {
                    //刷新父页面
                    parent.dqfxpgbgOut.reFreshTk();
                    layer.msg('提交成功');
                    //自我关闭
                    const index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                    parent.layer.close(index);
                }
            }
        }
    })*/
})