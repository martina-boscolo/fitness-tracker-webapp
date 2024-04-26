package it.unipd.dei.cyclek.dao.user;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class UpdateUserDAO  extends AbstractDAO<Boolean> {
    public static final String QUERY = "UPDATE users " +
            "SET name = ?, surname = ?, birthday = ?, gender = ?, username = ?, password = ? " +
            "WHERE id = ?;";

    private final User user;

    public UpdateUserDAO(Connection con, User user) {
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
            stmnt.setInt(7, user.getId());

            int rows = stmnt.executeUpdate();
            this.outputParam = rows > 0;
        }

    }
}
