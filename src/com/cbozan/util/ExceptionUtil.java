package com.cbozan.util;

import com.cbozan.exception.EntityException;

import javax.swing.*;
import java.sql.SQLException;

public class ExceptionUtil {

    private ExceptionUtil() {}

    public static void showEntityException(EntityException e, String msg) {
        String message = msg + " not added" +
                "\n" + e.getMessage() + "\n" + e.getLocalizedMessage() + e.getCause();
        JOptionPane.showMessageDialog(null, message);
    }

    public static void showSQLException(SQLException e) {
        String message = e.getErrorCode() + "\n" + e.getMessage() + "\n" + e.getLocalizedMessage() + "\n" + e.getCause();
        JOptionPane.showMessageDialog(null, message);
    }

}
