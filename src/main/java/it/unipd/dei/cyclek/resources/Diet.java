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

    public Diet(Integer id, Integer idUser, String planName, String diet) {

        this.id = id;
        this.idUser = idUser;
        this.planName = planName;
        this.diet = diet;

    }

    public String getDiet() {
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
}
