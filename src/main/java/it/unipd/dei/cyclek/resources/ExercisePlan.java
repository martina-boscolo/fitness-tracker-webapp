package it.unipd.dei.cyclek.resources;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.json.Json;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ExercisePlan extends AbstractResource {

    private final Integer id;
    private final Integer idUser;
    private final String planName;
    @JsonRawValue
    private final String plan;
    private final String planDate;

    public ExercisePlan(Integer id){

        this.id = id;
        this.idUser = null;
        this.planName = "";
        this.plan = null;
        this.planDate = "";

    }
    public ExercisePlan(Integer idUser, String planName, String plan) {

        this.idUser = idUser;
        this.planName = planName;
        this.plan = plan;
        this.planDate = "";
        this.id = null;

    }
    public ExercisePlan(Integer id, Integer idUser, String planName, String plan, String planDate) {

        this.id = id;
        this.idUser = idUser;
        this.planName = planName;
        this.plan = plan;
        this.planDate = planDate;


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

    public String getPlan() {
        return plan;
    }

    public String getPlanDate() {
        return planDate;
    }

    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        String json = new ObjectMapper()
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .writer().withRootName("exercise_plan")
                .writeValueAsString(this);
        out.write(json.getBytes(StandardCharsets.UTF_8));
    }
}