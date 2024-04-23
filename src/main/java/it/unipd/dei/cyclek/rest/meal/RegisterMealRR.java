package it.unipd.dei.cyclek.rest.meal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.dei.cyclek.dao.meal.RegisterMealDAO;
import it.unipd.dei.cyclek.resources.Actions;
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
        Message m = null;

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
                LOGGER.info("meal successfully saved.");

                res.setStatus(HttpServletResponse.SC_OK);
                m = new Message("meal successfully saved.");
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Failed to save meal.");

                m = new Message("Failed to save meal.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot save meal: unexpected database error.", ex);

            m = new Message("Cannot save meal: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}
