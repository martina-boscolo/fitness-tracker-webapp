package it.unipd.dei.cyclek.resources;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.OutputStream;

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
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.set("resource-list",objectMapper.valueToTree(this.list));
        jg.writeString(objectMapper.writeValueAsString(rootNode));
        jg.flush();
    }
}
