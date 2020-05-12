package gui;

import application.Main;
import db.DbException;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Vendeur;
import model.services.ServiceDepartement;
import model.services.ServiceVendeur;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListeVendeurController implements Initializable, DataChangeListener {
    private ServiceVendeur service;

    @FXML
    private TableView<Vendeur> tableViewVendeurs;
    @FXML
    private TableColumn<Vendeur, Integer> tableColumnId;
    @FXML
    private TableColumn<Vendeur, String> tableColumnNom;
    @FXML
    private TableColumn<Vendeur, String> tableColumnCourriel;
    @FXML
    private TableColumn<Vendeur, Date> tableColumnDateNaissance;
    @FXML
    private TableColumn<Vendeur, Double> tableColumnSalaireBase;
    @FXML
    private TableColumn<Vendeur, Vendeur> tableColumnEDIT;
    @FXML
    private TableColumn<Vendeur, Vendeur> tableColumnREMOVE;
    @FXML
    private Button btNouveau;

    private ObservableList<Vendeur> obsList;

    @FXML
    public void onBtNouveauAction(ActionEvent event) {
        Stage parentStage = Utils.currentStage(event);
        Vendeur obj = new Vendeur();
        createDialogForm(obj, "/gui/FormeVendeur.fxml", parentStage);
    }

    public void setServiceVendeur(ServiceVendeur service) {
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
        tableColumnCourriel.setCellValueFactory(new PropertyValueFactory<>("courriel"));
        tableColumnDateNaissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        Utils.formatTableColumnDate(tableColumnDateNaissance, "yyyy-MM-dd");
        tableColumnSalaireBase.setCellValueFactory(new PropertyValueFactory<>("salaireBase"));
        Utils.formatTableColumnDouble(tableColumnSalaireBase, 2);

        // Adaptez la fenêtre du tableau à la fenêtre principale.
        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewVendeurs.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Le service était nul");
        }
        List<Vendeur> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewVendeurs.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    // Ouvre la fenêtre du formulaire des départements à remplir.
    private void createDialogForm(Vendeur obj, String absoluteName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            FormeVendeurController controller = loader.getController();
            controller.setVendeur(obj);
            controller.setServices(new ServiceVendeur(), new ServiceDepartement() );
            controller.loadAssociateObjects();
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Entrer les données du vendeur:");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL); // Affiche la fenêtre précédente uniquement si la fenêtre actuelle est fermée.
            dialogStage.showAndWait();
        }
        catch (IOException e) {
            e.printStackTrace();
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
        tableColumnEDIT.setCellFactory(param -> new TableCell<Vendeur, Vendeur>() {
            private final Button button = new Button("edit");
            @Override
            protected void updateItem(Vendeur obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                obj, "/gui/FormeVendeur.fxml",Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Vendeur, Vendeur>() {
            private final Button button = new Button("supprimer");
            @Override
            protected void updateItem(Vendeur obj, boolean empty) {
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

    private void removeEntity(Vendeur obj) {
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
