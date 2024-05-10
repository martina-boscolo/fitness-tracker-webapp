package it.unipd.dei.cyclek.resources.stats;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import it.unipd.dei.cyclek.resources.AbstractResource;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanAdapter extends AbstractResource {
    private final String json;
    public PlanAdapter(String json) { this.json = json; }

    public String getJson() { return json; }

    //@Override
    protected void writeJSON(OutputStream out) throws Exception {
        out.write(this.getJson().getBytes());
    }

    public Map<String, ArrayList<ExerciseDetail>> fromJSON() throws IOException {
        Map<String, ArrayList<ExerciseDetail>> plan = new HashMap<>();
        try {
            final JsonParser jp = JSON_FACTORY.createParser(this.getJson());
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"plan".equals(jp.currentName())) {
                if (jp.nextToken() == null) {
                    LOGGER.error("No plan object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no plan object found.");
                }
            }

            while (jp.nextToken() != JsonToken.END_OBJECT) {
                String day = jp.currentName(); // Get the day name
                jp.nextToken(); // Move to the array of exercises
                ArrayList<ExerciseDetail> ed = new ArrayList<>();

                while (jp.nextToken() != JsonToken.END_ARRAY) {
                    Integer idExercise = null;
                    Integer reps = null;
                    Integer sets = null;
                    Integer weight = null;
                    while (jp.nextToken() != JsonToken.END_OBJECT) {
                        if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                            switch (jp.currentName()) {
                                case "idExercise":
                                    jp.nextToken();
                                    idExercise = jp.getIntValue();
                                    break;
                                case "reps":
                                    jp.nextToken();
                                    reps = jp.getIntValue();
                                    break;
                                case "sets":
                                    jp.nextToken();
                                    sets = jp.getIntValue();
                                    break;
                                case "weight":
                                    jp.nextToken();
                                    weight = jp.getIntValue();
                                    break;
                            }
                        }
                    }

                    if (idExercise != null && reps != null && sets != null && weight != null)
                        ed.add(new ExerciseDetail(idExercise, reps, sets, weight));
                }
                plan.put(day,ed);
            }
        } catch(IOException e) {
            LOGGER.error("Unable to parse an plan object from JSON.", e);
            throw e;
        }

        return plan;
    }

    public static class ExerciseDetail{
        private final Integer idExercise;
        private final Integer reps;
        private final Integer sets;
        private final Integer weight;

        public ExerciseDetail(Integer idExercise, Integer reps, Integer sets, Integer weight) {
            this.idExercise = idExercise;
            this.reps = reps;
            this.sets = sets;
            this.weight = weight;
        }

        public Integer getIdExercise() {
            return idExercise;
        }

        public int getReps() {
            return reps;
        }

        public int getSets() {
            return sets;
        }

        public int getWeight() {
            return weight;
        }

        protected static ExerciseDetail fromJSON(JsonParser jp) throws IOException {
            Integer idExercise = null;
            Integer reps = null;
            Integer sets = null;
            Integer weight = null;

            while (jp.nextToken() != JsonToken.END_OBJECT) {
                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    switch (jp.currentName()) {
                        case "idExercise":
                            jp.nextToken();
                            idExercise = jp.getIntValue();
                            break;
                        case "reps":
                            jp.nextToken();
                            reps = jp.getIntValue();
                            break;
                        case "sets":
                            jp.nextToken();
                            sets = jp.getIntValue();
                            break;
                        case "weight":
                            jp.nextToken();
                            weight = jp.getIntValue();
                            break;
                    }
                }
            }
            return new ExerciseDetail(idExercise, reps, sets, weight);
        }
    }

}


