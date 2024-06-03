package it.unipd.dei.cyclek.rest.exercisePlan;

import it.unipd.dei.cyclek.dao.exercisePlan.GetExercisePlanDao;
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

public class GetExercisePlanByIdRR extends AbstractRR {
    public Integer id;

    public GetExercisePlanByIdRR(HttpServletRequest req, HttpServletResponse res, Connection con, Integer id) {
        super(Actions.Get_ExercisePlan_Id, req, res, con);
        this.id = id;
    }

    @Override
    protected void doServe() throws IOException {
        ExercisePlan el;
        Message m;

        try {
            el = new GetExercisePlanDao(con, id).access().getOutputParam();
            if (el != null) {
                LOGGER.info("Exercise successfully fetch.");
                res.setStatus(HttpServletResponse.SC_OK);
                el.toJSON(res.getOutputStream());
            } else { // it should not happen
                LOGGER.error("Fatal error while fetching exercise.");
                m = ErrorCode.ID_EXERCISE_PLAN_NOT_FOUND.getMessage();
                res.setStatus(ErrorCode.ID_EXERCISE_PLAN_NOT_FOUND.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot fetch exercisePlan: unexpected database error.", ex);
            m = ErrorCode.GET_EXERCISE_PLAN_INTERNAL_ERROR.getMessage();
            res.setStatus(ErrorCode.GET_EXERCISE_PLAN_INTERNAL_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }


}