# Week 11 — Persistence ☕💾

Campus Coffee now **remembers** its data. The menu and the regular customers are saved in
`coffee.json` and loaded again when the program starts. Sam's loyalty stamps are still
there after you close the program.

## Week 11 learning outcome

Persist and load data and its relationships using a structured text-based format.

## What is new since Week 10

| Idea (lecture) | Where in the code |
|---|---|
| **Persistence layer** (Mon) | `persistence/CoffeePersistence.java` (interface) and `persistence/json/CoffeeJsonFile.java` (JSON) |
| **Unique IDs** (Mon) | drinks `D1`, `D2`, `D3`; customers `C-7`, `C-12`; cards `LC-0042`, `LC-0057` |
| **Dependency Inversion Principle** (Wed) | `CoffeeLogic` has a field of type `CoffeePersistence` (the interface), not `CoffeeJsonFile` |
| **Dependency injection** (Wed) | `CoffeeLogic(CoffeePersistence storage)` and `CoffeeUI(CoffeeService shop, Scanner input)` receive their objects |
| **Main connects the layers** (Wed) | `Main.java`: the only place that creates the layers |
| **Composition in JSON** (Wed) | the menu owns its drinks, and a customer owns their card: saved whole, **inside** |
| **Aggregation in JSON** (Wed) | a customer's favourite drinks: saved as **drink IDs only** |
| **JSON-P and builders** (Fri) | `pom.xml` (new dependency) and `CoffeeJsonFile.save` |
| **Loading in three steps** (Fri) | `CoffeeJsonFile.load` (steps 1, 2 and 3 are marked in the comments) |
| **Bad data cannot make an invalid object** (Fri) | the checks in `Drink`, `LoyaltyCard`, and `Customer.Builder` |

Point of persistence: after a customer gets a stamp, `CoffeeLogic.addStamp` saves the whole shop
(coarse-grained). Loading happens once, when `CoffeeLogic` is created.

## How the code is organised

```
week11-persistence/
├── pom.xml                  Guava + JSON-P
├── coffee.json              the saved state (menu and customers)
└── src/main/java/coffee/
    ├── Main.java            creates every layer and connects them
    ├── ui/                  CoffeeUI: reads input, prints messages
    ├── logic/               CoffeeService (interface) and CoffeeLogic
    ├── model/               Drink, Menu, Order, LoyaltyCard, Customer, CoffeeShop
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
  "customers": [
    { "id": "C-7", "name": "Sam",
      "card": { "number": "LC-0042", "stamps": 4 },
      "favourites": ["D1", "D3"] },
    { "id": "C-12", "name": "Priya",
      "card": { "number": "LC-0057", "stamps": 7 },
      "favourites": ["D1"] }
  ]
}
```

(The real `coffee.json` has the same data. After the first save, JSON-P writes it with its own
line breaks and spaces, starting with an empty line. The data is the same.)

## How to run

You need **Java 17 or newer**. Open **this folder** (`week11-persistence`) in IntelliJ as a Maven
project, then run `Main.java`. The program reads and writes `coffee.json` in this folder.

Try it:

1. Run it. Type `C-7` for the customer ID, then order anything. You see `Sam now has 5 stamps.`
2. Stop the program and run it again. Type `C-7`. You see `Stamps: 5`: the stamp was saved.
3. Open `coffee.json`. Sam's `"stamps"` is now 5.
4. Change Sam's `"stamps"` to `-3` in the file and run again. The program stops with
   `stamps must not be negative`: bad data never becomes an invalid `LoyaltyCard`.
   Change it back afterwards.

To start again with 4 stamps, undo your changes to `coffee.json` (or copy it from GitHub).
