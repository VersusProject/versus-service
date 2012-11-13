/*
 * This software was developed at the National Institute of Standards and
 * Technology by employees of the Federal Government in the course of
 * their official duties. Pursuant to title 17 Section 105 of the United
 * States Code this software is not subject to copyright protection and is
 * in the public domain. This software is an experimental system. NIST assumes
 * no responsibility whatsoever for its use by other parties, and makes no
 * guarantees, expressed or implied, about its quality, reliability, or
 * any other characteristic. We would appreciate acknowledgement if the
 * software is used.
 */
package edu.illinois.ncsa.versus.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author antoinev
 */
public class JDBCConnectionProvider {

    private Connection con;
    
    public Connection getConnection() {
        if (con == null) {
            con = openConnection();
        } else {
            boolean valid = false;
            try {
                valid = con.isValid(1);
            } catch (SQLException ex) {
                Logger.getLogger(JDBCConnectionProvider.class.getName()).log(Level.SEVERE, "Cannot determine if connection is valid.", ex);
            }
            if (!valid) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(JDBCConnectionProvider.class.getName()).log(Level.WARNING, "Cannot close invalid connection.", ex);
                }
                con = openConnection();
            }
        }
        return con;
    }

    private Connection openConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost/versus",
                    "versus", "versus");
        } catch (SQLException e) {
            Logger.getLogger(JDBCConnectionProvider.class.getName()).log(Level.SEVERE, null, e);
        } catch (ClassNotFoundException e) {
            Logger.getLogger(JDBCConnectionProvider.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}
