package com.serenitydojo.fruitmarket;

import com.serenitydojo.*;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static com.serenitydojo.Fruit.*;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CatalogTest {

    @Test
    public void get_price_per_kilo() throws FruitUnavailableException {

        Catalog catalog = Catalog.withItems(
                new CatalogItem(Pear,1, 4.50),
                new CatalogItem(Apple, 1, 4.00),
                new CatalogItem(Orange, 1,5.50),
                new CatalogItem(Banana, 1,6.00)
        );

        assertThat(catalog.getPriceOf(Pear)).isEqualTo(4.50);
        assertThat(catalog.getPriceOf(Apple)).isEqualTo(4.00);
        assertThat(catalog.getPriceOf(Orange)).isEqualTo(5.50);
        assertThat(catalog.getPriceOf(Banana)).isEqualTo(6.00);
    }

    @Test
    public void update_price_per_kilo() throws FruitUnavailableException {

        Catalog catalog = Catalog.withItems(
                new CatalogItem(Pear,1, 4.50)
        );

        assertThat(catalog.getPriceOf(Pear)).isEqualTo(4.50);
        catalog.setPriceOf(Pear, 4.25);
        assertThat(catalog.getPriceOf(Pear)).isEqualTo(4.25);
    }

    @Test
    public void get_quantity_by_kilo() throws FruitUnavailableException {

        Catalog catalog = Catalog.withItems(
                new CatalogItem(Pear,1000, 4.50),
                new CatalogItem(Apple, 1234, 4.00),
                new CatalogItem(Orange, 9999,5.50),
                new CatalogItem(Banana, 100,6.00)
        );

        assertThat(catalog.getQuantityOf(Pear)).isEqualTo(1000);
        assertThat(catalog.getQuantityOf(Apple)).isEqualTo(1234);
        assertThat(catalog.getQuantityOf(Orange)).isEqualTo(9999);
        assertThat(catalog.getQuantityOf(Banana)).isEqualTo(100);
    }

    @Test
    public void update_quantity_by_kilo() throws FruitUnavailableException {

        Catalog catalog = Catalog.withItems(
                new CatalogItem(Pear,1, 4.50)
        );

        assertThat(catalog.getQuantityOf(Pear)).isEqualTo(1);
        catalog.setQuantityOf(Pear, 23);
        assertThat(catalog.getQuantityOf(Pear)).isEqualTo(23);
    }

    @Test
    public void list_fruit_alphabetically() {

        Catalog catalog = Catalog.withItems(
                new CatalogItem(Pear,1, 4.50),
                new CatalogItem(Apple, 1, 4.00),
                new CatalogItem(Orange, 1,5.50),
                new CatalogItem(Banana, 1,6.00)
        );

        List<String> availableFruits = catalog.getAvailableFruits();
        assertThat(availableFruits.get(0)).isEqualTo(Apple.name());
        assertThat(availableFruits.get(1)).isEqualTo(Banana.name());
        assertThat(availableFruits.get(2)).isEqualTo(Orange.name());
        assertThat(availableFruits.get(3)).isEqualTo(Pear.name());
    }

    @Test
    public void get_quantity_when_fruit_not_in_stock() {

        Catalog catalog = Catalog.withItems(
                new CatalogItem(Pear, 0, 1.00)
        );

        assertThatExceptionOfType(FruitUnavailableException.class).isThrownBy(
                () -> { catalog.getQuantityOf(Pear); }
        );
    }

    @Test
    public void get_quantity_when_fruit_not_in_catalog() {

        Catalog catalog = Catalog.withItems(
                new CatalogItem(Pear, 0, 1.00)
        );

        assertThatExceptionOfType(FruitUnavailableException.class).isThrownBy(
                () -> { catalog.getQuantityOf(Melon); }
        );
    }

    @Test
    public void add_regular_items_to_cart() throws FruitUnavailableException {

        Catalog catalog = Catalog.withItems(
                new CatalogItem(Pear,15, 4.00),
                new CatalogItem(Apple, 15, 4.00),
                new CatalogItem(Orange, 15,5.50),
                new CatalogItem(Banana, 15,6.00)
        );

        Cart cart = new Cart();
        cart.add(Pear, 3);
        cart.add(Apple, 3);
        cart.add(Banana, 2);

        Checkout checkout = new Checkout(catalog, cart);
        assertThat(checkout.getTotalCost()).isEqualTo(36.00);
    }

    @Test
    public void add_more_of_item_to_cart() throws FruitUnavailableException {

        Catalog catalog = Catalog.withItems(
                new CatalogItem(Pear,15, 4.00),
                new CatalogItem(Apple, 15, 4.00),
                new CatalogItem(Orange, 15,5.50),
                new CatalogItem(Banana, 15,6.00)
        );

        Cart cart = new Cart();
        cart.add(Pear, 1);
        cart.add(Pear, 1);
        cart.add(Pear, 2);

        Checkout checkout = new Checkout(catalog, cart);
        assertThat(checkout.getTotalCost()).isEqualTo(16.00);
    }

    @Test
    public void remove_items_from_cart() throws FruitUnavailableException {

        Catalog catalog = Catalog.withItems(
                new CatalogItem(Pear,15, 4.00),
                new CatalogItem(Apple, 15, 4.00),
                new CatalogItem(Orange, 15,5.50),
                new CatalogItem(Banana, 15,6.00)
        );

        Cart cart = new Cart();
        cart.add(Pear, 3);
        cart.add(Apple, 3);
        cart.add(Banana, 3);

        cart.remove(Banana, 1);
        cart.remove(Pear, 1);

        Checkout checkout = new Checkout(catalog, cart);
        assertThat(checkout.getTotalCost()).isEqualTo(32.00);
    }

    @Test
    public void cannot_remove_more_items_than_in_cart() throws FruitUnavailableException {

        Catalog catalog = Catalog.withItems(
                new CatalogItem(Pear,15, 4.00),
                new CatalogItem(Apple, 15, 4.00),
                new CatalogItem(Orange, 15,5.50),
                new CatalogItem(Banana, 15,6.00)
        );

        Cart cart = new Cart();

        cart.add(Pear, 3);
        assertThat(cart.getQuantity(Pear)).isEqualTo(3);

        cart.remove(Pear, 5);
        assertThat(cart.getQuantity(Pear)).isEqualTo(0);

        Checkout checkout = new Checkout(catalog, cart);
        assertThat(checkout.getTotalCost()).isEqualTo(0.00);
    }

    @Test
    public void include_discount_when_over_item_quantity() throws FruitUnavailableException {

        Catalog catalog = Catalog.withItems(
                new CatalogItem(Pear,15, 4.00),
                new CatalogItem(Apple, 15, 4.00),
                new CatalogItem(Orange, 15,5.50),
                new CatalogItem(Banana, 15,6.00)
        );

        Cart cart = new Cart();
        cart.add(Pear, 8);

        Checkout checkout = new Checkout(catalog, cart);
        assertThat(checkout.getTotalCost()).isEqualTo(28.80);
    }

    @Test
    public void exclude_discount_when_under_item_quantity() throws FruitUnavailableException {

        Catalog catalog = Catalog.withItems(
                new CatalogItem(Pear,15, 4.00),
                new CatalogItem(Apple, 15, 4.00),
                new CatalogItem(Orange, 15,5.50),
                new CatalogItem(Banana, 15,6.00)
        );

        Cart cart = new Cart();
        cart.add(Pear, 4);

        Checkout checkout = new Checkout(catalog, cart);
        assertThat(checkout.getTotalCost()).isEqualTo(16.00);
    }
}