package it.unipd.dei.cyclek.dao.user;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class GetUserDAO extends AbstractDAO<List<User>>{
    private static final String QUERY = "SELECT * FROM users WHERE 1=1";

    private final User user;

    public GetUserDAO(Connection con, User user) {
        super(con);
        this.user = user;
    }

    @Override
    protected final void doAccess() throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // the results of the search
        final List<User> users = new ArrayList<>();

        try {
            StringBuilder sb = new StringBuilder(QUERY);

            if (this.user.getId() != null)
                sb.append("and id = ").append(this.user.getId());
            if (!this.user.getName().isEmpty())
                sb.append("and name = ").append(this.user.getName());
            if (!this.user.getSurname().isEmpty())
                sb.append("and surname = ").append(this.user.getSurname());
            if (!this.user.getBirthday().isEmpty())
                sb.append("and birthday = ").append(this.user.getBirthday());
            if (!this.user.getGender().isEmpty())
                sb.append("and gender = ").append(this.user.getGender());

            pstmt = con.prepareStatement(sb.toString());
            rs = pstmt.executeQuery();

            while (rs.next())
            {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("birthday"),
                        rs.getString("gender"),
                        rs.getString("username"),
                        rs.getString("password")
                ));
            }

            LOGGER.info("Users successfully fetched.");
        } finally {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
        }
        this.outputParam = users;
    }
}
