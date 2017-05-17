/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.statistics.VolunteerStatistics;

import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
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
    private JFXComboBox<String> comboGuild;
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
        } catch (DALException ex) {
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

    /**
     *
     */
    private void initializeComboBoxes() {
        comboVolunteer.setPromptText(stringComboVolunteerPrompt);
        comboGuild.setPromptText(stringComboGuildPrompt);

        comboVolunteer.setItems(volunteerModel.getCachedVolunteers());

        //Sets the list of volunteers to be their names.
        comboVolunteer.setCellFactory(new Callback<ListView<Volunteer>, ListCell<Volunteer>>() {

            @Override
            public ListCell<Volunteer> call(ListView<Volunteer> volunteer) {

                final ListCell<Volunteer> cell = new ListCell<Volunteer>() {

                    @Override
                    protected void updateItem(Volunteer volunteer, boolean bln) {
                        super.updateItem(volunteer, bln);

                        if (volunteer != null) {
                            setText(volunteer.getFullName());
                        } else {
                            setText(null);
                        }
                    }

                };
                return cell;
            }
        });

        //Sets the buttoncell to be the chosen Volunteer's name.
        comboVolunteer.setButtonCell(new ListCell<Volunteer>() {
            @Override
            protected void updateItem(Volunteer volunteer, boolean bln) {
                super.updateItem(volunteer, bln);
                if (volunteer != null) {
                    setText(volunteer.getFullName());
                } else {
                    setText(null);
                }
            }
        });

        //adds listener to the volunteer combobox.
        comboVolunteer.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateComboGuild(newValue);
            updateHours(comboGuild.getSelectionModel().getSelectedItem(), newValue);
        });

        //adds listener to the guild combobox
        comboGuild.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateHours(newValue, comboVolunteer.getSelectionModel().getSelectedItem());
        });
    }

    /**
     * Adds the guilds for the specific volunteer.
     *
     * @param volunteer
     */
    private void updateComboGuild(Volunteer volunteer) {
        comboGuild.getItems().clear();

        if (volunteer != null) {
            try {
                //TODO GREEN: Update the guild combobox with guilds the volunteer has worked on
                comboGuild.getItems().addAll(guildModel.getGuildsAVolunteerHasWorkedOn(volunteer));
            } catch (DALException ex) {
                ExceptionDisplayer.display(ex);
            }
        }
    }

    /**
     * updates the hours label to the hours from the specific volunteer and
     * guild.
     *
     * @param guild
     */
    private void updateHours(String guild, Volunteer volunteer) {
        if (volunteer != null && guild != null) {
            //TODO GREEN: show the hours the volunteer has put in the guild
            labelHours.setText(guild + " 30");
        } else if (volunteer != null && guild == null) {
            //TODO GREEN: show the overall hours the volunteer has put in all guilds.
            labelHours.setText("intet laug 300");
        }
    }

    /**
     * Clears the selection of a guild, so that one can see hours for all
     * guilds.
     */
    @FXML
    private void handleClear() {
        comboGuild.getSelectionModel().clearSelection();
    }
}
