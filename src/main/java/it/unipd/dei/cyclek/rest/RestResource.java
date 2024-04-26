package it.unipd.dei.cyclek.rest;

import java.io.IOException;


public interface RestResource {

    /**
     * Serves a REST request.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    void serve() throws IOException;

}
