/**
 * @author MrLu
 * @createTime 2020/8/18
 * @url webApp.js
 * @describe  访问登录
 */

$(function () {
    //用户名
    const oriName = utils.getUrlPar('username');
    //密码
    const oriPwd = utils.getUrlPar('pwd');
    //生成的key
    const key = utils.getUrlPar('key');
    location.href='/model/index.html';
    //验证登录
   /* $.post({
        url: '/Login/login',
        data: {loginMessage:loadMessageFactory()},
        success: (re) => {
            const reV = JSON.parse(re);
            if ('success' === reV.message) {
                //登录成功
                location.href='/model/index.html';
            } else {
                //
            }
        }
    });*/

       $.post({
               url: '/CaseSearch/test',
               data: {},
               success: (re) => {
                   const reV = JSON.parse(re);
                   if ('success' === reV.message) {
                   } else {
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
     function loadMessageFactory()  {
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