package com.bean.jzgl.Source;

/**
 * @author MrLu
 * @createTime 2020/12/23 10:46
 * @describe 前台搜索下拉的数据格式转换
 */
public class selectObj {

    private String name;
    private  String value;

    @Override
    public String toString() {
        return "selectObj{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public selectObj() {
    }

    public selectObj(String name, String value) {
        this.name = name;
        this.value = value;
    }

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
}
