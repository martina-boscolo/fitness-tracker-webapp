package it.unipd.dei.cyclek.dao.bodyObjective;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.BodyObj;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public final class CreateBodyObjDAO extends AbstractDAO<BodyObj>{
    private static final String QUERY = "INSERT INTO bodyobjective VALUES (DEFAULT, ?, ?, ?, ?, ?, CURRENT_DATE) RETURNING *";

    private final BodyObj bodyObj;

    public CreateBodyObjDAO(final Connection con, final BodyObj bodyObj) {
        super(con);

        if (bodyObj.getIdUser() == null || bodyObj.getWeight() == null ||
                bodyObj.getHeight() == null || bodyObj.getFatty() == null || bodyObj.getLean() == null) {
            LOGGER.error("Body must contains all parameters (idUser,weight,height,fatty,lean,statsDate).");
            throw new NullPointerException("Body must contains all parameters (idUser,weight,height,fatty,lean,statsDate)");
        }

        this.bodyObj = bodyObj;
    }

    @Override
    protected void doAccess() throws Exception {

        PreparedStatement pstmt = null;
        ResultSet rs;
        BodyObj bo = null;

        try {
            pstmt = con.prepareStatement(QUERY);
            pstmt.setInt(1, bodyObj.getIdUser());
            pstmt.setDouble(2, bodyObj.getWeight());
            pstmt.setDouble(3, bodyObj.getHeight());
            pstmt.setDouble(4, bodyObj.getFatty());
            pstmt.setDouble(5, bodyObj.getLean());
            rs = pstmt.executeQuery();

            if (rs.next()) {
                bo = new BodyObj(
                        rs.getInt("id"),
                        rs.getInt("idUser"),
                        rs.getDouble("weight"),
                        rs.getDouble("height"),
                        rs.getDouble("fatty"),
                        rs.getDouble("lean"),
                        rs.getString("objDate")
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
