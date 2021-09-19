package com.pluralsight.order.dao;

import com.pluralsight.order.dto.ParamsDto;
import com.pluralsight.order.util.Database;
import com.pluralsight.order.util.ExceptionHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DAO to delete an order
 */
public class DeleteOrderDao {
    private Database database;

    /**
     * Constructor
     * @param database Database object
     */
    public DeleteOrderDao(Database database) {
        this.database = database;
    }

    /**
     * Deletes one or more orders using their IDs
     * @param paramsDto Object with the parameters for the operation
     * @return Number of orders deleted
     */
    public int deleteOrdersById(ParamsDto paramsDto) {
        int numberResults = 0;

        try (Connection con = database.getConnection();
             PreparedStatement ps = createPreparedStatement(con, paramsDto.getOrderIds())
        ) {
            numberResults = ps.executeUpdate();
        } catch (SQLException ex) {
            ExceptionHandler.handleException(ex);
        }

        return numberResults;
    }

    /**
     * Method to build the delete SQL statement
     * @param orderIds IDs of the orders to delete
     * @return Delete SQL statement
     */
    private String buildDeleteSql(List<Long> orderIds) {
        /*
         * One way to do it is to generate the SQL directly, instead of streams instead of the suggested
         * 'String.join() and Collections.nCopies()' solution
         * String ids = orderIds.stream().map(Object::toString).collect(Collectors.joining(","));
         * But then ... there is no point of using a PreparedStatement.
         * The upside of using a PreparedStatement is that it will protect the code from SQL injection attacks.
         */
        final List<String> placeHolders = Collections.nCopies(orderIds.size(), "?");
        final String ids = String.join(",", placeHolders);
        return "DELETE FROM orders o WHERE o.order_id IN (" + ids + ")";
    }

    /**
     * Creates a PreparedStatement object to delete one or more orders
     * @param con Connection object
     * @param orderIds Order IDs to set on the PreparedStatement
     * @return A PreparedStatement object
     * @throws SQLException In case of an error
     */
    private PreparedStatement createPreparedStatement(Connection con, List<Long> orderIds) throws SQLException {
        String sql = buildDeleteSql(orderIds);
        PreparedStatement ps = con.prepareStatement(sql);
        for (int i = 1; i <= orderIds.size(); i++) {
            ps.setLong(i, orderIds.get(i - 1));
        }
        return ps;
    }
}
