package it.unipd.dei.cyclek.rest.diet;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.dei.cyclek.dao.diets.UpdateDietDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.Diet;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class UpdateDietRR extends AbstractRR {

    public UpdateDietRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.UPDATE_DIET, req, res, con);
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
            int id = rootNode.get("id").asInt();
            String planName = rootNode.get("planName").asText();
            String dietJson = rootNode.get("diet").toString();
            String data = rootNode.get("dietDate").asText();
            String hour = rootNode.get("dietHour").asText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
            LocalDateTime dataOra = LocalDateTime.parse(data + " " + hour, formatter);
            LocalDateTime dataOraPiu24 = dataOra.plusHours(24);
            LocalDateTime now = LocalDateTime.now();
            if(dataOraPiu24.isBefore(now)){
                LOGGER.info("Diet cant be modified, 24 hours has passed.");
                res.setStatus(HttpServletResponse.SC_OK);
                m = new Message("Diet cant be modified, 24 hours has passed.", "S1A1", null);
                m.toJSON(res.getOutputStream());
            }
            else {
                Diet diet = new Diet(id, null, planName, dietJson, "","" );
                Boolean updated = new UpdateDietDAO(con, diet).access().getOutputParam();
                if (updated) {
                    LOGGER.info("Diet successfully updated.");
                    res.setStatus(HttpServletResponse.SC_OK);
                    m = new Message("Diet successfully updated.", "S1A1", null);
                    m.toJSON(res.getOutputStream());
                } else {
                    LOGGER.error("Failed to update diet.");
                    m = new Message("Failed to update diet.", "E5A1", null);
                    res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    m.toJSON(res.getOutputStream());
                }
            }
        } catch (IOException | SQLException ex) {
            LOGGER.error("Cannot update diet: unexpected error.", ex);
            m = new Message("Cannot update diet: unexpected error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}