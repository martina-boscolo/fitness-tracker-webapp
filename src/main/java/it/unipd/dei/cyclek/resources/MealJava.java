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
import it.unipd.dei.cyclek.resources.Meal;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MealJava extends AbstractResource{
    private Integer id;                 //meal identificator
    private Integer id_user;            //id of user who registered it
    private Date meal_day;                  //day
    private Integer meal_type;          //meal type

    private ArrayList<String> eatenFood;                //eaten food during lunch
    private ArrayList<Integer> grams;
    private ArrayList<Integer> calories;

    public MealJava(Integer id, Integer id_user, Date meal_day, Integer meal_type, String meal) {
        this.id=id;
        this.id_user=id_user;
        this.meal_day=meal_day;
        this.meal_type=meal_type;
        Meal mealToConvert = new Meal(id, id_user, meal_day, meal_type, meal);
        ArrayList<FoodGramsAssociation> listMealJava = new ArrayList<>();
        listMealJava = mealToConvert.fromJsonToMealJava();

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
        return meal_day;
    }

    public void setDate(Date date) {
        this.meal_day=meal_day;
    }

    public Integer getMeal_type() {
        return meal_type;
    }

    public void setMeal_type(Integer meal_type) {
        this.meal_type=meal_type;
    }

    public ArrayList<String> getId_food() {
        return eatenFood;
    }
    public void setMeal(ArrayList<String> eatenFood) {
        this.eatenFood = eatenFood;
    }

    public ArrayList<Integer> getGrams(){
        return grams;
    }

    public void setGrams(ArrayList<Integer> grams){
        this.grams=grams;
    }

    public ArrayList<Integer> getCalories(){
        return calories;
    }

    public void setCalories(ArrayList<Integer> calories){
        this.calories=calories;
    }
}
