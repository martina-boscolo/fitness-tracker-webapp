package it.unipd.dei.cyclek.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public final class ResourceList<T extends Resource> extends AbstractResource {
    private final Iterable<T> list;

    public ResourceList(Iterable<T> list) {
        if(list == null) {
            LOGGER.error("Resource list cannot be null.");
            throw new NullPointerException("Resource list cannot be null.");
        }

        this.list = list;
    }

    @Override
    protected void writeJSON(OutputStream out) throws Exception {

        /*
        String json = new ObjectMapper()
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .writer().withRootName("resource-list")
                .writeValueAsString(this.list);
        out.write(json.getBytes(StandardCharsets.UTF_8));
        */


        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);
        jg.writeStartObject();
        jg.writeFieldName("resource-list");
        jg.writeStartArray();
        jg.flush();
        boolean firstElement = true;
        for (final Resource r : list) {
            // very bad work-around to add commas between resources
            if (firstElement) {
                r.toJSON(out);
                jg.flush();
                firstElement = false;
            } else {
                jg.writeRaw(',');
                jg.flush();
                r.toJSON(out);
                jg.flush();
            }
        }
        jg.writeEndArray();
        jg.writeEndObject();
        jg.flush();
    }
}
