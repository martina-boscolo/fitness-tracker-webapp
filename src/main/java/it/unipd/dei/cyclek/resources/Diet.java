package it.unipd.dei.cyclek.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.json.Json;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Diet extends AbstractResource {

    private final Integer id;
    private final Integer idUser;
    private final String planName;
    private final Json diet;

    public Diet(Integer id, Integer idUser, String planName, Json diet){

        this.id = id;
        this.idUser = idUser;
        this.planName = planName;
        this.diet = diet;

    }

    public Json getDiet() {
        return diet;
    }

    public String getPlanName() {
        return planName;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public Integer getId() {
        return id;
    }

    @Override
    protected void writeJSON(OutputStream out) throws Exception {

        String json = new ObjectMapper()
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .writer().withRootName("diet")
                .writeValueAsString(this);
        out.write(json.getBytes(StandardCharsets.UTF_8));
    }

    public static Diet fromJSON(final InputStream in) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(in, Diet.class);
    }








}
