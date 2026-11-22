package coffee.persistence.json;

import coffee.exceptions.InvalidCupCountException;
import coffee.model.CoffeeShop;
import coffee.model.Drink;
import coffee.model.Menu;
import coffee.model.Order;
import coffee.persistence.CoffeePersistence;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/** Saves and loads the whole shop in one JSON file. The only class that knows about JSON. */
public class CoffeeJsonFile implements CoffeePersistence {
    private final Path file;

    public CoffeeJsonFile(Path file) {
        this.file = checkNotNull(file, "file must not be null");
    }

    /** Coarse-grained: writes the whole shop every time and replaces the old file. */
    @Override
    public void save(CoffeeShop shop) throws IOException {
        checkNotNull(shop, "shop must not be null");

        JsonArrayBuilder menu = Json.createArrayBuilder();
        for (Drink drink : shop.menu().drinks()) {
            // composition: the whole drink goes inside the menu
            menu.add(Json.createObjectBuilder()
                    .add("id", drink.id())
                    .add("name", drink.name())
                    .add("price", drink.price()));
        }

        JsonArrayBuilder orders = Json.createArrayBuilder();
        for (Order order : shop.orders()) {
            orders.add(Json.createObjectBuilder()
                    .add("id", order.id())
                    .add("drinkId", order.drink().id())     // aggregation: only the ID
                    .add("cups", order.cups())
                    .add("nameOnCup", order.nameOnCup()));
        }

        JsonObject json = Json.createObjectBuilder()
                .add("menu", menu)
                .add("orders", orders)
                .build();

        // The factory only adds line breaks and spaces, so the file is easy to read.
        JsonWriterFactory factory = Json.createWriterFactory(Map.of(JsonGenerator.PRETTY_PRINTING, true));
        try (JsonWriter writer = factory.createWriter(Files.newBufferedWriter(file))) {
            writer.writeObject(json);
        }
    }

    /** Loads in three steps, so every drink ID can be linked to a Drink that already exists. */
    @Override
    public CoffeeShop load() throws IOException {
        JsonObject json;
        try (JsonReader reader = Json.createReader(Files.newBufferedReader(file))) {
            json = reader.readObject();
        }

        // Step 1: start building each order with its own values. Skip the drink for now.
        Map<String, Order.Builder> builders = new LinkedHashMap<>();
        for (JsonObject o : json.getJsonArray("orders").getValuesAs(JsonObject.class)) {
            try {
                Order.Builder builder = new Order.Builder()
                        .id(o.getString("id"))
                        .cups(o.getInt("cups"))
                        .nameOnCup(o.getString("nameOnCup"));
                builders.put(o.getString("id"), builder);
            } catch (InvalidCupCountException e) {
                throw new IOException("bad order " + o.getString("id") + " in " + file + ": " + e.getMessage(), e);
            }
        }

        // Step 2: the drinks on the menu, kept in a map by their ID.
        Map<String, Drink> drinksById = new LinkedHashMap<>();
        for (JsonObject d : json.getJsonArray("menu").getValuesAs(JsonObject.class)) {
            Drink drink = new Drink(d.getString("id"), d.getString("name"),
                    d.getJsonNumber("price").bigDecimalValue());
            drinksById.put(drink.id(), drink);
        }

        // Step 3: read the orders a second time, link each drink ID to its Drink, and build the order.
        List<Order> orders = new ArrayList<>();
        for (JsonObject o : json.getJsonArray("orders").getValuesAs(JsonObject.class)) {
            Drink drink = drinksById.get(o.getString("drinkId"));
            checkState(drink != null, "no drink on the menu with ID " + o.getString("drinkId"));
            orders.add(builders.get(o.getString("id")).drink(drink).build());
        }

        return new CoffeeShop(new Menu(new ArrayList<>(drinksById.values())), orders);
    }
}
