package coffee.logic;

import coffee.exceptions.InvalidCupCountException;
import coffee.exceptions.NotEnoughMoneyException;
import coffee.model.Customer;
import coffee.model.Drink;
import coffee.model.Order;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/** What the UI is allowed to ask the logic to do. */
public interface CoffeeService {
    List<Drink> drinks();
    Drink findDrink(String name);
    Customer findCustomer(String id);
    Order placeOrder(Drink drink, int cups, String nameOnCup) throws InvalidCupCountException;
    BigDecimal pay(Order order, BigDecimal paid) throws NotEnoughMoneyException;
    void addStamp(Customer customer) throws IOException;
}
