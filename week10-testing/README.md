# Week 10 — Automating software testing ☕🧪

The same Campus Coffee app as [`3-final`](../3-final) (Week 8), plus a **test harness**:
code that runs our tests, counts the passes, and counts the fails.

In every folder below, `src/main` is **exactly the same** as `3-final`.
Everything new is in `src/test`.

## Week 10 learning outcomes

1. Implement a test harness that automates the testing of an abstract data type.
2. Use an automated build tool to build a complex project (continuing).

## Three folders, three lectures

The **same harness** at the end of each lecture. Open them in order and compare.

| Folder | Lecture | Tests | What it shows |
|---|---|---|---|
| [`1-monday`](1-monday) | Mon Nov 16 | 6 | Our first harness: a `check` helper that counts PASS and FAIL. Tests that check a **return value**. |
| [`2-wednesday`](2-wednesday) | Wed Nov 18 | 12 | Tests that expect an **exception**, a **state** test, and a test with several steps. `newShop()` so the same tests can check any class that implements `CoffeeService`. Practice class `BuggyCoffeeLogic`. |
| [`3-friday`](3-friday) | Fri Nov 20 | 13 | One more test, `testMenuHasThreeDrinks`, added after **code coverage** showed that `drinks()` never ran. |

**Tip:** to see exactly what changed, compare two folders side by side
(in IntelliJ: select both folders → right-click → *Compare Directories*).

## How the code is organised

```
<day folder>/
├── pom.xml
└── src/
    ├── main/java/coffee/      the app (same as 3-final)
    └── test/java/coffee/      the tests (new this week)
        ├── CoffeeTestHarness.java
        └── BuggyCoffeeLogic.java   (from Wednesday, practice only)
```

Test code goes in `src/test`. It can use the app's classes, but the app can never use the
test classes, and test code is not packed into the finished app.

## How to run the tests

You need **Java 17 or newer**. Each day folder is its own Maven project.

**IntelliJ:** *File → Open…* → choose one day folder (for example `3-friday`) → open it as a
Maven project. Then right-click `src/test/java/coffee/CoffeeTestHarness.java` →
**Run 'CoffeeTestHarness.main()'**.

**With code coverage:** right-click the same file → **More Run/Debug** →
**Run 'CoffeeTestHarness.main()' with Coverage**. Green lines ran during the tests; red lines never ran.

## Expected output

| Folder | Last line of the output |
|---|---|
| `1-monday` | `Passed: 6, Failed: 0` |
| `2-wednesday` | `Passed: 12, Failed: 0` |
| `3-friday` | `Passed: 13, Failed: 0` |
