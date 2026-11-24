package coffee;

import coffee.model.CoffeeShop;
import coffee.model.Drink;
import coffee.model.Menu;
import coffee.persistence.CoffeePersistence;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Storage for tests only: it keeps the shop in memory and never touches coffee.json. */
public class MemoryStorage implements CoffeePersistence {
    private CoffeeShop savedShop;
    private int saveCount = 0;

    /** Starts with our three drinks and no orders. */
    public MemoryStorage() {
        List<Drink> drinks = new ArrayList<>();
        drinks.add(new Drink("D1", "latte", new BigDecimal("4.50")));
        drinks.add(new Drink("D2", "tea", new BigDecimal("2.75")));
        drinks.add(new Drink("D3", "hot chocolate", new BigDecimal("3.25")));
        savedShop = new CoffeeShop(new Menu(drinks), new ArrayList<>());
    }

    @Override
    public void save(CoffeeShop shop) {
        savedShop = shop;     // no file: keep it in memory
        saveCount++;
    }

    @Override
    public CoffeeShop load() {
        return savedShop;
    }

    /** How many times save was called. */
    public int saveCount() {
        return saveCount;
    }
}
