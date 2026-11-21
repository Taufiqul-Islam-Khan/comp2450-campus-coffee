package coffee.model;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * A regular customer.
 * The loyalty card belongs only to this customer (composition).
 * The favourite drinks are on the menu, and many customers can like the same drink (aggregation).
 */
public class Customer {
    private final String id;
    private final String name;
    private final LoyaltyCard card;
    private final List<Drink> favourites = new ArrayList<>();

    // private: the only way to make a Customer is through Customer.Builder
    private Customer(String id, String name, LoyaltyCard card) {
        this.id = id;
        this.name = name;
        this.card = card;
    }

    public String id() { return id; }
    public String name() { return name; }
    public LoyaltyCard card() { return card; }

    public List<Drink> favourites() {
        return List.copyOf(favourites);
    }

    /** Favourites are linked after the menu is loaded (step 3 of loading). */
    public void addFavourite(Drink drink) {
        checkNotNull(drink, "drink must not be null");
        favourites.add(drink);
    }

    /** Builds a Customer one piece at a time, checking each piece. */
    public static class Builder {
        private String id;
        private String name;
        private LoyaltyCard card;

        public Builder id(String id) {
            checkNotNull(id, "id must not be null");
            checkArgument(!id.isBlank(), "id must not be blank");
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            checkNotNull(name, "name must not be null");
            checkArgument(!name.isBlank(), "name must not be blank");
            this.name = name;
            return this;
        }

        public Builder card(LoyaltyCard card) {
            checkNotNull(card, "card must not be null");
            this.card = card;
            return this;
        }

        public Customer build() {
            checkState(id != null && name != null && card != null,
                    "set id, name and card before build()");
            return new Customer(id, name, card);
        }
    }
}
