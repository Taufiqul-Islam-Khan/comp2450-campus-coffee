package coffee.persistence;

import coffee.model.CoffeeShop;
import java.io.IOException;

/** What the logic layer needs from persistence: save the shop and load it. Not how. */
public interface CoffeePersistence {
    void save(CoffeeShop shop) throws IOException;
    CoffeeShop load() throws IOException;
}
