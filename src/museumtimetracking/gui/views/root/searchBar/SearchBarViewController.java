/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.searchBar;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import static museumtimetracking.be.enums.EFXMLName.*;

/**
 * FXML Controller class
 *
 * @author Mathias
 */
public class SearchBarViewController implements Initializable {

    @FXML
    private TextField txtSearchBar;

    private final String TEXT_FOR_SEARCHBAR = "Søg";

    private String mode;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mode = GUILDMANAGER_MODE.toString();
        txtSearchBar.setPromptText(TEXT_FOR_SEARCHBAR);
        initializeListenerForTextField();
    }

    @FXML
    private void handleClearSearchBar(ActionEvent event) {
        clearSearchBar();
    }

    private void initializeListenerForTextField() {
        txtSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            updateListView(newValue, mode);
        });
    }

    private void updateListView(String newValue, String mode) {
        switch (mode) {
            case "guild":
                //TODO
                break;
            case "guildManager":
                //TODO
                break;
            case "volunteer":
                //TODO
                break;
        }
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    private void clearSearchBar() {
        txtSearchBar.clear();
    }
}
