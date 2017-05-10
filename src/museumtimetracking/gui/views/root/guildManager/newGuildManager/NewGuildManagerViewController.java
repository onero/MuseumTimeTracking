/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.guildManager.newGuildManager;

import com.jfoenix.controls.JFXSnackbar;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import museumtimetracking.be.APerson;
import museumtimetracking.be.GM;
import museumtimetracking.be.Guild;
import museumtimetracking.bll.APersonManager;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.GuildManagerModel;
import museumtimetracking.gui.model.GuildModel;

/**
 * FXML Controller class
 *
 * @author Rasmus
 */
public class NewGuildManagerViewController implements Initializable {

    @FXML
    private VBox newGMBox;

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

    private JFXSnackbar snackBar;

    private GuildModel guildModel;

    public NewGuildManagerViewController() {
        try {
            guildModel = GuildModel.getInstance();
        } catch (IOException | DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeBomboBox();
        snackBar = new JFXSnackbar(newGMBox);
    }

    public void initializeBomboBox() {
        comboGuild.setItems(guildModel.getCachedAvailableGuilds());

        comboGuild.setCellFactory(guild -> new ListCell<Guild>() {
            @Override
            protected void updateItem(Guild guild, boolean empty) {
                super.updateItem(guild, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(guild.getName());
                }
            }

        });

        comboGuild.setButtonCell(new ListCell<Guild>() {
            @Override
            protected void updateItem(Guild guild, boolean bln) {
                super.updateItem(guild, bln);
                if (bln) {
                    setText("");
                } else {
                    setText(guild.getName());
                }
            }
        });
    }

    /**
     * Checks if the data is validated. If yes - Creates a new person and sends
     * it to the database to be registered as a GuildManager. If no - Shows a
     * information alert telling the informtion is incorrect.
     */
    @FXML
    private void handleAddButton() {
        if (validateData()) {
            APerson person = new GM(txtFirstName.getText(), txtLastName.getText(), txtEmail.getText(), Integer.parseInt(txtPhone.getText()));
            Guild selectedGuild = comboGuild.getSelectionModel().getSelectedItem();
            try {
                GuildManagerModel.getInstance().createNewGuildManager(person, selectedGuild.getName());
            } catch (IOException | DALException ex) {
                ExceptionDisplayer.display(ex);
            }
            closeWindow();
        } else {
            snackBar.show("Indtast venligst alle oplysninger", 2000);
//            AlertFactory.createValidationAlert().show();
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
        String phone = txtPhone.getText();
        Guild selectedGuild = comboGuild.getSelectionModel().getSelectedItem();
        boolean isPhoneValid = APersonManager.validatePhone(phone);
        boolean isGuildSelected = (selectedGuild != null);
        if (isGuildSelected) {
            return APersonManager.checkAllValidation(isFirstNameThere, isLastNameThere, isPhoneValid);
        }
        return false;
    }
}
