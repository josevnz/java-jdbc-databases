package com.pluralsight.order.util;

import java.sql.SQLException;

/**
 * Utility class to handle exceptions
 */
public class ExceptionHandler {

    /**
     * Method to extract and print information from a SQLException
     * @param sqlException Exception from which information will be extracted
     */
    public static void handleException(SQLException sqlException) {
        System.err.printf("Error code: %d%n", sqlException.getErrorCode());
        String state = sqlException.getSQLState();
        System.err.printf("Error state: %s%n", state == null? "": state);
        String message = sqlException.getMessage();
        System.err.printf("Error message: %s%n", message == null? "": message);
        System.err.printf("Stacktrace:");
        sqlException.printStackTrace();
    }
}
