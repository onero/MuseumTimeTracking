/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.volunteer.volunteerInfo;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import museumtimetracking.MuseumTimeTracking;
import museumtimetracking.be.Volunteer;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.ModelFacade;
import museumtimetracking.gui.model.VolunteerModel;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class VolunteerInfoViewController implements Initializable {

    @FXML
    private Button btnEdit;
    @FXML
    private TextArea txtVolunteerInfo;

    private Volunteer currentVolunteer;

    private Stage primStage;

    private final VolunteerModel volunteerModel;

    public VolunteerInfoViewController() {
        volunteerModel = ModelFacade.getInstance().getVolunteerModel();
    }

    @FXML
    private void handleBack() {
        primStage = (Stage) btnEdit.getScene().getWindow();
        primStage.close();
    }

    @FXML
    private void handleEditVolunteerInfo() {
        primStage = (Stage) btnEdit.getScene().getWindow();
        if (btnEdit.getText().equalsIgnoreCase(MuseumTimeTracking.bundle.getString("Edit"))) {
            btnEdit.setText(MuseumTimeTracking.bundle.getString("Save"));
            txtVolunteerInfo.setDisable(false);
        } else {
            btnEdit.setText(MuseumTimeTracking.bundle.getString("Edit"));
            txtVolunteerInfo.setDisable(true);
            currentVolunteer.setDescription(txtVolunteerInfo.getText());
            try {
                volunteerModel.setVolunteerDescription(currentVolunteer.getID(), txtVolunteerInfo.getText());
                volunteerModel.updateIdleVolunteer(currentVolunteer, true);
            } catch (DALException ex) {
                ExceptionDisplayer.display(ex);
            }
            primStage.close();
        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtVolunteerInfo.setDisable(true);
    }

    /**
     * Set the current volunteer
     *
     * @param volunteer
     */
    public void setCurrentVolunteer(Volunteer volunteer) {
        currentVolunteer = volunteer;
        txtVolunteerInfo.setText(currentVolunteer.getDescription());
    }
}
