import com.action.webSocket.JzzlWebSocketServer;
import com.service.test.test;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.extensions.permessage_deflate.PerMessageDeflateExtension;
import org.junit.Test;

import java.net.UnknownHostException;

/**
 * @author Mrlu
 * @createTime 2020/8/22
 * @describe websocket测试
 */
public class WebSocketTest {
    @Test
    public void testServer() throws UnknownHostException {
        int port, limit;
        try {
            port = 9003;
        } catch ( Exception e ) {
            System.out.println( "No port specified. Defaulting to 9003" );
            port = 9003;
        }
        try {
            limit = Integer.MAX_VALUE;
        } catch ( Exception e ) {
            System.out.println( "No limit specified. Defaulting to MaxInteger" );
            limit = Integer.MAX_VALUE;
        }
        try {
            JzzlWebSocketServer test = new JzzlWebSocketServer( port, limit, new Draft_6455( new PerMessageDeflateExtension()) );
            test.setConnectionLostTimeout( 0 );
            test.start();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @Test
    public void test2(){
        test a=new test();
        a.testq("112");
    }


}
