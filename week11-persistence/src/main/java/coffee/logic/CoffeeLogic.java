package coffee.logic;

import coffee.exceptions.InvalidCupCountException;
import coffee.exceptions.NotEnoughMoneyException;
import coffee.model.CoffeeShop;
import coffee.model.Customer;
import coffee.model.Drink;
import coffee.model.Order;
import coffee.persistence.CoffeePersistence;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/** Decides and calculates. Never reads input, never prints, never touches a file itself. */
public class CoffeeLogic implements CoffeeService {
    private final CoffeePersistence storage;   // the interface: what it can do, not how
    private final CoffeeShop shop;

    /** Dependency injection: Main creates the storage and passes it in. */
    public CoffeeLogic(CoffeePersistence storage) throws IOException {
        this.storage = checkNotNull(storage, "storage must not be null");
        this.shop = storage.load();             // load the saved state when the program starts
    }

    @Override
    public List<Drink> drinks() {
        return shop.menu().drinks();
    }

    @Override
    public Drink findDrink(String name) {
        return shop.menu().findDrink(name);     // null if we don't sell it
    }

    @Override
    public Customer findCustomer(String id) {
        return shop.findCustomer(id);           // null if there is no such customer
    }

    @Override
    public Order placeOrder(Drink drink, int cups, String nameOnCup)
            throws InvalidCupCountException {
        return new Order.Builder()              // the Builder checks each piece
                .drink(drink)
                .cups(cups)
                .nameOnCup(nameOnCup)
                .build();
    }

    /** Gives back the change. */
    @Override
    public BigDecimal pay(Order order, BigDecimal paid) throws NotEnoughMoneyException {
        // 1. PRECONDITION: is the call valid? Only our code can break this (contract)
        checkNotNull(order, "order must not be null");
        checkNotNull(paid, "paid must not be null");

        // 2. VALIDATION: did the customer pay enough? The customer can cause this
        if (paid.compareTo(order.total()) < 0) {
            throw new NotEnoughMoneyException("paid " + paid + ", total " + order.total());
        }

        BigDecimal change = paid.subtract(order.total());
        // 3. POSTCONDITION: is the result valid? (contract)
        checkState(change.compareTo(BigDecimal.ZERO) >= 0, "change must not be negative");
        return change;
    }

    /** One stamp for this order, then save the whole shop. This is our point of persistence. */
    @Override
    public void addStamp(Customer customer) throws IOException {
        checkNotNull(customer, "customer must not be null");
        customer.card().addStamp();
        storage.save(shop);
    }
}
