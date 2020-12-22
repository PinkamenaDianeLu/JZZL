package com.enums;

import com.util.EnumsUtil;

/**
 * @author MrLu
 * @createTime 2020/8/19 10:42
 * @describe 枚举定义类
 */
public class Enums {
    /**
     * 删除标记
     *
     * @author MrLu
     * @createTime 2020/8/19 10:54
     */
    public enum scbj {
        DELETED(1, "已删除"),//已删除
        ALIVE(0, "未删除");

        scbj(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private int value;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * 人员类型
     *
     * @author MrLu
     * @createTime 2020/8/19 10:56
     */
    public enum PersonType {
        PRIMARY(0, "主办人"),//主办人
        SECONDARY(1, "辅办人");//副办人

        PersonType(int value, String name) {
            this.value = value;
            this.name = name;
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
        public String toString() {
            return name;
        }

    }

    /**
     * 是否已发送
     *
     * @author MrLu
     * @createTime 2020/9/25 15:36
     */
    public enum IsSend {
        YES(1, "已发送"),
        NO(0, "未发送"),
        CANNOT(-1, "-");

        IsSend(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private int value;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * 是否已完结
     *
     * @author MrLu
     * @createTime 2020/9/25 15:36
     */
    public enum IsFinal {
        YES(1, "已完结"),
        NO(0, "未完结");

        IsFinal(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private int value;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return name;
        }
    }


    /**
     * 案件状态
     *
     * @author MrLu
     * @createTime 2020/9/27 14:55
     */
    public enum CaseState {
        NORMAL(0, "未完结"),
        MERGE(1, "已合案"),
        SPLIT(2, "已拆案件");

        CaseState(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private int value;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return name;
        }
    }


    /**
     * 案件类型
     *
     * @author MrLu
     * @createTime 2020/9/28 9:24
     */
    public enum CaseType {
        OPEN(0, "未定"),
        CRIMINAL(1, "刑事"),
        ADMINISTRATIVE(2, "行政");

        CaseType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private int value;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return name;
        }

    }

    /**
     * 是否安综系统
     *
     * @author MrLu
     * @createTime 2020/9/28 9:24
     */
    public enum IsAzxt {
        YES(0, "安综抽取"),
        NO(1, "系统新建");

        IsAzxt(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private int value;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return name;
        }
    }

     /**
     * websocket消息类型
     * @author MrLu
     * @createTime  2020/12/18 10:29
      */
    public enum messagetype{
         typeNegativeOne(-1, "确认收到"),
         typeZero(0, "智能整理"),
         typeOne(1, "文件打包"),
         typeTwo(2, "打包发送"),
         typeThree(3, "其它");
         messagetype(int value, String name) {
             this.value = value;
             this.name = name;
         }

         private int value;
         private String name;

         public String getName() {
             return name;
         }

         public void setName(String name) {
             this.name = name;
         }

         public int getValue() {
             return value;
         }

         public void setValue(int value) {
             this.value = value;
         }

         @Override
         public String toString() {
             return name;
         }
    }

    public static void main(String[] args) {
        System.out.println(EnumsUtil.getEnumByValue(PersonType.class, "1").getName());
//        System.out.print(Objects.requireNonNull(PersonType.getValueByCode("0")));
    }

}
