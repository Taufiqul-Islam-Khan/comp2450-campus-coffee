package coffee.logic;

import coffee.exceptions.InvalidCupCountException;
import coffee.model.Drink;
import coffee.model.Order;
import java.math.BigDecimal;
import java.util.List;

/** What the UI is allowed to ask the logic to do. */
public interface CoffeeService {
    List<Drink> drinks();
    Drink findDrink(String name);
    Order placeOrder(Drink drink, int cups) throws InvalidCupCountException;
    BigDecimal pay(Order order, BigDecimal paid);
}
