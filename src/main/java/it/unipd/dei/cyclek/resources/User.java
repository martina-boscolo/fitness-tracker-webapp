package it.unipd.dei.cyclek.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class User extends AbstractResource{

    private Integer id;
    private String name;
    private String surname;
    private String birthday;
    private String gender;
    private String username;
    private String password;

    public User() {}

    public User(Integer id, String name, String surname, String birthday, String gender, String username, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.gender = gender;
        this.username = username;
        this.password = password;
    }
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }
    public String getUsername() {  return username; }
    public String getPassword() { return password; }

    @Override
    protected void writeJSON(OutputStream out) throws Exception {

        String json = new ObjectMapper()
                .enable(SerializationFeature.WRAP_ROOT_VALUE)
                .writer().withRootName("user")
                .writeValueAsString(this);
        out.write(json.getBytes(StandardCharsets.UTF_8));
    }

    public static User fromJSON(final InputStream in) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(in, User.class);
    }
}
