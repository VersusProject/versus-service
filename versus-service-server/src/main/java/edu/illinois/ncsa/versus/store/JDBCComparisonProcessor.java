package edu.illinois.ncsa.versus.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.illinois.ncsa.versus.core.comparison.Comparison;
import edu.illinois.ncsa.versus.core.comparison.Comparison.ComparisonStatus;

public class JDBCComparisonProcessor implements ComparisonProcessor {

    private Connection con;

    public JDBCComparisonProcessor() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/versus",
                    "versus", "versus");
        } catch (SQLException e) {
            Logger.getLogger(JDBCComparisonProcessor.class.getName()).log(Level.SEVERE, null, e);
        } catch (ClassNotFoundException e) {
            Logger.getLogger(JDBCComparisonProcessor.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void addComparison(Comparison comparison) {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("INSERT INTO comparisons (id, firstDataset, secondDataset, adapterId, extractorId, measureId)"
                    + " VALUES ('"
                    + comparison.getId()
                    + "', '"
                    + comparison.getFirstDataset()
                    + "', '"
                    + comparison.getSecondDataset()
                    + "', '"
                    + comparison.getAdapterId()
                    + "', '"
                    + comparison.getExtractorId()
                    + "', '"
                    + comparison.getMeasureId() + "')");
        } catch (SQLException e) {
            Logger.getLogger(JDBCComparisonProcessor.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(JDBCComparisonProcessor.class.getName()).log(Level.SEVERE, null, e);
            }
        }

    }

    @Override
    public Comparison getComparison(String id) {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM comparisons WHERE id = '" + id
                    + "'");
            while (rs.next()) {
                Comparison comparison = new Comparison();
                comparison.setId(rs.getString("id"));
                comparison.setFirstDataset(rs.getString("firstDataset"));
                comparison.setSecondDataset(rs.getString("secondDataset"));
                comparison.setAdapterId(rs.getString("adapterId"));
                comparison.setExtractorId(rs.getString("extractorId"));
                comparison.setMeasureId(rs.getString("measureId"));
                comparison.setStatus(ComparisonStatus.valueOf(rs.getString("status")));
                comparison.setValue(rs.getString("value"));
                return comparison;
            }
        } catch (SQLException e) {
            Logger.getLogger(JDBCComparisonProcessor.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                Logger.getLogger(JDBCComparisonProcessor.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }

    @Override
    public Collection<Comparison> listAll() {
        Collection<Comparison> comparisons = new ArrayList<Comparison>();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM comparisons");
            while (rs.next()) {
                Comparison comparison = new Comparison();
                comparison.setId(rs.getString("id"));
                comparison.setFirstDataset(rs.getString("firstDataset"));
                comparison.setSecondDataset(rs.getString("secondDataset"));
                comparison.setAdapterId(rs.getString("adapterId"));
                comparison.setExtractorId(rs.getString("extractorId"));
                comparison.setMeasureId(rs.getString("measureId"));
                comparison.setStatus(ComparisonStatus.valueOf(rs.getString("status")));
                comparison.setValue(rs.getString("value"));
                comparisons.add(comparison);
            }
        } catch (SQLException e) {
            Logger.getLogger(JDBCComparisonProcessor.class.getName()).log(Level.SEVERE, null, e);
        }
        return comparisons;
    }

    @Override
    public void updateValue(String id, String value) {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("UPDATE comparisons SET value='" + value
                    + "' WHERE id ='" + id + "'");
        } catch (SQLException e) {
            Logger.getLogger(JDBCComparisonProcessor.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void setStatus(String id, ComparisonStatus status) {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate("UPDATE comparisons SET status='"
                    + status.name() + "' WHERE id ='" + id + "'");
        } catch (SQLException e) {
            Logger.getLogger(JDBCComparisonProcessor.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public ComparisonStatus getStatus(String id) {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT status FROM comparisons WHERE id = '"
                    + id + "'");
            while (rs.next()) {
                ComparisonStatus status = ComparisonStatus.valueOf(rs.getString("status"));
                return status;
            }
            return null;
        } catch (SQLException e) {
            Logger.getLogger(JDBCComparisonProcessor.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public void setError(String id, String error) {
        //TODO implements it by adding a column in the table.
    }
}
