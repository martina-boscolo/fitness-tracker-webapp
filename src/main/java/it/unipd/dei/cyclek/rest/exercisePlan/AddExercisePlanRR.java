package it.unipd.dei.cyclek.rest.exercisePlan;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.dei.cyclek.dao.exercisePlan.AddExercisePlanDao;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.ErrorCode;
import it.unipd.dei.cyclek.resources.entity.ExercisePlan;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.AbstractRR;
import it.unipd.dei.cyclek.utils.TokenJWT;
import jakarta.servlet.http.Cookie;
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
        ExercisePlan el;
        try {
            BufferedReader reader = req.getReader();
            StringBuilder jsonBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonBody.toString());
            Integer idUser = null;
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("authToken".equals(cookie.getName())) {
                        String cookieValue = cookie.getValue();
                        // Assuming the cookie value directly contains the idUser
                        idUser = Integer.parseInt(TokenJWT.extractUserId(cookieValue));
                        break;
                    }
                }
            }

            if (idUser == null) {
                LOGGER.error("Unauthorized");
                m = ErrorCode.UNAUTHORIZED.getMessage();
                res.setStatus(ErrorCode.UNAUTHORIZED.getHttpCode());
                m.toJSON(res.getOutputStream());
                return;
            }
            String planName = rootNode.get("planName").asText();
            String planJson = rootNode.get("plan").toString();

            ExercisePlan plan = new ExercisePlan(idUser, planName, planJson);
            // Assuming you set other properties of the ExercisePlan object
            // Create a DAO for accessing the database and save the ExercisePlan
            el = new AddExercisePlanDao(con, plan).access().getOutputParam();
            if (el != null) {
                LOGGER.info("Exercise successfully Added.");
                res.setStatus(HttpServletResponse.SC_OK);
                el.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Failed to save ExercisePlan.");
                m = ErrorCode.ADD_EXERCISE_PLAN_INSERT_FAIL.getMessage();
                res.setStatus(ErrorCode.ADD_EXERCISE_PLAN_INSERT_FAIL.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot save ExercisePlan: unexpected database error.", ex);
            m = ErrorCode.ADD_EXERCISE_PLAN_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.ADD_EXERCISE_PLAN_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        } catch (NullPointerException ex) {
            LOGGER.error("Failed to save ExercisePlan.");
            m = ErrorCode.ADD_EXERCISE_PLAN_BAD_REQUEST.getMessage();
            res.setStatus(ErrorCode.ADD_EXERCISE_PLAN_BAD_REQUEST.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}