package gui;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Departement;
import model.services.ServiceDepartement;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ListeDepartementController implements Initializable {
    private ServiceDepartement service;

    @FXML
    private TableView<Departement> tableViewDepartements;
    @FXML
    private TableColumn<Departement, Integer> tableColumnId;
    @FXML
    private TableColumn<Departement, String> tableColumnNom;
    @FXML
    private Button btNouveau;

    private ObservableList<Departement> obsList;

    @FXML
    public void onBtNouveauAction() {
        System.out.println("onBtNouveauAction");
    }

    public void setServiceDepartement(ServiceDepartement service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    // Norme JavaFX pour le démarrage du comportement des colonnes.
    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnNom.setCellValueFactory(new PropertyValueFactory<>("nom"));

        // Adaptez la fenêtre du tableau à la fenêtre principale.
        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewDepartements.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Le service était nul");
        }
        List<Departement> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewDepartements.setItems(obsList);
    }
}
