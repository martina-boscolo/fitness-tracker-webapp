package it.unipd.dei.cyclek.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Meal extends AbstractResource{
    private Integer id;               //meal identificator
    private Integer id_user;          //id of user who registered it
    private Integer id_food;          //id of food registered
    private Date date;                //day
    private Integer meal_type;        //meal
    private Integer grams;            //grams of food
    
    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        String json=new ObjectMapper()
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .writer().withRootName("meal")
                .writeValueAsString(this);
        out.write(json.getBytes(StandardCharsets.UTF_8));
    }

    public static Meal fromJSON(final InputStream in) throws IOException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.readValue(in, Meal.class);
    }

    public Meal(Integer id, Integer id_user, Integer id_food, Date date, Integer meal_type, Integer grams) {
        this.id=id;
        this.id_user=id_user;
        this.date=date;
        this.meal_type=meal_type;
        this.grams=grams;
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

    public Integer getId_food() {
        return id_food;
    }

    public void setId_food(Integer id_food) {
        this.id_food=id_food;
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

    public Integer getGrams() {
        return grams;
    }

    public void setGrams(Integer grams) {
        this.grams=grams;
    }

}
