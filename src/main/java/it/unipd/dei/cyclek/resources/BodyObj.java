package it.unipd.dei.cyclek.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BodyObj extends AbstractResource{

    private Integer id;
    private Integer idUser;
    private Double weight;
    private Double height;
    private Double fatty;
    private Double lean;
    private String objDate;

    public BodyObj() {}
    public BodyObj(Integer id, Integer idUser, Double weight, Double height, Double fatty, Double lean, String objDate) {
        this.id = id;
        this.idUser = idUser;
        this.weight = weight;
        this.height = height;
        this.fatty = fatty;
        this.lean = lean;
        this.objDate = objDate;
    }

    public Integer getId() {
        return id;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public Double getWeight() {
        return weight;
    }

    public Double getHeight() {
        return height;
    }

    public Double getFatty() {
        return fatty;
    }

    public Double getLean() {
        return lean;
    }

    public String getObjDate() {
        return objDate;
    }

    @Override
    protected void writeJSON(OutputStream out) throws Exception {

        String json = new ObjectMapper()
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .writer().withRootName("bodyObj")
                .writeValueAsString(this);
        out.write(json.getBytes(StandardCharsets.UTF_8));
    }

    public static BodyObj fromJSON(final InputStream in) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(in, BodyObj.class);
    }
}
