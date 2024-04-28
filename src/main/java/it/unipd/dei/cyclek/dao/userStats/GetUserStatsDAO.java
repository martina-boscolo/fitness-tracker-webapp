package it.unipd.dei.cyclek.dao.userStats;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.UserStats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class GetUserStatsDAO extends AbstractDAO<List<UserStats>>{
    private static final String QUERY = "SELECT * FROM userStats ORDER BY iduser, statsDate DESC";
        public GetUserStatsDAO(Connection con) {
        super(con);
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // the results of the search
        final List<UserStats> bsl = new ArrayList<>();

        try {

            pstmt = con.prepareStatement(QUERY);
            rs = pstmt.executeQuery();

            while (rs.next())
            {
                bsl.add(new UserStats(
                        rs.getInt("id"),
                        rs.getInt("idUser"),
                        rs.getDouble("weight"),
                        rs.getDouble("height"),
                        rs.getDouble("fatty"),
                        rs.getDouble("lean"),
                        rs.getString("statsDate")
                ));
            }

            LOGGER.info("Body Stats successfully fetched.");
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        this.outputParam = bsl.isEmpty() ? null : bsl;
    }
}
