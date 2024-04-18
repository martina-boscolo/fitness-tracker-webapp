package it.unipd.dei.cyclek.dao.bodyStats;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.BodyStats;
import it.unipd.dei.cyclek.resources.LogContext;
import it.unipd.dei.cyclek.resources.Message;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class GetBodyStatsDAO extends AbstractDAO<List<BodyStats>>{
    private static final String QUERY = "SELECT * FROM bodyStats WHERE 1=1";

    private final BodyStats bodyStats;

    public GetBodyStatsDAO(Connection con, BodyStats bodyStats) {
        super(con);
        this.bodyStats = bodyStats;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // the results of the search
        final List<BodyStats> bsl = new ArrayList<>();

        try {
            StringBuilder sb = new StringBuilder(QUERY);

            if (bodyStats.getId() != null)
                sb.append(" and id = ").append(bodyStats.getId());
            if (bodyStats.getIdUser() != null)
                sb.append(" and idUser = ").append(bodyStats.getIdUser());
            if (bodyStats.getWeight() != null)
                sb.append(" and weight = ").append(bodyStats.getWeight());
            if (bodyStats.getHeight() != null)
                sb.append(" and height = ").append(bodyStats.getHeight());
            if (bodyStats.getFatty() != null)
                sb.append(" and fatty = ").append(bodyStats.getFatty());
            if (bodyStats.getLean() != null)
                sb.append(" and lean = ").append(bodyStats.getLean());
            if (!bodyStats.getStatsDate().isEmpty())
                sb.append(" and statsDate = ").append(bodyStats.getStatsDate());

            sb.append(" ORDER BY statsDate DESC");

            pstmt = con.prepareStatement(sb.toString());

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
