package com.enums;

/**
 * @author MrLu
 * @createTime 2020/10/7 11:00
 * @describe 项目中的枚举
 */
public class EnumSoft {

    /**
     * 送检类型
     *
     * @author MrLu
     * @createTime 2020/10/7 15:48
     */
    public enum sjlx {
        AS062(11, "换押卷"),
        AS104(12, "起诉意见书(认罪认罚)"),
        AS021(8, "提请批捕卷"),
        AS061(9, "移送起诉卷"),
        AS065(7, "补充侦查工作卷");

        sjlx(int value, String name) {
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

    public enum fplx {
        COVER("ZL001", "封皮", -9999),
        XZCOVER("ZL004", "封皮", -9999),//这个是行政的封皮
        INDEX("ZL003", "文书目录", -9900),
        BACKCOVER("ZL002", "封底", 9999);

        fplx(String value, String name, int order) {
            this.value = value;
            this.name = name;
            this.order = order;
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
        public String toString() {
            return name;
        }
    }

    /**
     * 卷类型
     *
     * @author MrLu
     * @createTime 2021/1/5 16:50
     * @return |
     */
    public enum archivetype {
        CSJ(-1, "原始卷"),
        JCJ(0, "基础卷"),
        BCJCJ(7, "补充侦查工作卷"),
        TQPBJ(8, "提请批捕卷"),
        YSQSJ(9, "移送起诉卷"),
        ZAGLCFJ(1, "治安管理处罚卷"),
        HYJ(11, "换押卷"),
        OTHERS(10, "可能会有别卷");

        archivetype(int value, String name) {
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
     * 文书类型
     *
     * @author MrLu
     * @createTime 2021/1/5 17:30
     * @return |
     */
    public enum recordtype {
        CSJ(-1, "原始卷"),
        SSWSJ(1, "诉讼文书卷"),
        ZJCLJ(2, "证据材料卷"),
        BCZCJ(3, "补充侦查卷"),
        ZAGLCFJ(4, "治安管理处罚卷");

        recordtype(int value, String name) {
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
     * 文书类型
     *
     * @author MrLu
     * @createTime 2021/6/9 10:23
     */
    public enum recordstyle {
        DRWS(1, "对人文书"),
        DEWS(2, "对案文书"),
        XTWS(0, "无针对性的系统文书");

        recordstyle(int value, String name) {
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
     * 角色权限
     *
     * @author MrLu
     * @createTime 2021/6/9 10:26
     */
    public enum sysRole {
        O5("O5", "管理员"),
        //案宗的
        A1("A1", "部门领导"),
        B1("B1", "法制员"),
        D1("D1", "办案人"),
        //地市版新增角色
        C1("C1", "副队长"),
        F1("F1", "案管员");


        sysRole(String value, String name) {
            this.value = value;
            this.name = name;
        }

        private String value;
        private String name;

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

        @Override
        public String toString() {
            return name;
        }
    }

}
