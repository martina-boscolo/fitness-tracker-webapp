package it.unipd.dei.cyclek.dao.user;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.User;

public class LoginUserDAO extends AbstractDAO<User> {

    private static final String  QUERY = "SELECT id, username, password FROM users WHERE username=? AND password=?;";
    private final User user;

    public LoginUserDAO(final Connection con, final User user) {
        super(con);
        this.user = user;

    }
    @Override
    protected void doAccess() throws Exception {
        PreparedStatement stmnt = null;
        ResultSet rs = null;

        User userResult = null;
        try {
            stmnt = con.prepareStatement(QUERY);
            stmnt.setString(1, user.getUsername());
            stmnt.setString(2, user.getPassword());
            rs = stmnt.executeQuery();

            if(rs.next()) {
                userResult = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                LOGGER.info("User logged in {}.", userResult.getUsername());
            } else
                LOGGER.error("error logging in the user {}",user.getUsername());
        } finally {
            if (rs != null)
                rs.close();
            if (stmnt != null)
                stmnt.close();
        }
        this.outputParam = userResult;
    }

}
