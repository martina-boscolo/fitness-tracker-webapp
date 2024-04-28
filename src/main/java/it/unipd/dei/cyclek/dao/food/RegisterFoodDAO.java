package it.unipd.dei.cyclek.dao.food;

import it.unipd.dei.cyclek.dao.AbstractDAO;
import it.unipd.dei.cyclek.resources.entity.Food;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterFoodDAO extends AbstractDAO<Food> {

    private static final String QUERY = "INSERT INTO foods (fdnm, kcal, fats, carbohydrates, proteins) " +
            "                            VALUES (?, ?, ?, ?, ?)";

    private final Food food;

    public RegisterFoodDAO(final Connection con, final Food food) {
        super(con);
        this.food = food;
    }
    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(QUERY)) {
            int i=1;
            stmnt.setString(i++, food.getFdnm());
            stmnt.setInt(i++, food.getKcal());
            stmnt.setInt(i++, food.getFats());
            stmnt.setInt(i++, food.getCarbs());
            stmnt.setInt(i++, food.getProt());
            stmnt.execute();
            LOGGER.info("Food registered {}.", food.getFdnm());
        }
        this.outputParam = food;
    }
}
