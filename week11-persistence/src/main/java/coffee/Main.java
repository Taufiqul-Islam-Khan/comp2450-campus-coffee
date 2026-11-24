package coffee;

import coffee.logic.CoffeeLogic;
import coffee.logic.CoffeeService;
import coffee.persistence.CoffeePersistence;
import coffee.persistence.json.CoffeeJsonFile;
import coffee.ui.CoffeeUI;
import java.io.IOException;
import java.util.Scanner;

/** Main creates every layer and connects them. No other class creates a layer. */
public class Main {
    public static void main(String[] args) throws IOException {
        CoffeePersistence storage = new CoffeeJsonFile("coffee.json");
        CoffeeService logic = new CoffeeLogic(storage);
        CoffeeUI ui = new CoffeeUI(logic, new Scanner(System.in));
        for (int customer = 1; customer <= 2; customer++) {   // two customers in a row
            ui.run();
        }
    }
}
