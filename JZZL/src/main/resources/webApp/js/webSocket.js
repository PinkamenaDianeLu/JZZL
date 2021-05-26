/**
 * @author Mrlu
 * @createTime  2020/5/23 15:36
 * @describe webSocket
 * @version 1.0
 */


var webSocket = (function () {
    let stompClient;
    let boolSocket = false;
    let userNow;
    let messageIds = new Set();

    const webSocketMessage = function (message) {
        this.receiver = 'system';//发送给谁
        this.sender = userNow;//消息来源
        this.message = message;//信息
        this.messagetype = -1;//消息类型  确认收到
    }


    /**
     * 创建一条未读消息的li
     * @author MrLu
     * @param thisMes 消息
     * @createTime  2020/12/18 15:58
     * @return    |
     */
    function createUnreadMessageLi(thisMes) {

        let newMessage = utils.createElement.createElement({
            tag: 'li',
            attrs: {}, arg: '<b>发件人：' + thisMes.sender + '</b>' +
                '<font>' + utils.timeFormat.timestampToDate(thisMes.sendtime) + '</font>'
        });
        let readed = utils.createElement.createElement({
            tag: 'a',
            attrs: {
                'class': 'b_but edit'
            }, arg: '已读'
        });
        //确认已读按钮
        readed.addEventListener('click', function () {
            readMessage(thisMes.id);
            $(newMessage).remove();

        });

        let msg = document.createElement('p');
        msg.innerHTML = thisMes.message;
        $(newMessage).append(readed).append(msg);
        messageIds.add(thisMes.id);
        return newMessage;
    }

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
            selectUnreadMessage();
            // /user  /用户  /订阅地址
            stompClient.subscribe('/queues/' + userNow, function (chat) {
                showGreeting(chat.body);
            })

        })
    }


    /**
     * 全部已读
     * @author MrLu
     * @createTime  2020/12/18 16:50
     */
    function readAllMessage() {
        if (confirm('确认全部标记已读？')) {
            readMessage(Array.from(messageIds).join(','));
            $('#messageCount').hide();
        }
    }

    /**
     * 已读功能
     * @author MrLu
     * @param ids 消息表id
     * @createTime  2020/12/18 16:01
     */
    function readMessage(ids) {
        $.post({
            url: '/WebMessage/readMessage',
            data: {
                ids
            },
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {
                    console.log('已标记已读');
                    let messageCount=$('#messageCount').html();
                    if (messageCount&&+messageCount>=1){
                        messageCount--;
                    }
                    $('#messageCount').html(messageCount);
                } else {
                    console.error('标记已读失败');
                }
            }
        });

    }

    /**
     * @author Mrlu
     * @createTime  2020/5/23 15:41
     * @describe 发送信息
     * @version 1.0
     */
    function sendMsg() {
        if (boolSocket) {
            let wsm = new webSocketMessage("-用户已收到了消息-");
            stompClient.send("/app/chat", {}, JSON.stringify(wsm))
        } else {
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
        let messageObj=JSON.parse(message);
        if ('destroy'===messageObj.message){
            //提示关闭页面功能 不需要记录为消息
            return  ;
        }
        $('#webSocketMessageUl').prepend(createUnreadMessageLi(messageObj));
        let messageCount=$('#messageCount').html();
        if (!messageCount||messageCount<1){
            messageCount=0
        }
        $('#messageCount').show().html(+messageCount+1);

    }

    /**
     * 加载当前用户的未读消息
     * @author MrLu
     * @createTime  2020/12/18 15:05
     * @return    |
     */
    function selectUnreadMessage() {
        $.post({
            url: '/WebMessage/selectUnreadMessage',
            data: {},
            success: (re) => {
                const reV = JSON.parse(re);
                if ('success' === reV.message) {

                    if (reV.value.length>0){
                        let fd = document.createDocumentFragment();
                        for (let thisMes of reV.value) {
                            if ('destroy'===thisMes.message){
                                //提示关闭页面功能 不需要记录为消息
                                continue;
                            }
                            $(fd).append(createUnreadMessageLi(thisMes));
                        }
                        $('#webSocketMessageUl').append(fd);
                        $('#messageCount').html(reV.value.length)
                    } else {
                        $('#messageCount').hide();
                    }


                } else {
                    console.error('未读消息加载失败');
                }
            }
        });
    }

    function _webSocket(userP) {
        userNow = userP;
        connect();
    }

    _webSocket.prototype = {
        sendMsg, readAllMessage
    }
    return _webSocket;

})()


$(function () {
    let mes = new webSocket(sessionStorage.username);
    //全部已读按钮
    $('#haveRead').click(function () {
        //event不推荐了  那应该用啥啊  你倒是说啊
        event.stopPropagation();
        mes.readAllMessage();
    })
})