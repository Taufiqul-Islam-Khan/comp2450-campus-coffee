package coffee;

import coffee.logic.CoffeeLogic;
import coffee.logic.CoffeeService;
import coffee.model.Drink;
import coffee.model.Order;
import java.math.BigDecimal;

/**
 * Week 10, Monday: our first test harness.
 * It runs every test, counts the passes, and counts the fails.
 * Run it like any program: right-click, then Run 'CoffeeTestHarness.main()'.
 */
public class CoffeeTestHarness {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testFindTea();
        testFindMocha();
        testFindEmptyName();
        testTwoLattes();
        testOneTea();
        testTenHotChocolates();

        System.out.println();
        System.out.println("Passed: " + passed + ", Failed: " + failed);
    }

    /** The one place where we count passes and fails. */
    private static void check(String testName, boolean ok) {
        if (ok) {
            passed++;
            System.out.println("PASS: " + testName);
        } else {
            failed++;
            System.out.println("FAIL: " + testName);
        }
    }

    // ---------- findDrink: return value ----------

    private static void testFindTea() {
        CoffeeService shop = new CoffeeLogic();
        Drink tea = shop.findDrink("tea");
        check("findDrink: \"tea\" gives tea at $2.75",
                tea != null && tea.price().compareTo(new BigDecimal("2.75")) == 0);
    }

    private static void testFindMocha() {
        CoffeeService shop = new CoffeeLogic();
        check("findDrink: \"mocha\" gives null", shop.findDrink("mocha") == null);
    }

    private static void testFindEmptyName() {
        CoffeeService shop = new CoffeeLogic();
        check("findDrink: \"\" gives null", shop.findDrink("") == null);
    }

    // ---------- placeOrder: return value ----------
    // placeOrder can throw a checked exception, so Java makes us use try/catch.
    // These orders are correct, so any exception means FAIL.

    private static void testTwoLattes() {
        CoffeeService shop = new CoffeeLogic();
        try {
            Order order = shop.placeOrder(shop.findDrink("latte"), 2, "Sam");
            check("placeOrder: 2 lattes cost $9.00",
                    order.total().compareTo(new BigDecimal("9.00")) == 0);
        } catch (Exception e) {
            check("placeOrder: 2 lattes cost $9.00", false);   // should not happen
        }
    }

    private static void testOneTea() {
        CoffeeService shop = new CoffeeLogic();
        try {
            Order order = shop.placeOrder(shop.findDrink("tea"), 1, "");
            check("placeOrder: 1 tea costs $2.75",
                    order.total().compareTo(new BigDecimal("2.75")) == 0);
        } catch (Exception e) {
            check("placeOrder: 1 tea costs $2.75", false);
        }
    }

    private static void testTenHotChocolates() {
        CoffeeService shop = new CoffeeLogic();
        try {
            Order order = shop.placeOrder(shop.findDrink("hot chocolate"), 10, "");
            check("placeOrder: 10 hot chocolates cost $32.50",
                    order.total().compareTo(new BigDecimal("32.50")) == 0);
        } catch (Exception e) {
            check("placeOrder: 10 hot chocolates cost $32.50", false);
        }
    }
}
