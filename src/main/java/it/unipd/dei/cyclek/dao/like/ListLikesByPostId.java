package it.unipd.dei.cyclek.dao.like;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.Like;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListLikesByPostId extends AbstractDAO {

        private static final String STATEMENT = "SELECT * FROM cyclek.public.likes WHERE id_post = ?";

        private final int postId;

        public ListLikesByPostId(Connection con, int postId) {
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
            List<Like> result = null;

            try {
                pstmt = con.prepareStatement(STATEMENT);
                pstmt.setInt(1, postId);
                rs = pstmt.executeQuery();

                result = new ArrayList<>();
                while (rs.next()) {
                    result.add( new Like(rs.getInt("id"), rs.getInt("id_user"), rs.getInt("id_post"), rs.getBoolean("is_like")));

                }
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
            }

        }
}
