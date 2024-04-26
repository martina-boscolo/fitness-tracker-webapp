package it.unipd.dei.cyclek.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;

/**
 * Represents a social network post with various attributes such as post ID, user ID, text content, image path, like count, comment count, and post date.
 *
 * @author Martina Boscolo Bacheto
 */
public class Post extends AbstractResource {

    private final int postId;
    private final int userId;
    private final String textContent;
    private final byte[] photo;
    private final String photoMediaType;
    private final Timestamp postDate;

    /**
     * Constructs a new Post with the given attributes.
     *
     * @param postId       the ID of the post
     * @param userId       the ID of the user who created the post
     * @param textContent  the text content of the post
     * @param postDate     the date the post was created
     */
    public Post(int postId, int userId, String textContent, final byte[] photo, final String photoMediaType , Timestamp postDate) {
        this.postId = postId;
        this.userId = userId;
        this.textContent = textContent;
        this.photo = photo;
        this.photoMediaType = photoMediaType;
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


    public byte[] getPhoto() {
        return photo;
    }

    public String getPhotoMediaType() {
        return photoMediaType;
    }

    /**
     * Returns the date the post was created.
     *
     * @return the date the post was created
     */
    public Timestamp getPostDate() {
        return postDate;
    }


    public final boolean hasPhoto() {
        return photo != null && photo.length > 0 && photoMediaType != null && !photoMediaType.isBlank();
    }
    public final int getPhotoSize() {
        return photo != null ? photo.length : Integer.MIN_VALUE;
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

        if (hasPhoto()) {
            jg.writeStringField("photo", Base64.getEncoder().encodeToString(photo)); //not the best
            jg.writeStringField("photoMediaType", photoMediaType);
        }

        jg.writeStringField("postDate", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(postDate));

        jg.writeEndObject();

        jg.writeEndObject();

        jg.flush();
    }


    public static Post fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        int jPostId = -1;
        int jUserId = -1;
        String jTextContent = null;
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
                        case "postDate":
                            jp.nextToken();
                            jPostDate = Timestamp.valueOf(jp.getText());
                            break;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse a Post object from JSON.", e);
            throw e;
        }
        return new Post(jPostId, jUserId, jTextContent,null, null,  jPostDate);
    }
}