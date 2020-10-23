/**
 * @author Mrlu
 * @createTime 2020/10/22
 * @dependence 依赖js
 * @describe 卷宗封皮
 *
 */

$(function () {
    const thisCover = parent.recordImgLoad.pValue;
    console.log(thisCover)

    $.post({
        url: '/FileManipulation/loadArchiveCover',
        data: {fileId: thisCover.id},
        success: (re) => {
            const reV = JSON.parse(re);
            if ('success' === reV.message) {
                //判断是否有封皮信息
                if (reV.value) {
                    //更新封皮
                    //加载
                    for (let thisCol in reV.value) {
                        console.log(thisCol);
                        console.log(reV.value[thisCol]);
                    }
                } else {
                    //新建封皮
                    //加载应用信息
                    $.post({
                        url: '/FileManipulation/initializationCoverMessage',
                        data: {
                            sfcId: thisCover.archivesfcid,
                            recordId: thisCover.archiverecordid
                        },
                        success: (re) => {
                            const reV = JSON.parse(re);
                            if ('success' === reV.message) {
                                utils.setValue(reV.value);
                            } else {
                            }
                        }
                    });

                }
            } else {
            }
        }
    });
})