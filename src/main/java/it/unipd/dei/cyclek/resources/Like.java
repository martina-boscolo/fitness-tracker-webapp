package it.unipd.dei.cyclek.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a like action on a social network post
 *
 * @author Martina Boscolo Bacheto
 *
 */
public class Like extends AbstractResource {
    private final int likeId;
    private final int userId;
    private final int postId;

    /**
     * Constructs a new LikeOrDislike with the given attributes.
     *
     * @param likeDislikeId the ID of the like/dislike
     * @param userId        the ID of the user who made the like/dislike
     * @param postId        the ID of the post that was liked/disliked
     */
    public Like(int likeDislikeId, int userId, int postId) {
        this.likeId = likeDislikeId;
        this.userId = userId;
        this.postId = postId;
    }

    /**
     * Returns the ID of the like.
     *
     * @return the ID of the like
     */
    public int getLikeId() {
        return likeId;
    }


    /**
     * Returns the ID of the user who made the like.
     *
     * @return the ID of the user
     */
    public int getUserId() {
        return userId;
    }


    /**
     * Returns the ID of the post that was liked.
     *
     * @return the ID of the post
     */
    public int getPostId() {
        return postId;
    }


    @Override
    protected final void writeJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();

        jg.writeFieldName("like");

        jg.writeStartObject();

        jg.writeNumberField("likeId", likeId);

        jg.writeNumberField("userId", userId);

        jg.writeNumberField("postId", postId);


        jg.writeEndObject();

        jg.writeEndObject();

        jg.flush();
    }


    public static Like fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        int jLikeId = -1;
        int jUserId = -1;
        int jPostId = -1;




        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);

            // while we are not on the start of an element or the element is not
            // a token element, advance to the next element (if any)
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"like".equals(jp.getCurrentName())) {

                // there are no more events
                if (jp.nextToken() == null) {
                    LOGGER.error("No like object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no like object found.");
                }
            }

            while (jp.nextToken() != JsonToken.END_OBJECT) {

                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

                    switch (jp.getCurrentName()) {
                        case "likeId":
                            jp.nextToken();
                            jLikeId = jp.getIntValue();
                            break;
                        case "userId":
                            jp.nextToken();
                            jUserId = jp.getIntValue();
                            break;
                        case "postId":
                            jp.nextToken();
                            jPostId = jp.getIntValue();
                            break;


                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse a Like object from JSON.", e);
            throw e;
        }
        return new Like(jLikeId,jUserId, jPostId );
    }


}