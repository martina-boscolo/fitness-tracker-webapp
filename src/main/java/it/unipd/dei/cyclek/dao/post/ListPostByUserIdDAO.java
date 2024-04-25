package it.unipd.dei.cyclek.dao.post;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListPostByUserIdDAO extends AbstractDAO<List<Post>> {


    private static final String STATEMENT = "SELECT * FROM posts WHERE id_user = ?";

    private final int userId;
    public ListPostByUserIdDAO(final Connection con, int userId) {
        super(con);
        if ( userId <= 0) {
            throw new IllegalArgumentException("postId must be greater than 0.");
        }

        this.userId = userId;
    }

    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        final List<Post> posts = new ArrayList<Post>();

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, userId);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                posts.add(
                        new Post(
                                rs.getInt("id"),
                                rs.getInt("id_user"),
                                rs.getString("text_content"),
                                rs.getString("image_path"),
                                rs.getTimestamp("post_date")
                        )
                );
            }

            LOGGER.info("Posts successfully listed.");

        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }

        }
        outputParam = posts;
    }
}