package coffee.logic;

import coffee.exceptions.InvalidCupCountException;
import coffee.model.Drink;
import coffee.model.Menu;
import coffee.model.Order;
import java.math.BigDecimal;
import java.util.List;

/** Decides and calculates. Never reads input, never prints. */
public class CoffeeLogic implements CoffeeService {
    private final Menu menu = new Menu();

    @Override
    public List<Drink> drinks() {
        return menu.drinks();
    }

    @Override
    public Drink findDrink(String name) {
        return menu.findDrink(name);
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

    /** Returns the change. */
    @Override
    public BigDecimal pay(Order order, BigDecimal paid) {
        return paid.subtract(order.total());      // fixed on Friday
    }
}
