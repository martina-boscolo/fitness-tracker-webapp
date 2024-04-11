package it.unipd.dei.cyclek.resources;

public class User{

    private final int id;

    private final String name;

    private final String surname;
    private final String birthday;
    private final String gender;

    public User(int id, String name, String surname, String birthday, String gender) {
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

    public String getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

}
