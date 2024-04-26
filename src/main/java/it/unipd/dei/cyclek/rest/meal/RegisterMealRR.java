package it.unipd.dei.cyclek.rest.meal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.dei.cyclek.dao.meal.RegisterMealDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.ErrorCode;
import it.unipd.dei.cyclek.resources.Meal;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RegisterMealRR extends AbstractRR {

    public RegisterMealRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.SAVE_MEAL, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
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
            int idUser = rootNode.get("idUser").asInt();

            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateAsString = today.format(formatter);

            int mealType = rootNode.get("mealType").asInt();
            String mealJson = rootNode.get("meal").toString();

            Meal meal = new Meal(idUser, dateAsString, mealType, mealJson);

            boolean saved = new RegisterMealDAO(con, meal).access().getOutputParam();

            if (saved) {
                LOGGER.info("Meal successfully saved.");
                res.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                LOGGER.error("Failed to save meal.");
                m = ErrorCode.SAVE_MEAL_BAD_REQUEST.getMessage();
                res.setStatus(ErrorCode.SAVE_MEAL_BAD_REQUEST.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot save meal: unexpected database error.", ex);
            m = ErrorCode.SAVE_MEAL_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.SAVE_MEAL_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}
