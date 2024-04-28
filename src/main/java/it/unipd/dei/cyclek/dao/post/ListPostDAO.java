package it.unipd.dei.cyclek.dao.post;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for listing all social network posts from the database.
 *
 * @author Martina Boscolo Bacheto
 */
public class ListPostDAO extends AbstractDAO<List<Post>> {

    private static final String STATEMENT = "SELECT * FROM posts ";

    /**
     * Creates a new object for listing all the social network posts from the database.
     *
     * @param con the connection to the database.
     */
    public ListPostDAO(final Connection con) {
        super(con);
    }

    /**
     * Lists all the social network posts from the database.
     *
     * @throws SQLException if any error occurs while listing the social network posts from the database.
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        final List<Post> posts = new ArrayList<Post>();

        try {
            pstmt = con.prepareStatement(STATEMENT);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                posts.add(new Post(rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getString("text_content"),
                        rs.getBytes("photo"),
                        rs.getString("photoMediaType"),
                        rs.getTimestamp("post_date")

                ));
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

