package it.unipd.dei.cyclek.service;

import it.unipd.dei.cyclek.resources.Exercise;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.ListUserRR;
import it.unipd.dei.cyclek.rest.exercise.GetExerciseByIdRR;
import it.unipd.dei.cyclek.rest.exercise.ListExerciseRR;
import it.unipd.dei.cyclek.rest.userExercise.AddUserExerciseRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserExerciseService extends AbstractService {
    private static final String UserExercise = "user-exercises";
    public static boolean processUserExercise(HttpServletRequest req, HttpServletResponse res, Connection con) throws IOException {

        final String method = req.getMethod();
        String path = req.getRequestURI();
        Message m = null;

        if (path.lastIndexOf("rest/".concat(UserExercise)) <= 0)
            return false;

        path = path.substring(path.lastIndexOf(UserExercise) + UserExercise.length());

        String regex = ".*/(\\d+)$";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(regex);

        // Match the input against the pattern
        if (path.isEmpty() || path.equals("/")) {
            switch (method) {
                case "GET":
                    throw new UnsupportedOperationException();
                case "POST":
                    new AddUserExerciseRR(req, res, con).serve();
                    break;
                default:
            }
        } else if(method.equals("GET") && pattern.matcher(path).matches()){
            Matcher matcher = pattern.matcher(path);
            if (matcher.find()) {
                // Extract the ID from the matched group
                String id = matcher.group(1);
                new GetExerciseByIdRR(req, res, con, Integer.parseInt(id)).serve();
            }
        }
        else{
            LOGGER.warn("Unsopported operation for URI /%s: %s.", UserExercise, method);
            throw new UnsupportedOperationException();
        }

        return true;
    }
}



