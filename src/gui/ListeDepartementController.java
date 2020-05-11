package gui;

import application.Main;
import db.DbException;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Departement;
import model.services.ServiceDepartement;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListeDepartementController implements Initializable, DataChangeListener {
    private ServiceDepartement service;

    @FXML
    private TableView<Departement> tableViewDepartements;
    @FXML
    private TableColumn<Departement, Integer> tableColumnId;
    @FXML
    private TableColumn<Departement, String> tableColumnNom;
    @FXML
    private TableColumn<Departement, Departement> tableColumnEDIT;
    @FXML
    private TableColumn<Departement, Departement> tableColumnREMOVE;
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
        initEditButtons();
        initRemoveButtons();
    }

    // Ouvre la fenêtre du formulaire des départements à remplir.
    private void createDialogForm(Departement obj, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            FormeDepartementController controller = loader.getController();
            controller.setDepartement(obj);
            controller.setServiceDepartement(new ServiceDepartement());
            controller.subscribeDataChangeListener(this);
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

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    // Crée un bouton d'édition sur chaque ligne du tableau, permettant aux départements d'être édités.
    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Departement, Departement>() {
            private final Button button = new Button("edit");
            @Override
            protected void updateItem(Departement obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                obj, "/gui/FormeDepartement.fxml",Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Departement, Departement>() {
            private final Button button = new Button("supprimer");
            @Override
            protected void updateItem(Departement obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void removeEntity(Departement obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Voulez-vous vraiment supprimer?");

        if (result.get() == ButtonType.OK) {
            if (service == null) {
                throw new IllegalStateException("Le service était nul");
            }
            try {
                service.remove(obj);
                updateTableView();
            }
            catch (DbException e) {
                Alerts.showAlert("Erreur lors de la suppression de l'objet", null, e.getMessage(), AlertType.ERROR);
            }
        }
    }
}
