package com.enums;

import com.util.EnumsUtil;

import java.util.Objects;

/**
 * @author MrLu
 * @createTime 2020/8/19 10:42
 * @describe  枚举定义类
 */
public class Enums  {
     /**
     * 删除标记
     * @author MrLu
     * @createTime  2020/8/19 10:54
      */
    public enum scbj {
        DELETED(1),//已删除
        ALIVE(0);
         scbj(int i) {
        }

     }
     /**
     * 人员类型
     * @author MrLu
     * @createTime  2020/8/19 10:56
      */
    public  enum  PersonType  {
         PRIMARY(0,"主办人"),//主办人
         SECONDARY(1,"辅办人");//副办人
         PersonType(int value,String name){
             this.value=value;
             this.name=name;
         }
         // 定义一个 private 修饰的实例变量
         private int value;
         private String name;
         public int getValue() {
             return value;
         }
         public String getName() {
             return name;
         }
         // 重写 toString() 方法
         @Override
         public String toString(){
             return name;
         }

    }
     /**
     * webSocket 消息类型
     * @author Mrlu
     * @createTime  2020/8/22 18:51
      */
    public enum WebSocketMessageType {
         SEND(0,"发送消息"),//主办人
         CLIENT(1,"连接"),//副办人
         PULL(2,"服务端推送消息"),
         CLOSE(3,"主动关闭连接"),
         ServerStart(999,"开启连接");
         WebSocketMessageType(int value,String name){
             this.value=value;
             this.name=name;
         }

         private int value;
         private String name;
         public int getValue() {
             return value;
         }
         public String getName() {
             return name;
         }
         @Override
         public String toString(){
             return name;
         }
    }




    public static void main(String[] args) {
        System.out.println( Objects.requireNonNull(EnumsUtil.getEnumByValue(PersonType.class, "1")));
//        System.out.print(Objects.requireNonNull(PersonType.getValueByCode("0")));
    }

}
