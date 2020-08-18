/**
 * @author MrLu
 * @createTime  2020/5/12 22:18
 * @describe 权限验证加载  原生js不需要任何依赖
 * @version 1.0
 */

(function(){
   function  getUrlPar(name) {
        let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        let r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }
    let modelCodes = document.querySelectorAll('[pCode]');
    let xhr = new XMLHttpRequest();
    xhr.open('GET','/Login/loadUserPermissions',  true);
    xhr.onreadystatechange = function() {
        // readyState == 4说明请求已完成
        if (xhr.readyState == 4 && xhr.status == 200 || xhr.status == 304) {
            // 从服务器获得数据

            const reV = JSON.parse( xhr.responseText);
            if ("success" === reV.message) {
                let permissions = reV.value;
                //最高权限
                if ('sdx' === permissions)  return true;
                //无法获取到用户的权限
                if ('No Permissions'===permissions&&!getUrlPar('user')){
                    sessionStorage.clear();
                    self.location.href = '/index.html?user=noLogin';
                }
                //循环模块列表
                for (let thisEl of modelCodes) {
                    //比对是否有权限
                    if ((permissions).indexOf(thisEl.getAttribute('pCode') + ',') < 0) {
                        //该用户不具备此权限
                        thisEl.remove();
                    }
                }
            } else {

                //权限获取失败了 只有自尽了
                sessionStorage.clear();
                xhr.open('POST','/Login/logOut',  true);
                xhr.onreadystatechange = function() {
                    if (xhr.readyState == 4 && xhr.status == 200 || xhr.status == 304) {
                        //清空sessionStorage
                        self.location.href = '/index.html';
                    }
                };
                xhr.send();

                self.location.href = '/index.html';
            }
        }
    };
    xhr.send();
//webApp/Framework/js/language/bootstrap-table-zh-CN.min.js
})();