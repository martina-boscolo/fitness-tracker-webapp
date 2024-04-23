package it.unipd.dei.cyclek.rest.userGoals;

import it.unipd.dei.cyclek.dao.userGoals.CreateUserGoalDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.UserGoals;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.EOFException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class CreateUserGoalsRR extends AbstractRR {
    public CreateUserGoalsRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.CREATE_BODY_OBJ, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        UserGoals bs = null;
        Message m = null;

        try {
            final UserGoals userGoals = UserGoals.fromJSON(req.getInputStream());

            // creates a new DAO for accessing the database and lists the employee(s)
            bs = new CreateUserGoalDAO(con, userGoals).access().getOutputParam();

            if (bs != null) {
                LOGGER.info("Body Obj successfully created.");

                res.setStatus(HttpServletResponse.SC_CREATED);
                bs.toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while creating Body Obj.");

                m = new Message("Cannot create Body Obj: unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (EOFException ex) {
            LOGGER.warn("Cannot create the Body Obj: no Body Obj JSON object found in the request.", ex);

            m = new Message("Cannot create the Body Obj: no Body Obj JSON object found in the request.", "E4A8",
                    ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot create the Body Obj: it already exists.");

                m = new Message("Cannot create the Body Obj: it already exists.", "E5A2", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot create the Body Obj: unexpected database error.", ex);

                m = new Message("Cannot create the Body Obj: unexpected database error.", "E5A1", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (NullPointerException ex) {
            LOGGER.error("Body must contains all parameters (idUser,weight,height,fatty,lean,ObjDate).", ex);

            m = new Message("Body must contains all parameters (idUser,weight,height,fatty,lean,ObjDate).", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        }
    }
}
