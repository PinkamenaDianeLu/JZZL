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
        url: '/Login/getPrevLoginHistory',
        data: {},
        success: (re) => {
            const reV = JSON.parse(re);
            if ('success' === reV.message) {
                console.log(reV)
                const log=reV.value;
                $('#heartUsername').html(log.sysusername);
                $('#heartPrevIp').html(log.ip);
                $('#heartPrevTime').html(utils.timeFormat.timestampToDate(log.createtime));



            } else {
            }
        }
    });
})