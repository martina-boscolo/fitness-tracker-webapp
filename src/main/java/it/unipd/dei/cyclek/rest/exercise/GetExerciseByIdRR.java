package it.unipd.dei.cyclek.rest.exercise;

import it.unipd.dei.cyclek.dao.exercise.GetExerciseDao;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GetExerciseByIdRR extends AbstractRR {
    public Integer exerciseId;
    public GetExerciseByIdRR(HttpServletRequest req, HttpServletResponse res, Connection con, Integer id) {
        super(Actions.GET_Exercise_Id, req, res, con);
        this.exerciseId = id;
    }

    @Override
    protected void doServe() throws IOException {

        Exercise el = null;
        Message m = null;

        try {
            // creates a new DAO for accessing the database and lists the exercise(s)
            el = new GetExerciseDao(con, exerciseId).access().getOutputParam();

            if (el != null) {
                LOGGER.info("Exercise successfully fetch.");
                res.setStatus(HttpServletResponse.SC_OK);
                el.toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while fetching exercise.");
                m = ErrorCode.ID_EXERCISE_NOT_FOUND.getMessage();
                res.setStatus(ErrorCode.ID_EXERCISE_NOT_FOUND.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot fetch exercise: unexpected database error.", ex);
            m = ErrorCode.GET_EXERCISE_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.GET_EXERCISE_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }


}