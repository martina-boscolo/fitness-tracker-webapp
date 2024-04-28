package it.unipd.dei.cyclek.dao.user;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class RegisterUserDAO extends AbstractDAO<User> {

    private static final String QUERY = "INSERT INTO users (name, surname, birthday, gender, username, password) " +
            "                            VALUES (?, ?, ?, ?, ?, ?);";


    private final User user;

    public RegisterUserDAO(final Connection con, final User user) {
        super(con);
        this.user = user;
    }
    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(QUERY)) {
            stmnt.setString(1, user.getName());
            stmnt.setString(2, user.getSurname());
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            stmnt.setDate(3, new Date(format.parse(user.getBirthday()).getTime()));
            stmnt.setString(4, user.getGender());
            stmnt.setString(5, user.getUsername());
            stmnt.setString(6, user.getPassword());
            stmnt.execute();
            LOGGER.info("User registered {}.", user.getUsername());
        }
        this.outputParam = user;
    }
}
