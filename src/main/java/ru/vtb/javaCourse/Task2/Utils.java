package ru.vtb.javaCourse.Task2;

import ru.vtb.javaCourse.Task2.annotations.Cashe;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class Utils {
    public static <T> T cashe(T obj){
        ClassLoader classLoader = obj.getClass().getClassLoader();
        Class[] interfaces = obj.getClass().getInterfaces();
        T a = (T) Proxy.newProxyInstance(classLoader, interfaces, new AInvocationHandler(obj));
        return a;
    }
}
