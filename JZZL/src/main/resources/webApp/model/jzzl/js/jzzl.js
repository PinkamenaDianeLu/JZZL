/**
 * @author Mrlu
 * @createTime 2020/10/8
 * @dependence 依赖js
 * @describe 卷宗整理
 *
 */


$(function () {
    $('#userHeart').load('/userHeart.html');
    //送检的id
    const sfc = utils.getUrlPar('id');
    alert(sfc);
})