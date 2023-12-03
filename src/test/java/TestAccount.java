import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.vtb.javaCourse.Task1.Account;
import ru.vtb.javaCourse.Task1.AccountMemento;
import ru.vtb.javaCourse.Task1.Currency;

import java.util.EmptyStackException;

public class TestAccount {
    private Account account;
    @Before
    public void account() {
        account = new Account("Name1");
    }

    @Test()
    public void testAccountName(){
        Assert.assertThrows(IllegalArgumentException.class, ()-> new Account(null));
        Assert.assertThrows(IllegalArgumentException.class, ()-> new Account(""));
        Assert.assertEquals("Не корректное имя клиента", "Name1", account.getName());
        Assert.assertThrows(IllegalArgumentException.class, ()-> account.setName(null));
        Assert.assertThrows(IllegalArgumentException.class, ()-> account.setName(""));
    }

    @Test
    public void testAccountCurrency(){
        Account account = new Account("Name1");
        account.setCurrency(Currency.CHY,255);
        Integer val = account.getCurrency().get(Currency.CHY);
        //Проверяем, что через ссылку на MAP изменить список мы не сможем
        account.getCurrency().put(Currency.EUR, 127);
        Assert.assertEquals("Запись добавляется через ссылку на Map", 1, account.getCurrency().size());
        Assert.assertEquals("Значение валюты не совпадает с заданным значением", 255, val.intValue());
        Assert.assertThrows(IllegalArgumentException.class, ()-> account.setCurrency(Currency.EUR, -1));
    }

    @Test
    public void testUndoName(){
        Account account = new Account("Name1");
        Assert.assertEquals("В стеке отката сохраняется значение Name при вызове конструктора", 0, account.getUndoCount());
        account.setName("Name2");
        account.setName("Name3");
        account.setName("Name4");
        account.setName("Name5");
        Assert.assertEquals("Не корректное количество записей в стеке отката", 4, account.getUndoCount());
        account.undo().undo();
        Assert.assertEquals("Не корректное значение поля Name после отката", "Name3", account.getName());
        //Тестирование отката изменений к первоначальному состоянию
        while(account.getUndoCount()>0){
            account.undo();
        }
        Assert.assertThrows(EmptyStackException.class, ()->account.undo());
        Assert.assertEquals("Не корректное значение поля Name после отката", "Name1", account.getName());

    }

    @Test
    public void testUndoCurrency() {
        account.setCurrency(Currency.CHY, 1);
        account.setCurrency(Currency.EUR, 2);
        account.setCurrency(Currency.RUR, 3);
        account.setCurrency(Currency.CHY, 4);
        Assert.assertEquals("Не корректное количество записей в стеке отката", 4, account.getUndoCount());
        Assert.assertEquals("Не корректное количество записей в списке валют", 3, account.getCurrency().size());
        Assert.assertEquals("Не верное значение валюты CHY", 4, account.getCurrency().get(Currency.CHY).intValue());
        account.undo().undo();
        Assert.assertEquals("Не корректное количество записей в списке валют", 2, account.getCurrency().size());
        Assert.assertEquals("Не верное значение валюты CHY", 1, account.getCurrency().get(Currency.CHY).intValue());
    }

    @Test
    public void testSerialize(){
        account.setName("Name2");
        account.setCurrency(Currency.EUR,100);
        account.setCurrency(Currency.USD,50);
        byte[] saveObj = account.getSerializedObject();

        account.setName("Name3");
        account.setCurrency(Currency.RUR,1000);
        account.setCurrency(Currency.EUR,101);
        account.setCurrency(Currency.CHY,50);
        Account a = Account.restoreSerializedObject(saveObj);
        Assert.assertEquals("Не корректное имя владельца","Name2", a.getName());
        Assert.assertEquals("Не корректное количество валют",2, a.getCurrency().size());
        Assert.assertEquals("Не корректное количество для валюты EUR",100, a.getCurrency().get(Currency.EUR).intValue());
        a = Account.restoreSerializedObject(account.getSerializedObject());
        Assert.assertEquals("Не корректное имя владельца","Name3", a.getName());
        Assert.assertEquals("Не корректное количество валют",4, a.getCurrency().size());
        Assert.assertEquals("Не корректное количество для валюты EUR",101, a.getCurrency().get(Currency.EUR).intValue());
    }

    @Test
    public void testMemento(){
        account.setName("Name2");
        account.setCurrency(Currency.EUR,100);
        account.setCurrency(Currency.USD,50);
        AccountMemento memento1 = account.getMemento();
        Account accountClone1 = account.clone();

        account.setName("Name3");
        account.setCurrency(Currency.RUR,1000);
        account.setCurrency(Currency.EUR,101);
        account.setCurrency(Currency.CHY,50);

        AccountMemento memento2 = account.getMemento();
        Account accountClone2 = account.clone();

        Assert.assertNotEquals(accountClone1, account);
        account.setMemento(memento1);
        Assert.assertEquals(accountClone1, account);

        account.setMemento(memento2);
        Assert.assertEquals(accountClone2, account);
    }
}
