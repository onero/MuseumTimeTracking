/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.idle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import museumtimetracking.be.GuildManager;
import museumtimetracking.be.Volunteer;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.GuildManagerModel;
import museumtimetracking.gui.model.VolunteerModel;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class IdleViewController implements Initializable {

    @FXML
    private TableColumn<GuildManager, String> clmGMDescription;
    @FXML
    private TableColumn<GuildManager, String> clmGMName;
    @FXML
    private TableColumn<Volunteer, String> clmVolunteerDescription;
    @FXML
    private TableColumn<Volunteer, String> clmVolunteerName;
    @FXML
    private TableView<GuildManager> tableIdleGM;
    @FXML
    private TableView<Volunteer> tableIdleVolunteer;
    @FXML
    private VBox vBoxGMOptions;
    @FXML
    private VBox vBoxVolunteerOptions;

    private GuildManagerModel guildManagerModel;

    private VolunteerModel volunteerModel;

    private GuildManager selectedManager;
    private Volunteer selectedVolunteer;

    public IdleViewController() {
        try {
            guildManagerModel = GuildManagerModel.getInstance();
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
        initializeTables();
        setIdleGMOptionsVisibility(false);
        setIdleVolunteerOptionsVisibility(false);
    }

    @FXML
    private void handleDeleteGM() {
        try {
            guildManagerModel.deleteGuildManager(selectedManager);
        } catch (DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    @FXML
    private void handleDeleteVolunteer() {
        try {
            volunteerModel.deleteVolunteer(selectedVolunteer);
        } catch (DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    @FXML
    private void handleRestoreGM() {
        try {
            guildManagerModel.updateIdleManager(selectedManager, false);
        } catch (DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    @FXML
    private void handleRestoreVolunteer() {
        try {
            volunteerModel.updateIdleVolunteer(selectedVolunteer, false);
        } catch (DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    @FXML
    private void handleSelectGM() {
        setIdleGMOptionsVisibility(true);
        selectedManager = tableIdleGM.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void handleSelectVounteer() {
        setIdleVolunteerOptionsVisibility(true);
        selectedVolunteer = tableIdleVolunteer.getSelectionModel().getSelectedItem();
    }

    /**
     * Set the visibility of idle options
     *
     * @param shown
     */
    public void setIdleGMOptionsVisibility(boolean shown) {
        vBoxGMOptions.setDisable(!shown);
        vBoxGMOptions.setVisible(shown);
    }

    /**
     * Set the visibility of idle options
     *
     * @param shown
     */
    public void setIdleVolunteerOptionsVisibility(boolean shown) {
        vBoxVolunteerOptions.setDisable(!shown);
        vBoxVolunteerOptions.setVisible(shown);
    }

    /**
     * Fill information in the tables
     */
    private void initializeTables() {
        tableIdleGM.setItems(guildManagerModel.getCachedIdleGuildManagers());

        clmGMName.setCellValueFactory(gm -> gm.getValue().getFullNameProperty());
        clmGMDescription.setCellValueFactory(gm -> gm.getValue().getDescription());

        tableIdleVolunteer.setItems(volunteerModel.getCachedIdleVolunteers());

        clmVolunteerName.setCellValueFactory(v -> v.getValue().getFullNameProperty());
        clmGMDescription.setCellValueFactory(v -> v.getValue().getDescription());
    }

}