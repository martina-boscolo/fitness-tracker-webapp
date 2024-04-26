package it.unipd.dei.cyclek.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Resource {
    void toJSON(final OutputStream out) throws IOException;
}
