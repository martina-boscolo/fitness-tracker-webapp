package it.unipd.dei.cyclek.dao.diets;


import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Diet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetDietDAO extends AbstractDAO<List<Diet>> {

    public static final String QUERY = "SELECT * FROM dietplans WHERE 1=1";
    private final Diet diet;
public GetDietDAO(Connection con, Diet diet){

    super(con);
    this.diet = diet;
}

@Override
protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // the results of the search
        final List<Diet> dietplans = new ArrayList<>();

        try {
            StringBuilder sb = new StringBuilder(QUERY);

            if (diet.getId() != null)
                sb.append(" and id = ").append(diet.getId());
            if (diet.getIdUser() != null)
                sb.append(" and idUser = ").append(diet.getIdUser());
            if (!diet.getPlanName().isEmpty())
                sb.append(" and planName = ").append(diet.getPlanName());
            if (diet.getDiet() != null)
                sb.append(" and diet = ").append(diet.getDiet());
            if (!diet.getDietDate().isEmpty())
                sb.append(" and date = ").append(diet.getDietDate());

            pstmt = con.prepareStatement(sb.toString());


            rs = pstmt.executeQuery();

            while (rs.next())
            {

                dietplans.add(new Diet(
                        rs.getInt("id"),
                        rs.getInt("idUser"),
                        rs.getString("planName"),
                        rs.getString("diet"),
                        rs.getString("dietDate")
                ));
            }

            LOGGER.info("Diet successfully fetched.");
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }

        this.outputParam = dietplans;
    }
}
