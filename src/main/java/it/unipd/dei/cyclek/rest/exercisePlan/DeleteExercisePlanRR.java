package it.unipd.dei.cyclek.rest.exercisePlan;

import it.unipd.dei.cyclek.dao.exercisePlan.DeleteExercisePlanDao;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.ErrorCode;
import it.unipd.dei.cyclek.resources.entity.ExercisePlan;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DeleteExercisePlanRR extends AbstractRR {
    public Integer id;
    public DeleteExercisePlanRR(HttpServletRequest req, HttpServletResponse res, Connection con , Integer id) {
        super(Actions.DELETE_Exercise_Plan, req, res, con);
        this.id = id;
    }

    @Override
    protected void doServe() throws IOException {
        ExercisePlan el;
        Message m;

        try {

            el = new DeleteExercisePlanDao(con, id).access().getOutputParam();


            if (el != null) {
                LOGGER.info("Exercise successfully deleted.");
                res.setStatus(HttpServletResponse.SC_OK);
                el.toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while deleting exercise.");
                m = ErrorCode.DELETE_EXERCISE_PLAN_BAD_REQUEST.getMessage();
                res.setStatus(ErrorCode.DELETE_EXERCISE_PLAN_BAD_REQUEST.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot delete exercisePlan: unexpected database error.", ex);
            m = ErrorCode.DELETE_EXERCISE_PLAN_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.DELETE_EXERCISE_PLAN_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }


}