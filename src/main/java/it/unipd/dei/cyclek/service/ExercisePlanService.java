package it.unipd.dei.cyclek.service;

import it.unipd.dei.cyclek.resources.ExercisePlan;
import it.unipd.dei.cyclek.rest.exercise.GetExerciseByIdRR;
import it.unipd.dei.cyclek.rest.exercisePlan.AddExercisePlanRR;
import it.unipd.dei.cyclek.rest.exercisePlan.DeleteExercisePlanRR;
import it.unipd.dei.cyclek.rest.exercisePlan.GetExercisePlanByIdRR;
import it.unipd.dei.cyclek.rest.exercisePlan.ListExercisePlanRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExercisePlanService extends AbstractService {

    private static final String TABLE_NAME = "exercise_plan";

    public static boolean processExercisePlan(HttpServletRequest req, HttpServletResponse res, Connection con) throws IOException {
        final String method = req.getMethod();
        String path = req.getRequestURI();
        // the requested resource was not a ExercisePlan
        if (path.lastIndexOf("rest/".concat(TABLE_NAME)) <= 0)
            return false;

        path = path.substring(path.lastIndexOf(TABLE_NAME) + TABLE_NAME.length());
        String regex = ".*/(\\d+)$";
        // Compile the regex pattern
        Pattern pattern = Pattern.compile(regex);

        if (path.isEmpty() || path.equals("/")) {
            switch (method) {
                case "GET":
                    new ListExercisePlanRR(req, res, con).serve();
                    break;
                case "POST":
                    new AddExercisePlanRR(req, res, con).serve();
                    break;
                default:
                    LOGGER.warn("Unsopported operation for URI /%s: %s.", TABLE_NAME, method);
            }
        } else if (method.equals("GET") && pattern.matcher(path).matches()) {
            Matcher matcher = pattern.matcher(path);
            if (matcher.find()) {
                // Extract the ID from the matched group
                String id = matcher.group(1);
                new GetExercisePlanByIdRR(req, res, con, Integer.parseInt(id)).serve();
            }
        }else if (method.equals("DELETE") && pattern.matcher(path).matches()) {
            Matcher matcher = pattern.matcher(path);
            if (matcher.find()) {
                // Extract the ID from the matched group
                String id = matcher.group(1);
                new DeleteExercisePlanRR(req, res, con, Integer.parseInt(id)).serve();
            }
        }else {
            LOGGER.warn("Unsopported operation for URI /%s: %s.", TABLE_NAME, method);
            throw new UnsupportedOperationException();
        }

        return true;
    }
}


