package it.unipd.dei.cyclek.dao.userStats;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.UserStats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public final class CreateUserStatsDAO extends AbstractDAO<UserStats>{
    private static final String QUERY = "INSERT INTO userStats VALUES (DEFAULT, ?, ?, ?, ?, ?, CURRENT_DATE) RETURNING *";

    private final UserStats userStats;

    public CreateUserStatsDAO(final Connection con, final UserStats userStats) {
        super(con);

        if (userStats.getIdUser() == null || userStats.getWeight() == null ||
                userStats.getHeight() == null || userStats.getFatty() == null || userStats.getLean() == null) {
            LOGGER.error("Body must contains all parameters (idUser,weight,height,fatty,lean,statsDate).");
            throw new NullPointerException("Body must contains all parameters (idUser,weight,height,fatty,lean,statsDate)");
        }

        this.userStats = userStats;
    }

    @Override
    protected void doAccess() throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs;
        UserStats bs = null;

        try {
            pstmt = con.prepareStatement(QUERY);
            pstmt.setInt(1, userStats.getIdUser());
            pstmt.setDouble(2, userStats.getWeight());
            pstmt.setDouble(3, userStats.getHeight());
            pstmt.setDouble(4, userStats.getFatty());
            pstmt.setDouble(5, userStats.getLean());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                bs = new UserStats(
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
