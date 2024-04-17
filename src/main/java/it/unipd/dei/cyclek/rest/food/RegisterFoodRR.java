package it.unipd.dei.cyclek.rest.food;

import it.unipd.dei.cyclek.dao.food.RegisterFoodDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.Food;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.postgresql.util.PSQLException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class RegisterFoodRR extends AbstractRR {
    /**
     * Creates a new REST resource.
     *
     * @param req    the HTTP request.
     * @param res    the HTTP response.
     * @param con    the connection to the database.
     */
    public RegisterFoodRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.REGISTER_FOOD, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Food f ;
        Message m;
        try {
            final Food food = Food.fromJSON(req.getInputStream());
            f = new RegisterFoodDAO(con, food).access().getOutputParam();
            if (f != null) {
                LOGGER.info("User(s) successfully added to database.");
                res.setStatus(HttpServletResponse.SC_OK);

            } else { // it should not happen
                LOGGER.error("Fatal error while adding user(s).");
                m = new Message("Cannot add user(s): unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }

        } catch (PSQLException ex) {
            LOGGER.error("Cannot add the user(s): duplicate usernames.", ex);
            m = new Message("Cannot add user(s): duplicate usernames.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
        catch (SQLException ex) {
            LOGGER.error("Cannot add the user(s): unexpected database error.", ex);
            m = new Message("Cannot add user(s): unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}
