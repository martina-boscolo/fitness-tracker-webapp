package it.unipd.dei.cyclek.resources.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import it.unipd.dei.cyclek.resources.AbstractResource;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a comment in the social network.
 *
 * @author Martina Boscolo Bacheto
 */

public class Comment extends AbstractResource {
    private final int commentId;
    private final int userId;
    private final int postId;
    private final String commentText;

    private final String username;

    /**
     * Constructs a new Comment with the given attributes.
     *
     * @param commentId   the ID of the comment
     * @param postId      the ID of the post that the comment is on
     * @param userId      the ID of the user who made the comment
     * @param commentText the text of the comment
     */
    public Comment(int commentId, int userId, int postId,  String commentText, String username) {
        this.commentId = commentId;
        this.userId = userId;
        this.postId = postId;
        this.commentText = commentText;
        this.username = username;
    }

    /**
     * Returns the ID of the comment.
     *
     * @return the ID of the comment
     */
    public int getCommentId() {
        return commentId;
    }


    /**
     * Returns the ID of the post that the comment is on.
     *
     * @return the ID of the post
     */
    public int getPostId() {
        return postId;
    }


    /**
     * Returns the ID of the user who made the comment.
     *
     * @return the ID of the user
     */
    public int getUserId() {
        return userId;
    }


    /**
     * Returns the text of the comment.
     *
     * @return the text of the comment
     */
    public String getCommentText() {
        return commentText;
    }

    public String getUsername() {
        return username;
    }

    /**
     * Writes this Comment as a JSON object to the given output stream.
     *
     * @param out the output stream
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected final void writeJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();

        jg.writeFieldName("comment");

        jg.writeStartObject();

        jg.writeNumberField("commentId", commentId);

        jg.writeNumberField("userId", userId);

        jg.writeNumberField("postId", postId);

        jg.writeStringField("commentText", commentText);

        jg.writeStringField("username", username);

        jg.writeEndObject();

        jg.writeEndObject();

        jg.flush();
    }


    /**
     * Creates a {@code Comment} object from its JSON representation.
     *
     * @param in the input stream containing the JSON document
     * @return the {@code Comment} object created from the JSON representation
     * @throws IOException if an I/O error occurs
     */
    public static Comment fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        int jCommentId = -1;
        int jUserId = -1;
        int jPostId = -1;
        String jCommentText = null;
        String jUsername = null;


        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);

            // while we are not on the start of an element or the element is not
            // a token element, advance to the next element (if any)
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"comment".equals(jp.currentName())) {

                // there are no more events
                if (jp.nextToken() == null) {
                    LOGGER.error("No comment object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no comment object found.");
                }
            }

            while (jp.nextToken() != JsonToken.END_OBJECT) {

                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

                    switch (jp.currentName()) {
                        case "likeId":
                            jp.nextToken();
                            jCommentId = jp.getIntValue();
                            break;
                        case "userId":
                            jp.nextToken();
                            jUserId = jp.getIntValue();
                            break;
                        case "postId":
                            jp.nextToken();
                            jPostId = jp.getIntValue();
                            break;
                        case "commentText":
                            jp.nextToken();
                            jCommentText = jp.getText();
                            break;
                        case "username":
                            jp.nextToken();
                            jUsername = jp.getText();
                            break;

                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse a comment object from JSON.", e);
            throw e;
        }
        return new Comment(jCommentId, jUserId, jPostId, jCommentText, jUsername);
    }


}