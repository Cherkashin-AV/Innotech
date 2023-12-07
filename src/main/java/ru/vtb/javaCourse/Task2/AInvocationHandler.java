package ru.vtb.javaCourse.Task2;

import ru.vtb.javaCourse.Task2.annotations.Cashe;
import ru.vtb.javaCourse.Task2.annotations.ReturnFromCashFlag;
import ru.vtb.javaCourse.Task2.annotations.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

public class AInvocationHandler<T> implements InvocationHandler {
    private T a;
    private Field fromCashField;
    private void setFromCashField(Boolean value) throws IllegalAccessException {
        if (fromCashField != null){
            fromCashField.set(a, value);
        }
    }

    public AInvocationHandler(T a)  {
        this.a = a;
        fromCashField = Arrays.stream(a.getClass().getDeclaredFields())
                .filter(f->f.isAnnotationPresent(ReturnFromCashFlag.class)
                        && f.getType() == Boolean.class)
                .findFirst()
                .orElse(null);
        if (fromCashField != null)
            fromCashField.setAccessible(true);
    }

    private static HashMap<Method, Object> cashMap = new HashMap();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Method m;
        m = a.getClass().getMethod(method.getName(), method.getParameterTypes());

        if (m.getAnnotation(Setter.class) != null){
            System.out.println("Чистим кэш:");
            cashMap.clear();
            setFromCashField(null);
        }
        if (m.getAnnotation(Cashe.class) != null){
            if (cashMap.containsKey(method)){
                System.out.println("Не вызываем функцию, используем кэшированное значение!");
                setFromCashField(true);
                return cashMap.get(method);
            }
            else{
                System.out.println("Вызываем функцию и сохраняем в кэш результат ее выполнения:");
                Object result = method.invoke(a, args);
                cashMap.put(method, result);
                setFromCashField(false);
                return result;
            }
        }
        setFromCashField(null);
        return method.invoke(a, args);
    }
}
