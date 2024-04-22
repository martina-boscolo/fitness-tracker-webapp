package it.unipd.dei.cyclek.resources;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class UserExercise extends AbstractResource {
    //private final Integer id;
    private final Integer id_user;
    private final Integer id_exercise;
    private final Integer weight;
    private final Integer repetition;

    public UserExercise(Integer id_user, Integer id_exercise, Integer weight, Integer repetition) {
//    this.id = id;
        this.id_user = id_user;
        this.id_exercise = id_exercise;
        this.weight = weight;
        this.repetition = repetition;
    }
//
//
//    public Integer getId() {
//        return id;
//    }

    public Integer getId_user() {
        return id_user;
    }

    public Integer getId_exercise() {
        return id_exercise;
    }

    public Integer getWeight() {
        return weight;

    }

    public Integer getRepetition() {
        return repetition;
    }

    protected void writeJSON(OutputStream out) throws Exception {

        String json = new ObjectMapper()
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .writer().withRootName("user's exercises")
                .writeValueAsString(this);
        out.write(json.getBytes(StandardCharsets.UTF_8));
    }

    public static UserExercise fromJSON(final InputStream in) throws IOException {
        // the fields read from JSON
//        int jid = -1;
        int juser_id = -1;
        int jexercuse_id = -1;
        int jweight = -1;
        int jrepetition = -1;

        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);

            // while we are not on the start of an element or the element is not
            // a token element, advance to the next element (if any)
//            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"user's exercises".equals(jp.getCurrentName())) {
//
//                // there are no more events
//                if (jp.nextToken() == null) {
//                    LOGGER.error("No UserExercise object found in the stream.");
//                    throw new EOFException("Unable to parse JSON: no UserExercise object found.");
//                }
//            }

            while (jp.nextToken() != JsonToken.END_OBJECT) {
                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    switch (jp.getCurrentName()) {
//                        case "id":
//                            jp.nextToken();
//                            jid = jp.getIntValue();
//                            break;
                        case "id_user":
                            jp.nextToken();
                            juser_id = jp.getIntValue();
                            break;
                        case "id_exercise":
                            jp.nextToken();
                            jexercuse_id = jp.getIntValue();
                            break;
                        case "weight":
                            jp.nextToken();
                            jweight = jp.getIntValue();
                            break;
                        case "repetition":
                            jp.nextToken();
                            jrepetition = jp.getIntValue();
                            break;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse an UserExercise object from JSON.", e);
            throw e;
        }

//        return new UserExercise(jid,juser_id,jexercuse_id,jweight,jrepetition);
        return new UserExercise(juser_id, jexercuse_id, jweight, jrepetition);
    }
}

