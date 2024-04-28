package it.unipd.dei.cyclek.resources.entity;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.unipd.dei.cyclek.resources.AbstractResource;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Diet extends AbstractResource {

    private final Integer id;
    private final Integer idUser;
    private final String planName;
    @JsonRawValue
    private final String diet;
    private final String dietDate;

    public Diet(Integer id){

        this.id = id;
        this.idUser = null;
        this.planName = "";
        this.diet = null;
        this.dietDate = "";

    }
    public Diet(Integer idUser, String planName, String diet) {

        this.idUser = idUser;
        this.planName = planName;
        this.diet = diet;
        this.dietDate = "";
        this.id = null;

    }
    public Diet(Integer id, Integer idUser, String planName, String diet, String dietDate) {

        this.id = id;
        this.idUser = idUser;
        this.planName = planName;
        this.diet = diet;
        this.dietDate = dietDate;


    }

    public Integer getId() {
        return id;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public String getPlanName() {
        return planName;
    }

    public String getDiet() {
        return diet;
    }

    public String getDietDate() {
        return dietDate;
    }

    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        String json = new ObjectMapper()
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .writer().withRootName("diet")
                .writeValueAsString(this);
        out.write(json.getBytes(StandardCharsets.UTF_8));
    }
}
