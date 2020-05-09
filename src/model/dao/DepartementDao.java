package model.dao;

import java.util.List;

import model.entities.Departement;

public interface DepartementDao {
	
	void insert(Departement obj);
	void update(Departement obj);
	void deleteById(Integer id);
	Departement findById(Integer id);
	List<Departement> findAll();
}
