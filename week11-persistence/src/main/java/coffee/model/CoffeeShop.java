package coffee.model;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/** Everything we save and load: the menu and the order history (the program state). */
public class CoffeeShop {
    private final Menu menu;
    private final List<Order> orders;

    public CoffeeShop(Menu menu, List<Order> orders) {
        this.menu = checkNotNull(menu, "menu must not be null");
        this.orders = new ArrayList<>(checkNotNull(orders, "orders must not be null"));      // our own copy
    }

    public Menu menu() { return menu; }

    /** A copy of the order history, so no other class can change our list. */
    public List<Order> orders() {
        return new ArrayList<>(orders);
    }

    /** Adds a paid order to the order history. */
    public void addOrder(Order order) {
        checkNotNull(order, "order must not be null");
        orders.add(order);
    }
}
