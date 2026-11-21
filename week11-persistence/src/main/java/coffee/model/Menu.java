package coffee.model;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/** The drinks the shop sells. The menu owns its drinks (composition). */
public class Menu {
    private final List<Drink> drinks;

    public Menu(List<Drink> drinks) {
        checkNotNull(drinks, "drinks must not be null");
        checkArgument(!drinks.isEmpty(), "a menu needs at least one drink");
        this.drinks = List.copyOf(drinks);
    }

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
