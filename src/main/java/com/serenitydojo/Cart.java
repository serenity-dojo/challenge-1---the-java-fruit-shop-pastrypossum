package com.serenitydojo;

import java.util.HashMap;
import java.util.Map;

public class Cart {

    private Map<Fruit, Integer> shopping = new HashMap<>();

    public void add(Fruit fruit, int quantity) {

        if (shopping.containsKey(fruit))
            shopping.put(fruit, shopping.get(fruit) + quantity);
        else
            shopping.put(fruit, quantity);
    }

    public void remove(Fruit fruit, int quantity) {

        if (shopping.get(fruit) < quantity)
            quantity = shopping.get(fruit);

        shopping.put(fruit, shopping.get(fruit) - quantity);
    }

    public int getQuantity(Fruit fruit) {

        return shopping.get(fruit);
    }

    public Map<Fruit, Integer> getShopping() {
        return shopping;
    }
}
