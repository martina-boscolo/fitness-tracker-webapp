package it.unipd.dei.cyclek.dao.userStats;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.UserStats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class GetUserStatsDAO extends AbstractDAO<List<UserStats>>{
    private static final String QUERY = "SELECT * FROM userStats WHERE 1=1";

    private final UserStats userStats;

    public GetUserStatsDAO(Connection con, UserStats userStats) {
        super(con);
        this.userStats = userStats;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // the results of the search
        final List<UserStats> bsl = new ArrayList<>();

        try {
            StringBuilder sb = new StringBuilder(QUERY);

            if (userStats.getId() != null)
                sb.append(" and id = ").append(userStats.getId());
            if (userStats.getIdUser() != null)
                sb.append(" and idUser = ").append(userStats.getIdUser());
            if (userStats.getWeight() != null)
                sb.append(" and weight = ").append(userStats.getWeight());
            if (userStats.getHeight() != null)
                sb.append(" and height = ").append(userStats.getHeight());
            if (userStats.getFatty() != null)
                sb.append(" and fatty = ").append(userStats.getFatty());
            if (userStats.getLean() != null)
                sb.append(" and lean = ").append(userStats.getLean());
            if (!userStats.getStatsDate().isEmpty())
                sb.append(" and statsDate = ").append(userStats.getStatsDate());

            sb.append(" ORDER BY statsDate DESC");

            pstmt = con.prepareStatement(sb.toString());

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

        this.outputParam = bsl;
    }
}
