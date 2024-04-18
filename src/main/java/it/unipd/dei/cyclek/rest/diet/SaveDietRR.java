package it.unipd.dei.cyclek.rest.diet;

import it.unipd.dei.cyclek.dao.diets.SaveDietDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.Diet;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SaveDietRR extends AbstractRR {

        public SaveDietRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
            super(Actions.SAVE_DIET, req, res, con);
        }

        @Override
        protected void doServe() throws IOException {
            Message m = null;

            try {
                // Retrieve diet data from request parameters or body
                String idUser = req.getParameter("idUser");
                

                // Assuming you have more parameters to retrieve

                // Create a new Diet object with the retrieved data
                Diet diet = new Diet(null, idUser, , null);
                // Assuming you set other properties of the Diet object

                // Create a DAO for accessing the database and save the diet
                boolean saved = new SaveDietDAO(con, diet).access().getOutputParam();

                if (saved) {
                    LOGGER.info("Diet successfully saved.");

                    res.setStatus(HttpServletResponse.SC_OK);
                    m = new Message("Diet successfully saved.", "S1A1", null);
                    m.toJSON(res.getOutputStream());
                } else {
                    LOGGER.error("Failed to save diet.");

                    m = new Message("Failed to save diet.", "E5A1", null);
                    res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    m.toJSON(res.getOutputStream());
                }
            } catch (SQLException ex) {
                LOGGER.error("Cannot save diet: unexpected database error.", ex);

                m = new Message("Cannot save diet: unexpected database error.", "E5A1", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        }
}
