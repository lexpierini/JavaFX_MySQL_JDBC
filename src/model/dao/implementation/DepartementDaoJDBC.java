package model.dao.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartementDao;
import model.entities.Departement;

public class DepartementDaoJDBC implements DepartementDao {

	private Connection conn;
	
	public DepartementDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Departement obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
				"INSERT INTO departement (Nom) "
				+ "VALUES(?)",
				Statement.RETURN_GENERATED_KEYS
			);
			
			st.setString(1, obj.getNom());
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
				
			} else {
				throw new DbException("Erreur inattendue! Aucune ligne affectée!");
			}
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
			
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Departement obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
				"UPDATE departement "
				+ "SET Nom = ? "
				+ "WHERE Id = ?"
			);
			
			st.setString(1, obj.getNom());
			st.setInt(2, obj.getId());
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
			
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
				"DELETE FROM departement "
				+ "WHERE Id = ?"
			);
			
			st.setInt(1, id);
			int rows = st.executeUpdate();
			
			if (rows == 0) {
				throw new DbException("L'id n'existe pas!");
			}
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
			
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Departement findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
				"SELECT * FROM departement "
				+ "WHERE Id = ?"
			);
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if (rs.next()) {
				Departement obj = new Departement();
				obj.setId(rs.getInt("Id"));
				obj.setNom(rs.getString("Nom"));
				return obj;
			}
			
			return null;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
			
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Departement> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
				"SELECT * FROM departement "
				+ "ORDER BY Nom"
			);
			
			rs = st.executeQuery();
			
			List<Departement> list = new ArrayList<>();
			
			while (rs.next()) {
				Departement obj = new Departement();
				obj.setId(rs.getInt("Id"));
				obj.setNom(rs.getString("Nom"));
				list.add(obj);
			}
			
			return list;
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
			
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
