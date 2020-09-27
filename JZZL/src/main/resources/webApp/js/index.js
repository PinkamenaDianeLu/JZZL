/**
 * @author MrLu
 * @createTime 2020/8/18
 * @url webApp.js
 * @describe  访问登录
 */

$(function () {
    // //用户名
    // const oriName = utils.getUrlPar('username');
    // //密码
    // const oriPwd = utils.getUrlPar('pwd');
    //生成的key
    const key = utils.getUrlPar('key');
    location.href='../model/ajcx/ajcx.html';
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
               url: '/Salt/getSalt',
               success: (re) => {
                   const reV = JSON.parse(re);
                   if ('success' === reV.message) {
                       sessionStorage.salt = reV.value;
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