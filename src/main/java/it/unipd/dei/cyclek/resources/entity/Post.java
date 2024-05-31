package it.unipd.dei.cyclek.resources.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import it.unipd.dei.cyclek.resources.AbstractResource;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Base64;

/**
 * Represents a post in the social network.
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
    private final  String username;
    private final int likeCount;
    private final int commentCount;


    /**
     * Creates a new post.
     *
     * @param postId         the post ID
     * @param userId         the user ID of the user who created the post
     * @param textContent    the text content of the post
     * @param photo          the photo of the post
     * @param photoMediaType the media type of the photo of the post
     * @param postDate       the date the post was created
     * @param username       the username of the user who created the post
     * @param likeCount      the number of likes the post has
     * @param commentCount   the number of comments the post has
     */
    public Post(int postId, int userId, String textContent, final byte[] photo, final String photoMediaType, Timestamp postDate, String username, int likeCount, int commentCount) {
        this.postId = postId;
        this.userId = userId;
        this.textContent = textContent;
        this.photo = photo;
        this.photoMediaType = photoMediaType;
        this.postDate = postDate;
        this.username = username;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }


    /**
     * Returns the post ID.
     *
     * @return the post ID
     */
    public int getPostId() {
        return postId;
    }


    /**
     * Returns the user ID of the user who created the post.
     *
     * @return the user ID of the user who created the post
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
     * Returns the photo of the post.
     *
     * @return the photo of the post
     */
    public byte[] getPhoto() {
        return photo;
    }

    /**
     * Returns the media type of the photo of the post.
     *
     * @return the media type of the photo of the post
     */
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

    /**
     * Returns the username of the user who created the post.
     *
     * @return the username of the user who created the post
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the number of likes the post has.
     *
     * @return the number of likes the post has
     */
    public int getLikeCount() {
        return likeCount;
    }

    /**
     * Returns the number of comments the post has.
     *
     * @return the number of comments the post has
     */
    public int getCommentCount() {
        return commentCount;
    }



    /**
     * Returns whether the post has a photo.
     *
     * @return {@code true} if the post has a photo, {@code false} otherwise
     */
    public final boolean hasPhoto() {
        return photo != null && photo.length > 0 && photoMediaType != null && !photoMediaType.isBlank();
    }

    public final int getPhotoSize() {
        return photo != null ? photo.length : Integer.MIN_VALUE;
    }

    /**
     * Returns a string representation of the post.
     */
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

        jg.writeStringField("username", username);

        jg.writeNumberField("likeCount", likeCount);

        jg.writeNumberField("commentCount", commentCount);

        jg.writeEndObject();

        jg.writeEndObject();

        jg.flush();
    }

    /**
     * Creates a {@code Post} object from its JSON representation.
     *
     * @param in the input stream containing the JSON document
     * @return the {@code Post} object created from the JSON representation
     * @throws IOException if an I/O error occurs
     */
    public static Post fromJSON(final InputStream in) throws IOException {

        // the fields read from JSON
        int jPostId = -1;
        int jUserId = -1;
        String jTextContent = null;
        Timestamp jPostDate = null;
        String jUsername = null;
        int jLikeCount = 0;
        int jCommentCount = 0;
        byte[] jPhoto = null;
        String jPhotoMediaType= null;



        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);

            // while we are not on the start of an element or the element is not
            // a token element, advance to the next element (if any)
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"post".equals(jp.currentName())) {

                // there are no more events
                if (jp.nextToken() == null) {
                    LOGGER.error("No post object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no post object found.");
                }
            }

            while (jp.nextToken() != JsonToken.END_OBJECT) {

                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

                    switch (jp.currentName()) {
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
                        case "username":
                            jp.nextToken();
                            jUsername = jp.getText();
                            break;
                        case "likeCount":
                            jp.nextToken();
                            jLikeCount = jp.getIntValue();
                            break;
                        case "commentCount":
                            jp.nextToken();
                            jCommentCount = jp.getIntValue();
                            break;
                        case "photo":
                            jp.nextToken();
                            jPhoto = Base64.getDecoder().decode(jp.getText());
                            break;
                        case "photoMediaType":
                            jp.nextToken();
                            jPhotoMediaType = jp.getText();
                            break;


                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse a post object from JSON.", e);
            throw e;
        }
        return new Post(jPostId, jUserId, jTextContent, jPhoto, jPhotoMediaType, jPostDate, jUsername, jLikeCount, jCommentCount);
    }
}