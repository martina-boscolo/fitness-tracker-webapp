/*
 * Copyright 2023 University of Padua, Italy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.unipd.dei.cyclek.dao.post;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.Post;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Load the photo of a post from the database.
 * @author Martina Boscolo Bacheto
 */
public final class LoadPostPhotoDAO extends AbstractDAO<Post> {

    private static final String STATEMENT = "SELECT photo, photoMediaType FROM posts WHERE id = ?";

    private final int postId;

    /**
     * Creates a new object for loading the photo of a post from the database.
     *
     * @param con    the connection to the database.
     * @param postId the ID of the post whose photo is to be loaded.
     * @throws IllegalArgumentException if postId is less than or equal to 0.
     * @throws NullPointerException     if con is null.
     */
    public LoadPostPhotoDAO(final Connection con, final int postId) {
        super(con);
        this.postId = postId;
    }

    /**
     * Loads the photo of a post from the database.
     *
     * @throws SQLException if any error occurs while loading the photo of a post from the database.
     */
    @Override
    public final void doAccess() throws SQLException {

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Post post = null;

        try {
            pstmt = con.prepareStatement(STATEMENT);
            pstmt.setInt(1, postId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                post = new Post(Integer.MIN_VALUE,
                        Integer.MIN_VALUE,
                        null,
                        rs.getBytes("photo"),
                        rs.getString("photoMediaType"),
                        Timestamp.valueOf(LocalDateTime.now()));

                LOGGER.info("Photo for post %s successfully loaded.", postId);

            } else {

                LOGGER.warn("Post %s not found.", postId);
                throw new SQLException(String.format("Post %d not found.", postId), "NOT_FOUND");
            }


        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }

        this.outputParam = post;
    }
}
