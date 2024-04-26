package it.unipd.dei.cyclek.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Exercise extends AbstractResource {
    private final Integer id;
    private final Integer id_category;
    private final String exercise_name;
    private final String description;
    private final String exercise_equipment;

    public Exercise(Integer id, Integer id_category, String exercise_name, String description,String exercise_equipment){
        this.id = id;
        this.id_category = id_category;
        this.exercise_name = exercise_name;
        this.description = description;
        this.exercise_equipment = exercise_equipment;
    }
    public Integer getId() {
        return id;
    }
    public Integer getId_category() {
        return id_category;
    }
    public String getExercise_name() {
        return exercise_name;
    }
    public String getDescription() {
        return description;
    }
    public String getExercise_equipment() {
        return exercise_equipment;
    }

    protected void writeJSON(OutputStream out) throws Exception {

        String json = new ObjectMapper()
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .writer().withRootName("exercise")
                .writeValueAsString(this);
        out.write(json.getBytes(StandardCharsets.UTF_8));
    }

}
