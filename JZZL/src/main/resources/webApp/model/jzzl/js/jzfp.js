/**
 * @author Mrlu
 * @createTime 2020/10/22
 * @dependence 依赖js
 * @describe 卷宗封皮
 *
 */
//共多少页
var recordCover = (function () {
    let file;

    /**
     * 计算封皮下方共几卷第几卷多少页
     * @author MrLu
     * @createTime  2020/10/30 14:57
     * @return  Arrary  |  []
     */
    function getColumnNum() {
        let reValue = ['0', '0', '0'];
        //查找该文书类型的总页数
        const records = $('#P' + file.archivetypeid).find('.recordSortZone').find('.v2');
        /*  let pageCount = 0;
          utils.functional.forEach(records, function (thisRecord) {
              pageCount += $(thisRecord).find('.v3').length;
          })
          reValue[2] = pageCount;*/
        //共多少卷
        let typeCount = 0;
        let typeNum = 1;
        for (let thisType of $('#archiveIndex').find('.v1')) {
            if ($(thisType).find('.recordSortZone').find('.v2').length > 0) {
                typeCount++;
                //第几卷
                if (+$(thisType).attr('id').replace('P', '') === file.archivetypeid) {
                    typeNum = typeCount;
                }
            }
        }
        reValue[0] = utils.convertToChinaNum(typeCount);
        //第几卷
        reValue[1] = utils.convertToChinaNum(typeNum);

        return reValue;
    }

    /**
     * 计算一个卷的页数
     * @author Mrlu
     * @param typeid  archivetypeid
     * @createTime  2021/2/23 14:35
     * @return    |
     */
    function getArchivePageNum(typeid) {
        $.post({
            url: '/FileManipulation/getArchivePageNum',
            data: {typeid},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    $('#pagecount').val(reV.value);//总页数
                } else {
                    console.error('卷宗页数计算失败');
                }
            }
        });
    }

    /**
     * 初始化卷宗封皮
     * @author MrLu
     * @createTime  2020/10/30 14:58
     * @return    |
     */
    function initializationCover() {
        $.post({
            url: '/FileManipulation/initializationCoverMessage',
            data: {
                sfcId: file.archivesfcid,
                recordId: file.archiverecordid
            },
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    console.log(reV.value);
                    utils.setValue(reV.value);
                    const ColumnNum = getColumnNum();
                    $('#archivecount').val(ColumnNum[0]);//共几卷
                    $('#recordcount').val(ColumnNum[1]);//第几卷
                    getArchivePageNum(file.archivetypeid);
                    // $('#pagecount').val(+ColumnNum[2]);//总页数
                } else {
                    console.error('卷封皮初始化失败');
                }
            }
        });
    }

    /**
     * 加载封皮信息
     * @author MrLu
     * @createTime  2020/10/30 15:02
     * @return    |
     */
    function loadCover() {
        $.post({
            url: '/FileManipulation/loadArchiveCover',
            data: {fileId: file.id},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    //判断是否有封皮信息
                    if (reV.value) {
                        //更新封皮
                        //加载
                        for (let thisCol in reV.value) {
                            $('#' + thisCol).val(reV.value[thisCol]);
                        }
                        //保存卷宗封皮
                        $('#saveCover').unbind().click(function () {
                            saveCover(reV.value.id)
                        });
                    } else {
                        //新建封皮
                        //加载应用信息
                        initializationCover();
                        $('#saveCover').unbind().click(function () {
                            saveCover(-1);
                        });

                    }
                } else {
                    console.error('卷封皮信息加载错误');
                }
            }
        });
    }


    /**
     * 保存封皮信息
     * @author MrLu
     * @param coverId 封皮id 没有传-1
     * @createTime  2020/10/30 15:03
     * @return    |
     */
    function saveCover(coverId) {
        $('#saveCover').unbind().html('生成中...');
        let cover = {};
        for (let thisCol of $('.coverInfo')) {
            cover[$(thisCol).attr('id')] = $(thisCol).val() || ' ';
        }

        let coverDiv=document.getElementById('jzfpDiv');
        $('#qzname').css('font-size','20px')



        //生成图片至画布
        html2canvas(coverDiv).then(function (canvas) {
            $('#qzname').css('font-size','36px');
            //将画布的图片转成base64再转成File
            const coverImg= utils.Base64.base64ToFile(canvas.toDataURL('image/jpg'),'fileCover','jpg');
            let formData = new FormData();//fileType
            formData.append('newFile',coverImg);//文件本身
            formData.append('fileid',file.id);//
            formData.append('coverid',coverId);//
            formData.append('cover',JSON.stringify(cover));//
            $.post({
                url: '/FileManipulation/saveCover',
                ache: false,
                processData: false,
                contentType: false,
                data: formData,
                success: (re) => {
                    $('#saveCover').unbind().html('保存').click(function () {
                        saveCover(reV.id)
                    });
                    const reV = JSON.parse(re);
                    if ('success' === reV.message) {
                        loadCover();
                        layer.msg('保存成功');
                    } else {
                        layer.alert('卷封皮保存失败');
                    }
                }
            })
        })

    }

    function _recordCover(fileP) {
        file = fileP;
    }

    _recordCover.prototype = {
        getColumnNum, loadCover, getArchivePageNum
    };
    return _recordCover;
})();
$(function () {
    const thisCover = parent.recordImgLoad.pValue;
    console.log(thisCover.id)
    let rc = new recordCover(thisCover);
    rc.loadCover();

    //自动生成页码
    $('#getPageNum').click(function () {
        let ColumnNum = rc.getColumnNum();
        $('#archivecount').val(ColumnNum[0]);//共几卷
        $('#recordcount').val(ColumnNum[1]);//第几卷
        $('#pagecount').val(rc.getArchivePageNum(thisCover.archivetypeid));//总页数
        layer.msg('计算完成！');
    })

})