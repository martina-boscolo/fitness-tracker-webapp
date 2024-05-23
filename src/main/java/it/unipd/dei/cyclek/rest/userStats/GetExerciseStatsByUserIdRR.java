package it.unipd.dei.cyclek.rest.userStats;

import it.unipd.dei.cyclek.dao.userStats.GetExercisesAndPlansByUserIdDAO;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.ErrorCode;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.entity.Exercise;
import it.unipd.dei.cyclek.resources.entity.ExercisePlan;
import it.unipd.dei.cyclek.resources.stats.ExercisePlanStats;
import it.unipd.dei.cyclek.resources.stats.FoodStats;
import it.unipd.dei.cyclek.resources.stats.PlanAdapter;
import it.unipd.dei.cyclek.rest.AbstractRR;
import it.unipd.dei.cyclek.utils.TokenJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetExerciseStatsByUserIdRR extends AbstractRR {

    public GetExerciseStatsByUserIdRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.GET_MEALS_BY_IDUSER, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<ExercisePlan> plans = null;
        Message m = null;

        try {

            Integer idUser = null;
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("authToken".equals(cookie.getName())) {
                        String cookieValue = cookie.getValue();
                        // Assuming the cookie value directly contains the idUser
                        idUser = Integer.parseInt(TokenJWT.extractUserId(cookieValue));
                        break;
                    }
                }
            }

            if (idUser == null) {
                LOGGER.error("Unauthorized");
                m = ErrorCode.UNAUTHORIZED.getMessage();
                res.setStatus(ErrorCode.UNAUTHORIZED.getHttpCode());
                m.toJSON(res.getOutputStream());
                return;
            }

            // creates a new DAO for accessing the database and lists the employee(s)
            plans = new GetExercisesAndPlansByUserIdDAO(con, idUser).access().getOutputParam();

            if (plans == null) {
                LOGGER.error("No stats found for searched user.");
                m = ErrorCode.GET_MEALS_NOT_FOUND.getMessage();
                res.setStatus(ErrorCode.GET_MEALS_NOT_FOUND.getHttpCode());
                m.toJSON(res.getOutputStream());
                return;
            }

            LOGGER.info("Plans successfully listed.");

            Integer userId = plans.get(0).getIdUser();
            Integer bestEx = -1;
            Double bestExWeight = Double.NEGATIVE_INFINITY;
            Integer worstEx = -1;
            Double worstExWeight = Double.POSITIVE_INFINITY;
            Map<Integer,Integer> favourite = new HashMap<>();

            for (ExercisePlan plan : plans) {
                // Map contains <day, list of exercises>
                Map<String, ArrayList<PlanAdapter.ExerciseDetail>> exerciseDetailList = new PlanAdapter(plan.getPlan()).fromJSON();
                for (Map.Entry<String, ArrayList<PlanAdapter.ExerciseDetail>> entry : exerciseDetailList.entrySet()) {
                    for (PlanAdapter.ExerciseDetail ed : entry.getValue()) {
                        favourite.put(ed.getIdExercise(), favourite.getOrDefault(ed.getIdExercise(),0)+1);
                        if (ed.getWeight() > bestExWeight) {
                            bestEx = ed.getIdExercise();
                            bestExWeight = (double) ed.getWeight();
                        }
                        if (ed.getWeight() < worstExWeight) {
                            worstEx = ed.getIdExercise();
                            worstExWeight = (double) ed.getWeight();
                        }
                    }
                }
            }
            int favoriteExerciseId = -1;
            int favoriteExCount = 0;
            for (Map.Entry<Integer, Integer> entry : favourite.entrySet()) {
                if (entry.getValue() > favoriteExCount) {
                    favoriteExCount = entry.getValue();
                    favoriteExerciseId = entry.getKey();
                }
            }

            res.setStatus(HttpServletResponse.SC_OK);
            new ExercisePlanStats(bestEx,bestExWeight,worstEx,worstExWeight,favoriteExerciseId,favoriteExCount).toJSON(res.getOutputStream());

        } catch (SQLException ex) {
            LOGGER.error("Cannot list Body Stats: unexpected database error.", ex);
            m = ErrorCode.GET_MEALS_FOOD_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.GET_MEALS_FOOD_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}