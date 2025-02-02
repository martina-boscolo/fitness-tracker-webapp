package it.unipd.dei.cyclek.servlet;

import it.unipd.dei.cyclek.resources.ErrorCode;
import it.unipd.dei.cyclek.resources.LogContext;
import it.unipd.dei.cyclek.resources.Message;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;

import static it.unipd.dei.cyclek.service.DietService.processDiet;
import static it.unipd.dei.cyclek.service.ExercisePlanService.processExercisePlan;
import static it.unipd.dei.cyclek.service.ExerciseService.processExercise;
import static it.unipd.dei.cyclek.service.FoodService.processFood;
import static it.unipd.dei.cyclek.service.MealService.processMeal;
import static it.unipd.dei.cyclek.service.PostService.processPost;
import static it.unipd.dei.cyclek.service.UserGoalsService.processUserGoals;
import static it.unipd.dei.cyclek.service.UserService.processUser;
import static it.unipd.dei.cyclek.service.UserStatsService.processUserStats;


public class RestDispatcherServlet extends AbstractDatabaseServlet {
    private static final String JSON_UTF_8_MEDIA_TYPE = "application/json; charset=utf-8";

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse res) throws IOException {

        LogContext.setIPAddress(req.getRemoteAddr());

        final OutputStream out = res.getOutputStream();

        try {
            Connection con = getConnection();

            if (processUser(req, res, con)) return;

            if (processUserStats(req, res, con)) return;

            if (processUserGoals(req, res, con)) return;

            if (processDiet(req, res, con)) return;

            if (processPost(req, res, con)) return;

            if (processFood(req, res, con)) return;

            if (processMeal(req, res, con)) return;

            if (processExercise(req, res, con)) return;

            if (processExercisePlan(req, res, con)) return;


            // if none of the above process methods succeeds, it means an unknown resource has been requested
            LOGGER.warn("Unknown resource requested: %s.", req.getRequestURI());
            final Message m = ErrorCode.REST_NOT_FOUND.getMessage();
            res.setStatus(ErrorCode.REST_NOT_FOUND.getHttpCode());
            res.setContentType(JSON_UTF_8_MEDIA_TYPE);
            m.toJSON(out);
        } catch (Throwable t) {
            LOGGER.error("Unexpected error while processing the REST resource.", t);
            final Message m = ErrorCode.INTERNAL_ERROR.getMessage();
            res.setStatus(ErrorCode.INTERNAL_ERROR.getHttpCode());
            m.toJSON(out);
        } finally {

            // ensure to always flush and close the output stream
            if (out != null) {
                out.flush();
                out.close();
            }

            LogContext.removeIPAddress();
        }
    }
}
