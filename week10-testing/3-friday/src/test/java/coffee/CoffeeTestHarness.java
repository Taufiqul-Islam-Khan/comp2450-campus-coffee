package coffee;

import coffee.exceptions.InvalidCupCountException;
import coffee.exceptions.NotEnoughMoneyException;
import coffee.logic.CoffeeLogic;
import coffee.logic.CoffeeService;
import coffee.model.Drink;
import coffee.model.Order;
import java.math.BigDecimal;

/**
 * Week 10, Friday: Wednesday's harness plus testMenuHasThreeDrinks,
 * added after code coverage showed that drinks() never ran.
 * It runs every test, counts the passes, and counts the fails.
 * Run it like any program: right-click, then Run 'CoffeeTestHarness.main()'.
 */
public class CoffeeTestHarness {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        // drinks (added on Friday: coverage showed drinks() never ran)
        testMenuHasThreeDrinks();

        // findDrink (return value)
        testFindTea();
        testFindMocha();
        testFindEmptyName();

        // placeOrder (return value and state)
        testTwoLattes();
        testOneTea();
        testTenHotChocolates();
        testNameSpacesRemoved();

        // placeOrder (exception)
        testElevenCupsRejected();
        testZeroCupsRejected();

        // pay (return value and exception)
        testPayTen();
        testPayExact();
        testPayTooLittle();

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

    /** Every test starts with a fresh shop. To test another class, change only this line. */
    private static CoffeeService newShop() {
        return new CoffeeLogic();
    }

    // ---------- drinks ----------

    private static void testMenuHasThreeDrinks() {
        CoffeeService shop = newShop();
        check("drinks: the menu has 3 drinks", shop.drinks().size() == 3);
    }

    // ---------- findDrink ----------

    private static void testFindTea() {
        CoffeeService shop = newShop();
        Drink tea = shop.findDrink("tea");
        check("findDrink: \"tea\" gives tea at $2.75",
                tea != null && tea.price().compareTo(new BigDecimal("2.75")) == 0);
    }

    private static void testFindMocha() {
        CoffeeService shop = newShop();
        check("findDrink: \"mocha\" gives null", shop.findDrink("mocha") == null);
    }

    private static void testFindEmptyName() {
        CoffeeService shop = newShop();
        check("findDrink: \"\" gives null", shop.findDrink("") == null);
    }

    // ---------- placeOrder: return value and state ----------

    private static void testTwoLattes() {
        CoffeeService shop = newShop();
        try {
            Order order = shop.placeOrder(shop.findDrink("latte"), 2, "Sam");
            check("placeOrder: 2 lattes cost $9.00",
                    order.total().compareTo(new BigDecimal("9.00")) == 0);
        } catch (Exception e) {
            check("placeOrder: 2 lattes cost $9.00", false);
        }
    }

    private static void testOneTea() {
        CoffeeService shop = newShop();
        try {
            Order order = shop.placeOrder(shop.findDrink("tea"), 1, "");
            check("placeOrder: 1 tea costs $2.75",
                    order.total().compareTo(new BigDecimal("2.75")) == 0);
        } catch (Exception e) {
            check("placeOrder: 1 tea costs $2.75", false);
        }
    }

    private static void testTenHotChocolates() {
        CoffeeService shop = newShop();
        try {
            Order order = shop.placeOrder(shop.findDrink("hot chocolate"), 10, "");
            check("placeOrder: 10 hot chocolates cost $32.50",
                    order.total().compareTo(new BigDecimal("32.50")) == 0);
        } catch (Exception e) {
            check("placeOrder: 10 hot chocolates cost $32.50", false);
        }
    }

    private static void testNameSpacesRemoved() {
        CoffeeService shop = newShop();
        try {
            Order order = shop.placeOrder(shop.findDrink("latte"), 1, "  Sam  ");
            check("placeOrder: name \"  Sam  \" is saved as \"Sam\"",
                    order.nameOnCup().equals("Sam"));
        } catch (Exception e) {
            check("placeOrder: name \"  Sam  \" is saved as \"Sam\"", false);
        }
    }

    // ---------- placeOrder: exception ----------

    private static void testElevenCupsRejected() {
        CoffeeService shop = newShop();
        try {
            shop.placeOrder(shop.findDrink("latte"), 11, "");
            check("placeOrder: 11 cups is rejected", false);   // no exception: fail
        } catch (InvalidCupCountException e) {
            check("placeOrder: 11 cups is rejected", true);    // the exception we expected
        } catch (Exception e) {
            check("placeOrder: 11 cups is rejected", false);   // a different exception: fail
        }
    }

    private static void testZeroCupsRejected() {
        CoffeeService shop = newShop();
        try {
            shop.placeOrder(shop.findDrink("latte"), 0, "");
            check("placeOrder: 0 cups is rejected", false);
        } catch (InvalidCupCountException e) {
            check("placeOrder: 0 cups is rejected", true);
        } catch (Exception e) {
            check("placeOrder: 0 cups is rejected", false);
        }
    }

    // ---------- pay ----------

    private static void testPayTen() {
        CoffeeService shop = newShop();
        try {
            Order order = shop.placeOrder(shop.findDrink("latte"), 2, "");
            BigDecimal change = shop.pay(order, new BigDecimal("10.00"));
            check("pay: $10.00 for $9.00 gives $1.00 change",
                    change.compareTo(new BigDecimal("1.00")) == 0);
        } catch (Exception e) {
            check("pay: $10.00 for $9.00 gives $1.00 change", false);
        }
    }

    private static void testPayExact() {
        CoffeeService shop = newShop();
        try {
            Order order = shop.placeOrder(shop.findDrink("latte"), 2, "");
            BigDecimal change = shop.pay(order, new BigDecimal("9.00"));
            check("pay: exactly $9.00 gives $0.00 change",
                    change.compareTo(BigDecimal.ZERO) == 0);
        } catch (Exception e) {
            check("pay: exactly $9.00 gives $0.00 change", false);
        }
    }

    private static void testPayTooLittle() {
        CoffeeService shop = newShop();
        try {
            Order order = shop.placeOrder(shop.findDrink("latte"), 2, "");
            shop.pay(order, new BigDecimal("8.99"));
            check("pay: $8.99 for $9.00 is rejected", false);
        } catch (NotEnoughMoneyException e) {
            check("pay: $8.99 for $9.00 is rejected", true);
        } catch (Exception e) {
            check("pay: $8.99 for $9.00 is rejected", false);
        }
    }
}
