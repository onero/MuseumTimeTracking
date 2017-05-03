/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.volunteer.idleVolunteers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import museumtimetracking.be.Volunteer;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.VolunteerModel;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class IdleVolunteerViewController implements Initializable {

    @FXML
    private TableColumn<Volunteer, String> clmVolunteerDescription;
    @FXML
    private TableColumn<Volunteer, String> clmVolunteerName;
    @FXML
    private BorderPane guildTableBorderPane;
    @FXML
    private TableView<Volunteer> tableVolunteer;

    private VolunteerModel volunteerModel;

    public IdleVolunteerViewController() {
        volunteerModel = null;
        try {
            volunteerModel = VolunteerModel.getInstance();
        } catch (IOException | DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableVolunteer.setItems(volunteerModel.getCachedIdleVolunteers());

        clmVolunteerName.setCellValueFactory(v -> v.getValue().getFullNameProperty());
        clmVolunteerDescription.setCellValueFactory(v -> v.getValue().getDescriptionProperty());

    }

    @FXML
    private void handleRestoreFromArchive() {
        Volunteer selectedVolunteer = tableVolunteer.getSelectionModel().getSelectedItem();
        try {
            volunteerModel.updateIdleVolunteer(selectedVolunteer, false);
        } catch (DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

}
