package it.unipd.dei.cyclek.dao.bodyStats;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.BodyStats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class CreateBodyStatsDAO extends AbstractDAO<BodyStats>{
    private static final String QUERY = "INSERT INTO bodyStats VALUES (DEFAULT, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP) RETURNING *";

    private final BodyStats bodyStats;

    public CreateBodyStatsDAO(final Connection con, final BodyStats bodyStats) {
        super(con);

        if (bodyStats.getIdUser() == null || bodyStats.getWeight() == null ||
                bodyStats.getHeight() == null || bodyStats.getFatty() == null || bodyStats.getLean() == null) {
            LOGGER.error("Body must contains all parameters (idUser,weight,height,fatty,lean,statsDate).");
            throw new NullPointerException("Body must contains all parameters (idUser,weight,height,fatty,lean,statsDate)");
        }

        this.bodyStats = bodyStats;
    }

    @Override
    protected void doAccess() throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs;
        BodyStats bs = null;

        try {
            pstmt = con.prepareStatement(QUERY);
            pstmt.setInt(1, bodyStats.getIdUser());
            pstmt.setDouble(2, bodyStats.getWeight());
            pstmt.setDouble(3, bodyStats.getHeight());
            pstmt.setDouble(4, bodyStats.getFatty());
            pstmt.setDouble(5, bodyStats.getLean());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                bs = new BodyStats(
                        rs.getInt("id"),
                        rs.getInt("idUser"),
                        rs.getDouble("weight"),
                        rs.getDouble("height"),
                        rs.getDouble("fatty"),
                        rs.getDouble("lean"),
                        rs.getString("statsDate")
                );
                LOGGER.info("Body Stats registered {}.", bs.getId());
            }
        } finally {
            if (pstmt != null)
                pstmt.close();
        }
        outputParam = bs;
    }
}
