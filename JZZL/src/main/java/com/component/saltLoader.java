package com.component;

import com.util.GlobalUtil;
import org.springframework.boot.CommandLineRunner;

import java.time.LocalDateTime;

/**
 * @author MrLu
 * @createTime 2020/9/27 9:48
 * @describe 加载盐 预计用于更新并加载到redis中
 */
public class saltLoader implements CommandLineRunner {
    //TODO MrLu 2020/9/27  预计将salt与时间等加密传入redis
    @Override
    public void run(String... args) throws Exception {
        LocalDateTime currentTime = LocalDateTime.now();
        String saltSalt = currentTime.getHour() + "," + currentTime.getSecond();
        String finSalt = GlobalUtil.getGlobal("salt") + saltSalt;
    }
}
