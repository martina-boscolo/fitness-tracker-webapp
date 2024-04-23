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
    private static final String QUERY = "SELECT * FROM userGoals WHERE 1=1";

    private final UserGoals userGoals;

    public GetUserGoalsDAO(Connection con, UserGoals userGoals) {
        super(con);
        this.userGoals = userGoals;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // the results of the search
        final List<UserGoals> bol = new ArrayList<>();

        try {
            StringBuilder sb = new StringBuilder(QUERY);

            if (userGoals.getId() != null)
                sb.append(" and id = ").append(userGoals.getId());
            if (userGoals.getIdUser() != null)
                sb.append(" and idUser = ").append(userGoals.getIdUser());
            if (userGoals.getWeight() != null)
                sb.append(" and weight = ").append(userGoals.getWeight());
            if (userGoals.getHeight() != null)
                sb.append(" and height = ").append(userGoals.getHeight());
            if (userGoals.getFatty() != null)
                sb.append(" and fatty = ").append(userGoals.getFatty());
            if (userGoals.getLean() != null)
                sb.append(" and lean = ").append(userGoals.getLean());
            if (!userGoals.getObjDate().isEmpty())
                sb.append(" and objDate = ").append(userGoals.getObjDate());

            sb.append(" ORDER BY objDate DESC");

            pstmt = con.prepareStatement(sb.toString());

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
                        rs.getString("objDate")
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

        this.outputParam = bol;
    }
}
