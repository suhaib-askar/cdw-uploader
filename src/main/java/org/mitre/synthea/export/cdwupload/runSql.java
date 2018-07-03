package org.mitre.synthea.export.cdwupload;

import java.io.BufferedWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.function.Function;

public class runSql {
	
	public static void runUpdate(Connection con, String sql) throws Exception {
		Statement stmt = null;
		try {
			stmt = con.createStatement();
			int resultCode = stmt.executeUpdate(sql);
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			con.rollback();
			throw (e);
		}
		finally {
			try {
				stmt.close();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	public static ArrayList<String> getColumnNames(ResultSet rs) throws SQLException {
		ArrayList<String> colList = new ArrayList();
		ResultSetMetaData rsmd = rs.getMetaData();
		int colCount = rsmd.getColumnCount();
		for (int i =1; i <= colCount; i++ ) {
			colList.add( rsmd.getColumnName(i));
		}
		return(colList);
	}
	
	public static ArrayList<String> getColumnTypes(ResultSet rs) throws SQLException {
		ArrayList<String> colList = new ArrayList();
		ResultSetMetaData rsmd = rs.getMetaData();
		int colCount = rsmd.getColumnCount();
		for (int i =1; i <= colCount; i++ ) {
			colList.add( rsmd.getColumnClassName(i));
		}
		return(colList);
	}
	
	public static void runQuery(Connection con, String sql, BufferedWriter out) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			ArrayList<String> colHeaders = getColumnNames(rs);
			ArrayList<String> colTypes = getColumnTypes(rs);
			for (int i=0; i < colHeaders.size(); i++) {
				System.out.print(colHeaders.get(i) + ",");
			}
			System.out.println();
			
			StringBuilder sb = new StringBuilder();
			while (rs.next()) {
				sb.setLength(0);;
				for (int i=1; i <= colCount; i++ ) {
					if (i> 1) sb.append(",");
					sb.append(rs.getString(i));
				}	
				System.out.println(sb.toString());
				out.write(sb.toString() + "\n");
			}
			
			
			con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			con.rollback();
			throw (e);
		}
		finally {
			try {
				stmt.close();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
}
