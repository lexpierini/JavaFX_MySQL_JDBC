package model.services;

import model.dao.DaoFactory;
import model.dao.DepartementDao;
import model.entities.Departement;

import java.util.ArrayList;
import java.util.List;

public class ServiceDepartement {
    private DepartementDao dao = DaoFactory.createDepartementDao();

    public List<Departement> findAll() {
       return dao.findAll();
    }
}
