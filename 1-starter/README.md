# Stage 1 — Starter (Monday, Oct 26)

The whole app on the **happy path**: it works only if nobody makes a mistake.

Read the files in this order:

1. `model/Drink.java`, `model/Menu.java`, `model/Order.java`: the data
2. `logic/CoffeeService.java` (the interface) and `logic/CoffeeLogic.java`
3. `ui/CoffeeUI.java`: `run()` asks the questions
4. `Main.java`: starts everything

## Known problems (we fix these this week)

| # | Type this | What happens | Fixed in |
|---|---|---|---|
| 1 | `two` for cups | ✗ crash: `InputMismatchException` from `nextInt()` | stage 2 |
| 2 | `mocha` | ✗ crash: `NullPointerException` (`findDrink` returned `null` and nobody checked) | stage 3 |
| 3 | `0` for cups | ✗ wrong order: Total $0.00 | stage 2 |
| 4 | pay `1.00` for $9.00 | ✗ wrong change: $-8.00 | stage 3 |
