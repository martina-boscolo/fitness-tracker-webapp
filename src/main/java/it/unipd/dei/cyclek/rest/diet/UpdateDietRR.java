package it.unipd.dei.cyclek.rest.diet;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.dei.cyclek.dao.diets.UpdateDietDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.entity.Diet;
import it.unipd.dei.cyclek.resources.ErrorCode;
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


public class UpdateDietRR extends AbstractRR {

    public UpdateDietRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.UPDATE_DIET, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Message m;

        try {


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

            BufferedReader reader = req.getReader();
            StringBuilder jsonBody = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBody.append(line);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonBody.toString());
            int id = rootNode.get("id").asInt();
            String planName = rootNode.get("planName").asText();
            String dietJson = rootNode.get("diet").toString();

            boolean saved = new UpdateDietDAO(con, new Diet(id, idUser, planName, dietJson, "")).access().getOutputParam();
            if (saved) {
                LOGGER.info("Diet successfully updated.");
                res.setStatus(HttpServletResponse.SC_OK);
            } else {
                LOGGER.info("Diet cant be modified, 24 hours has passed.");
                m = ErrorCode.UPDATE_DIET_CONSTRAINT_VIOLATION.getMessage();
                res.setStatus(ErrorCode.UPDATE_DIET_CONSTRAINT_VIOLATION.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (IOException | SQLException ex) {
            LOGGER.error("Cannot update diet: unexpected error.", ex);
            m = ErrorCode.UPDATE_DIET_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.UPDATE_DIET_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}