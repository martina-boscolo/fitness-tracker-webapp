package it.unipd.dei.cyclek.rest.exercisePlan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.dei.cyclek.dao.exercisePlan.AddExercisePlanDao;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.ErrorCode;
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
            } else {
                LOGGER.error("Failed to save ExercisePlan.");
                m = ErrorCode.ADD_EXERCISE_PLAN_BAD_REQUEST.getMessage();
                res.setStatus(ErrorCode.ADD_EXERCISE_PLAN_BAD_REQUEST.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot save ExercisePlan: unexpected database error.", ex);
            m = ErrorCode.ADD_EXERCISE_PLAN_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.ADD_EXERCISE_PLAN_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}