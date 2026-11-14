# Week 10 — Automating software testing ☕🧪

The same Campus Coffee app as [`3-final`](../3-final) (Week 8), plus a **test harness**:
code that runs our tests, counts the passes, and counts the fails.

`src/main` is **exactly the same** as `3-final`. Everything new is in `src/test`.

## Week 10 learning outcomes

1. Implement a test harness that automates the testing of an abstract data type.
2. Use an automated build tool to build a complex project (continuing).

## What's in this folder

```
week10-testing/
├── pom.xml
└── src/
    ├── main/java/coffee/          the app (same as 3-final)
    │   ├── Main.java
    │   ├── ui/  logic/  model/  exceptions/
    └── test/java/coffee/          the tests (new this week)
        ├── CoffeeTestHarness.java
        └── BuggyCoffeeLogic.java
```

| File | What it is |
|---|---|
| `CoffeeTestHarness.java` | The test harness. 13 tests, a `check` helper that counts PASS and FAIL, and `newShop()`, the one line that chooses which class to test. |
| `BuggyCoffeeLogic.java` | **For practice only.** Another class that implements `CoffeeService`, with one logic bug in `pay`. It lives in `src/test`, so the real app can never use it. |

## How to run the tests

You need **Java 17 or newer**.

**IntelliJ:** *File → Open…* → choose the `week10-testing` folder → open it as a Maven project.
Then right-click `src/test/java/coffee/CoffeeTestHarness.java` → **Run 'CoffeeTestHarness.main()'**.

The output should end with:

```
Passed: 13, Failed: 0
```

**With code coverage:** right-click the same file → **More Run/Debug** →
**Run 'CoffeeTestHarness.main()' with Coverage**. Green lines ran during the tests; red lines never ran.

## What the harness tests

| Method | Tests | Kind of expected outcome |
|---|---|---|
| `drinks` | the menu has 3 drinks | return value |
| `findDrink` | `"tea"` → tea at $2.75 · `"mocha"` → `null` · `""` → `null` | return value |
| `placeOrder` | 2 lattes → $9.00 · 1 tea → $2.75 · 10 hot chocolates → $32.50 | return value |
| `placeOrder` | name `"  Sam  "` is saved as `"Sam"` | state |
| `placeOrder` | 11 cups · 0 cups → `InvalidCupCountException` | exception |
| `pay` | $10.00 → $1.00 change · exactly $9.00 → $0.00 change | return value |
| `pay` | $8.99 for $9.00 → `NotEnoughMoneyException` | exception |

Every test starts with a fresh shop (`newShop()`), so the tests are independent:
the order they run in does not change the results.

## Try it yourself

1. **Break the code, then run the harness.** In `src/main/java/coffee/model/Order.java`, change
   `cups > MAX_CUPS` to `cups >= MAX_CUPS`. Run the harness. Which test fails, and why only that one?
2. **Test a different class.** In `CoffeeTestHarness.newShop()`, return `new BuggyCoffeeLogic()`
   instead of `new CoffeeLogic()`. Run the harness. Which test finds the bug?
3. **Read the coverage.** Comment out `testMenuHasThreeDrinks();` in `main`, then run with coverage.
   Which method in `CoffeeLogic` turns red?

Undo each change when you are done. All 13 tests should pass again.
