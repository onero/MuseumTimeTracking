/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.volunteer.newVolunteer;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import museumtimetracking.be.Volunteer;
import museumtimetracking.gui.model.VolunteerModel;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class NewVolunteerViewController implements Initializable {

    @FXML
    private CheckBox checkBoxDanish;
    @FXML
    private CheckBox checkBoxEnglish;
    @FXML
    private CheckBox checkBoxGerman;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtPhone;

    private final VolunteerModel volunteerModel;

    public NewVolunteerViewController() {
        volunteerModel = VolunteerModel.getInstance();
    }

    @FXML
    private void handleAddButton() {

        Volunteer newVolunteer = new Volunteer(txtFirstName.getText(), txtLastName.getText(), txtEmail.getText(), Integer.parseInt(txtPhone.getText()));
        volunteerModel.addVolunteer(newVolunteer);

        closeWindow();
    }

    @FXML
    private void handleCancelButton() {
        closeWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private void closeWindow() {
        Stage stage = (Stage) txtEmail.getScene().getWindow();
        stage.close();
    }

}
