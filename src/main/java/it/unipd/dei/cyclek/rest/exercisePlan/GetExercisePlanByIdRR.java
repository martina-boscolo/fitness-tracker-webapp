package it.unipd.dei.cyclek.rest.exercisePlan;

import it.unipd.dei.cyclek.dao.exercise.GetExerciseDao;
import it.unipd.dei.cyclek.dao.exercisePlan.GetExercisePlanDao;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.ExercisePlan;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GetExercisePlanByIdRR extends AbstractRR {
    public Integer id;
    public GetExercisePlanByIdRR(HttpServletRequest req, HttpServletResponse res, Connection con , Integer id) {
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

                m = new Message("Cannot fetch exercise: unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot fetch exercisePlan: unexpected database error.", ex);

            m = new Message("Cannot fetch exercisePlan: unexpected database error.", "E5A1", "");
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }


}