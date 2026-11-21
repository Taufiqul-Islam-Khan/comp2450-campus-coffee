package coffee.persistence.json;

import coffee.model.CoffeeShop;
import coffee.model.Customer;
import coffee.model.Drink;
import coffee.model.LoyaltyCard;
import coffee.model.Menu;
import coffee.persistence.CoffeePersistence;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
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

        JsonArrayBuilder customers = Json.createArrayBuilder();
        for (Customer customer : shop.customers()) {
            JsonArrayBuilder favourites = Json.createArrayBuilder();
            for (Drink drink : customer.favourites()) {
                favourites.add(drink.id());                    // aggregation: only the ID
            }
            customers.add(Json.createObjectBuilder()
                    .add("id", customer.id())
                    .add("name", customer.name())
                    .add("card", Json.createObjectBuilder()    // composition: the whole card
                            .add("number", customer.card().number())
                            .add("stamps", customer.card().stamps()))
                    .add("favourites", favourites));
        }

        JsonObject json = Json.createObjectBuilder()
                .add("menu", menu)
                .add("customers", customers)
                .build();

        // The factory only adds line breaks and spaces, so the file is easy to read.
        JsonWriterFactory factory = Json.createWriterFactory(Map.of(JsonGenerator.PRETTY_PRINTING, true));
        try (JsonWriter writer = factory.createWriter(Files.newBufferedWriter(file))) {
            writer.writeObject(json);
        }
    }

    /** Loads in three steps, so every favourite ID can be linked to a Drink that already exists. */
    @Override
    public CoffeeShop load() throws IOException {
        JsonObject json;
        try (JsonReader reader = Json.createReader(Files.newBufferedReader(file))) {
            json = reader.readObject();
        }

        // Step 1: the customers, with the card each one owns. Skip the favourites for now.
        Map<String, Customer> customersById = new LinkedHashMap<>();
        for (JsonObject c : json.getJsonArray("customers").getValuesAs(JsonObject.class)) {
            JsonObject card = c.getJsonObject("card");
            Customer customer = new Customer.Builder()
                    .id(c.getString("id"))
                    .name(c.getString("name"))
                    .card(new LoyaltyCard(card.getString("number"), card.getInt("stamps")))
                    .build();
            customersById.put(customer.id(), customer);
        }

        // Step 2: the drinks on the menu, kept in a map by their ID.
        Map<String, Drink> drinksById = new LinkedHashMap<>();
        for (JsonObject d : json.getJsonArray("menu").getValuesAs(JsonObject.class)) {
            Drink drink = new Drink(d.getString("id"), d.getString("name"),
                    d.getJsonNumber("price").bigDecimalValue());
            drinksById.put(drink.id(), drink);
        }

        // Step 3: read the customers a second time and link each favourite ID to its Drink.
        for (JsonObject c : json.getJsonArray("customers").getValuesAs(JsonObject.class)) {
            Customer customer = customersById.get(c.getString("id"));
            for (JsonString drinkId : c.getJsonArray("favourites").getValuesAs(JsonString.class)) {
                Drink drink = drinksById.get(drinkId.getString());
                checkState(drink != null, "no drink on the menu with ID " + drinkId.getString());
                customer.addFavourite(drink);
            }
        }

        return new CoffeeShop(new Menu(new ArrayList<>(drinksById.values())),
                new ArrayList<>(customersById.values()));
    }
}
