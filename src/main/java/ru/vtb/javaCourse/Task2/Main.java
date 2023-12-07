package ru.vtb.javaCourse.Task2;

public class Main {
    public static void main(String[] args) {
        Able a = Utils.cashe(new A());
        a.method();
        a.method();
        a.setValue();
        a.method();
    }
}
