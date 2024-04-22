package it.unipd.dei.cyclek.rest.userExercise;

import it.unipd.dei.cyclek.dao.exercise.GetExerciseDao;
import it.unipd.dei.cyclek.dao.userExercise.AddUserExerciseDao;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.LogContext;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.UserExercise;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.EOFException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class AddUserExerciseRR extends AbstractRR {
    public AddUserExerciseRR(final HttpServletRequest req , final HttpServletResponse res , final Connection con) {
        super(Actions.Add_User_Exercise,req,res,con);
    }

    @Override
    protected void doServe() throws IOException {
        UserExercise u = null;
        Message m = null;

        try {
            UserExercise userExercise = UserExercise.fromJSON(req.getInputStream());
            u = new AddUserExerciseDao(con,userExercise).access().getOutputParam();
            if (u != null) {
                LOGGER.info("user's exercise successfully added.");
                res.setStatus(HttpServletResponse.SC_CREATED);
                u.toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while adding user's exercise");
                m = new Message("Cannot add user's exercises: unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (EOFException ex) {
            LOGGER.warn("Cannot add user's exercises: no user's exercises JSON object found in the request.", ex);
            m = new Message("Cannot add the user's exercises: no user's exercises JSON object found in the request.", "E4A8",
                    ex.getMessage());
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(res.getOutputStream());
        } catch (SQLException ex) {
            if ("23505".equals(ex.getSQLState())) {
                LOGGER.warn("Cannot add user's exercises: it already exists.");
                m = new Message("Cannot add user's exercises: it already exists.", "E5A2", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_CONFLICT);
                m.toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Cannot add user's exercises: unexpected database error.", ex);
                m = new Message("Cannot add user's exercises: unexpected database error.", "E5A1", ex.getMessage());
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        }
    }
}
