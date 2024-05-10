package it.unipd.dei.cyclek.resources.stats;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import it.unipd.dei.cyclek.resources.AbstractResource;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ExercisePlanStats extends AbstractResource {
    private final Integer bestEx;
    private final Double bestExWeight;
    private final Integer worstEx;
    private final Double worstExWeight;
    private final Integer favEx;
    private final Integer favExCount;


    public ExercisePlanStats(Integer bestEx, Double bestExWeight, Integer worstEx, Double worstExWeight, Integer favEx, Integer favExCount) {
        this.bestEx = bestEx;
        this.bestExWeight = bestExWeight;
        this.worstEx = worstEx;
        this.worstExWeight = worstExWeight;
        this.favEx = favEx;
        this.favExCount = favExCount;
    }

    public Integer getBestEx() {
        return bestEx;
    }

    public Double getBestExWeight() {
        return bestExWeight;
    }

    public Integer getWorstEx() {
        return worstEx;
    }

    public Double getWorstExWeight() {
        return worstExWeight;
    }

    public Integer getFavEx() {
        return favEx;
    }

    public Integer getFavExCount() {
        return favExCount;
    }

    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        String json = new ObjectMapper()
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .writer().withRootName("ExercisePlanStats")
                .writeValueAsString(this);
        out.write(json.getBytes(StandardCharsets.UTF_8));
    }
}
