package model.dao;

import java.util.List;

import model.entities.Departement;
import model.entities.Vendeur;

public interface VendeurDao {
	
	void insert(Vendeur obj);
	void update(Vendeur obj);
	void deleteById(Integer id);
	Vendeur findById(Integer id);
	List<Vendeur> findAll();
	List<Vendeur> findByDepartement(Departement departement);
}
