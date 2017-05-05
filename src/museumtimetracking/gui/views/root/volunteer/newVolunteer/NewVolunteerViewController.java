/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.volunteer.newVolunteer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import museumtimetracking.be.Volunteer;
import museumtimetracking.be.enums.ELanguage;
import museumtimetracking.bll.APersonManager;
import museumtimetracking.exception.AlertFactory;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.VolunteerModel;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class NewVolunteerViewController implements Initializable {

    @FXML
    private RadioButton checkBoxDanish;
    @FXML
    private RadioButton checkBoxEnglish;
    @FXML
    private RadioButton checkBoxGerman;
    @FXML
    private ToggleGroup language;
    @FXML
    private TextArea txtDescription;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtPhone;

    private VolunteerModel volunteerModel;

    public NewVolunteerViewController() {
        try {
            volunteerModel = VolunteerModel.getInstance();
        } catch (IOException | DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    @FXML
    private void handleAddButton() {
        ELanguage selectedLanguage;
        selectedLanguage = setSelectedLanguage();

        if (validateInput()) {
            String firstName = txtFirstName.getText();
            String lastname = txtLastName.getText();
            String email = txtEmail.getText();
            int phone = Integer.parseInt(txtPhone.getText());
            String description = txtDescription.getText();
            Volunteer newVolunteer = new Volunteer(firstName, lastname, email, phone, selectedLanguage);
            newVolunteer.setDescription(description);
            try {
                volunteerModel.addVolunteer(newVolunteer);
            } catch (DALException ex) {
                ExceptionDisplayer.display(ex);
            }
            closeWindow();
        } else {
            AlertFactory.createValidationAlert().show();
        }
    }

    /**
     * Get user selected language
     *
     * @return
     */
    private ELanguage setSelectedLanguage() {
        ELanguage selectedLanguage;
        if (checkBoxDanish.isSelected()) {
            selectedLanguage = ELanguage.DANISH;
        } else if (checkBoxEnglish.isSelected()) {
            selectedLanguage = ELanguage.ENGLISH;
        } else {
            selectedLanguage = ELanguage.GERMAN;
        }
        return selectedLanguage;
    }

    /**
     * Validate user input
     *
     * @return
     */
    private boolean validateInput() {
        boolean isFirstNameThere = !txtFirstName.getText().isEmpty();
        boolean isLastNameThere = !txtLastName.getText().isEmpty();
        String phone = txtPhone.getText();
        boolean isPhoneValid = APersonManager.validatePhone(phone);
        return APersonManager.checkAllValidation(isFirstNameThere, isLastNameThere, isPhoneValid);
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
