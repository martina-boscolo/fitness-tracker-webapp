package it.unipd.dei.cyclek.dao.user;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class GetUserByIdDAO extends AbstractDAO<User>{
    private static final String QUERY = "SELECT * FROM users WHERE id = ?";
    private final int id;

    public GetUserByIdDAO(final Connection con, int id) {
        super(con);
        this.id = id;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // the results of the search
        User user = null;

        try {
            pstmt = con.prepareStatement(QUERY);

            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            if(rs.next()) {
                user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("birthday"),
                        rs.getString("gender")
                );
            }

            LOGGER.info("User with id %d successfully fetched.", id);
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        this.outputParam = user;
    }
}
