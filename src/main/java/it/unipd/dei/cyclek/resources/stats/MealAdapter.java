package it.unipd.dei.cyclek.resources.stats;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import it.unipd.dei.cyclek.resources.AbstractResource;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MealAdapter extends AbstractResource {
    private final String json;

    public MealAdapter(String json) {
        this.json = json;
    }

    public String getJson() {
        return json;
    }

    //@Override
    protected void writeJSON(OutputStream out) throws Exception {
        out.write(this.getJson().getBytes());
    }

    public List<FoodIntake> fromJSON() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<FoodIntake> meal = new ArrayList<>();
        for (JsonNode node : mapper.readTree(this.getJson()).get("meal"))
            meal.add(new FoodIntake(node.get("idFood").intValue(), node.get("qty").intValue()));
        return meal;
    }

    public static class FoodIntake {
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


