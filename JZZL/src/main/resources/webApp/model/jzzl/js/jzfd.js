/**
 * @author Mrlu
 * @createTime 2020/10/27
 * @dependence 依赖js
 * @describe  卷宗封底
 *
 */

var recordBackCover=(function () {
    function loadBackCover() {

    }
    
    function saveBackCover() {

    }


    function _recordBackCover(fileP) {
        file = fileP;
    }

    _recordBackCover.prototype = {
         loadBackCover, saveBackCover
    }
    return _recordBackCover;
})()



$(function () {
    const thisCover = parent.recordImgLoad.pValue;
    console.log(thisCover)
})