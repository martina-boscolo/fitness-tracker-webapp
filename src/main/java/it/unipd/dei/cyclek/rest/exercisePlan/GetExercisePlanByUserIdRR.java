package it.unipd.dei.cyclek.rest.exercisePlan;

import it.unipd.dei.cyclek.dao.exercisePlan.GetExercisePlanByUserIdDao;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.resources.entity.ExercisePlan;
import it.unipd.dei.cyclek.rest.AbstractRR;
import it.unipd.dei.cyclek.utils.TokenJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Connection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GetExercisePlanByUserIdRR extends AbstractRR {


    public GetExercisePlanByUserIdRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.Get_ExercisePlan_UserId, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        Message m;
        List<ExercisePlan> dl = null;
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
        } catch (Exception ex) {
            LOGGER.error("Cannot list exercisePlan(s): unexpected database error.", ex);

            m = ErrorCode.GET_ID_USER_EXERCISE_PLAN_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.GET_ID_USER_EXERCISE_PLAN_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}