/**
 * @author Mrlu
 * @createTime 2020/12/29
 * @url webApp.model.jzzl.js
 * @describe 卷整理锁
 */

/**
 * @author Mrlu
 * @createTime  2020/5/23 15:36
 * @describe webSocket
 * @version 1.0
 */


var recordWebSocket = (function () {
    let stompClient;
    let sfcid;
    let boolSocket = false;
    let userNow;


    /**
     * @author Mrlu
     * @createTime  2020/5/23 17:02
     * @describe 订阅
     * @version 1.0
     */
    function connect() {
        let socket = new SockJS('/chat');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            boolSocket = true;
            // 订阅指定页面
            console.log('/queues/' + userNow + '/' + sfcid);
            stompClient.subscribe('/queues/' + userNow + '/' + sfcid, function (chat) {
                console.log(chat)
                showGreeting(chat.body);
            })

        })
    }


    /**
     * @author Mrlu
     * @param  message
     * @createTime  2020/5/23 15:40
     * @describe 处理拿到的信息
     * @version 1.0
     */
    function showGreeting(message) {
        console.log(message);
        let thisMes = JSON.parse(message);
        if ('destroy' === thisMes.message) {
            alert("用户：" + thisMes.sender + "需要编辑该案卷，页面即将关闭！");
            window.close();
        }
    }

    function _webSocket(userP, sfcidP) {
        userNow = userP;
        sfcid = sfcidP;
        connect();
    }

    _webSocket.prototype = {
    }
    return _webSocket;

})()


