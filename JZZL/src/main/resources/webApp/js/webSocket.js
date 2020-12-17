/**
 * @author Mrlu
 * @createTime  2020/5/23 15:36
 * @describe webSocket
 * @version 1.0
 */


var webSocket = (function () {
    let stompClient;
    let boolSocket=false;


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
            boolSocket=true;
            // /user  /用户  /订阅地址
            stompClient.subscribe('/queues/230105199999999999', function (chat) {
                alert('接收');
                showGreeting(chat.body);
            })

        })
    }

    /**
     * @author Mrlu
     * @createTime  2020/5/23 15:41
     * @describe 发送信息
     * @version 1.0
     */
    function sendMsg() {
        if (boolSocket){
            stompClient.send("/app/chat", {}, JSON.stringify({'message': '12345', 'to': '123', 'from': '230105199999999999'}))
        }else {
            console.error('未订阅成功')
        }
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
    }

    function _webSocket() {
        connect();
    }

    _webSocket.prototype = {
        sendMsg,
    }
    return _webSocket;

})()


$(function () {
    let mes = new webSocket();
    //
    $('#websocketMessage').click(function () {
        //event不推荐了  那应该用啥啊  你倒是说啊
        event.stopPropagation();
        mes.sendMsg();
    })
})