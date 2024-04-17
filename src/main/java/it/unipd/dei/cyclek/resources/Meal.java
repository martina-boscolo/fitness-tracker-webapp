package it.unipd.dei.cyclek.resources;

import java.io.OutputStream;
import java.sql.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Meal extends AbstractResource{
    private Integer id;
    private Integer idUte;
    private Date day;
    private String meal;

    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        String json = new ObjectMapper()
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .writer().withRootName("meal")
                .writeValueAsString(this);
        out.write(json.getBytes(StandardCharsets.UTF_8));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUte() {
        return idUte;
    }

    public void setIdUte(int idUte) {
        this.idUte = idUte;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public String getMeal() {
        return meal;
    }

    public Meal(Integer id, Integer idUte, Date day, String meal) {
        this.id = id;
        this.idUte = idUte;
        this.day = day;
        this.meal = meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }
}
