package com.cbozan.dao;


import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.List;

import com.cbozan.entity.Employer;
import com.cbozan.entity.Employer.EmployerBuilder;
import com.cbozan.exception.EntityException;
import com.cbozan.util.ExceptionUtil;

public class EmployerDAO {
	
	private final HashMap<Integer, Employer> cache = new HashMap<>();
	private boolean usingCache = true;
	
	private EmployerDAO() {list();}

	public Employer findById(int id) {
		
		if(!usingCache)
			list();
		
		if(cache.containsKey(id))
			return cache.get(id);
		return null;
	}

	/**
	 * Fetches all employs from db and updates the cache
	 * @return A list of all the employs
	 */
	public List<Employer> list() {
		if (usingCache && !cache.isEmpty()) {
			return new ArrayList<>(cache.values());
		}

		List<Employer> list = new ArrayList<>();

		try (Connection conn = DB.getConnection();
			 Statement st = conn.createStatement();
			 ResultSet rs = st.executeQuery("SELECT * FROM employer;")) {

			addEmployersResultToListAndCache(rs, list);

		} catch (SQLException e) {
			ExceptionUtil.showSQLException(e);
		}

		return list;
	}

	private void addEmployersResultToListAndCache(ResultSet resultSet, List<Employer> employers) throws SQLException {
		while (resultSet.next()) {
			try {
				Employer employer = new EmployerBuilder().buildEmployerFromResultSet(resultSet);
				employers.add(employer);
				cache.put(employer.getId(), employer);
			} catch (EntityException e) {
				ExceptionUtil.showEntityException(e, resultSet.getString("fname") + " " + resultSet.getString("lname"));
			}
		}
	}

	public boolean create(Employer employer) {

		if(createControl(employer) == false)
			return false;

		Connection conn;
		PreparedStatement pst;
		int result = 0;
		String query = "INSERT INTO employer (fname,lname,tel,description) VALUES (?,?,?,?);";
		String query2 = "SELECT * FROM employer ORDER BY id DESC LIMIT 1;";

		try {
			conn = DB.getConnection();
			pst = conn.prepareStatement(query);
			pst.setString(1, employer.getFname());
			pst.setString(2, employer.getLname());

			if(employer.getTel() == null)
				pst.setArray(3, null);
			else {
				java.sql.Array phones = conn.createArrayOf("VARCHAR", employer.getTel().toArray());
				pst.setArray(3, phones);
			}

			pst.setString(4, employer.getDescription());
			result = pst.executeUpdate();

			// adding cache
			if(result != 0) {

				ResultSet rs = conn.createStatement().executeQuery(query2);

				while (rs.next()) {
					try {
						Employer createdEmployer = new EmployerBuilder().buildEmployerFromResultSet(rs);
						cache.put(createdEmployer.getId(), createdEmployer);
					} catch (EntityException e) {
						ExceptionUtil.showEntityException(e, rs.getString("fname") + " " + rs.getString("lname"));
					}
				}
			}

		} catch (SQLException e) {
			ExceptionUtil.showSQLException(e);
		}

		return result == 0 ? false : true;
	}
	
	private boolean createControl(Employer employer) {
		
		for(Entry<Integer, Employer> obj : cache.entrySet()) {
			if(obj.getValue().getFname().equals(employer.getFname())
					&& obj.getValue().getLname().equals(employer.getLname())) {
				
				DB.ERROR_MESSAGE = obj.getValue().getFname() + " " + obj.getValue().getLname() + " kaydı zaten mevcut.";
				return false;
			}
		}
		
		return true;
	}
	
	
	public boolean update(Employer employer) {
		
		if(updateControl(employer) == false)
			return false;
		
		Connection conn;
		PreparedStatement pst;
		int result = 0;
		String query = "UPDATE employer SET fname=?,"
				+ "lname=?, tel=?, description=? WHERE id=?;";
		
		try {
			conn = DB.getConnection();
			pst = conn.prepareStatement(query);
			pst.setString(1, employer.getFname());
			pst.setString(2, employer.getLname());
			
			Array phones = conn.createArrayOf("VARCHAR", employer.getTel().toArray());
			pst.setArray(3, phones);
			
			pst.setString(4, employer.getDescription());
			pst.setInt(5, employer.getId());
			
			result = pst.executeUpdate();
			
			// update cache
			if(result != 0) {
				cache.put(employer.getId(), employer);
			}
			
		} catch (SQLException e) {
			ExceptionUtil.showSQLException(e);
		}
		
		return result == 0 ? false : true;
	}
	
	private boolean updateControl(Employer employer) {
		for(Entry<Integer, Employer> obj : cache.entrySet()) {
			if(obj.getValue().getFname().equals(employer.getFname()) 
					&& obj.getValue().getLname().equals(employer.getLname()) 
					&& obj.getValue().getId() != employer.getId()) {
				DB.ERROR_MESSAGE = obj.getValue().getFname() + " " + obj.getValue().getLname() + " kaydı zaten mevcut.";
				return false;
			}
		}
		return true;
	}
	
	public boolean delete(Employer employer) {
		
		Connection conn;
		PreparedStatement ps;
		int result = 0;
		String query = "DELETE FROM employer WHERE id=?;";
		
		try {
			
			conn = DB.getConnection();
			ps = conn.prepareStatement(query);
			ps.setInt(1, employer.getId());
			result = ps.executeUpdate();
			
			if(result != 0) {
				cache.remove(employer.getId());
			}

			
		} catch (SQLException e) {
			ExceptionUtil.showSQLException(e);
		}
		
		return result == 0 ? false : true;
		
	}
	
	
	public boolean isUsingCache() {
		return this.usingCache;
	}
	
	public void setUsingCache(boolean usingCache) {
		this.usingCache = usingCache;
	}
	
	private static class EmployerDAOHelper{
		private static final EmployerDAO instance = new EmployerDAO();
	}
	
	public static EmployerDAO getInstance() {
		return EmployerDAOHelper.instance;
	}
	
}
