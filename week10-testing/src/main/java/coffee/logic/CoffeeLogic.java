package coffee.logic;

import coffee.exceptions.InvalidCupCountException;
import coffee.exceptions.NotEnoughMoneyException;
import coffee.model.Drink;
import coffee.model.Menu;
import coffee.model.Order;
import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/** Decides and calculates. Never reads input, never prints. */
public class CoffeeLogic implements CoffeeService {
    private final Menu menu = new Menu();

    @Override
    public List<Drink> drinks() {
        return menu.drinks();
    }

    @Override
    public Drink findDrink(String name) {
        return menu.findDrink(name);      // null if we don't sell it
    }

    @Override
    public Order placeOrder(Drink drink, int cups, String nameOnCup)
            throws InvalidCupCountException {
        return new Order.Builder()        // the Builder checks each piece
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
}
