/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.activeGuilds.editGuild;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import museumtimetracking.be.GM;
import museumtimetracking.be.Guild;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.GuildManagerModel;
import museumtimetracking.gui.model.GuildModel;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class EditGuildViewController implements Initializable {

    @FXML
    private Button btnSave;
    @FXML
    private TextArea txtGuildDescription;
    @FXML
    private TextField txtGuildName;
    @FXML
    private ListView<GM> listPeople;
    @FXML
    private Button btnAssignGM;
    @FXML
    private TextField txtGMCandidateSearch;

    private Guild currentGuild;
    private GuildManagerModel guildManagerModel;

    private GM selectedGuildManager;

    private GuildModel guildModel;

    public EditGuildViewController() {
        try {
            guildManagerModel = GuildManagerModel.getInstance();
            guildModel = GuildModel.getInstance();
        } catch (IOException | DALException ex) {
            ExceptionDisplayer.display(ex);
        }
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
        initializeGMCandidateList();
        setAssignGMVisibility(false);
    }

    /**
     * Set visibility of assignGM button
     *
     * @param value
     */
    private void setAssignGMVisibility(boolean value) {
        btnAssignGM.setDisable(!value);
        btnAssignGM.setVisible(value);
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
            Guild updatedGuild = new Guild(txtGuildName.getText(), txtGuildDescription.getText(), false);
            try {
                GuildModel.getInstance().updateGuild(currentGuild.getName(), updatedGuild);
            } catch (IOException | DALException ex) {
                ExceptionDisplayer.display(ex);
            }
            handleBack();
        }
    }

    /**
     * Set the current guild being worked on
     *
     * @param guild
     */
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

    @FXML
    private void handleSearchForGM() {
        String searchString = txtGMCandidateSearch.getText();
        guildManagerModel.searchForPersonWithoutGuild(searchString);
    }

    private void initializeGMCandidateList() {
        listPeople.setItems(guildManagerModel.getCachedGMCandidates());

        listPeople.setCellFactory(gm -> new ListCell<GM>() {
            @Override
            protected void updateItem(GM guildManager, boolean empty) {
                super.updateItem(guildManager, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(guildManager.getFullName());
                }
            }
        });
    }

    @FXML
    private void handleAssignGM() {
        try {
            if (currentGuild.getGuildManager() == null) {
                guildManagerModel.assignGuildToManager(selectedGuildManager, currentGuild);
                guildModel.removeCachedAvailableGuild(currentGuild);
            } else {
                guildModel.updateGMForGuild(selectedGuildManager, currentGuild);
            }
        } catch (DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    @FXML
    private void handleSelectGuildManager() {
        if (listPeople.getSelectionModel().getSelectedItem() != null) {
            selectedGuildManager = listPeople.getSelectionModel().getSelectedItem();
            setAssignGMVisibility(true);
        }
    }
}
