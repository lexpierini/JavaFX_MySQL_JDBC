package gui;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.entities.Departement;
import model.entities.Vendeur;
import model.exceptions.ValidationException;
import model.services.ServiceDepartement;
import model.services.ServiceVendeur;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class FormeVendeurController implements Initializable {
    private Vendeur entity;
    private ServiceVendeur service;
    private ServiceDepartement serviceDepartement;
    private final List<DataChangeListener> dataChangeListeners = new ArrayList<>();
    private ObservableList<Departement> obsList;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtCourriel;
    @FXML
    private DatePicker dpDateNaissance;
    @FXML
    private TextField txtSalaireBase;
    @FXML
    ComboBox<Departement> comboBoxDepartement;
    @FXML
    private Label labelErreurNom;
    @FXML
    private Label labelErreurCourriel;
    @FXML
    private Label labelErreurDateNaissance;
    @FXML
    private Label labelErreurSalaireBase;
    @FXML
    private Button btEnregistrer;
    @FXML
    private Button btAnnuler;

    public void setVendeur(Vendeur entity) {
        this.entity = entity;
    }

    public void setServices(ServiceVendeur service, ServiceDepartement serviceDepartement) {
        this.service = service;
        this.serviceDepartement = serviceDepartement;
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

    private Vendeur getFormData() {
        Vendeur obj = new Vendeur();

        ValidationException exception = new ValidationException("Erreur de validation.");

        obj.setId(Utils.tryParseToInt(txtId.getText()));

        if (txtNom.getText() == null || txtNom.getText().trim().equals("")) {
            exception.addError("nom", "Le champ ne peut pas être vide.");
        }
        obj.setNom(txtNom.getText());

        if (txtCourriel.getText() == null || txtCourriel.getText().trim().equals("")) {
            exception.addError("courriel", "Le champ ne peut pas être vide.");
        }
        obj.setCourriel(txtCourriel.getText());

        if (dpDateNaissance.getValue() == null) {
            exception.addError("dateNaissance", "Le champ ne peut pas être vide.");
        }
        else {
            Instant instant = Instant.from(dpDateNaissance.getValue().atStartOfDay(ZoneId.systemDefault()));
            obj.setDateNaissance(Date.from(instant));
        }

        if (txtSalaireBase.getText() == null || txtSalaireBase.getText().trim().equals("")) {
            exception.addError("salaireBase", "Le champ ne peut pas être vide.");
        }
        obj.setSalaireBase(Utils.tryParseToDouble(txtSalaireBase.getText()));

        obj.setDepartement(comboBoxDepartement.getValue());

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
        Constraints.setTextFieldMaxLength(txtNom, 70);
        Constraints.setTextFieldDouble(txtSalaireBase);
        Constraints.setTextFieldMaxLength(txtCourriel, 60);
        Utils.formatDatePicker(dpDateNaissance, "yyyy-MM-dd");
        initializeComboBoxDepartment();
    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("L'entité était nulle.");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtNom.setText(entity.getNom());
        txtCourriel.setText(entity.getCourriel());
        Locale.setDefault(Locale.CANADA);
        txtSalaireBase.setText(String.format("%.2f", entity.getSalaireBase()));
        if (entity.getDateNaissance() != null) {
            dpDateNaissance.setValue(LocalDate.ofInstant(entity.getDateNaissance().toInstant(), ZoneId.systemDefault()));
        }
        if (entity.getDepartement() == null) {
            comboBoxDepartement.getSelectionModel().selectFirst();
        }
        else {
            comboBoxDepartement.setValue(entity.getDepartement());
        }
    }

    public void loadAssociateObjects() {
        if (serviceDepartement == null) {
            throw new IllegalStateException("DepartementService était nul");
        }
        List<Departement> list = serviceDepartement.findAll();
        obsList = FXCollections.observableArrayList(list);
        comboBoxDepartement.setItems(obsList);
    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        labelErreurNom.setText(fields.contains("nom") ? errors.get("nom") : "");
        labelErreurCourriel.setText(fields.contains("courriel") ? errors.get("courriel") : "");
        labelErreurSalaireBase.setText(fields.contains("salaireBase") ? errors.get("salaireBase") : "");
        labelErreurDateNaissance.setText(fields.contains("dateNaissance") ? errors.get("dateNaissance") : "");
    }

    private void initializeComboBoxDepartment() {
        Callback<ListView<Departement>, ListCell<Departement>> factory = lv -> new ListCell<Departement>() {
            @Override
            protected void updateItem(Departement item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getNom());
            }
        };
        comboBoxDepartement.setCellFactory(factory);
        comboBoxDepartement.setButtonCell(factory.call(null));
    }
}
