# Campus Coffee ☕ — COMP 2450, Week 8

A small Java console app for ordering coffee. We build it together during
**Week 8: Implementing errors and exceptions**.

The app asks three questions (which drink, how many cups, how much you pay),
then shows your change and prints a receipt.

## Three folders, three stages

The **same app** at three points in the week. Open them in order and compare.

| Folder | Lecture | What it shows |
|---|---|---|
| [`1-starter`](1-starter) | Mon Oct 26 | Works only on the happy path. Bad input crashes it or gives wrong answers. |
| [`2-after-wednesday`](2-after-wednesday) | Wed Oct 28 | Input validation: safe reading of numbers, and `Order` checks its own rule (1 to 10 cups). |
| [`3-final`](3-final) | Fri Oct 30 | Three ways to report a problem: return value, global state, exception. Nothing a customer types can crash it. |

**Tip:** to see exactly what changed, compare two folders side by side
(in IntelliJ: select both folders → right-click → *Compare Directories*).

## Week 8 learning outcomes

1. Implement input validation and error handling in an application.
2. Identify and use different strategies for error handling, including exceptions, return values, and global states.

## How the code is organised (layers of responsibility)

```
src/main/java/coffee/
├── Main.java            starts the app
├── ui/                  UI: the only place that reads input and prints messages
├── logic/               Logic: decides and calculates, never prints
├── model/               Domain model: Drink, Menu, Order (the data)
└── exceptions/          our own exception classes (from stage 2)
```

The UI calls the logic (through the `CoffeeService` interface), and the logic
uses the domain model. Never the other way around.

## How to run

You need **Java 17 or newer**. Each stage folder is its own Maven project.

**IntelliJ:** *File → Open…* → choose one stage folder (for example `3-final`) →
open it as a Maven project → run `coffee.Main`.

**Command line** (from inside a stage folder):

```bash
mvn compile
java -cp target/classes coffee.Main
```

## Try to break it

Run each stage and type these. Watch how the answer changes from stage to stage.

| Type this | 1-starter | 2-after-wednesday | 3-final |
|---|---|---|---|
| `two` for cups | ✗ crash (`InputMismatchException`) | ✓ "Please type a whole number, like 2." | ✓ same |
| `0` for cups | ✗ wrong order: Total $0.00 | ✓ "You can order 1 to 10 cups." | ✓ same |
| `mocha` for the drink | ✗ crash (`NullPointerException`) | ✗ crash | ✓ "Sorry, we don't sell that." |
| pay `5` for a $9.00 order | ✗ change: $-4.00 | ✗ change: $-4.00 | ✓ "That's not enough." |
| 2nd customer's receipt | (prints) | (prints) | ✓ "Sorry, no receipt today (the printer is out of paper)." |
