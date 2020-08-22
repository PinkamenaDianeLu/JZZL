package com.webSocket;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.extensions.permessage_deflate.PerMessageDeflateExtension;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;

/**
 * @author Mrlu
 * @createTime 2020/8/22
 * @describe webSocket服务端
 */
public class JzzlWebSocketServer extends WebSocketServer {
    private static int openCounter = 0;//打开链接数
    private static int closeCounter = 0;//关闭连接数
    private int limit = Integer.MAX_VALUE; //限制连接数

    /**
     * 方法注释
     *
     * @param port  打开的端口号
     * @param limit 允许连接上限
     * @return |   |
     * @author Mrlu
     * @createTime 2020/8/22 12:51
     */
    public JzzlWebSocketServer(int port, int limit, Draft d) throws UnknownHostException {
        super(new InetSocketAddress(port), Collections.singletonList(d));
        this.limit = limit;
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        openCounter++;
        System.out.println("///////////Opened connection number" + openCounter);
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        closeCounter++;
        System.out.println("closed");
        if (closeCounter >= limit) {
            System.exit(0);
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("这里是服务端，我收到了"+message);
        if (!message.equals("1")){
            conn.send(message+"这是服务端返回的！");
        }else {
            try {
                Thread.sleep(10000);
                conn.send("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-  延迟返回  *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer blob) {
        conn.send(blob);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        System.out.println("Error:");
        e.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("服务开启！ Server started!");
    }

    public static void main(String[] args) throws UnknownHostException {
        int port, limit;
        try {
            port = new Integer(args[0]);
            System.out.println(port);
        } catch (Exception e) {
            System.out.println("No port specified. Defaulting to 9003");
            port = 9003;
        }
        try {
            limit = new Integer(args[1]);
            System.out.println(limit);
        } catch (Exception e) {
            System.out.println("No limit specified. Defaulting to MaxInteger");
            limit = Integer.MAX_VALUE;
        }
        JzzlWebSocketServer test = new JzzlWebSocketServer(port, limit, new Draft_6455(new PerMessageDeflateExtension()));
        test.setConnectionLostTimeout(0);
        test.start();
    }
}
