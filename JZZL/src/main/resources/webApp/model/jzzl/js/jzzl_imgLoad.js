/**
 * @author Mrlu
 * @createTime 2020/10/15
 * @dependence jquery.min.js,layer.js,utils.js
 * @describe  文书图片加载
 *
 */

var recordImgLoad = (function () {
    let recordId;

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
                    $('#ImgBigDiv').empty();
                    console.log(reV.value)
                    const files = reV.value;
                    if (files && files.length > 0)
                        utils.functional.forEach(files, function (thisFile) {
                            loadImgs(thisFile);
                        })

                } else {
                }
            }
        });
    }

    function loadImgs(file) {
        let a = utils.createElement.createElement({
            tag: 'img', attrs: {
                id: 'Img' + file.id,
                src: file.fileurl,
                class: 'img_text',
                width:'700px',height:'900px'
            }
        });
        $('#ImgBigDiv').append(a);
        //<img src="/images/未标题-1.jpg" class="img_text" id="img1"/>
    }

    let _recordImgLoad = function (recordIdP) {
        recordId = recordIdP;
        loadFilesByRecord(recordId)
    }
    _recordImgLoad.prototype = {}
    return _recordImgLoad;

})()