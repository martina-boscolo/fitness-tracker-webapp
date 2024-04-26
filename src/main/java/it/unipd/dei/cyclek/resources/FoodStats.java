package it.unipd.dei.cyclek.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class FoodStats extends AbstractResource{
    private final double avg_kcal;

    private final double avg_fats;
    private final double avg_carb;
    private final double avg_prot;
    private final String fav_food;
    private final int fav_food_count;

    public FoodStats(double avg_kcal, double avg_fats, double avg_carb, double avg_prot, String fav_food, int fav_food_count) {
        this.avg_kcal = avg_kcal;
        this.avg_fats = avg_fats;
        this.avg_carb = avg_carb;
        this.avg_prot = avg_prot;
        this.fav_food = fav_food;
        this.fav_food_count = fav_food_count;
    }

    public double getAvg_kcal() {
        return avg_kcal;
    }

    public double getAvg_fats() {
        return avg_fats;
    }

    public double getAvg_carb() {
        return avg_carb;
    }

    public double getAvg_prot() {
        return avg_prot;
    }

    public String getFav_food() {
        return fav_food;
    }

    public int getFav_food_count() {
        return fav_food_count;
    }

    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        String json = new ObjectMapper()
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .writer().withRootName("MealStats")
                .writeValueAsString(this);
        out.write(json.getBytes(StandardCharsets.UTF_8));
    }
}
