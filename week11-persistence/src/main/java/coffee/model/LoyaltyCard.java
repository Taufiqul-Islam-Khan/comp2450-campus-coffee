package coffee.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/** A customer's loyalty card: one stamp for each order. It belongs to one customer (composition). */
public class LoyaltyCard {
    private final String number;
    private int stamps;

    public LoyaltyCard(String number, int stamps) {
        checkNotNull(number, "number must not be null");
        checkArgument(!number.isBlank(), "number must not be blank");
        checkArgument(stamps >= 0, "stamps must not be negative");
        this.number = number;
        this.stamps = stamps;
    }

    public String number() { return number; }
    public int stamps() { return stamps; }

    public void addStamp() {
        int before = stamps;
        stamps++;
        checkState(stamps == before + 1, "addStamp must add exactly one stamp");   // postcondition
    }
}
