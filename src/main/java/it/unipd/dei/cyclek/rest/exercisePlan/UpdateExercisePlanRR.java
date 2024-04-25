package it.unipd.dei.cyclek.rest.exercisePlan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.dei.cyclek.dao.exercisePlan.UpdateExercisePlanDao;
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

public class UpdateExercisePlanRR extends AbstractRR {
    public UpdateExercisePlanRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.Update_Exercise_Plan, req, res, con);
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
            int id = rootNode.get("id").asInt();
            int idUser = rootNode.get("idUser").asInt();
            String planName = rootNode.get("planName").asText();
            String planJson = rootNode.get("plan").toString();



            boolean saved = new UpdateExercisePlanDao(con, new ExercisePlan(id, idUser, planName, planJson, "")).access().getOutputParam();
            if (saved) {
                LOGGER.info("ExercisePlan successfully updated.");
                res.setStatus(HttpServletResponse.SC_OK);
                m = new Message("ExercisePlan successfully updated.");
                m.toJSON(res.getOutputStream());
            }
        } catch (IOException | SQLException ex) {
            LOGGER.error("Cannot update ExercisePlan: unexpected error.", ex);
            m = new Message("Cannot update ExercisePlan: unexpected error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }

}
