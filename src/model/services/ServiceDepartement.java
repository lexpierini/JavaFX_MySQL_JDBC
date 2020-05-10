package model.services;

import model.dao.DaoFactory;
import model.dao.DepartementDao;
import model.entities.Departement;

import java.util.List;

public class ServiceDepartement {
    private final DepartementDao dao = DaoFactory.createDepartementDao();

    public List<Departement> findAll() {
       return dao.findAll();
    }

    public void saveOrUpdate(Departement obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        }
        else {
            dao.update(obj);
        }
    }
}
