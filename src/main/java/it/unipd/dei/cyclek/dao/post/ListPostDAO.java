package it.unipd.dei.cyclek.dao.post;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for listing all social network posts from the database.
 * It extends the AbstractDAO class and overrides the doAccess method.
 *
 * @author Martina Boscolo Bacheto
 */
public class ListPostDAO extends AbstractDAO<List<Post>> {

    /**
     * SQL statement to list all social network posts from the database.
     */
    private static final String STATEMENT = "SELECT * FROM posts ";


    /**
     * Constructs a new ListPostDAO object with the given connection.
     *
     * @param con the connection to the database.
     */
    public ListPostDAO(final Connection con) {
        super(con);
    }

    /**
     * Lists all the social network posts from the database.
     *
     * @throws SQLException if any SQL error occurs while listing the posts.
     */
    @Override
    protected void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the results of the search
        final List<Post> posts = new ArrayList<Post>();

        try {
            pstmt = con.prepareStatement(STATEMENT);

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

