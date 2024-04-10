package it.unipd.dei.cyclek.resources;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.OutputStream;
import java.util.Date;

public class User extends BaseResource{
    private final int id;
    private final String name;
    private final String surname;
    private final Date birthday;
    private final String gender;

    public User(int id, String name, String surname, Date birthday, String gender) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.gender = gender;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Date getDate() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        jg.writeFieldName("user");
        jg.writeStartObject();
        jg.writeNumberField("id", id);
        jg.writeStringField("name", name);
        jg.writeStringField("surname", surname);
        jg.writeStringField("birthday", birthday.toString() != null ? birthday.toString() : "");
        jg.writeStringField("gender", gender);
        jg.writeEndObject();
        jg.writeEndObject();
        jg.flush();
    }
}
