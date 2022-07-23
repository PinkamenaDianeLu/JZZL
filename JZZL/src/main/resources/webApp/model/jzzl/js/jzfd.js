/**
 * @author Mrlu
 * @createTime 2020/10/27
 * @dependence 依赖js
 * @describe  卷宗封底
 *
 */

var recordBackCover = (function () {

    let file;

    /**
     * 保存封底
     * @author Mrlu
     * @param backcoverid
     * @createTime  2021/2/25 14:27
     * @return    |
     */
    function saveBackCover(backcoverid) {
        $('#saveBackCover').unbind().html('保存中...');
        if (!$('#ljr').val()){
            layer.alert("请填写立卷人！");
            return false;
        }
        let cover = {};
        for (let thisCol of $('.coverInfo')) {
            cover[$(thisCol).attr('id')] = $(thisCol).val() || ' ';
        }
        $('#coverTitle').css('font-size','20px')
        let backCoverDiv=document.getElementById('jzfdDiv');
        html2canvas(backCoverDiv).then(function (canvas) {
            //将画布的图片转成base64再转成File
            $('#coverTitle').css('font-size','40px')
            const  imgBase64=canvas.toDataURL('image/jpg');
            const coverImg= utils.Base64.base64ToFile(imgBase64,'BackCover','jpg');
            let formData = new FormData();//fileType
            formData.append('newFile',coverImg);//文件本身
            formData.append('fileid',file.id);//
            formData.append('backcoverid',backcoverid);//
            formData.append('backcover',JSON.stringify(cover));//
            $.post({
                url: '/FileManipulation/saveBackcover',
                ache: false,
                processData: false,
                contentType: false,
                data: formData,
                success: (re) => {
                    const reV = JSON.parse(re);
                    if ('success' === reV.message) {
                        // loadCover();
                        $('#saveBackCover').unbind().html('保存').click(function () {
                            saveBackCover(reV.value);
                        });
                        layer.msg('保存成功');
                    } else {
                        layer.alert('保存失败');
                    }
                }
            })
        })

    }

    /**
     * 加载封底
     * @author Mrlu
     * @createTime  2021/2/25 14:36
     * @return    |
     */
    function loadBackCover() {
        //
        $.post({
            url: '/FileManipulation/loadArchiveBackCover',
            data: {fileId: file.id},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    //有已经保存的封底
                    if (reV.value) {
                        //更新
                        //加载
                        for (let thisCol in reV.value) {
                            $('#' + thisCol).val(reV.value[thisCol]);
                        }
                        //保存卷宗封皮
                        $('#saveBackCover').unbind().click(function () {
                            saveBackCover(reV.value.id)
                        });
                    } else {
                        //新建
                        //加载应用信息
                        // initializationCover();
                        $('#saveBackCover').unbind().click(function () {
                            saveBackCover(-1);
                        });

                    }
                } else {
                    console.error('卷封底信息加载错误');
                }
            }
        });

    }


    function _recordBackCover(fileP) {
        file = fileP;
    }

    _recordBackCover.prototype = {
        loadBackCover, saveBackCover
    };
    return _recordBackCover;
})();


$(function () {
    const thisCover = parent.recordImgLoad.pValue;
    let rbc = new recordBackCover(thisCover);
    rbc.loadBackCover();

});