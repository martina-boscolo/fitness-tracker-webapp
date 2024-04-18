package it.unipd.dei.cyclek.dao.bodyStats;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.BodyStats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class GetLatestBodyStatsDAO extends AbstractDAO<List<BodyStats>>{
    private static final String QUERY = "SELECT *" +
            "FROM bodyStats bs" +
            "         INNER JOIN (" +
            "    SELECT idUser, MAX(statsDate) AS latestStatsDate" +
            "    FROM bodyStats" +
            "    GROUP BY idUser" +
            ") latestStats ON bs.idUser = latestStats.idUser AND bs.statsDate = latestStats.latestStatsDate " +
            "ORDER BY bs.idUser";

    public GetLatestBodyStatsDAO(Connection con) {
        super(con);
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // the results of the search
        final List<BodyStats> bsl = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(QUERY);

            rs = pstmt.executeQuery();

            while (rs.next())
            {
                bsl.add(new BodyStats(
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

        this.outputParam = bsl;
    }
}
