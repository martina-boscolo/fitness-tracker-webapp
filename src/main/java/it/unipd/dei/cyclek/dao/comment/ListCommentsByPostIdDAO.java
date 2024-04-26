package it.unipd.dei.cyclek.dao.comment;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Comment;
import it.unipd.dei.cyclek.resources.Like;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListCommentsByPostIdDAO extends AbstractDAO<List<Comment>> {
    private static final String STATEMENT = "SELECT * FROM comments WHERE id_post = ?";

    private final int postId;

    public ListCommentsByPostIdDAO(Connection con, int postId) {
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
        final List<Comment> comments = new ArrayList<>();


        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, postId);
            rs = pstmt.executeQuery();


            while (rs.next()) {
                comments.add(new Comment(rs.getInt("id"), rs.getInt("id_user"), rs.getInt("id_post"), rs.getString("text_content")));

            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        outputParam = comments;
    }
}
