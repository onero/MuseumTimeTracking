/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.statistics.VolunteerStatistics;

import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import museumtimetracking.be.Guild;
import museumtimetracking.be.Volunteer;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.GuildModel;
import museumtimetracking.gui.model.VolunteerModel;

/**
 * FXML Controller class
 *
 * @author Mathias
 */
public class VolunteerStatisticsViewController implements Initializable {

    @FXML
    private JFXComboBox<Volunteer> comboVolunteer;
    @FXML
    private JFXComboBox<Guild> comboGuild;
    @FXML
    private Label labelHours;

    private VolunteerModel volunteerModel;
    private GuildModel guildModel;

    private static final String stringComboVolunteerPrompt = "Vælg en frivillig";
    private static final String stringComboGuildPrompt = "Alle laug";

    public VolunteerStatisticsViewController() {
        try {
            volunteerModel = VolunteerModel.getInstance();
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
        initializeComboBoxes();
    }

    private void initializeComboBoxes() {
        comboVolunteer.setPromptText(stringComboVolunteerPrompt);
        comboGuild.setPromptText(stringComboGuildPrompt);
        comboVolunteer.setItems(volunteerModel.getCachedVolunteers());
        comboVolunteer.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateComboGuild(newValue);
        });
    }

    private void updateComboGuild(Volunteer volunteer) {
        comboGuild.getItems().clear();
        int hours;

        if (volunteer == null) {
            //TODO GREEN: Show hours from all guilds.
            hours = 300;
        } else {
            //TEST
            labelHours.setText(volunteer.getFullName());
            //TODO GREEN: Show hours from a specific guild.
            hours = 30;
        }
        labelHours.setText("" + hours);
    }

    @FXML
    private void handleClear() {
        comboGuild.getSelectionModel().clearSelection();
    }

}
