# Week 10 · Monday (Nov 16): our first test harness

**New file:** `src/test/java/coffee/CoffeeTestHarness.java` (6 tests). `src/main` is the same as `3-final`.

## What's new

| Idea | Where in the code |
|---|---|
| A **test harness** runs our tests and counts passes and fails | `passed` and `failed` counters, `main` runs every test |
| Write the counting code once | `check(String testName, boolean ok)` |
| Each test: set up, call, compare, record | every `test...()` method |
| Compare values the right way | `compareTo(...) == 0` for money, `== null` when we expect null |
| A test that should work: any exception means FAIL | `catch (Exception e)` in the `placeOrder` tests |

## The tests

| Test | Input | Expected outcome |
|---|---|---|
| `testFindTea` | `findDrink("tea")` | tea, $2.75 |
| `testFindMocha` | `findDrink("mocha")` | `null` (edge: we don't sell it) |
| `testFindEmptyName` | `findDrink("")` | `null` (edge: empty) |
| `testTwoLattes` | `placeOrder(latte, 2, "Sam")` | total $9.00 |
| `testOneTea` | `placeOrder(tea, 1, "")` | total $2.75 (edge: lowest) |
| `testTenHotChocolates` | `placeOrder(hot chocolate, 10, "")` | total $32.50 (edge: highest) |

## Run it

Open this folder in IntelliJ as a Maven project. Right-click `CoffeeTestHarness.java` →
**Run 'CoffeeTestHarness.main()'**. Expected last line: `Passed: 6, Failed: 0`.

## Try it: the harness finds a bug

In `src/main/java/coffee/model/Order.java`, change `cups > MAX_CUPS` to `cups >= MAX_CUPS`.
Run the harness again. You should see:

```
FAIL: placeOrder: 10 hot chocolates cost $32.50

Passed: 5, Failed: 1
```

Only the edge-case test finds it. Undo the change when you are done.
