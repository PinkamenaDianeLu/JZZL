/**
 * @author Mrlu
 * @createTime 2020/11/2
 * @dependence 依赖js
 * @describe 卷详细信息
 *
 */

$(function () {
    const recordId = utils.getUrlPar('recordId');
    const fileCode = utils.getUrlPar('fileCode');
    console.log(recordId + '            ' + fileCode);
    //加载文书所有图片至swiper
    //每个文书有点击事件加载对应信息
    //添加按钮点击增加tags
    //查询所有图片
    $.post({
        url: '/ArrangeArchives/selectFilesByRecordId',
        data: {recordid: recordId},
        success: (re) => {
            const reV = JSON.parse(re);
            if ('success' === reV.message) {
                let swiperDiv = document.createDocumentFragment();
                for (let thisFile of reV.value) {
                    let thisFileImg = utils.createElement.createElement({
                        tag: 'div', attrs: {
                            id: 'fileId' + thisFile.id,
                            class:'swiper-slide'
                        }, arg: '<img src="' + thisFile.fileurl + '" style="width: 120px;height: 120px" alt="">'
                    })
                    swiperDiv.append(thisFileImg);
                }
                $('#swiperDIv').append(swiperDiv);
                var mySwiper = new Swiper('.swiper-container', {
                    autoplay: true,//可选选项，自动滑动
                })
            } else {
            }
        }
    });
})