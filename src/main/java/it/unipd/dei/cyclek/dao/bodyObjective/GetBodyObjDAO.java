package it.unipd.dei.cyclek.dao.bodyObjective;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.BodyObj;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class GetBodyObjDAO extends AbstractDAO<List<BodyObj>>{
    private static final String QUERY = "SELECT * FROM bodyobjective WHERE 1=1";

    private final BodyObj bodyObj;

    public GetBodyObjDAO(Connection con, BodyObj bodyObj) {
        super(con);
        this.bodyObj = bodyObj;
    }

    @Override
    protected final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // the results of the search
        final List<BodyObj> bol = new ArrayList<>();

        try {
            StringBuilder sb = new StringBuilder(QUERY);

            if (bodyObj.getId() != null)
                sb.append(" and id = ").append(bodyObj.getId());
            if (bodyObj.getIdUser() != null)
                sb.append(" and idUser = ").append(bodyObj.getIdUser());
            if (bodyObj.getWeight() != null)
                sb.append(" and weight = ").append(bodyObj.getWeight());
            if (bodyObj.getHeight() != null)
                sb.append(" and height = ").append(bodyObj.getHeight());
            if (bodyObj.getFatty() != null)
                sb.append(" and fatty = ").append(bodyObj.getFatty());
            if (bodyObj.getLean() != null)
                sb.append(" and lean = ").append(bodyObj.getLean());
            if (!bodyObj.getObjDate().isEmpty())
                sb.append(" and objDate = ").append(bodyObj.getObjDate());

            sb.append(" ORDER BY objDate DESC");

            pstmt = con.prepareStatement(sb.toString());

            rs = pstmt.executeQuery();

            while (rs.next())
            {
                bol.add(new BodyObj(
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
