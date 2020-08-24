package com.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author MrLu
 * @createTime 2020/8/24 13:47
 * @describe 面向切面编程
 */
public class AopConfig  implements InvocationHandler {


    private Object targetObject;

    //绑定委托对象，并返回代理类
    public Object bind(Object targetObject){
        this.targetObject = targetObject;
        return Proxy.newProxyInstance(targetObject.getClass().getClassLoader(),targetObject.getClass().getInterfaces(),this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result = method.invoke(targetObject,args);
        after();
        return result;
    }
     /**
     * 方法前
     * @author MrLu
     * @param 
     * @createTime  2020/8/24 13:53
     * @return    |  
      */
    private void before(){
        System.out.println("we can do something before calculate.");
    }

     /**
     * 方法后
     * @author MrLu
     * @param 
     * @createTime  2020/8/24 13:53
     * @return    |  
      */
    private void after(){
    }
}
