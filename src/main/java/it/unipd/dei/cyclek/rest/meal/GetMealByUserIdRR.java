package it.unipd.dei.cyclek.rest.meal;

import it.unipd.dei.cyclek.dao.meal.GetMealDao;
import it.unipd.dei.cyclek.resources.*;
import it.unipd.dei.cyclek.resources.entity.Meal;
import it.unipd.dei.cyclek.rest.AbstractRR;
import it.unipd.dei.cyclek.utils.TokenJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class GetMealByUserIdRR extends AbstractRR {

    public GetMealByUserIdRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.GET_MEAL_USER_ID, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
        List<Meal> ml;
        Message m;

        try {
            Integer idUser = null;
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("authToken".equals(cookie.getName())) {
                        String cookieValue = cookie.getValue();
                        // Assuming the cookie value directly contains the idUser
                        idUser = Integer.parseInt(TokenJWT.extractUserId(cookieValue));
                        LOGGER.info("idUser: " + idUser);
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
            LOGGER.info("%%%%%%%%%%%%%%%%%%%%% "+idUser+" %%%%%%%%%%%%%%%%%%%%%");
            LOGGER.info("authorized");

            ml = new GetMealDao(con, new Meal(idUser)).access().getOutputParam();

            if (ml != null && !ml.isEmpty()) {
                LOGGER.info("meal(s) successfully listed.");
                for (Meal meal : ml) {
                    LOGGER.info("%%%%%%%%%%%%%%%%%%%%% " + meal.getMeal() + " %%%%%%%%%%%%%%%%%%%%%");
                }
                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(ml).toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Fatal error while listing meal(s).");
                m = ErrorCode.ID_USER_MEAL_NOT_FOUND.getMessage();
                res.setStatus(ErrorCode.ID_USER_MEAL_NOT_FOUND.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot list food(s): unexpected database error.", ex);
            m = ErrorCode.GET_ID_USER_FOOD_INTERNAL_SERVER_ERROR.getMessage();
            res.setStatus(ErrorCode.GET_ID_USER_FOOD_INTERNAL_SERVER_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}