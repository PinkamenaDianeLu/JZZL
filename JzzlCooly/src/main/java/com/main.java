package com;

import com.action.webSocket.JzzlWebSocketServer;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.extensions.permessage_deflate.PerMessageDeflateExtension;

import java.net.UnknownHostException;

/**
 * @author MrLu
 * @createTime 2020/8/21 15:29
 * @describe 程序入口类
 */
public class main {
    public static void main(String[] args) throws UnknownHostException {
        int port, limit;
        try {
            port = new Integer( args[0] );
            System.out.println(port);
        } catch ( Exception e ) {
            System.out.println( "No port specified. Defaulting to 9003" );
            port = 9003;
        }
        try {
            limit = new Integer( args[1] );
            System.out.println(limit);
        } catch ( Exception e ) {
            System.out.println( "No limit specified. Defaulting to MaxInteger" );
            limit = Integer.MAX_VALUE;
        }
        JzzlWebSocketServer test = new JzzlWebSocketServer( port, limit, new Draft_6455( new PerMessageDeflateExtension()) );
        test.setConnectionLostTimeout( 0 );
        test.start();
    }
}
