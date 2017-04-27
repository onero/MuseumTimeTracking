/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.volunteer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import museumtimetracking.be.Volunteer;
import museumtimetracking.be.enums.EFXMLName;
import museumtimetracking.gui.views.NodeFactory;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class VolunteerOverviewController implements Initializable {

    @FXML
    private Button btnEdit;

    @FXML
    private ListView<Volunteer> lstVolunteer;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtPhone;

    private Node newVolunteer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        newVolunteer = NodeFactory.getInstance().createNewView(EFXMLName.ADD_NEW_VOLUNTEER);

//        lstVolunteer.setItems(volunteers);
        setVolunteerCellFactory();

        setTextVisibility(false);
    }

    private void setTextVisibility(boolean value) {
        txtFirstName.setDisable(!value);
        txtLastName.setDisable(!value);
        txtEmail.setDisable(!value);
        txtPhone.setDisable(!value);
    }

    /**
     * For each Volunteer in the list, show only their full name
     */
    private void setVolunteerCellFactory() {
        lstVolunteer.setCellFactory(v -> new ListCell<Volunteer>() {
            @Override
            public void updateItem(Volunteer volunteer, boolean empty) {
                super.updateItem(volunteer, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(volunteer.getFullName());
                }
            }
        });
    }

    @FXML
    private void handleDeleteVolunteer() {
    }

    @FXML
    private void handleEditVolunteer() {
        if (btnEdit.getText().equalsIgnoreCase("rediger")) {
            btnEdit.setText("Gem");
            setTextVisibility(true);
        } else {
            btnEdit.setText("Rediger");
            setTextVisibility(false);
        }
    }

    @FXML
    private void handleNewVolunteer() throws IOException {
        Stage primStage = (Stage) btnEdit.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(EFXMLName.ADD_NEW_VOLUNTEER.toString()));
        Parent root = loader.load();

        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));

        //Create new modal window from FXMLLoader
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(primStage);

        newStage.show();
    }

    @FXML
    private void handleSelectVolunteer() {
        Volunteer selectedVolunteer = lstVolunteer.getSelectionModel().getSelectedItem();
        txtFirstName.setText(selectedVolunteer.getFirstName());
        txtLastName.setText(selectedVolunteer.getLastName());
        txtEmail.setText(selectedVolunteer.getEmail());
        txtPhone.setText("" + selectedVolunteer.getPhone());
    }

}
