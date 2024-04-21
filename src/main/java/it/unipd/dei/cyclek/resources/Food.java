package it.unipd.dei.cyclek.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Food  extends AbstractResource{
    private Integer id;         //food identificator
    private String fdnm;    //food name
    private Integer kcal;       //number of Calories for 100g of food
    private Integer fats;       //grams of fats for 100g of food
    private Integer carbs;      //grams of carbs for 100g of food
    private Integer prot;       //grams of prot for 100g of food
    public Food(Integer id, String fdnm, Integer kcal, Integer fats, Integer carbs, Integer prot) {
        this.id = id;
        this.fdnm = fdnm;
        this.kcal = kcal;
        this.fats = fats;
        this.carbs = carbs;
        this.prot = prot;
    }

    public Food(Integer id) {
        new Food(id, null, null, null, null, null);
    }

    public static Food fromJSON(final InputStream in) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(in, Food.class);
    }


    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        String json = new ObjectMapper()
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .writer().withRootName("food")
                .writeValueAsString(this);
        out.write(json.getBytes(StandardCharsets.UTF_8));
    }

    public Integer getCarbs() {
        return carbs;
    }

    public Integer getId() {
        return id;
    }

    public String getFdnm() {
        return fdnm;
    }


    public Integer getKcal() {
        return kcal;
    }


    public Integer getFats() {
        return fats;
    }

    public Integer getProt() {
        return prot;
    }

}
