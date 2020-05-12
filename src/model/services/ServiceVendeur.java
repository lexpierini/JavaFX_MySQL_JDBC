package model.services;

import model.dao.DaoFactory;
import model.dao.VendeurDao;
import model.entities.Vendeur;

import java.util.List;

public class ServiceVendeur {
    private final VendeurDao dao = DaoFactory.createVendeurDao();

    public List<Vendeur> findAll() {
       return dao.findAll();
    }

    public void saveOrUpdate(Vendeur obj) {
        if (obj.getId() == null) {
            dao.insert(obj);
        }
        else {
            dao.update(obj);
        }
    }

    public void remove(Vendeur obj) {
        dao.deleteById(obj.getId());
    }
}
