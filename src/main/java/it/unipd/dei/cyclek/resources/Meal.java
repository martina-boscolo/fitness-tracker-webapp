package it.unipd.dei.cyclek.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.unipd.dei.cyclek.rest.meal.ListMealRR;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Meal extends AbstractResource{
    private Integer id;                 //meal identificator
    private Integer id_user;            //id of user who registered it
    private Date date;                  //day
    private Integer meal_type;          //meal type
    @JsonRawValue
    private String meal;                //meal

    public Meal(Integer id, Integer id_user, Date date, Integer meal_type, String meal) {
        this.id=id;
        this.id_user=id_user;
        this.date=date;
        this.meal_type=meal_type;
        this.meal=meal;
    }

    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        String json = new ObjectMapper()
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .writer().withRootName("meal")
                .writeValueAsString(this);
        out.write(json.getBytes(StandardCharsets.UTF_8));
    }

    public static Meal fromJSON(final InputStream in) throws IOException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.readValue(in, Meal.class);
    }

    public ArrayList<FoodGramsAssociation> fromJsonToMealJava(){
        ArrayList<FoodGramsAssociation> listMealJava = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(this.getMeal());
            JsonNode mealsNode = rootNode.get("meal");

            if (mealsNode != null && mealsNode.isArray()) {
                for (JsonNode mealNode : mealsNode) {
                    if (mealNode.isObject()) {
                        mealNode.fields().forEachRemaining(entry -> {
                            Integer id_food = Integer.parseInt(entry.getKey()); // Nome del campo (id_food o grams)
                            Integer grams = entry.getValue().asInt(); // Valore del campo

                            listMealJava.add(new FoodGramsAssociation(id_food, grams));
                        });
                    }
                }
            }
            return listMealJava;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id=id;
    }

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user=id_user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date=date;
    }

    public Integer getMeal_type() {
        return meal_type;
    }

    public void setMeal_type(Integer meal_type) {
        this.meal_type=meal_type;
    }

    public String getMeal() {
        return meal;
    }
    public void setMeal(String meal) {
        this.meal = meal;
    }
}
