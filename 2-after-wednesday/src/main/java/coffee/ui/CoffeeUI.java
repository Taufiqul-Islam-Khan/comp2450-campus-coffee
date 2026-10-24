package coffee.ui;

import coffee.exceptions.InvalidCupCountException;
import coffee.logic.CoffeeService;
import coffee.model.Drink;
import coffee.model.Order;
import java.math.BigDecimal;
import java.util.Scanner;

/** The only class that reads input and prints messages. */
public class CoffeeUI {
    private final Scanner input = new Scanner(System.in);
    private final CoffeeService shop;

    public CoffeeUI(CoffeeService shop) {
        this.shop = shop;
    }

    public void run() {
        System.out.println("Welcome to Campus Coffee!");
        for (Drink d : shop.drinks()) {
            System.out.println("  " + d.name() + "  $" + d.price());
        }
        System.out.print("Which drink? ");
        Drink drink = shop.findDrink(input.nextLine());          // fixed on Friday
        Order order = readOrder(drink);                           // NEW today
        System.out.println("Total: $" + order.total());
        System.out.print("Pay how much? $");
        BigDecimal paid = new BigDecimal(input.nextLine().strip()); // fixed on Friday
        System.out.println("Change: $" + shop.pay(order, paid));
        System.out.println("Receipt: " + order.cups() + " x " + drink.name());
        System.out.println("Enjoy your drink!");
    }

    // Exception from the domain model: Order checks the number of cups
    private Order readOrder(Drink drink) {
        while (true) {
            int cups = readWholeNumber("How many cups? ");
            try {
                return shop.placeOrder(drink, cups);
            } catch (InvalidCupCountException e) {
                System.out.println("You can order 1 to " + Order.MAX_CUPS + " cups. Please try again.");
            }
        }
    }

    /** Keeps asking until the person types a whole number, like 2. */
    private int readWholeNumber(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = input.nextLine().strip();
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                System.out.println("Please type a whole number, like 2.");
            }
        }
    }
}
