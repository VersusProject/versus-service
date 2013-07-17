package edu.illinois.ncsa.versus.store;
/* 
 * 
 * @author Smruti Padhy
 * 
 * */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import edu.illinois.ncsa.versus.rest.MapObject;



public class JDBCMapObjectProcessor implements MapObjectProcessor {
	private Connection con;

	public JDBCMapObjectProcessor() {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/versus",
					"versus", "versus");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	@Override
	public void addMapObject(MapObject map) {
		Statement stmt = null;
		System.out.println("INSIDE JDBC ADD MAP method");
		try {
			stmt = con.createStatement();
			stmt.executeUpdate("INSERT INTO mapObjects (versusId,mediciId,indexId,buildStatus)"
					+ " VALUES ('"
					+ map.getversusId()
					+ "', '"
					+ map.getmediciId()
					+ "', '"
					+ map.getindexId()
					+ "', '"
					+ map.getbuildStatus()
					 + "')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public MapObject getMapObject(String versusId) {
		
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM mapObjects WHERE versusId = '" + versusId
							+ "'");
			while (rs.next()) {
				MapObject map=new MapObject();
				map.setversusId(rs.getString("versusId"));
				map.setmediciId(rs.getString("mediciId"));
				map.setindexId(rs.getString("indexId"));
				map.setbuildStatus(rs.getString("buildStatus"));
				return map;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	
	}

	@Override
	public void updateBuildStatus(String indexId, String buildStatus) {
		
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			//ResultSet rs=stmt.executeQuery("SELECT * FROM mapObjects WHERE indexId = '" + indexId
			//		+ "' AND buildStatus='"+"false'");
			//while(rs.next()){
				//if(rs.getString("buildStatus").equals("false"))
					stmt.executeUpdate("UPDATE mapObjects SET buildStatus='" + buildStatus
					+ "' WHERE indexId ='" +indexId + "' AND buildStatus='"+"false'");
			//}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	@Override
	public String getBuildStatus(String versusId) {
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT buildStatus FROM mapObjects WHERE versusId = '"
							+ versusId + "'");
			while (rs.next()) {
				String buildStatus = rs.getString("buildStatus");
				return buildStatus;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	@Override
	public void setBuildStatus(String versusId, String buildStatus) {
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			stmt.executeUpdate("UPDATE mapObjects SET buildStatus='"
					+ buildStatus + "' WHERE versusId ='" + versusId + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
