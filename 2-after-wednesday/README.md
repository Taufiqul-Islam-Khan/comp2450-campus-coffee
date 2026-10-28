# Stage 2 — After Wednesday (Oct 28): input validation

## What changed since stage 1

- `ui/CoffeeUI.java`
  - `readWholeNumber()`: reads the whole line with `nextLine()`, then parses it with
    `Integer.parseInt`. If the text isn't a whole number, it explains and asks again.
  - `readOrder()`: catches `InvalidCupCountException` and asks again.
  - New (optional) question: *Name for the cup (press Enter to skip)*.
- `model/Order.java` now uses the **Builder pattern** from Week 7:
  - the constructor is `private`; the only way to make an `Order` is `new Order.Builder()`
  - `cups(int)` checks the rule (1 to 10 cups) and throws `InvalidCupCountException`
  - `drink(Drink)` uses `Objects.requireNonNull` (a contract for our own code)
  - `nameOnCup(String)` is optional; the default is no name
  - `build()` returns an `Order` that is always valid
- `logic/CoffeeService.java` and `logic/CoffeeLogic.java`:
  `placeOrder(drink, cups, nameOnCup)` chains the Builder calls and declares
  `throws InvalidCupCountException`.
- **New file:** `exceptions/InvalidCupCountException.java`

**Fixed:** problem 1 (`two` for cups) and problem 3 (`0` cups).
**Still broken (fixed in stage 3):** problem 2 (`mocha`) and problem 4 (paying too little).

## The idea

Each layer checks only what it understands.
The UI checks the **type** (is it a whole number?). The domain model checks its own **rules**
(is it 1 to 10 cups?), inside `Order.Builder`.

Why a Builder? Adding the new `nameOnCup` field to a public constructor would break every
`new Order(...)` call. With a Builder, the new piece is one more optional method with a default.
