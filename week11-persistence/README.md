# Week 11 — Persistence ☕💾

Campus Coffee now **remembers** its data. The menu and the **order history** are saved in
`coffee.json` and loaded again when the program starts. Close the program, run it again,
and every order is still there.

## Week 11 learning outcomes

1. Measure the coverage of a test suite using an IDE.
2. Persist and load data and its relationships using a structured text-based format.
3. Apply the dependency inversion principle (SOLID) to an existing layered code base.
4. Persist/load instances of domain model objects to/from a JSON-formatted file.

## What is new since Week 10

| Idea (lecture) | Where in the code |
|---|---|
| **Persistence layer** (Mon) | `persistence/CoffeePersistence.java` (interface) and `persistence/json/CoffeeJsonFile.java` (JSON) |
| **Unique IDs** (Mon) | drinks `D1`, `D2`, `D3`; orders `O-1001`, `O-1002`, … (`Order` now has an `id`) |
| **Order history** (Mon) | `CoffeeShop` holds the menu and every paid order |
| **Dependency Inversion Principle** (Wed) | `CoffeeLogic` has a field of type `CoffeePersistence` (the interface), not `CoffeeJsonFile` |
| **Dependency injection** (Wed) | `CoffeeLogic(CoffeePersistence storage)` and `CoffeeUI(CoffeeService shop, Scanner input)` receive their objects |
| **Main connects the layers** (Wed) | `Main.java`: the only place that creates the layers |
| **Composition in JSON** (Wed) | the menu owns its drinks: each drink is saved whole, **inside** the menu |
| **Aggregation in JSON** (Wed) | an order points to a drink on the menu: the order saves **only the drink's ID** |
| **JSON-P and builders** (Fri) | `pom.xml` (new dependency) and `CoffeeJsonFile.save` |
| **Loading in three steps** (Fri) | `CoffeeJsonFile.load` (steps 1, 2 and 3 are marked in the comments) |
| **Bad data cannot make an invalid object** (Fri) | `Order.Builder` and the checks in `Drink` |

Point of persistence: after an order is paid, `CoffeeLogic.recordOrder` adds it to the history
and saves the whole shop (coarse-grained). Loading happens once, when `CoffeeLogic` is created.

## From the slides to the code

| Slide (day) | Open this file |
|---|---|
| Example: Coverage of Order.Builder.cups (Mon) | `model/Order.java` → `Builder.cups` |
| Layers of Responsibility with Persistence (Mon) | the packages `ui`, `logic`, `model`, `persistence` |
| Applying DIP to CoffeeLogic (Wed) | `persistence/CoffeePersistence.java`, the `storage` field in `logic/CoffeeLogic.java` |
| Dependency Injection (Wed) | the constructors of `CoffeeLogic` and `ui/CoffeeUI.java` |
| Main Connects the Layers (Wed) | `Main.java` |
| Composition and Aggregation in coffee.json (Wed) | `coffee.json` |
| Builders in JSON-P, Saving the Shop with JSON-P (Fri) | `CoffeeJsonFile.save` |
| Reading coffee.json with JSON-P, Loading in Three Steps (Fri) | `CoffeeJsonFile.load` |
| Builders Reject Bad Data When Loading (Fri) | `Order.Builder`, and the `catch` in `CoffeeJsonFile.load` |

## How the code is organised

```
week11-persistence/
├── pom.xml                  Guava + JSON-P
├── coffee.json              the saved state (menu and order history)
└── src/main/java/coffee/
    ├── Main.java            creates every layer and connects them
    ├── ui/                  CoffeeUI: reads input, prints messages
    ├── logic/               CoffeeService (interface) and CoffeeLogic
    ├── model/               Drink, Menu, Order, CoffeeShop
    ├── persistence/         CoffeePersistence (interface)
    │   └── json/            CoffeeJsonFile: the only class that knows about JSON
    └── exceptions/          same as Week 8
```

```
CoffeeUI ──uses──► CoffeeService ◄── CoffeeLogic ──uses──► CoffeePersistence ◄── CoffeeJsonFile
                    (interface)                              (interface)
```

## The saved file

```json
{
  "menu": [
    { "id": "D1", "name": "latte", "price": 4.50 },
    { "id": "D2", "name": "tea", "price": 2.75 },
    { "id": "D3", "name": "hot chocolate", "price": 3.25 }
  ],
  "orders": [
    { "id": "O-1001", "drinkId": "D1", "cups": 2, "nameOnCup": "Sam" },
    { "id": "O-1002", "drinkId": "D2", "cups": 1, "nameOnCup": "" },
    { "id": "O-1003", "drinkId": "D1", "cups": 1, "nameOnCup": "Priya" }
  ]
}
```

(The real `coffee.json` has the same data. After the first save, JSON-P writes it with its own
line breaks and spaces, starting with an empty line. The data is the same.)

## How loading works (three steps)

An `Order` cannot be built without its `Drink`, so we keep each order's builder open until the
drink is found:

1. Start an `Order.Builder` for each order with its own values (`id`, `cups`, `nameOnCup`). Skip the drink.
2. Build each drink on the menu and keep it in a map by its ID.
3. Read the orders a second time: find each order's drink by `drinkId`, give it to that order's builder, and build the order.

## How to run

You need **Java 17 or newer**. Open **this folder** (`week11-persistence`) in IntelliJ as a Maven
project, then run `Main.java`. The program reads and writes `coffee.json` in this folder.

Try it:

1. Run it. The first line after the menu says `Orders so far: 3`. Order anything and pay.
   Your receipt shows the new order ID, `O-1004`.
2. Stop the program and run it again. It now says `Orders so far: 5` (Main serves two customers).
   The orders were saved.
3. Open `coffee.json`. Your new orders are at the end of `"orders"`, each with only a `drinkId`.
4. Change the first order's `"cups"` to `11` in the file and run again. The program stops with
   `bad order O-1001 in coffee.json: cups was 11`: bad data never becomes an invalid `Order`.
   Change it back afterwards.

To start again with 3 orders, undo your changes to `coffee.json` (or copy it from GitHub).
