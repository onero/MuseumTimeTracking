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
import museumtimetracking.be.Volunteer;

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
    }

    @FXML
    private void handleEditVolunteerInfo() {
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        txtVolunteerInfo.setText(currentVolunteer.getDescription());
    }

    /**
     * Set the current volunteer
     *
     * @param volunteer
     */
    public void setCurrentVolunteer(Volunteer volunteer) {
        currentVolunteer = volunteer;
    }

}
