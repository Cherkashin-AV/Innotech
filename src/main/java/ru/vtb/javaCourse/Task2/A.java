package ru.vtb.javaCourse.Task2;

import ru.vtb.javaCourse.Task2.annotations.Cashe;
import ru.vtb.javaCourse.Task2.annotations.ReturnFromCashFlag;
import ru.vtb.javaCourse.Task2.annotations.Setter;

public class A implements Able{
    @Override
    @Cashe
    public void method() {
        System.out.println("original method");
    }
    @Override
    @Setter
    public void setValue() {
        System.out.println("setter method");
    }
}
