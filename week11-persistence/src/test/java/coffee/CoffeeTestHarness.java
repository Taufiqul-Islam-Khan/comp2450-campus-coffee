package coffee;

import coffee.exceptions.InvalidCupCountException;
import coffee.logic.CoffeeLogic;
import coffee.model.Order;
import java.io.IOException;

/**
 * Week 11: tests for saving. Every test gives CoffeeLogic a MemoryStorage
 * (dependency injection), so no test ever changes the real coffee.json.
 * Run it like any program: right-click, then Run 'CoffeeTestHarness.main()'.
 */
public class CoffeeTestHarness {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws IOException, InvalidCupCountException {
        testNewShopHasNoOrders();
        testPlaceOrderDoesNotSave();
        testRecordOrderSavesOnce();
        testFirstOrderId();

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

    private static void testNewShopHasNoOrders() throws IOException {
        CoffeeLogic shop = new CoffeeLogic(new MemoryStorage());
        check("a new shop has 0 orders", shop.orderCount() == 0);
    }

    private static void testPlaceOrderDoesNotSave() throws IOException, InvalidCupCountException {
        MemoryStorage storage = new MemoryStorage();
        CoffeeLogic shop = new CoffeeLogic(storage);
        shop.placeOrder(shop.findDrink("latte"), 2, "Sam");
        check("placeOrder does not save", storage.saveCount() == 0);
    }

    private static void testRecordOrderSavesOnce() throws IOException, InvalidCupCountException {
        MemoryStorage storage = new MemoryStorage();
        CoffeeLogic shop = new CoffeeLogic(storage);          // inject the test storage
        Order order = shop.placeOrder(shop.findDrink("latte"), 2, "Sam");
        shop.recordOrder(order);
        check("recordOrder saves once", storage.saveCount() == 1 && shop.orderCount() == 1);
    }

    private static void testFirstOrderId() throws IOException, InvalidCupCountException {
        CoffeeLogic shop = new CoffeeLogic(new MemoryStorage());
        Order order = shop.placeOrder(shop.findDrink("tea"), 1, "");
        check("the first order is O-1001", order.id().equals("O-1001"));
    }
}
