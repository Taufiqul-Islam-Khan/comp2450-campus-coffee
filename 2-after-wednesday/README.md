# Stage 2 — After Wednesday (Oct 28): input validation

## What changed since stage 1

- `ui/CoffeeUI.java`
  - `readWholeNumber()`: reads the whole line with `nextLine()`, then parses it with
    `Integer.parseInt`. If the text isn't a whole number, it explains and asks again.
  - `readOrder()`: catches `InvalidCupCountException` and asks again.
- `model/Order.java`: the constructor checks its own rule (1 to 10 cups) and throws
  `InvalidCupCountException`. A wrong `Order` can never exist.
- `logic/CoffeeService.java` and `logic/CoffeeLogic.java`: `placeOrder` declares
  `throws InvalidCupCountException`.
- **New file:** `exceptions/InvalidCupCountException.java`

**Fixed:** problem 1 (`two` for cups) and problem 3 (`0` cups).
**Still broken (fixed in stage 3):** problem 2 (`mocha`) and problem 4 (paying too little).

## The idea

Each layer checks only what it understands.
The UI checks the **type** (is it a whole number?). The domain model checks its own **rules**
(is it 1 to 10 cups?).
