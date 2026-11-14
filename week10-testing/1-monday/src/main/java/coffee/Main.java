package coffee;

import coffee.logic.CoffeeLogic;
import coffee.ui.CoffeeUI;

public class Main {
    public static void main(String[] args) {
        CoffeeUI shop = new CoffeeUI(new CoffeeLogic());
        for (int customer = 1; customer <= 2; customer++) {   // two customers in a row
            shop.run();
        }
    }
}
