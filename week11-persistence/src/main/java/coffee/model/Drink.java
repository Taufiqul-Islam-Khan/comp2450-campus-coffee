package coffee.model;

import java.math.BigDecimal;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/** One drink the shop sells, for example D1: a latte for $4.50. */
public record Drink(String id, String name, BigDecimal price) {

    /** The checks run every time a Drink is made, also when we load one from a file. */
    public Drink {
        checkNotNull(id, "id must not be null");
        checkArgument(!id.isBlank(), "id must not be blank");
        checkNotNull(name, "name must not be null");
        checkArgument(!name.isBlank(), "name must not be blank");
        checkNotNull(price, "price must not be null");
        checkArgument(price.signum() > 0, "price must be more than 0");
    }
}
