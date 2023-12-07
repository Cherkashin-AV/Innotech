package ru.vtb.javaCourse.Task2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.vtb.javaCourse.Task2.annotations.ReturnFromCashFlag;

public class TestProxy {
    //Создаем дочерний класс от тестируемого (A), чтобы через поле с аннотацией @ReturnFromCashFlag
    //отслеживать каким образом получен результат (через вызов метода или через кэш)
    private static class A4Test extends A implements Able{
        @ReturnFromCashFlag
        protected Boolean returnFromCash;

        public Boolean getReturnFromCash(){
            return returnFromCash;
        }
    }

    static private A4Test a4Test;
    static private Able able;

    @BeforeAll
    public static void init(){
        a4Test = new A4Test();
        able = Utils.cashe(a4Test);
    }

    @Test
    @DisplayName("Проверка кэширования методов")
    public void testProxy(){
        //Проверяем первоначальное состояние поля returnFromCash
        Assertions.assertNull(a4Test.getReturnFromCash());
        //Проверяем, что при первом вызове значение получается вызовом функции
        able.method();
        Assertions.assertEquals(false, a4Test.getReturnFromCash());
        //Проверяем, что значение из кэша
        able.method();
        Assertions.assertEquals(true, a4Test.getReturnFromCash());
        //Проверяем, сброс флага кеширования
        able.setValue();
        Assertions.assertNull(a4Test.getReturnFromCash());
        //Проверяем, что значение не из кэша
        able.method();
        Assertions.assertEquals(false, a4Test.getReturnFromCash());
        //Проверяем, что значение из кэша
        able.method();
        Assertions.assertEquals(true, a4Test.getReturnFromCash());
    }
}
