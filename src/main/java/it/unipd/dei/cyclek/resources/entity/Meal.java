package it.unipd.dei.cyclek.resources.entity;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.unipd.dei.cyclek.resources.AbstractResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Meal extends AbstractResource {
    private Integer id;                 //meal identificator
    private Integer idUser;            //id of user who registered it
    private String date;                //day
    private Integer mealType;          //meal type
    @JsonRawValue
    private String meal;                //meal

    public Meal(Integer id, Integer idUser, String date, Integer mealType, String meal) {
        this.id = id;
        this.idUser = idUser;
        this.date = date;
        this.mealType = mealType;
        this.meal = meal;
    }

    public Meal(Integer idUser, String date, Integer mealType, String mealJson) {
        this.idUser = idUser;
        this.date = date;
        this.mealType = mealType;
        this.meal = mealJson;
    }

    public Meal(Integer idUser) {
        this.idUser = idUser;
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
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(in, Meal.class);
    }

    public Integer getId() {
        return id;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public String getDate() {
        return date;
    }

    public Integer getMealType() {
        return mealType;
    }

    public String getMeal() {
        return meal;
    }
}
