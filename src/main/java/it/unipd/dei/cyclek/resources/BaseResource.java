package it.unipd.dei.cyclek.resources;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;

import java.io.IOException;
import java.io.OutputStream;

public abstract class BaseResource {
    public BaseResource() {
    }
    protected static final Logger LOGGER = LogManager.getLogger(BaseResource.class, StringFormatterMessageFactory.INSTANCE);
    protected static final JsonFactory JSON_FACTORY;

    static {
        JSON_FACTORY = new JsonFactory();
        JSON_FACTORY.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        JSON_FACTORY.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
    }

    public void toJSON(final OutputStream out) throws IOException {
        if(out == null) {
            LOGGER.error("The output stream cannot be null.");
            throw new IOException("The output stream cannot be null.");
        }

        try {
            writeJSON(out);
        } catch(Exception e) {
            LOGGER.error("Unable to serialize the resource to JSON.", e);
            throw new IOException("Unable to serialize the resource to JSON.", e);
        }
    }
    protected abstract void writeJSON(OutputStream out) throws Exception;
}
