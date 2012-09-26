package edu.illinois.ncsa.versus.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("INSERT INTO comparisons "
                    + "(id, firstDataset, secondDataset, "
                    + "adapterId, extractorId, measureId, slave)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, comparison.getId());
            stmt.setString(2, comparison.getFirstDataset());
            stmt.setString(3, comparison.getSecondDataset());
            stmt.setString(4, comparison.getAdapterId());
            stmt.setString(5, comparison.getExtractorId());
            stmt.setString(6, comparison.getMeasureId());
            stmt.setString(7, comparison.getSlave());
            stmt.executeUpdate();
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
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("SELECT * FROM comparisons WHERE id=?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return getComparisonFromResultSet(rs);
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
        return null;
    }

    @Override
    public Collection<Comparison> listAll() {
        Collection<Comparison> comparisons = new ArrayList<Comparison>();
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM comparisons");
            while (rs.next()) {
                comparisons.add(getComparisonFromResultSet(rs));
            }
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
        return comparisons;
    }

    private Comparison getComparisonFromResultSet(ResultSet rs) throws SQLException {
        Comparison comparison = new Comparison();
        comparison.setId(rs.getString("id"));
        comparison.setFirstDataset(rs.getString("firstDataset"));
        comparison.setSecondDataset(rs.getString("secondDataset"));
        comparison.setAdapterId(rs.getString("adapterId"));
        comparison.setExtractorId(rs.getString("extractorId"));
        comparison.setMeasureId(rs.getString("measureId"));
        String status = rs.getString("status");
        if (status != null) {
            comparison.setStatus(ComparisonStatus.valueOf(status));
        }
        comparison.setValue(rs.getString("value"));
        comparison.setError(rs.getString("error"));
        comparison.setSlave(rs.getString("slave"));
        return comparison;
    }

    @Override
    public void updateValue(String id, String value) {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("UPDATE comparisons SET value=? "
                    + "WHERE id=?");
            stmt.setString(1, value);
            stmt.setString(2, id);
            stmt.executeUpdate();
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
    public void setStatus(String id, ComparisonStatus status) {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("UPDATE comparisons SET status=? "
                    + "WHERE id=?");
            stmt.setString(1, status.name());
            stmt.setString(2, id);
            stmt.executeUpdate();
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
    public ComparisonStatus getStatus(String id) {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("SELECT status FROM comparisons "
                    + "WHERE id=?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ComparisonStatus status = ComparisonStatus.valueOf(rs.getString("status"));
                return status;
            }
            return null;
        } catch (SQLException e) {
            Logger.getLogger(JDBCComparisonProcessor.class.getName()).log(Level.SEVERE, null, e);
            return null;
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
    public void setError(String id, String error) {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("UPDATE comparisons SET error=? "
                    + "WHERE id=?");
            stmt.setString(1, error);
            stmt.setString(2, id);
            stmt.executeUpdate();
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
}
