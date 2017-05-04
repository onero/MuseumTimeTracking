/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.volunteer.volunteerSearch;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import museumtimetracking.gui.views.root.sharedComponents.search.ISearch;

/**
 * FXML Controller class
 *
 * @author Mathias
 */
public class VolunteerSearchController implements Initializable, ISearch {

    @FXML
    private TextField txtSearchBar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setPromptText();
        initializeListenerForTextField();
    }

    @FXML
    private void handleClearSearchBar(ActionEvent event) {
        clearSearchBar();
    }

    @Override
    public void initializeListenerForTextField() {
        txtSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            updateListView(newValue);
        });
    }

    @Override
    public void updateListView(String newValue) {
        //TODO MSP: Update ListView
        System.out.println("Frivillig + " + newValue);
    }

    @Override
    public void setPromptText() {
        txtSearchBar.setPromptText(TEXT_FOR_SEARCHBAR);
    }

    @Override
    public void clearSearchBar() {
        txtSearchBar.clear();
        System.out.println("Cleared from VOLUNTEER");
    }
}
