/**
 * @author Mrlu
 * @createTime 2021/12/30
 * @url webApp.model.jzzl.js
 * @describe 封皮、目录、卷尾 页转图片
 *
 * 需要：html2canvas.min.js、jQuery、utils.js
 *
 */



function saveImg(divID) {

    let cover=document.getElementById(divID);
/*    var width=cover.offsetWidth;
    var height=cover.offsetHeight;
    var canvas=document.createElement('canvas');
    canvas.width=width*2;
    canvas.height=height*2;
    canvas.getContext("2d").scale(2,2);*/
    html2canvas(cover,{
        // scale:2,
        // canvas:canvas,
        // width:width,height:height,useCORS: true,logging: true
    }).then(function (canvas) {
        $('#'+divID).after(canvas)
        console.log(canvas)
    })

}
