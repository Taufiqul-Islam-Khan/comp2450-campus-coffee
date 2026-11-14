# Week 10 · Friday (Nov 20): Maven and code coverage

**Changed:** `CoffeeTestHarness.java` (now 13 tests). `src/main` is the same as `3-final`.

## What's new since Wednesday

| Idea | Where |
|---|---|
| Main code and test code live apart | `src/main/java` (the app) and `src/test/java` (the tests) |
| Dependencies come from `pom.xml` | Guava (`checkNotNull`, `checkState`) in `<dependencies>` |
| **Code coverage:** which lines ran while the tests ran | IntelliJ: Run with Coverage |
| Coverage found a test we forgot | new `testMenuHasThreeDrinks`: `drinks()` should give 3 drinks |

## Run it with coverage

Right-click `CoffeeTestHarness.java` → **More Run/Debug** →
**Run 'CoffeeTestHarness.main()' with Coverage**. Expected last line: `Passed: 13, Failed: 0`.

In the code, a green mark means the line ran during the tests, and a red mark means it never ran.
The UI classes (`CoffeeUI`, `Main`, `ReceiptPrinter`) stay red: we don't test the UI.

## Try it: read the coverage

1. In `main`, comment out `testMenuHasThreeDrinks();` and run with coverage.
   Open `CoffeeLogic.java`. Which method is red?
2. Put the line back and run with coverage again. Is it green now?

Remember: coverage shows which lines **ran**, not that their answers were **checked**.
Every test still needs a real expected outcome.
