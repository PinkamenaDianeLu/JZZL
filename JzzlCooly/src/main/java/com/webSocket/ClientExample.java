package com.webSocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * @author Mrlu
 * @createTime 2020/8/22
 * @describe websocket 连接测试
 */
public class ClientExample extends WebSocketClient {
    public ClientExample(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public ClientExample(URI serverURI) {
        super(serverURI);
    }

    public ClientExample(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        send("测试发送websocket连接");
        System.out.println("发送连接文本");
    }

    @Override
    public void onMessage(String s) {
        System.out.println("received: " + s);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        ClientExample c = new ClientExample(new URI("ws://localhost:9003")); // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
        c.connectBlocking();

        c.getReadyState();
        c.send("客户端第二次发送");
        c.send("1");
//        c.close(1,"我关了啊");

    }
}
