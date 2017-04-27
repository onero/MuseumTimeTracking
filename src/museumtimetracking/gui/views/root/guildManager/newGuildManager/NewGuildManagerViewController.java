/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.guildManager.newGuildManager;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import museumtimetracking.be.APerson;
import museumtimetracking.be.Guild;
import museumtimetracking.be.GuildManager;
import museumtimetracking.gui.model.GuildManagerModel;

/**
 * FXML Controller class
 *
 * @author Rasmus
 */
public class NewGuildManagerViewController implements Initializable {

    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPhone;
    @FXML
    private ComboBox<Guild> comboGuild;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleAddButton() {
        if (validateData()) {
            APerson person = new GuildManager(txtFirstName.getText(), txtLastName.getText(), txtEmail.getText(), Integer.parseInt(txtPhone.getText()));
            Guild guild = new Guild("Mock", "Mock");
            GuildManagerModel.getInstance().createNewGuildManager(person, guild);
            closeWindow();
        } else {
            showWrongInformationWarning();
        }
    }

    @FXML
    private void handleCancelButton() {
        closeWindow();
    }

    /**
     * Closes the new guild manager window.
     */
    private void closeWindow() {
        Stage stage = (Stage) txtEmail.getScene().getWindow();
        stage.close();
    }

    /**
     * Validates the textfields' info, so that it matches the requirements from
     * the DB.
     *
     * @return
     */
    private boolean validateData() {
        boolean isFirstNameThere = !txtFirstName.getText().isEmpty();
        boolean isLastNameThere = !txtLastName.getText().isEmpty();
        //Checks if the textfield only contains numbers.
        boolean isPhoneValid = (txtPhone.getText().matches("[0-9]+") && txtPhone.getText().length() == 8);

        return (isFirstNameThere == true && isLastNameThere == true && isPhoneValid == true);
    }

    private void showWrongInformationWarning() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Den indtastede information er ikke gyldig.");
        alert.setContentText("Skal udfyldes:\nFornavn.\nEfternavn\n\n"
                + "Tjekt eventuelt at:\n"
                + "Telefon nummeret kun indeholder tal.\nTelefon nummeret er 8 cifre.");
        alert.show();
    }
}
