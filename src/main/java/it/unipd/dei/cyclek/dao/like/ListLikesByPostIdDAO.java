package it.unipd.dei.cyclek.dao.like;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Like;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListLikesByPostIdDAO extends AbstractDAO<List<Like>> {

    private static final String STATEMENT = "SELECT * FROM likes WHERE id_post = ? ";

    private final int postId;

    public ListLikesByPostIdDAO(Connection con, int postId) {
        super(con);
        if (postId <= 0) {
            throw new IllegalArgumentException("postId must be greater than 0.");
        }

        this.postId = postId;
    }

    @Override
    protected void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        final List<Like> likes = new ArrayList<>();


        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, postId);
            rs = pstmt.executeQuery();


            while (rs.next()) {
                likes.add(new Like(rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getInt("id_post")));

            }
            LOGGER.info("Likes successfully listed.");
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        outputParam = likes;
    }
}
