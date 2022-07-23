/**
 * @author MrLu
 * @createTime 2020/8/18
 * @url webApp.js
 * @describe  访问登录
 * test : 127.0.0.1:8080?username=123&pwd=123&key=123
 * --http://127.0.0.1:8080/?username=YWRtaW4=&password=MjAyY2I5NjJhYzU5MDc1Yjk2NGIwNzE1MmQyMzRiNzA=&key=123
 *
 * http:///127.0.0.1:8080/?username=MjMxMDg0MTk4NzAxMjEzNzFY&password=c3RIdzkyNg==&key=U1NIQl9BWlhULDIzMTA4NDIwMjEwODA2MDUwMDAwMDAwNjkseyJjb2RlIjoiY3JlYXRlWmlwIiwiYWpiaCI6IkEyMzEwODQxNzAwMDAyMDIxMDgwMDA3IiwianFiaCI6IjIzMTA4NDIwMjEwODA2MDUwMDAwMDAwNjkiLCJsY2RtIjoiMDEwNEEiLCJmc2R3ZG0iOiIyMzEwODQwMDAwMDAiLCJqc2R3ZG0iOiIyMzEwODQiLCJ5d2RtIjoiMDEwNCIsInh5cmJoIjoiUjIzMTA4NDE3MDAwMDIwMjEwODAwMDciLCJ3amJtIjoiWFNfUVNZSlMiLCJ3amJpZCI6IjQ3OTM4OCIsIndqdXVpZCI6IjNlNTQ0YzYzNmQyNjgxNGYiLCJqcyI6ImZ6In0=

 */

function farmeworkLoad(url, callback) {
    //按照顺序加载所需的框架文件  不会阻碍dom的构成，当第二个jsp文件完成下载时应用所需的dom结构已经构建完毕，并做好了准备

    //这种写法因为是想dom中添加标签所以会牺牲些许性能，但是相比之前js标签的加载顺序和确保加载更为重要
    let script = document.createElement("script");
    script.type = "text/javascript";

    if (!script.readyState) {
        script.onload = function () {
            callback();
        }

    } else {
        //else 部分是留给ie浏览器的  虽然之前的判断此项目不会使用ie浏览器  但是还是带着把
        script.onreadystatechange = function () {
            if ("loaded" === script.readyState || "complete" === script.readyState) {
                script.onreadystatechange = null;
                callback();
            }
        };
    }
    script.src = url;

    document.getElementsByTagName("head")[0].appendChild(script);
}


(function () {

    farmeworkLoad("/utils/utils.js", function () {
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
                    sessionStorage.groupcode = reV.groupcode;//当前地市

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
                                    var urlP = window.btoa(reV.value + sessionStorage.salt);

                                    sessionStorage.username = oriName;
                                    //c保存案综传递来的角色
                                    if (sessionStorage.version === 'province' || sessionStorage.version === 'provinceTest') {
                                        sessionStorage.js = reV.azParam.js;
                                    }
                                    location.href = '../model/ajcx/sjjl.html?id=' + urlP;
                                    break;
                                case 'urlLogin_reorganizeCase':
                                    //外部跳转
                                    var urlP = window.btoa(reV.value + sessionStorage.salt);

                                    sessionStorage.username = oriName;
                                    sessionStorage.js = 'reorganizeCase';
                                    location.href = '../model/ajcx/sjjl.html?id=' + urlP;
                                    break;
                                case 'urlLogin_success_ALLCASE':
                                    sessionStorage.username = oriName;
                                    location.href = '../model/ajcx/ajcx.html';
                                    break;
                                case 'deny':
                                    layer.alert('用户名或密码错误！')
                                    break;
                                case 'NoCase':
                                    layer.alert('案件不存在！（系统已为您开始提前抽取，请稍等.....）')
                                    reorganizeArchive(reV.information, oriName, reV.azParam.js);

                                    break;
                                case 'urlLogin_deny':
                                    //information

                                    layer.alert(reV.information);
                                    break;
                                default:
                                    layer.alert('缺少登录所需要的必要值！');
                            }


                            //urlLogin_success
                            /*     if ('success' === reV.message) {
                                     //登录成功
                                     location.href = '../model/ajcx/ajcx.html';
                                     sessionStorage.username = oriName;
                                 }else if ('deny' === reV.message){
                                     layer.alert('用户名或密码错误！')
                                 }else {
                                     layer.alert('用户名密码key呢？')
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


        /**
         * 抽取不存在的案件
         * @author MrLu
         * @param jqbh
         * @param oriName
         * @param azParamJs
         * @createTime  2021/12/29 17:06
         * @return    |
         */
        function reorganizeArchive(jqbh, oriName, azParamJs) {
            $.post({
                url: '/tempRemedial/reorganizeArchive',
                data: {jqbh},
                success: (re) => {
                    const reV = JSON.parse(re);
                    if ('success' === reV.message) {
                        if (reV.value !== 0) {
                            //外部跳转
                            let urlP = window.btoa(reV.value + sessionStorage.salt);

                            sessionStorage.username = oriName;
                            //c保存案综传递来的角色
                            if (sessionStorage.version === 'province' || sessionStorage.version === 'provinceTest') {
                                sessionStorage.js = azParamJs;
                            }
                            location.href = '../model/ajcx/sjjl.html?id=' + urlP;
                        } else {
                            layer.alert("案件已抽取，请尝试重新跳转。若重新跳转未果请确认您与案件的关系");
                        }

                    } else {
                        layer.alert("案件抽取失败，请联系客服人员");
                    }
                }
            });
        }

    })

})()