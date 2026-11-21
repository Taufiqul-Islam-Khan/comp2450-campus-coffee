package coffee.model;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/** Everything we save and load: the menu and the regular customers (the program state). */
public class CoffeeShop {
    private final Menu menu;
    private final List<Customer> customers;

    public CoffeeShop(Menu menu, List<Customer> customers) {
        this.menu = checkNotNull(menu, "menu must not be null");
        this.customers = List.copyOf(checkNotNull(customers, "customers must not be null"));
    }

    public Menu menu() { return menu; }
    public List<Customer> customers() { return customers; }

    /** Returns the customer with this ID, or null if there is none. */
    public Customer findCustomer(String id) {
        for (Customer customer : customers) {
            if (customer.id().equals(id)) {
                return customer;
            }
        }
        return null;
    }
}
