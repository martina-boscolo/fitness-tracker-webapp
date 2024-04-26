package it.unipd.dei.cyclek.dao.userGoals;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.UserGoals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public final class CreateUserGoalDAO extends AbstractDAO<UserGoals>{
    private static final String QUERY = "INSERT INTO userGoals VALUES (DEFAULT, ?, ?, ?, ?, ?, CURRENT_DATE) RETURNING *";

    private final UserGoals userGoals;

    public CreateUserGoalDAO(final Connection con, final UserGoals userGoals) {
        super(con);

        if (userGoals.getIdUser() == null || userGoals.getWeight() == null ||
                userGoals.getHeight() == null || userGoals.getFatty() == null || userGoals.getLean() == null) {
            LOGGER.error("Body must contains all parameters (idUser,weight,height,fatty,lean,statsDate).");
            throw new NullPointerException("Body must contains all parameters (idUser,weight,height,fatty,lean,goalDate)");
        }

        this.userGoals = userGoals;
    }

    @Override
    protected void doAccess() throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs;
        UserGoals bo = null;

        try {
            pstmt = con.prepareStatement(QUERY);
            pstmt.setInt(1, userGoals.getIdUser());
            pstmt.setDouble(2, userGoals.getWeight());
            pstmt.setDouble(3, userGoals.getHeight());
            pstmt.setDouble(4, userGoals.getFatty());
            pstmt.setDouble(5, userGoals.getLean());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                bo = new UserGoals(
                        rs.getInt("id"),
                        rs.getInt("idUser"),
                        rs.getDouble("weight"),
                        rs.getDouble("height"),
                        rs.getDouble("fatty"),
                        rs.getDouble("lean"),
                        rs.getString("goalDate")
                );
                LOGGER.info("Body Stats registered {}.", bo.getId());
            }
        } finally {
            if (pstmt != null)
                pstmt.close();
        }
        outputParam = bo;
    }
}
