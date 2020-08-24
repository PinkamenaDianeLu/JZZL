package com.action.webSocket;

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
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mrlu
 * @createTime 2020/8/22
 * @describe webSocket服务端
 */
public class JzzlWebSocketServer extends WebSocketServer {
    private static int openCounter = 0;//打开链接数
    private static int closeCounter = 0;//关闭连接数
    private int limit = Integer.MAX_VALUE; //限制连接数
    private static ConcurrentHashMap<String, JzzlWebSocketServer> webSocketSet = new ConcurrentHashMap<String, JzzlWebSocketServer>();
    /**
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

    /**
     * 由用户建立连接
     *
     * @param
     * @author MrLu
     * @createTime 2020/8/24 10:22
     */
    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println("/////Waring//////用户建立了连接///////Waring///////" + openCounter);
//        webSocketSet.put(clientHandshake.getResourceDescriptor(), this);//加入map中
        //TODO MrLu 2020/8/24 记录用户连接 缓存userId
        //TODO MrLu 2020/8/24  加载未推送的消息进行推送
        openCounter++;
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        //TODO MrLu 2020/8/24  记录用户关闭连接并将消息记录至数据库  
        closeCounter++;
        System.out.println("!!!!!!!!Waring!!!!!!!!!!用户关闭了连接!!!!!!!!Waring!!!!!!!");
        if (closeCounter >= limit) {
            System.exit(0);
        }
    }

     /**
     * 接受信息
     * @author MrLu
     * @param 
     * @createTime  2020/8/24 11:18
     * @return    |  
      */
    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("这里是服务端，我收到了" + message);
        if (!message.equals("1")) {
            conn.send(message + "这是服务端返回的！");
        } else {
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
        //TODO MrLu 2020/8/24 记录物理日志  
        System.out.println("Error:");
        e.printStackTrace();
    }

    /**
     * 发送信息给用户
     *
     * @param userid  用户id
     * @param message 要发送的消息
     * @author MrLu
     * @createTime 2020/8/24 10:24
     */
    public void sendToUser(String userid, String message) {
//TODO MrLu 2020/8/24 发送消息给指定用户  
    }

     /**
     * websocket服务打开时方法
     * @author MrLu
     * @createTime  2020/8/24 10:35
      */
    @Override
    public void onStart() {
        //TODO MrLu 2020/8/24  写物理日志  
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
