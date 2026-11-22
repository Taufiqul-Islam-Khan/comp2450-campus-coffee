package coffee.ui;

import coffee.exceptions.InvalidCupCountException;
import coffee.exceptions.NotEnoughMoneyException;
import coffee.logic.CoffeeService;
import coffee.model.Drink;
import coffee.model.Order;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Scanner;

import static com.google.common.base.Preconditions.checkNotNull;

/** The only class that reads input and prints messages. */
public class CoffeeUI {
    private final CoffeeService shop;
    private final Scanner input;

    /** Dependency injection: the UI receives its objects instead of creating them. */
    public CoffeeUI(CoffeeService shop, Scanner input) {
        this.shop = checkNotNull(shop, "shop must not be null");
        this.input = checkNotNull(input, "input must not be null");
    }

    public void run() {
        System.out.println("Welcome to Campus Coffee!");
        for (Drink d : shop.drinks()) {
            System.out.println("  " + d.name() + "  $" + d.price());
        }
        System.out.println("Orders so far: " + shop.orderCount());
        Drink drink = readDrink();
        System.out.print("Name for the cup (press Enter to skip): ");
        String nameOnCup = input.nextLine();
        Order order = readOrder(drink, nameOnCup);
        System.out.println("Total: $" + order.total());
        BigDecimal change = readPayment(order);
        System.out.println("Change: $" + change);
        recordOrder(order);

        ReceiptPrinter.print("Receipt " + order.id() + ": " + receiptLine(order));
        if (ReceiptPrinter.lastError != null) {
            System.out.println("Sorry, no receipt today (the printer is "
                    + ReceiptPrinter.lastError + ").");
        }
        System.out.println("Enjoy your drink!");
    }

    // Saving can fail (for example, the file is read-only): tell the person, don't crash
    private void recordOrder(Order order) {
        try {
            shop.recordOrder(order);
        } catch (IOException e) {
            System.out.println("Sorry, we could not save this order.");
        }
    }

    private String receiptLine(Order order) {
        String line = order.cups() + " x " + order.drink().name();
        if (!order.nameOnCup().isEmpty()) {
            line = line + " for " + order.nameOnCup();
        }
        return line;
    }

    // Return value: findDrink gives back null if we don't sell it
    private Drink readDrink() {
        while (true) {
            System.out.print("Which drink? ");
            String name = input.nextLine().strip().toLowerCase();
            Drink drink = shop.findDrink(name);
            if (drink != null) {
                return drink;
            }
            System.out.println("Sorry, we don't sell that. Please type a drink from the menu.");
        }
    }

    // Exception from the domain model: Order.Builder checks the number of cups
    private Order readOrder(Drink drink, String nameOnCup) {
        while (true) {
            int cups = readWholeNumber("How many cups? ");
            try {
                return shop.placeOrder(drink, cups, nameOnCup);
            } catch (InvalidCupCountException e) {
                System.out.println("You can order 1 to " + Order.MAX_CUPS + " cups. Please try again.");
            }
        }
    }

    // Exception from the logic: pay checks the money
    private BigDecimal readPayment(Order order) {
        while (true) {
            BigDecimal paid = readMoney("Pay how much? $");
            try {
                return shop.pay(order, paid);
            } catch (NotEnoughMoneyException e) {
                System.out.println("That's not enough. The total is $" + order.total() + ".");
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

    /** Keeps asking until the person types an amount of money, like 10.00. */
    private BigDecimal readMoney(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = input.nextLine().strip();
            try {
                return new BigDecimal(text);
            } catch (NumberFormatException e) {
                System.out.println("Please type an amount, like 10.00.");
            }
        }
    }
}
