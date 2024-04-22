package it.unipd.dei.cyclek.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Meal extends AbstractResource{
    private final Integer idUser;
    private final String mealDate;
    private final Integer mealType;
    private final List<FoodIntake> meal;

    public Meal(Integer idUser, String mealDate, Integer mealType, List<FoodIntake> meal) {
        this.idUser = idUser;
        this.mealDate = mealDate;
        this.mealType = mealType;
        this.meal = meal;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public String getMealDate() {
        return mealDate;
    }

    public Integer getMealType() {
        return mealType;
    }

    public List<FoodIntake> getMeal() {
        return meal;
    }

    public static class FoodIntake extends AbstractResource{
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

        @Override
        protected void writeJSON(OutputStream out) throws Exception {
            String json = new ObjectMapper()
                    .writeValueAsString(this);
            out.write(json.getBytes(StandardCharsets.UTF_8));
        }

        protected static FoodIntake toJSON(JsonParser jp) throws IOException {
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

    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        jg.writeStartObject();
        jg.writeFieldName("meal");
        jg.writeNumber(this.getIdUser());
        jg.writeString(this.getMealDate());
        jg.writeNumber(this.getMealType());
        jg.writeStartObject();
        jg.writeFieldName("meal");
        jg.writeStartArray();
        jg.flush();
        boolean firstElement = true;
        for (final FoodIntake r : meal) {
            // very bad work-around to add commas between resources
            if (firstElement) {
                r.toJSON(out);
                jg.flush();
                firstElement = false;
            } else {
                jg.writeRaw(',');
                jg.flush();
                r.toJSON(out);
                jg.flush();
            }
        }
        jg.writeEndArray();
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }


    public static Meal fromJSON(final InputStream in) throws IOException {
        Integer idUser = null;
        String mealDate = "";
        Integer mealType = null;
        List<FoodIntake> meal = null;
        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"meal".equals(jp.currentName())) {
                if (jp.nextToken() == null) {
                    LOGGER.error("No Meal object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no Meal object found.");
                }
            }

            while (jp.nextToken() != JsonToken.END_OBJECT) {

                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

                    switch (jp.currentName()) {
                        case "idUser":
                            jp.nextToken();
                            idUser = jp.getIntValue();
                            break;
                        case "mealDate":
                            jp.nextToken();
                            mealDate = jp.getText();
                            break;
                        case "mealType":
                            jp.nextToken();
                            mealType = jp.getIntValue();
                            break;
                        case "meal":
                            while (jp.nextToken() != JsonToken.END_ARRAY) {
                                // Parse FoodIntake object from JSON
                                FoodIntake foodIntake = FoodIntake.toJSON(jp);
                                meal.add(foodIntake); // Add FoodIntake to meal list
                            }
                            break;
                    }
                }
            }
        } catch(IOException e) {
            LOGGER.error("Unable to parse an Meal object from JSON.", e);
            throw e;
        }

        return new Meal(idUser, mealDate, mealType, meal);
    }

}
