package it.unipd.dei.cyclek.rest;

import java.io.IOException;


/**
 * Represents a generic REST resource. The {@link #serve()} method handles the request served by this REST resource.
 *
 * @author Nicola Ferro (ferro@dei.unipd.it)
 * @version 1.0
 * @since 1.0
 */
public interface RestResource {

    /**
     * Serves a REST request.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    void serve() throws IOException;

}
