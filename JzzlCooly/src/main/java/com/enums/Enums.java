package com.enums;

import com.util.EnumsUtil;

/**
 * @author MrLu
 * @createTime 2020/8/19 10:42
 * @describe  枚举定义类
 */
public class Enums extends EnumsUtil {
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

         public void setValue(int value) {
             this.value = value;
         }

         public String getName() {
             return name;
         }

         public void setName(String name) {
             this.name = name;
         }

         // 重写 toString() 方法
         @Override
         public String toString(){
             return name;
         }

    }




    public static void main(String[] args) {
        System.out.println( EnumsUtil.getEnumByValue(PersonType.class,"1").getName());
//        System.out.print(Objects.requireNonNull(PersonType.getValueByCode("0")));
    }

}
