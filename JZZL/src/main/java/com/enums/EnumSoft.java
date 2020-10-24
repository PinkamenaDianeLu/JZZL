package com.enums;

/**
 * @author MrLu
 * @createTime 2020/10/7 11:00
 * @describe 项目中的枚举
 */
public class EnumSoft {

     /**
     * 送检类型
     * @author MrLu
     * @createTime  2020/10/7 15:48
      */
    public enum sjlx {
        AS021(8,"提请批捕卷"),
        AS061(9,"移送起诉卷"),
        AS065(7,"补充侦查工作卷");

        sjlx(int value,String name) {
            this.value=value;
            this.name=name;
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
        public String toString(){
            return name;
        }
    }

    public enum fplx{
        COVER("ZL001","封皮",-9999),
        INDEX("ZL003","文书目录",-9900),
        BACKCOVER("ZL002","封底",9999);

        fplx(String value,String name,int order) {
            this.value=value;
            this.name=name;
            this.order=order;
        }
        private String value;
        private String name;
        private int order;
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        @Override
        public String toString(){
            return name;
        }
    }
}
