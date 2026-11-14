package coffee;

import coffee.exceptions.InvalidCupCountException;
import coffee.exceptions.NotEnoughMoneyException;
import coffee.logic.CoffeeService;
import coffee.model.Drink;
import coffee.model.Menu;
import coffee.model.Order;
import java.math.BigDecimal;
import java.util.List;

/**
 * Week 10 practice: another class that implements CoffeeService.
 * It has ONE logic bug in pay. It never crashes; it just gives a wrong answer.
 * Lives in src/test because it is only for testing practice.
 */
public class BuggyCoffeeLogic implements CoffeeService {
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
        return new Order.Builder()
                .drink(drink)
                .cups(cups)
                .nameOnCup(nameOnCup)
                .build();
    }

    @Override
    public BigDecimal pay(Order order, BigDecimal paid) throws NotEnoughMoneyException {
        if (paid.compareTo(order.total()) <= 0) {   // BUG: should be < 0
            throw new NotEnoughMoneyException("paid " + paid + ", total " + order.total());
        }
        return paid.subtract(order.total());
    }
}
