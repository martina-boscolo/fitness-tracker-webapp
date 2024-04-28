package it.unipd.dei.cyclek.resources.stats;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import it.unipd.dei.cyclek.resources.AbstractResource;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MealAdapter extends AbstractResource {
    private final String json;
    public MealAdapter(String json) { this.json = json; }

    public String getJson() { return json; }

    //@Override
    protected void writeJSON(OutputStream out) throws Exception {
        out.write(this.getJson().getBytes());
    }

    public List<FoodIntake> fromJSON() throws IOException {
        List<FoodIntake> meal = new ArrayList<>();
        try {
            final JsonParser jp = JSON_FACTORY.createParser(this.getJson());
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"meal".equals(jp.currentName())) {
                if (jp.nextToken() == null) {
                    LOGGER.error("No Meal object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no Meal object found.");
                }
            }

            while (jp.nextToken() != JsonToken.END_OBJECT) {

                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    while (jp.nextToken() != JsonToken.END_ARRAY) {
                        Integer idFood = null;
                        Integer qty = null;

                        while (jp.nextToken() != JsonToken.END_OBJECT) {
                            String fieldName = jp.currentName();
                            jp.nextToken(); // move to the value

                            if ("idFood".equals(fieldName)) {
                                idFood = jp.getIntValue();
                            } else if ("qty".equals(fieldName)) {
                                qty = jp.getIntValue();
                            }
                        }

                        if (idFood != null && qty != null) {
                            meal.add(new FoodIntake(idFood, qty));
                        }
                    }
                }
            }
        } catch(IOException e) {
            LOGGER.error("Unable to parse an Meal object from JSON.", e);
            throw e;
        }

        return meal;
    }

    public static class FoodIntake{
        private final Integer idFood;
        private final int qty;
        public FoodIntake(Integer idFood, int qty) {
            this.idFood = idFood;
            this.qty = qty;
        }

        public Integer getIdFood() {
            return idFood;
        }

        public int getQty() {
            return qty;
        }

        protected static FoodIntake fromJSON(JsonParser jp) throws IOException {
            Integer idFood = null;
            Integer qty = null;

            while (jp.nextToken() != JsonToken.END_OBJECT) {
                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    switch (jp.currentName()) {
                        case "idFood":
                            jp.nextToken();
                            idFood = jp.getIntValue();
                            break;
                        case "qty":
                            jp.nextToken();
                            qty = jp.getIntValue();
                            break;
                    }
                }
            }
            return new FoodIntake(idFood, qty);
        }
    }

}


