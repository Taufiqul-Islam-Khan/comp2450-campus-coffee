package coffee.model;

import java.math.BigDecimal;
import java.util.List;

/** The drinks the shop sells. */
public class Menu {
    private final List<Drink> drinks = List.of(
            new Drink("latte", new BigDecimal("4.50")),
            new Drink("tea", new BigDecimal("2.75")),
            new Drink("hot chocolate", new BigDecimal("3.25")));

    public List<Drink> drinks() {
        return drinks;
    }

    /** Returns the drink with this name, or null if we don't sell it. */
    public Drink findDrink(String name) {
        for (Drink drink : drinks) {
            if (drink.name().equals(name)) {
                return drink;
            }
        }
        return null;
    }
}
