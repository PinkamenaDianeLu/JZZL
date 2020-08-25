package com.aop;

/**
 * @author MrLu
 * @createTime 2020/8/24 15:11
 * @describe
 */
public class  test {
    public String testq(String aa){
        System.out.println(aa);
        return aa;
    }

    public static void main(String[] args) {
        test a=new test();
        a.testq("112");}
}
