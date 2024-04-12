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

    private final Integer id;
    private final String name;
    private final String surname;
    private final String birthday;
    private final String gender;

    public GetUserDAO(Connection con, User user) {
        super(con);
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.birthday = user.getBirthday();
        this.gender = user.getGender();
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // the results of the search
        final List<User> users = new ArrayList<>();

        try {
            StringBuilder sb = new StringBuilder(QUERY);

            if (id != null)
                sb.append("and id = ").append(id);
            if (!name.isEmpty())
                sb.append("and name = ").append(name);
            if (!surname.isEmpty())
                sb.append("and surname = ").append(surname);
            if (!birthday.isEmpty())
                sb.append("and birthday = ").append(birthday);
            if (!gender.isEmpty())
                sb.append("and gender = ").append(gender);


            pstmt = con.prepareStatement(sb.toString());


            rs = pstmt.executeQuery();

            while (rs.next())
            {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("birthday"),
                        rs.getString("gender")
                ));
            }

            LOGGER.info("Users successfully fetched.");
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        this.outputParam = users;
    }
}
