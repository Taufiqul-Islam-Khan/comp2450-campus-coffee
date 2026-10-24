package coffee.ui;

import coffee.logic.CoffeeService;
import coffee.model.Drink;
import coffee.model.Order;
import java.math.BigDecimal;
import java.util.Scanner;

/** The only class that reads input and prints messages. (Starter: happy path only.) */
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
        Drink drink = shop.findDrink(input.nextLine());
        System.out.print("How many cups? ");
        int cups = input.nextInt();
        Order order = shop.placeOrder(drink, cups);
        System.out.println("Total: $" + order.total());
        System.out.print("Pay how much? $");
        BigDecimal paid = input.nextBigDecimal();
        input.nextLine();   // throw away the Enter key left behind
        System.out.println("Change: $" + shop.pay(order, paid));
        System.out.println("Receipt: " + cups + " x " + drink.name());
        System.out.println("Enjoy your drink!");
    }
}
