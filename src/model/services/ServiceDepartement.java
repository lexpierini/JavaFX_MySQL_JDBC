package model.services;

import model.entities.Departement;

import java.util.ArrayList;
import java.util.List;

public class ServiceDepartement {
    public List<Departement> findAll() {
        List<Departement> list = new ArrayList<>();
        list.add(new Departement(1, "Livres"));
        list.add(new Departement(2, "Ordinateurs"));
        list.add(new Departement(3, "Électroniques"));
        return list;
    }
}
