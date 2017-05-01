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
import museumtimetracking.be.Volunteer;
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

    @FXML
    private void handleBack() {
        Stage primStage = (Stage) btnEdit.getScene().getWindow();
        primStage.close();
    }

    @FXML
    private void handleEditVolunteerInfo() {
        if (btnEdit.getText().equalsIgnoreCase("rediger")) {
            btnEdit.setText("Gem");
            txtVolunteerInfo.setDisable(false);
        } else {
            btnEdit.setText("Rediger");
        Volunteer updatedVolunteer = new Volunteer(txtVolunteerInfo.getText());
        VolunteerModel.getInstance().updateVolunteer(volunteerToUpdate, updatedVolunteer);
            txtVolunteerInfo.setDisable(true);
        }
    }

    /**
     * Initializes the controller class.
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
