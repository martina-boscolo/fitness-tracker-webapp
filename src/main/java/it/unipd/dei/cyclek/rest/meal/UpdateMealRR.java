package it.unipd.dei.cyclek.rest.meal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unipd.dei.cyclek.dao.meal.RegisterMealDAO;
import it.unipd.dei.cyclek.dao.meal.UpdateMealDao;
import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.ErrorCode;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.entity.Meal;
import it.unipd.dei.cyclek.resources.entity.User;
import it.unipd.dei.cyclek.rest.AbstractRR;
import it.unipd.dei.cyclek.utils.TokenJWT;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UpdateMealRR extends AbstractRR {

    public UpdateMealRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.SAVE_MEAL, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {
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

            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateAsString = today.format(formatter);
            LOGGER.info("data fetched");

             Meal meal = Meal.fromJSON(req.getInputStream());
            meal.setIdUser(idUser);
            meal.setDate(dateAsString);
            boolean saved = new UpdateMealDao(con, meal).access().getOutputParam();

            if (saved) {
                LOGGER.info("Meal successfully updated.");
                res.setStatus(HttpServletResponse.SC_CREATED);
            } else {
                LOGGER.error("Failed to update meal.");
                m = ErrorCode.SAVE_MEAL_BAD_REQUEST.getMessage();
                res.setStatus(ErrorCode.SAVE_MEAL_BAD_REQUEST.getHttpCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error("Cannot update user(s): unexpected database error.", ex);
            m = ErrorCode.UPDATE_USER_DB_ERROR.getMessage();
            res.setStatus(ErrorCode.UPDATE_USER_DB_ERROR.getHttpCode());
            m.toJSON(res.getOutputStream());
        }
    }
}
