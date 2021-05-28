/**
 * @author MrLu
 * @createTime 2020/8/18
 * @url webApp.js
 * @describe  访问登录
 * test : 127.0.0.1:8080?username=123&pwd=123&key=123
 * --http://35.2.31.58:8080/?username=YWRtaW4=&password=MjAyY2I5NjJhYzU5MDc1Yjk2NGIwNzE1MmQyMzRiNzA=&key=123
 */

$(function () {
    // //用户名
    const oriName = window.atob(utils.getUrlPar('username'));
    // //密码
    const oriPwd = window.atob(utils.getUrlPar('password'));
    //生成的key
    const key = utils.getUrlPar('key');
    $.post({
        url: '/Salt/getSalt',
        success: (re) => {
            const reV = JSON.parse(re);
            if ('success' === reV.message) {
                sessionStorage.salt = reV.value;//加密的盐
                sessionStorage.version = reV.version;//当前版本

                //验证登录
                $.post({
                    url: '/Login/login',
                    data: {loginMessage: loadMessageFactory()},
                    success: (re) => {
                        const reV = JSON.parse(re);
                        switch (reV.message) {
                            case 'success':
                                //正常登录成功
                                location.href = '../model/ajcx/ajcx.html';
                                sessionStorage.username = oriName;
                                break
                            case 'urlLogin_success':
                                //外部跳转
                                let urlP = window.btoa(reV.value + sessionStorage.salt);
                                location.href = '../model/ajcx/sjjl.html?id=' + urlP;
                                sessionStorage.username = oriName;
                                break;
                            case 'deny':
                                alert('用户名或密码错误！')
                                break;
                            case 'urlLogin_deny':
                                alert('传递的参数有误！')
                                break;
                            default:
                                alert('缺少登录所需要的必要值！');
                        }


                        //urlLogin_success
                        /*     if ('success' === reV.message) {
                                 //登录成功
                                 location.href = '../model/ajcx/ajcx.html';
                                 sessionStorage.username = oriName;
                             }else if ('deny' === reV.message){
                                 alert('用户名或密码错误！')
                             }else {
                                 alert('用户名密码key呢？')
                             }*/
                    }
                });
            } else {
                //TODO MrLu 2020/9/27 不允许登录
            }
        }
    });

    /**
     *  登录信息
     * @author MrLu
     * @param  oriName
     * @param  oriPwd
     * @param  key
     * @createTime  2020/8/18 16:25
     * @return  string  |
     */
    /**
     * @description  废弃 只传key即可
     * @log  2020/8/20 11:35  MrLu  废弃
     **/
    function loadMessageFactory() {
        const loginMessage = function (oriName, oriPwd, key) {
            this.oriName = oriName;
            this.oriPwd = oriPwd;
            this.key = key;
        }
        let loginMessageObj = new loginMessage();
        loginMessageObj.key = key;
        loginMessageObj.oriPwd = oriPwd;
        loginMessageObj.oriName = oriName;
        return JSON.stringify(loginMessageObj);
    }
})