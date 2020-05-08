package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    @FXML
    private MenuItem menuItemVendeur;
    @FXML
    private MenuItem menuItemDepartement;
    @FXML
    private MenuItem menuItemAPropos;

    @FXML
    public void onMenuItemVendeurAction(){
        System.out.println("onMenuItemVendeurAction");
    }

    @FXML
    public void onMenuItemDepartementAction(){
        System.out.println("onMenuItemDepartementAction");
    }

    @FXML
    public void onMenuItemAProposAction(){
        System.out.println("onMenuItemAProposAction");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
