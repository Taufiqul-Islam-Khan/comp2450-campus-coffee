# Stage 3 — Final (Friday, Oct 30): three ways to report a problem

## What changed since stage 2

| Strategy | Where | Problem it handles |
|---|---|---|
| **Return value** | `Menu.findDrink` returns `null`; `CoffeeUI.readDrink()` checks it | the customer types a drink we don't sell |
| **Global state** | `ui/ReceiptPrinter.java`: `lastError`, a `static` field shared by the whole program | the printer runs out of paper |
| **Exception** | `CoffeeLogic.pay` throws `NotEnoughMoneyException`; `CoffeeUI.readPayment()` catches it | the customer pays too little |

Also new: `readMoney()`, which reads an amount like `10.00` the same safe way as `readWholeNumber()`.

**New files:** `exceptions/NotEnoughMoneyException.java`, `ui/ReceiptPrinter.java`

`Order.Builder` and the optional name on the cup are the same as in stage 2. The receipt now
reads like *Receipt: 2 x latte for Sam*.

## Checked or unchecked?

- `InvalidCupCountException` and `NotEnoughMoneyException` extend `Exception`, so they are
  **checked**: the compiler makes you catch them or declare `throws`.
- `NumberFormatException` (from `parseInt`) is **unchecked**. We catch it right inside the small
  reading helpers, because that's the normal Java way to test whether text is a number.
- `checkNotNull` and `checkState` in `Order.Builder` are contracts for **our** bugs. They throw
  Java's `NullPointerException` and `IllegalStateException` (unchecked). We never catch them.

## Try it

`main` serves **two customers in a row**. The printer has paper for only one receipt, so the
second customer sees: *"Sorry, no receipt today (the printer is out of paper)."*
