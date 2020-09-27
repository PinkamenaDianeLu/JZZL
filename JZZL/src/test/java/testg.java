import org.junit.Test;

import java.time.LocalDateTime;

/**
 * @author MrLu
 * @createTime 2020/9/27 10:10
 * @describe
 */
public class testg {

    @Test
    public  void timeTest(){
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println("当前时间: " + currentTime.getHour()+currentTime.getSecond());
    }
}
