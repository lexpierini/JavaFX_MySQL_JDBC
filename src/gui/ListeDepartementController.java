package gui;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Departement;
import model.services.ServiceDepartement;

import java.io.IOException;
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
    public void onBtNouveauAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Departement obj = new Departement();
        createDialogForm(obj, "/gui/FormeDepartement.fxml", parentStage);
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

    // Ouvre la fenêtre du formulaire des départements à remplir.
    private void createDialogForm(Departement obj, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            FormeDepartementController controller = loader.getController();
            controller.setDepartement(obj);
            controller.setServiceDepartement(new ServiceDepartement());
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Entrer les données du département:");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL); // Affiche la fenêtre précédente uniquement si la fenêtre actuelle est fermée.
            dialogStage.showAndWait();
        }
        catch (IOException e) {
            Alerts.showAlert("IO Exception", "Erreur lors du chargement de la vue.", e.getMessage(), AlertType.ERROR);
        }
    }
}
