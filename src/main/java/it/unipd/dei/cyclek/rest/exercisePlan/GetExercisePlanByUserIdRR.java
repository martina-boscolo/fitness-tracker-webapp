package it.unipd.dei.cyclek.rest.exercisePlan;

import it.unipd.dei.cyclek.dao.exercisePlan.GetExercisePlanByUserIdDao;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.resources.entity.ExercisePlan;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GetExercisePlanByUserIdRR extends AbstractRR {

    public Integer idUser;
    public GetExercisePlanByUserIdRR(HttpServletRequest req, HttpServletResponse res, Connection con, Integer idUser) {
        super(Actions.Get_ExercisePlan_UserId, req, res, con);
        this.idUser = idUser;
    }

    @Override
    protected void doServe() throws IOException {
        Message m;
        List<ExercisePlan> dl = null;
        try {

            dl = new GetExercisePlanByUserIdDao(con, idUser).access().getOutputParam();

            if (dl != null) {
                LOGGER.info("exercisePlan successfully fetch.");
                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(dl).toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Fatal error while listing exercisePlan(s).");
                m = ErrorCode.ID_USER_EXERCISE_PLAN_NOT_FOUND.getMessage();
                res.setStatus(ErrorCode.ID_USER_EXERCISE_PLAN_NOT_FOUND.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list exercisePlan(s): unexpected database error.", ex);
            m = ErrorCode.GET_ID_USER_EXERCISE_PLAN_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.GET_ID_USER_EXERCISE_PLAN_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}