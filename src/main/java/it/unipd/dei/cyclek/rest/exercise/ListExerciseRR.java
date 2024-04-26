package it.unipd.dei.cyclek.rest.exercise;

import it.unipd.dei.cyclek.dao.exercise.ListAllExercisesDao;
import it.unipd.dei.cyclek.dao.user.GetUserDAO;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListExerciseRR extends AbstractRR {
    public ListExerciseRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.LIST_Exercise, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {

        List<Exercise> ul = null;
        Message m = null;

        try {
            // creates a new DAO for accessing the database and lists the exercises(s)
            ul = new ListAllExercisesDao(con).access().getOutputParam();

            if (ul != null) {
                LOGGER.info("Exercises successfully listed.");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(ul).toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while listing Exercises.");

                m = new Message("Cannot list Exercises: unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list Exercises: unexpected database error.", ex);

            m = new Message("Cannot list Exercises: unexpected database error.", "E5A1", ex.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }
}