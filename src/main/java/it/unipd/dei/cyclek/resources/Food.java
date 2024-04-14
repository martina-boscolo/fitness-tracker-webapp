package it.unipd.dei.cyclek.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Food  extends AbstractResource{
    private Integer id;         //food identificator
    private String fdnm;    //food name
    private int kcal;       //number of Calories for 100g of food
    private int fats;       //grams of fats for 100g of food
    private int carbs;      //grams of carbs for 100g of food
    private int prot;       //grams of prot for 100g of food
    public Food(Integer id, String fdnm, int kcal, int fats, int carbs, int prot) {
        this.id = id;
        this.fdnm = fdnm;
        this.kcal = kcal;
        this.fats = fats;
        this.carbs = carbs;
        this.prot = prot;
    }
    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        String json = new ObjectMapper()
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .writer().withRootName("food")
                .writeValueAsString(this);
        out.write(json.getBytes(StandardCharsets.UTF_8));
    }

    public int getCarbs() {
        return carbs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFdnm() {
        return fdnm;
    }

    public void setFdnm(String fdnm) {
        this.fdnm = fdnm;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }

    public int getFats() {
        return fats;
    }

    public void setFats(int fats) {
        this.fats = fats;
    }

    public int getProt() {
        return prot;
    }

    public void setProt(int prot) {
        this.prot = prot;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }}
