package gui;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Departement;

import java.net.URL;
import java.util.ResourceBundle;

public class FormeDepartementController implements Initializable {
    private Departement entity;
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

    @FXML
    public void onBtEnregistrerAction() {
        System.out.println("onBtEnregistrerAction");
    }

    @FXML
    public void onBtAnnulerAction() {
        System.out.println("onBtAnnulerAction");
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
}
