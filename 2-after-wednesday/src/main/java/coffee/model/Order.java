package coffee.model;

import coffee.exceptions.InvalidCupCountException;
import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/** One order: a drink, how many cups, and an optional name for the cup. */
public class Order {
    public static final int MAX_CUPS = 10;

    private final Drink drink;
    private final int cups;
    private final String nameOnCup;

    // private: the only way to make an Order is through Order.Builder
    private Order(Drink drink, int cups, String nameOnCup) {
        this.drink = drink;
        this.cups = cups;
        this.nameOnCup = nameOnCup;
    }

    public Drink drink() { return drink; }
    public int cups() { return cups; }
    public String nameOnCup() { return nameOnCup; }

    public BigDecimal total() {
        return drink.price().multiply(BigDecimal.valueOf(cups));
    }

    /** Builds an Order one piece at a time, checking each piece. */
    public static class Builder {
        private Drink drink;
        private int cups;                  // 0 means "not set yet"
        private String nameOnCup = "";     // default: no name on the cup

        public Builder drink(Drink drink) {
            checkNotNull(drink, "drink must not be null");         // contract: only our code can pass null
            this.drink = drink;
            return this;
        }

        public Builder cups(int cups) throws InvalidCupCountException {
            if (cups < 1 || cups > MAX_CUPS) {                    // validation: the customer can cause this
                throw new InvalidCupCountException("cups was " + cups);
            }
            this.cups = cups;
            return this;
        }

        public Builder nameOnCup(String name) {                   // optional
            checkNotNull(name, "name must not be null");           // contract
            this.nameOnCup = name.strip();
            return this;
        }

        public Order build() {
            checkState(drink != null && cups != 0,                 // contract: our code skipped a step
                    "set drink and cups before build()");
            return new Order(drink, cups, nameOnCup);
        }
    }
}
