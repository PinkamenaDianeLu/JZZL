import com.bean.jzgl.Source.SysUser;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author MrLu
 * @createTime 2020/9/27 10:10
 * @describe
 */
public class testg {

    @Test public  void timeTest(){
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println("当前时间: " + currentTime.getHour()+currentTime.getSecond());
    }

    @Test public void ofNullable(){
        SysUser user = null;
        System.out.println(user.getId());
        user = Optional.ofNullable(user).orElse(new SysUser());
        System.out.println(user.getId());
        user = Optional.ofNullable(user).orElseGet(() -> new SysUser());
        System.out.println(user.getId());
    }
    @Test public  void caseStart(){
        String name="recordstyle_name";
        name=name.replace("_name", "");
        char[] cs=name.toCharArray();
        cs[0]-=32;
        System.out.println(String.valueOf(cs));
    }
}
