package coffee.model;

import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkArgument;

/** One drink the shop sells, for example D1: a latte for $4.50. */
public record Drink(String id, String name, BigDecimal price) {

    // The constructor of a record. It runs every time a Drink is made,
    // also when we load one from coffee.json, so a bad drink is never made.
    public Drink {
        checkArgument(id != null && !id.isBlank(), "a drink needs an ID");
        checkArgument(name != null && !name.isBlank(), "a drink needs a name");
        checkArgument(price != null && price.compareTo(BigDecimal.ZERO) > 0, "price must be more than 0");
    }
}
