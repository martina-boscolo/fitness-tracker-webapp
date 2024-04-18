package it.unipd.dei.cyclek.resources;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Diet extends AbstractResource {

    private final Integer id;
    private final Integer idUser;
    private final String planName;
    @JsonRawValue
    private final String diet;
    private final String dietDate;
    private final String dietHour;

    public Diet(Integer id){

        this.id = id;
        this.idUser = null;
        this.planName = "";
        this.diet = null;
        this.dietDate = "";
        this.dietHour = "";

    }
    public Diet(Integer idUser, String planName, String diet) {

        this.idUser = idUser;
        this.planName = planName;
        this.diet = diet;
        this.dietDate = "";
        this.dietHour = "";
        this.id = null;

    }
    public Diet(Integer id, Integer idUser, String planName, String diet, String dietDate, String dietHour) {

        this.id = id;
        this.idUser = idUser;
        this.planName = planName;
        this.diet = diet;
        this.dietDate = dietDate;
        this.dietHour = dietHour;

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

    public String getDietHour() {
        return dietHour;
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
