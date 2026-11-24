package coffee.persistence.json;

import coffee.exceptions.InvalidCupCountException;
import coffee.model.CoffeeShop;
import coffee.model.Drink;
import coffee.model.Menu;
import coffee.model.Order;
import coffee.persistence.CoffeePersistence;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;

import static com.google.common.base.Preconditions.checkNotNull;

/** Saves and loads the whole shop in one JSON file. The only class that knows about JSON. */
public class CoffeeJsonFile implements CoffeePersistence {
    private final String fileName;

    public CoffeeJsonFile(String fileName) {
        this.fileName = checkNotNull(fileName, "fileName must not be null");
    }

    /** Coarse-grained: writes the whole shop every time and replaces the old file. */
    @Override
    public void save(CoffeeShop shop) throws IOException {
        checkNotNull(shop, "shop must not be null");

        // The menu: each drink is saved whole
        JsonArrayBuilder menu = Json.createArrayBuilder();
        for (Drink drink : shop.menu().drinks()) {
            JsonObjectBuilder drinkJson = Json.createObjectBuilder();
            drinkJson.add("id", drink.id());
            drinkJson.add("name", drink.name());
            drinkJson.add("price", drink.price());
            menu.add(drinkJson);          // composition: the whole drink goes inside the menu
        }

        // The orders: each order saves only the ID of its drink
        JsonArrayBuilder orders = Json.createArrayBuilder();
        for (Order order : shop.orders()) {
            JsonObjectBuilder orderJson = Json.createObjectBuilder();
            orderJson.add("id", order.id());
            orderJson.add("drinkId", order.drink().id());     // aggregation: only the ID
            orderJson.add("cups", order.cups());
            orderJson.add("nameOnCup", order.nameOnCup());
            orders.add(orderJson);
        }

        // One object holds the menu and the orders
        JsonObjectBuilder shopJson = Json.createObjectBuilder();
        shopJson.add("menu", menu);
        shopJson.add("orders", orders);
        JsonObject json = shopJson.build();

        // Write it to the file (the old file is replaced)
        JsonWriter writer = Json.createWriter(new FileWriter(fileName));
        writer.writeObject(json);
        writer.close();
    }

    /** Loads in three steps, so every drink ID can be linked to a Drink that already exists. */
    @Override
    public CoffeeShop load() throws IOException {
        JsonReader reader = Json.createReader(new FileReader(fileName));
        JsonObject json = reader.readObject();
        reader.close();

        JsonArray menuJson = json.getJsonArray("menu");
        JsonArray ordersJson = json.getJsonArray("orders");

        // Step 1: start a builder for each order with its own values. No drink yet.
        List<Order.Builder> builders = new ArrayList<>();
        for (int i = 0; i < ordersJson.size(); i++) {
            JsonObject orderJson = ordersJson.getJsonObject(i);
            Order.Builder builder = new Order.Builder();
            builder.id(orderJson.getString("id"));
            builder.nameOnCup(orderJson.getString("nameOnCup"));
            try {
                builder.cups(orderJson.getInt("cups"));      // the builder checks 1 to 10 cups
            } catch (InvalidCupCountException e) {
                throw new IOException("bad order " + orderJson.getString("id") + ": " + e.getMessage());
            }
            builders.add(builder);
        }

        // Step 2: build every drink on the menu.
        List<Drink> drinks = new ArrayList<>();
        for (int i = 0; i < menuJson.size(); i++) {
            JsonObject drinkJson = menuJson.getJsonObject(i);
            BigDecimal price = drinkJson.getJsonNumber("price").bigDecimalValue();
            drinks.add(new Drink(drinkJson.getString("id"), drinkJson.getString("name"), price));
        }
        Menu menu = new Menu(drinks);

        // Step 3: read the orders a second time, find each order's drink, and build the order.
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < ordersJson.size(); i++) {
            JsonObject orderJson = ordersJson.getJsonObject(i);
            Drink drink = menu.findDrinkById(orderJson.getString("drinkId"));
            if (drink == null) {
                throw new IOException("bad order " + orderJson.getString("id") + ": no drink "
                        + orderJson.getString("drinkId") + " on the menu");
            }
            Order.Builder builder = builders.get(i);         // the builder from step 1 for this order
            builder.drink(drink);
            orders.add(builder.build());
        }

        return new CoffeeShop(menu, orders);
    }
}
