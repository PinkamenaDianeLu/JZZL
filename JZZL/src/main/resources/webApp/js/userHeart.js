/**
 * @author Mrlu
 * @createTime 2020/10/8
 * @dependence 依赖js
 * @describe  页面头部部分
 *
 */

//TODO MrLu 2020/10/8   显示用户信息 websocket接受信息  退出登录

$(function () {


    $.post({
        url: '/Login/getUserLoginVersion',
        data: {},
        success: (re) => {
            const reV = JSON.parse(re);
            if ('success' === reV.message) {
                if (! reV.value){
                    layer.alert('无法获取登录信息！请使用正常的方式登录！');
                    window.close();
                }
                if ('YTJ' === reV.value) {
                    //当前为一体机版本
                    $('#pageHeader').parent().hide();
                }
            } else {
                //其他方式再说
            }
        }
    });


    $.post({
        url: '/Login/getPrevLoginHistory',
        data: {},
        success: (re) => {
            const reV = JSON.parse(re);
            if ('success' === reV.message) {
                const log = reV.value;
                $('#heartUsername').html(log.xm);
                $('#heartPrevIp').html(log.ip||'无记录');
                $('#heartPrevTime').html(utils.timeFormat.timestampToDate(log.createtime)||'无记录');
            } else {
            }
        }
    });
    $('#closeWindow').click(function () {
        window.location.href = 'about:blank';
        window.close();
    })

    // if (){
    //
    // }
})