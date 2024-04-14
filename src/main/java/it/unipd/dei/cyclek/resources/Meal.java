package it.unipd.dei.cyclek.resources;

import java.io.OutputStream;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Meal extends AbstractResource{
    private int id;
    private int idUte;
    Date day;
    String meal;

    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        String json = new ObjectMapper()
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .writer().withRootName("meal")
                .writeValueAsString(this);
        out.write(json.getBytes(StandardCharsets.UTF_8));
    }
}
