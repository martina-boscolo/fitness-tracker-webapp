package it.unipd.dei.cyclek.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import jakarta.json.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import static it.unipd.dei.cyclek.resources.AbstractResource.JSON_FACTORY;

/**
 * Represents a social network post with various attributes such as post ID, user ID, text content, image path, like count, comment count, and post date.
 *
 * @author Martina Boscolo Bacheto
 */
public class SocialNetworkPost extends AbstractResource {

    private final int postId;
    private final int userId;
    private final String textContent;
    private final String imagePath;
    private final int likeCount;
    private final int commentCount;
    private final Timestamp postDate;

    /**
     * Constructs a new SocialNetworkPost with the given attributes.
     *
     * @param postId       the ID of the post
     * @param userId       the ID of the user who created the post
     * @param textContent  the text content of the post
     * @param imagePath    the path to the image of the post
     * @param likeCount    the number of likes the post has received
     * @param commentCount the number of comments the post has received
     * @param postDate     the date the post was created
     */
    public SocialNetworkPost(int postId, int userId, String textContent, String imagePath, int likeCount, int commentCount, Timestamp postDate) {
        this.postId = postId;
        this.userId = userId;
        this.textContent = textContent;
        this.imagePath = imagePath;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.postDate = postDate;
    }

    /**
     * Returns the ID of the post.
     *
     * @return the ID of the post
     */
    public int getPostId() {
        return postId;
    }


    /**
     * Returns the ID of the user who created the post.
     *
     * @return the ID of the user
     */
    public int getUserId() {
        return userId;
    }


    /**
     * Returns the text content of the post.
     *
     * @return the text content of the post
     */
    public String getTextContent() {
        return textContent;
    }


    /**
     * Returns the path to the image of the post.
     *
     * @return the path to the image of the post
     */
    public String getImagePath() {
        return imagePath;
    }


    /**
     * Returns the number of likes the post has received.
     *
     * @return the number of likes
     */
    public int getLikeCount() {
        return likeCount;
    }


    /**
     * Returns the number of comments the post has received.
     *
     * @return the number of comments
     */
    public int getCommentCount() {
        return commentCount;
    }


    /**
     * Returns the date the post was created.
     *
     * @return the date the post was created
     */
    public Timestamp getPostDate() {
        return postDate;
    }


    @Override
    protected final void writeJSON(final OutputStream out) throws IOException {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();

        jg.writeFieldName("post");

        jg.writeStartObject();

        jg.writeNumberField("postId", postId);

        jg.writeNumberField("userId", userId);

        jg.writeStringField("textContent", textContent);

        jg.writeStringField("imagePath", imagePath);

        jg.writeNumberField("likeCount", likeCount);

        jg.writeNumberField("commentCount", commentCount);

        jg.writeStringField("postDate", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(postDate));

        jg.writeEndObject();

        jg.writeEndObject();

        jg.flush();
    }


    public static SocialNetworkPost fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        int jPostId = -1;
        int jUserId = -1;
        String jTextContent = null;
        String jImagePath = null;
        int jLikeCount = 0; //default value
        int jCommentCount = 0; //default value
        Timestamp jPostDate = null;


        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);

            // while we are not on the start of an element or the element is not
            // a token element, advance to the next element (if any)
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"post".equals(jp.getCurrentName())) {

                // there are no more events
                if (jp.nextToken() == null) {
                    LOGGER.error("No Social Network Post object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no Social Network Post object found.");
                }
            }

            while (jp.nextToken() != JsonToken.END_OBJECT) {

                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

                    switch (jp.getCurrentName()) {
                        case "postId":
                            jp.nextToken();
                            jPostId = jp.getIntValue();
                            break;
                        case "userId":
                            jp.nextToken();
                            jUserId = jp.getIntValue();
                            break;
                        case "textContent":
                            jp.nextToken();
                            jTextContent = jp.getText();
                            break;
                        case "imagePath":
                            jp.nextToken();
                            jImagePath = jp.getText();
                            break;
                        case "likeCount":
                            jp.nextToken();
                            jLikeCount = jp.getIntValue();
                            break;
                        case "commentCount":
                            jp.nextToken();
                            jCommentCount = jp.getIntValue();
                            break;
                        case "postDate":
                            jp.nextToken();
                            jPostDate = Timestamp.valueOf(jp.getText());
                            break;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse a Social Network Post object from JSON.", e);
            throw e;
        }
        return new SocialNetworkPost(jPostId, jUserId, jTextContent, jImagePath, jLikeCount, jCommentCount, jPostDate);
    }
}