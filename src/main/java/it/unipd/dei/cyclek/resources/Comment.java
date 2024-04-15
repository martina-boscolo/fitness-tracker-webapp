package it.unipd.dei.cyclek.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import jakarta.json.Json;
import jakarta.json.JsonObject;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a comment on a social network post with various attributes such as comment ID, post ID, user ID, comment text, and comment date.
 *
 * @author Martina Boscolo Bacheto
 *
 */
public class Comment extends AbstractResource {
    private final int commentId;
    private final int postId;
    private final int userId;
    private final String commentText;

    /**
     * Constructs a new Comment with the given attributes.
     *
     * @param commentId   the ID of the comment
     * @param postId      the ID of the post that the comment is on
     * @param userId      the ID of the user who made the comment
     * @param commentText the text of the comment
     */
    public Comment(int commentId, int postId, int userId, String commentText) {
        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.commentText = commentText;
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

    @Override
    protected final void writeJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();

        jg.writeFieldName("socialNetworkPost");

        jg.writeStartObject();

        jg.writeNumberField("commentId", commentId);

        jg.writeNumberField("postId", postId);

        jg.writeNumberField("userId", userId);

        jg.writeStringField("commentText", commentText);

        jg.writeEndObject();

        jg.writeEndObject();

        jg.flush();
    }


    public static Comment fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        int jCommentId = -1;
        int jPostId = -1;
        int jUserId = -1;
        String jCommentText = null;



        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);

            // while we are not on the start of an element or the element is not
            // a token element, advance to the next element (if any)
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"comment".equals(jp.getCurrentName())) {

                // there are no more events
                if (jp.nextToken() == null) {
                    LOGGER.error("No comment object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no comment object found.");
                }
            }

            while (jp.nextToken() != JsonToken.END_OBJECT) {

                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

                    switch (jp.getCurrentName()) {
                        case "likeId":
                            jp.nextToken();
                            jCommentId = jp.getIntValue();
                            break;
                        case "postId":
                            jp.nextToken();
                            jPostId = jp.getIntValue();
                            break;
                        case "userId":
                            jp.nextToken();
                            jUserId = jp.getIntValue();
                            break;
                        case "isLike":
                            jp.nextToken();
                            jCommentText = jp.getText();
                            break;


                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse a Comment object from JSON.", e);
            throw e;
        }
        return new Comment(jCommentId, jPostId, jUserId, jCommentText);
    }


}