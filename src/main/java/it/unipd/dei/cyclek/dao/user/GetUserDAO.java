package it.unipd.dei.cyclek.dao.user;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.commons.lang3.StringUtils;
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
    protected void doAccess() throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // the results of the search
        final List<User> users = new ArrayList<>();

        try {
            StringBuilder sb = new StringBuilder(QUERY);

            if (user.getId() != null)
                sb.append(" and id = ").append(user.getId());
            if(StringUtils.isNotBlank(user.getName()))
                sb.append(" and name = '").append(user.getName()).append("'");
            if(StringUtils.isNotBlank(user.getSurname()))
                sb.append(" and surname = '").append(user.getSurname()).append("'");
            if(!StringUtils.isNotBlank(user.getBirthday()))
                sb.append(" and birthday = '").append(user.getBirthday()).append("'");
            if(!StringUtils.isNotBlank(user.getGender()))
                sb.append(" and gender = '").append(user.getGender()).append("'");
            if(!StringUtils.isNotBlank(user.getUsername()))
                sb.append(" and username = '").append(user.getUsername()).append("'");
            if(!StringUtils.isNotBlank(user.getPassword()))
                sb.append(" and password = '").append(user.getPassword()).append("'");

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
