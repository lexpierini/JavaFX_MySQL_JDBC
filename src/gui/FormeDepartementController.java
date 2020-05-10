package gui;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class FormeDepartementController implements Initializable {
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
}
