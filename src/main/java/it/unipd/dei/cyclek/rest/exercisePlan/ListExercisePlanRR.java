package it.unipd.dei.cyclek.rest.exercisePlan;

import it.unipd.dei.cyclek.dao.exercisePlan.GetExercisePlanDao;
import it.unipd.dei.cyclek.dao.exercisePlan.ListExercisePlanDao;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ListExercisePlanRR extends AbstractRR {

    public ListExercisePlanRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.LIST_ExercisePlan, req, res, con);
    }
    @Override
    protected void doServe() throws IOException {
        List<ExercisePlan> dl;
        Message m;

        try {

            ExercisePlan plan = new ExercisePlan(null,"",null);

            // creates a new DAO for accessing the database and lists the ExercisePlan(s)
            dl = new ListExercisePlanDao(con, plan).access().getOutputParam();

            if (dl != null) {
                LOGGER.info("ExercisePlan(s) successfully listed.");
                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(dl).toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Fatal error while listing ExercisePlan(s).");
                m = ErrorCode.LIST_ALL_EXERCISE_PLANS_NOT_FOUND.getMessage();
                res.setStatus(ErrorCode.LIST_ALL_EXERCISE_PLANS_NOT_FOUND.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list ExercisePlan(s): unexpected database error.", ex);
            m = ErrorCode.LIST_ALL_EXERCISE_PLANS_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.LIST_ALL_EXERCISE_PLANS_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }


}
