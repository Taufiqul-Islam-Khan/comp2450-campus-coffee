# Week 10 · Wednesday (Nov 18): every kind of outcome, and testing an ADT

**Changed:** `CoffeeTestHarness.java` (now 12 tests). **New:** `BuggyCoffeeLogic.java` (practice only).
`src/main` is the same as `3-final`.

## What's new since Monday

| Idea | Where in the code |
|---|---|
| **Exception test:** no exception = FAIL, our exception = PASS, any other = FAIL | `testElevenCupsRejected`, `testZeroCupsRejected`, `testPayTooLittle` |
| Specific `catch` first, `catch (Exception e)` last | the same three tests |
| **State test:** call, then check the object with its getters | `testNameSpacesRemoved` (`nameOnCup()`) |
| **One test, several steps:** set up the starting state with code | `testPayTen`, `testPayExact` (place an order, then pay) |
| **Independent tests:** every test starts with a fresh shop | `CoffeeService shop = newShop();` |
| **Test the interface, not the class** | `newShop()` returns a `CoffeeService`; change one line to test another class |

## New tests

| Test | Input | Expected outcome |
|---|---|---|
| `testNameSpacesRemoved` | `placeOrder(latte, 1, "  Sam  ")` | name on cup is `"Sam"` (state) |
| `testElevenCupsRejected` | `placeOrder(latte, 11, "")` | `InvalidCupCountException` |
| `testZeroCupsRejected` | `placeOrder(latte, 0, "")` | `InvalidCupCountException` |
| `testPayTen` | pay $10.00 for $9.00 | change $1.00 |
| `testPayExact` | pay $9.00 for $9.00 | change $0.00 (edge: exactly equal) |
| `testPayTooLittle` | pay $8.99 for $9.00 | `NotEnoughMoneyException` (edge) |

## Run it

Right-click `CoffeeTestHarness.java` → **Run 'CoffeeTestHarness.main()'**.
Expected last line: `Passed: 12, Failed: 0`.

## Try it: same tests, another class

`BuggyCoffeeLogic` also implements `CoffeeService`, but its `pay` has one bug (`<=` instead of `<`).
In `CoffeeTestHarness.newShop()`, change `return new CoffeeLogic();` to `return new BuggyCoffeeLogic();`
and run again. You should see:

```
FAIL: pay: exactly $9.00 gives $0.00 change

Passed: 11, Failed: 1
```

The bug never crashes. It gives a wrong answer, and only the "exactly equal" edge case finds it.
Change `newShop()` back when you are done.
