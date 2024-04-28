package it.unipd.dei.cyclek.rest.food;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.dei.cyclek.dao.food.RegisterFoodDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.ErrorCode;
import it.unipd.dei.cyclek.resources.entity.Food;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class RegisterFoodRR extends AbstractRR {
    public RegisterFoodRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.REGISTER_FOOD, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Food f ;
        Message m;
        try {

            BufferedReader reader = req.getReader();
            StringBuilder jsonBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonBody.toString());

            String fdnm = rootNode.get("fdnm").textValue();

            int kcal = rootNode.get("kcal").asInt();

            int fats = rootNode.get("fats").asInt();

            int carbohydrates = rootNode.get("carbohydrates").asInt();

            int proteins = rootNode.get("proteins").asInt();

            Food food = new Food(fdnm, kcal, fats, carbohydrates, proteins);

            f = new RegisterFoodDAO(con, food).access().getOutputParam();

            if (f != null) {
                LOGGER.info("Food(s) successfully added to database.");
                res.setStatus(HttpServletResponse.SC_CREATED);

            } else {
                LOGGER.error("Failed to save food.");
                m = ErrorCode.REGISTER_FOOD_BAD_REQUEST.getMessage();
                res.setStatus(ErrorCode.REGISTER_FOOD_BAD_REQUEST.getHttpCode());
                m.toJSON(res.getOutputStream());
            }

        } catch (SQLException ex) {
            LOGGER.error("Cannot save food: unexpected database error.", ex);
            m = ErrorCode.REGISTER_FOOD_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.REGISTER_FOOD_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}
