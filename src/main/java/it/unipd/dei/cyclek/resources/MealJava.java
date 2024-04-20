package it.unipd.dei.cyclek.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.json.*;
import org.apache.commons.lang3.StringUtils;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MealJava{

        private Integer id_food;
        private Integer grams;

        public MealJava(Integer id_food, Integer grams) {
            this.id_food = id_food;
            this.grams = grams;
        }

        public Integer getId_food() {
            return id_food;
        }
        public void setId_food(Integer id_food) {
            this.id_food = id_food;
        }

        public Integer getGrams() {
            return grams;
        }
        public void setGrams(Integer grams) {
            this.grams = grams;
        }
}
