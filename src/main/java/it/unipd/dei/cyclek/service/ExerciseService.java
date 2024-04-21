package it.unipd.dei.cyclek.service;

import it.unipd.dei.cyclek.resources.Exercise;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.rest.ListUserRR;
import it.unipd.dei.cyclek.rest.exercise.ListExerciseRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

public class ExerciseService extends AbstractService {
    private static final String TABLE_NAME = "exercise";
    public static boolean processExercise(HttpServletRequest req, HttpServletResponse res, Connection con) throws IOException {

        final String method = req.getMethod();
        String path = req.getRequestURI();
        Message m = null;

        if (path.lastIndexOf("rest/".concat(TABLE_NAME)) <= 0)
            return false;

        path = path.substring(path.lastIndexOf(TABLE_NAME) + TABLE_NAME.length());

        if (path.isEmpty() || path.equals("/")) {
            switch (method) {
                case "GET":
                    new ListExerciseRR(req, res, con).serve();
                    break;
                case "POST":
                    throw new UnsupportedOperationException();
                default:
                    LOGGER.warn("Unsopported operation for URI /%s: %s.", TABLE_NAME, method);
            }
        }

        return true;
    }
}



