/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.guild.editGuild;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import museumtimetracking.be.Guild;
import museumtimetracking.gui.model.GuildModel;
import museumtimetracking.gui.views.root.guild.guildComponents.GuildTableViewController;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class EditGuildViewController implements Initializable {

    @FXML
    private Button btnSave;
    @FXML
    private BorderPane editGuildBorderPane;
    @FXML
    private TextArea txtGuildDescription;
    @FXML
    private TextField txtGuildName;

    private Guild currentGuild;

    public EditGuildViewController() {
        GuildTableViewController.getIntance().setButtonVisibility(false);
    }

    @FXML
    private void handleBack() {
        Stage primStage = (Stage) btnSave.getScene().getWindow();
        primStage.close();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setTextFieldVisibility(false);
    }

    private void setTextFieldVisibility(boolean visible) {
        txtGuildName.setDisable(!visible);
        txtGuildDescription.setDisable(!visible);
    }

    @FXML
    private void handleSaveGuild() {
        if (btnSave.getText().toLowerCase().equals("rediger")) {
            btnSave.setText("Gem");
            setTextFieldVisibility(true);
        } else {
            Guild updatedGuild = new Guild(txtGuildName.getText(), txtGuildDescription.getText());
            GuildModel.getInstance().updateGuild(currentGuild.getName(), updatedGuild);
            handleBack();
        }
    }

    public void setCurrentGuild(Guild guild) {
        currentGuild = guild;
        setTextFields(currentGuild.getName(), currentGuild.getDescription());
    }

    /**
     * Set the description of the fields
     *
     * @param name
     * @param description
     */
    private void setTextFields(String name, String description) {
        txtGuildName.setText(name);
        txtGuildDescription.setText(description);
    }
}
