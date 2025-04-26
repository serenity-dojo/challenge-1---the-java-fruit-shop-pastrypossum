package com.serenitydojo;

import java.util.HashMap;
import java.util.Map;

public class Checkout {

    private static final double bulkDiscount = 0.9;

    private Catalog catalog;
    private Cart cart;

    public Checkout(Catalog catalog, Cart cart) {
        this.catalog = catalog;
        this.cart = cart;
    }

    public double getTotalCost() {

        Map<Fruit, Integer> shopping = cart.getShopping();
        Map<Fruit, Double> priceList = catalog.getPriceList();

        double totalCost = 0.0;
        for(Fruit fruit: shopping.keySet()) {

            Double price = priceList.get(fruit);
            int quantity = shopping.get(fruit);

            if(quantity >=5)
                totalCost = totalCost + ((quantity * price) * bulkDiscount);
            else
                totalCost = totalCost + (quantity * price);
        }
        return totalCost;
    }
}
