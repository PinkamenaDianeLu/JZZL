package com.module.Interface.Controllers;/**
 * @author Mrlu
 * @createTime 2021/5/24
 * @describe
 */

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @author MrLu
 * @createTime 2021/5/24 9:48
 * @describe
 */
public class WebSocketClientUtil extends WebSocketClient{
    public WebSocketClientUtil(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public WebSocketClientUtil(URI serverURI) {
        super(serverURI);
    }

    public WebSocketClientUtil(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("连接成功！");
    }

     /**
     * 接收到消息
     * @author MrLu
     * @param 
     * @createTime  2021/5/26 14:45
     * @return    |  
      */
    @Override
    public void onMessage(String s) {
        System.out.println("received: " + s);
    }

     /**
     * 断开连接
     * @author MrLu
     * @param 
     * @createTime  2021/5/26 15:06
     * @return    |  
      */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        WebSocketClientUtil c = new WebSocketClientUtil(new URI("ws://localhost:9003")); // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
        c.connectBlocking();

//        c.getReadyState();
        c.send("客户端第二次发送");
        c.send("1");
//        c.close(1,"我关了啊");

    }

}
