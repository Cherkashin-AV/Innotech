package ru.vtb.javaCourse.Task1;

import java.util.HashMap;
import java.util.Map;

public class AccountMemento {
    private final String name;

    private final Map<Currency, Integer> currency;

    public String getName() {
        return name;
    }

    public Map<Currency, Integer> getCurrency() {
        return new HashMap<>(currency);
    }

    public AccountMemento(String name, Map<Currency, Integer> currency) {
        this.name = name;
        this.currency = currency;
    }
}
