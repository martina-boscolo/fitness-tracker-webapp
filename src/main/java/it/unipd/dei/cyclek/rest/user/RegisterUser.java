package it.unipd.dei.cyclek.rest.user;

import it.unipd.dei.cyclek.resources.Actions;
import it.unipd.dei.cyclek.resources.Message;
import it.unipd.dei.cyclek.resources.User;
import it.unipd.dei.cyclek.rest.AbstractRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Connection;

public class RegisterUser extends AbstractRR {

    public RegisterUser(HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.LIST_USER, req, res, con);
    }
    @Override
    protected void doServe() {
        User u ;
        Message m;

        //TODO implemnts fromJson in AbstractResource
        // https://bitbucket.org/frrncl/tutor-2023/src/master/group_creation_rest_17042023/src/main/java/GroupCreation/rest/AddStudentToGroupRR.java
    }
}
