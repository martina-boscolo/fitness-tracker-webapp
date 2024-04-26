package it.unipd.dei.cyclek.dao.userGoals;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.UserGoals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class GetUserGoalsDAO extends AbstractDAO<List<UserGoals>>{
    private static final String QUERY = "SELECT * FROM userGoals ORDER BY user, goalDate DESC";

    public GetUserGoalsDAO(Connection con) {
        super(con);
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // the results of the search
        final List<UserGoals> bol = new ArrayList<>();

        try {

            pstmt = con.prepareStatement(QUERY);
            rs = pstmt.executeQuery();

            while (rs.next())
            {
                bol.add(new UserGoals(
                        rs.getInt("id"),
                        rs.getInt("idUser"),
                        rs.getDouble("weight"),
                        rs.getDouble("height"),
                        rs.getDouble("fatty"),
                        rs.getDouble("lean"),
                        rs.getString("goalDate")
                ));
            }

            LOGGER.info("Body Obj successfully fetched.");
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        this.outputParam = bol.isEmpty() ? null : bol;
    }
}
