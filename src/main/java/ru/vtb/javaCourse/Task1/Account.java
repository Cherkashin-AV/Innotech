package ru.vtb.javaCourse.Task1;

import java.io.*;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Account implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    protected String name = null;
    private final Map<Currency, Integer> currency = new EnumMap<>(Currency.class);
    private transient final Stack<Runnable> undoStack = new Stack<>();

    public Account(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException("Имя владельца не может быть пустым");
        //Не добавляем откат для конструктора
        if (this.name != null) {
            String oldName = this.name;
            undoStack.push(() -> this.name = oldName);
        }
        this.name = name;
    }

    public Map<Currency, Integer> getCurrency() {
        return new HashMap<>(currency);
    }

    public void setCurrency(Currency currency, Integer value){
        if (value == null || value<0) throw new IllegalArgumentException("Количество валюты должно быть больше нуля");
        if (currency == null ) throw new IllegalArgumentException("Небходимо указать валюту");
        if (this.currency.containsKey(currency)){
            Integer oldValue = this.currency.get(currency);
            undoStack.push(()->this.currency.put(currency, oldValue));
        }
        else{
            undoStack.push(()->this.currency.remove(currency));
        }
        this.currency.put(currency, value);
    }

    public Account undo(){
        undoStack.pop().run();
        return this;
    }

    public int getUndoCount(){
        return undoStack.size();
    }

    public byte[] getSerializedObject() {
        byte[] result = null;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try(ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)){
            objectStream.writeObject(this);
            result = byteStream.toByteArray();
        }
        catch (IOException ex){
            System.out.println("Ошибка сериализации");
        }
        return result;
    }

    public static Account restoreSerializedObject(byte[] serializedObject) {
        Account acc = null;
        if (serializedObject == null)
            return null;
        ByteArrayInputStream byteStream = new ByteArrayInputStream(serializedObject);
        try(ObjectInputStream objectStream = new ObjectInputStream(byteStream)){
            acc = (Account) objectStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка десериализации");
        }
        return acc;

    }

}

