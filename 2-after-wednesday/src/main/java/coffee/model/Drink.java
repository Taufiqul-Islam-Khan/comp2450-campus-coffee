package coffee.model;

import java.math.BigDecimal;

/** One drink the shop sells, for example a latte for $4.50. */
public record Drink(String name, BigDecimal price) { }
