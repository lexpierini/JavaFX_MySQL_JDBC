package model.dao;

import db.DB;
import model.dao.implementation.DepartementDaoJDBC;
import model.dao.implementation.VendeurDaoJDBC;

public class DaoFactory {

	public static VendeurDao createVendeurDao() {
		return new VendeurDaoJDBC(DB.getConnection());
	}
	
	public static DepartementDao createDepartementDao() {
		return new DepartementDaoJDBC(DB.getConnection());
	}
}
