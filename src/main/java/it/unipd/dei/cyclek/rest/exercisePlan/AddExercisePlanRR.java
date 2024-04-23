package it.unipd.dei.cyclek.rest.exercisePlan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.dei.cyclek.dao.exercisePlan.AddExercisePlanDao;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.ExercisePlan;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class AddExercisePlanRR extends AbstractRR {

    public AddExercisePlanRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.ADD_Exercise_Plan, req, res, con);
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
            String planName = rootNode.get("planName").asText();
            String planJson = rootNode.get("plan").toString();

            ExercisePlan plan = new ExercisePlan(idUser, planName, planJson);
            // Assuming you set other properties of the ExercisePlan object
            // Create a DAO for accessing the database and save the ExercisePlan
            boolean saved = new AddExercisePlanDao(con, plan).access().getOutputParam();
            if (saved) {
                LOGGER.info("ExercisePlan successfully saved.");
                res.setStatus(HttpServletResponse.SC_OK);
                m = new Message("ExercisePlan successfully saved.", "S1A1", null);
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Failed to save ExercisePlan.");
                m = new Message("Failed to save ExercisePlan.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot save ExercisePlan: unexpected database error.", ex);
            m = new Message("Cannot save ExercisePlan: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}