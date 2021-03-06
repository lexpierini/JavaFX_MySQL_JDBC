package gui;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.ServiceDepartement;
import model.services.ServiceVendeur;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MainViewController implements Initializable {
    @FXML
    private MenuItem menuItemVendeur;
    @FXML
    private MenuItem menuItemDepartement;
    @FXML
    private MenuItem menuItemAPropos;

    @FXML
    public void onMenuItemVendeurAction(){
        loadView("/gui/ListeVendeur.fxml", (ListeVendeurController controller) -> {
            controller.setServiceVendeur(new ServiceVendeur());
            controller.updateTableView();
        });
    }

    @FXML
    public void onMenuItemDepartementAction(){
        loadView("/gui/ListeDepartement.fxml", (ListeDepartementController controller) -> {
            controller.setServiceDepartement(new ServiceDepartement());
            controller.updateTableView();
        });
    }

    @FXML
    public void onMenuItemAProposAction(){
        loadView("/gui/APropos.fxml", x -> {});
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    // Affiche un nouvelle écran sur l'écran principal.
    private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVbox = loader.load();

            Scene mainScene = Main.getMainScene();
            VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent(); //Prenez le premier élément de la vue.

            // Remplace le MenuBar MainView par le MenuBar APropos.
            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVbox.getChildren());

            T controller = loader.getController();
            initializingAction.accept(controller);

        } catch (IOException e) {
            Alerts.showAlert("IO Exception", "Erreur lors du chargement de la vue.", e.getMessage(), AlertType.ERROR);
        }
    }
}
