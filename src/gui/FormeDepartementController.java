package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Departement;
import model.exceptions.ValidationException;
import model.services.ServiceDepartement;

import java.net.URL;
import java.util.*;

public class FormeDepartementController implements Initializable {
    private Departement entity;
    private ServiceDepartement service;
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNom;
    @FXML
    private Label labelErreurNom;
    @FXML
    private Button btEnregistrer;
    @FXML
    private Button btAnnuler;

    public void setDepartement(Departement entity) {
        this.entity = entity;
    }

    public void setServiceDepartement(ServiceDepartement service) {
        this.service = service;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtEnregistrerAction(ActionEvent event) {
        if (entity == null) {
            throw new IllegalStateException("L'entité était nulle.");
        }
        if (service == null) {
            throw new IllegalStateException("Service était nul");
        }
        try {
            entity = getFormData();
            service.saveOrUpdate(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        }
        catch (ValidationException e) {
            setErrorMessages(e.getErrors());
        }
        catch (DbException e) {
            Alerts.showAlert("Erreur lors de l'enregistrement de l'objet.", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    private Departement getFormData() {
        Departement obj = new Departement();

        ValidationException exception = new ValidationException("Erreur de validation.");

        obj.setId(Utils.tryParseToInt(txtId.getText()));

        if (txtNom.getText() == null || txtNom.getText().trim().equals("")) {
            exception.addError("nom", "Le champ ne peut pas être vide.");
        }
        obj.setNom(txtNom.getText());

        if (exception.getErrors().size() > 0) {
            throw exception;
        }

        return obj;
    }

    @FXML
    public void onBtAnnulerAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtNom, 30);
    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("L'entité était nulle.");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtNom.setText(entity.getNom());
    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        if (fields.contains("nom")) {
            labelErreurNom.setText(errors.get("nom"));
        }
    }
}
