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
package gov.nist.itl.ssd.versus.store;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.illinois.ncsa.versus.store.JDBCComparisonProcessor;
import gov.nist.itl.ssd.sampling.Sampling;
import gov.nist.itl.ssd.sampling.Sampling.SamplingStatus;

/**
 *
 * @author antoinev
 */
public class JDBCSamplingService implements SamplingService {

    private Connection con;

    public JDBCSamplingService() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/versus",
                    "versus", "versus");
            con.setAutoCommit(false);
            con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        } catch (SQLException e) {
            Logger.getLogger(JDBCComparisonProcessor.class.getName()).log(Level.SEVERE, null, e);
        } catch (ClassNotFoundException e) {
            Logger.getLogger(JDBCComparisonProcessor.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void addSampling(Sampling sampling) {
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("INSERT INTO samplings "
                    + "(id, individual, sampler, sampleSize) "
                    + "VALUES (?, ?, ?, ?)");
            stmt.setString(1, sampling.getId());
            stmt.setString(2, sampling.getIndividual());
            stmt.setString(3, sampling.getSampler());
            stmt.setInt(4, sampling.getSampleSize());
            stmt.executeUpdate();

            for (String ds : sampling.getDatasets()) {
                PreparedStatement ps = null;
                try {
                    ps = con.prepareStatement(
                            "INSERT INTO sampling_datasets "
                            + "(id, sampling)"
                            + " VALUES (?, ?)");
                    ps.setString(1, ds);
                    ps.setString(2, sampling.getId());
                    ps.executeUpdate();
                } finally {
                    try {
                        if (ps != null) {
                            ps.close();
                        }
                    } catch (SQLException e) {
                        Logger.getLogger(JDBCSamplingService.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            }
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(JDBCSamplingService.class.getName()).log(Level.SEVERE, null, ex);
            }
            Logger.getLogger(JDBCSamplingService.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(JDBCSamplingService.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    @Override
    public Sampling getSampling(String id) {
        PreparedStatement dsStmt = null;
        PreparedStatement samplingStmt = null;
        try {
            dsStmt = con.prepareStatement("SELECT * FROM sampling_datasets WHERE sampling=?");
            dsStmt.setString(1, id);
            ResultSet dsRs = dsStmt.executeQuery();

            samplingStmt = con.prepareStatement("SELECT * FROM samplings WHERE id=?");
            samplingStmt.setString(1, id);
            ResultSet samplingRs = samplingStmt.executeQuery();
            con.commit();
            
            if (!samplingRs.next()) {
                return null;
            }

            return getSamplingFromResultSet(dsRs, samplingRs);
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(JDBCSamplingService.class.getName()).log(Level.SEVERE, null, ex);
            }
            Logger.getLogger(JDBCSamplingService.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                if (dsStmt != null) {
                    dsStmt.close();
                }
                if (samplingStmt != null) {
                    samplingStmt.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(JDBCSamplingService.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return null;
    }

    private Sampling getSamplingFromResultSet(ResultSet datasets,
            ResultSet sampling) throws SQLException {
        String id = sampling.getString("id");
        String individual = sampling.getString("individual");
        String sampler = sampling.getString("sampler");
        int sampleSize = sampling.getInt("sampleSize");
        String status = sampling.getString("status");
        String error = sampling.getString("error");

        ArrayList<String> datasetsList = new ArrayList<String>();
        ArrayList<String> sample = new ArrayList<String>();
        while (datasets.next()) {
            String ds = datasets.getString("id");
            boolean selected = datasets.getBoolean("selected");
            datasetsList.add(ds);
            if (selected) {
                sample.add(ds);
            }
        }

        Sampling result = new Sampling(id, individual, sampler, sampleSize,
                datasetsList);
        result.setSample(sample);
        if(status!= null) {
            result.setStatus(SamplingStatus.valueOf(status));
        }
        result.setError(error);
        return result;
    }

    @Override
    public Collection<Sampling> listAll() {
        ArrayList<Sampling> samplings = new ArrayList<Sampling>();
        Statement samplingStmt = null;
        try {
            samplingStmt = con.createStatement();
            ResultSet rs = samplingStmt.executeQuery("SELECT * FROM samplings");
            while (rs.next()) {
                String id = rs.getString("id");
                PreparedStatement ps = null;
                try {
                    ps = con.prepareStatement(
                            "SELECT * FROM sampling_datasets WHERE sampling=?");
                    ps.setString(1, id);
                    ResultSet datasets = ps.executeQuery();
                    Sampling sampling = getSamplingFromResultSet(datasets, rs);
                    samplings.add(sampling);
                } finally {
                    try {
                        if (ps != null) {
                            ps.close();
                        }
                    } catch (SQLException e) {
                        Logger.getLogger(JDBCSamplingService.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
            }
            con.commit();
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(JDBCSamplingService.class.getName()).log(Level.SEVERE, null, ex);
            }
            Logger.getLogger(JDBCSamplingService.class.getName()).log(Level.SEVERE, null, e);
            return null;
        } finally {
            try {
                if (samplingStmt != null) {
                    samplingStmt.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(JDBCSamplingService.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return samplings;
    }

    @Override
    public void updateSample(String id, List<String> sample) {
        StringBuilder query = new StringBuilder("UPDATE sampling_datasets "
                + "SET selected=TRUE WHERE sampling=? AND ( ");
        Iterator<String> iterator = sample.iterator();
        if (iterator.hasNext()) {
            while (true) {
                iterator.next();
                query.append("id=?");
                if (!iterator.hasNext()) {
                    break;
                }
                query.append(" OR ");
            }
        }
        query.append(" )");

        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query.toString());
            ps.setString(1, id);
            int i = 2;
            for (String ind : sample) {
                ps.setString(i, ind);
                i++;
            }
            int nbUpdate = ps.executeUpdate();
            if(nbUpdate != sample.size()) {
                throw new RuntimeException("Incorrect sample size updated.");
            }
            con.commit();
        } catch (Exception ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(JDBCSamplingService.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(JDBCSamplingService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(JDBCSamplingService.class.getName()).log(Level.SEVERE, null, e);
            }
        }

    }

    @Override
    public void setStatus(String id, SamplingStatus status) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("UPDATE samplings SET status=? WHERE id=?");
            ps.setString(1, status.toString());
            ps.setString(2, id);
            ps.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(JDBCSamplingService.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(JDBCSamplingService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(JDBCSamplingService.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    @Override
    public void setError(String id, String error) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement("UPDATE samplings SET error=? WHERE id=?");
            ps.setString(1, error);
            ps.setString(2, id);
            ps.executeUpdate();
            con.commit();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(JDBCSamplingService.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(JDBCSamplingService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(JDBCSamplingService.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}
