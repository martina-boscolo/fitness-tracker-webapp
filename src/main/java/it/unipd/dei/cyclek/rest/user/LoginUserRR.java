package it.unipd.dei.cyclek.rest.user;

import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

public class LoginUserRR extends AbstractRR {

    public LoginUserRR(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.ADD_USER, req, res, con);
    }

    @Override
    protected void doServe() throws IOException {

    }
}
