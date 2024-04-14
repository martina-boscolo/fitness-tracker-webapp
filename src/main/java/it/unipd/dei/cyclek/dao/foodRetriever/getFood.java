package it.unipd.dei.cyclek.dao.foodRetriever;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.User;

import java.sql.Connection;
import java.util.List;

public final class  getFood extends AbstractDAO<List<User>> {
    /**
     * Creates a new DAO object.
     *
     * @param con the connection to be used for accessing the database.
     */
    public getFood(Connection con) {
        super(con);
    }

    @Override
    protected void doAccess() throws Exception {

    }
}
