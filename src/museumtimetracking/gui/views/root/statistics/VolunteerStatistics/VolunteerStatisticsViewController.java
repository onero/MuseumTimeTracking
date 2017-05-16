/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.statistics.VolunteerStatistics;

import com.jfoenix.controls.JFXComboBox;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
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
    private static final String labelPrompt = "Intet Valgt";

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
        comboVolunteer.setPromptText(stringComboVolunteerPrompt);
        comboGuild.setPromptText(stringComboGuildPrompt);
        labelHours.setText(labelPrompt);
        initializeComboBoxes();
    }

    /**
     *
     */
    private void initializeComboBoxes() {

        resetGuildCombo();
        resetVolunteerCombo();
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
            updateHours(comboGuild.getSelectionModel().getSelectedItem(), newValue);

            if (comboGuild.getSelectionModel().getSelectedItem() == null) {
                updateComboGuild(newValue);
            }
        });

        //adds listener to the guild combobox
        comboGuild.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateHours(newValue, comboVolunteer.getSelectionModel().getSelectedItem());

            if (comboVolunteer.getSelectionModel().getSelectedItem() == null) {
                updateComboVolunteer(newValue);
            }
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
                //Updates the guild combobox with guilds the volunteer has worked on
                comboGuild.getItems().addAll(guildModel.getGuildsAVolunteerHasWorkedOn(volunteer));
            } catch (DALException ex) {
                ExceptionDisplayer.display(ex);
            }
        }
    }

    /**
     * Adds the Volunteers that have worked in a specific guild to the combobox.
     *
     * @param guildName
     */
    private void updateComboVolunteer(String guildName) {
        comboVolunteer.getItems().clear();

        if (guildName != null) {
            try {
                //Updates the guild combobox with guilds the volunteer has worked on
                comboVolunteer.getItems().addAll(volunteerModel.getVolunteersThatHasWorkedOnGuild(guildName));
            } catch (DALException ex) {
                ExceptionDisplayer.display(ex);
            }
        }
    }

    /**
     * updates the hours label to the hours from the specific volunteer and
     * guild.
     *
     * @param guildName
     */
    private void updateHours(String guildName, Volunteer volunteer) {
        if (volunteer != null && guildName != null) {
            try {
                //Shows the hours the volunteer has put in the guild.
                labelHours.setText("" + volunteerModel.getWorkHoursForAVolunteerInAGuild(guildName, volunteer));
            } catch (DALException ex) {
                ExceptionDisplayer.display(ex);
            }
        } else if (volunteer != null && guildName == null) {
            try {
                //Shows the overall hours the volunteer has put in all guilds.
                labelHours.setText("" + volunteerModel.getWorkHoursForAVolunteerInAllGuilds(volunteer));
            } catch (DALException ex) {
                ExceptionDisplayer.display(ex);
            }
        } else if (volunteer == null && guildName != null) {
            try {
                labelHours.setText("" + guildModel.getWorkHoursInGuild(guildName));
            } catch (DALException ex) {
                ExceptionDisplayer.display(ex);
            }
        } else if (volunteer == null && guildName == null) {
            labelHours.setText(labelPrompt);
        }
    }

    /**
     * Clears the selection of a guild, so that one can see hours for all
     * guilds.
     */
    @FXML
    private void handleClearGuildCombo(ActionEvent event) {
        comboGuild.getSelectionModel().clearSelection();
//        if (comboVolunteer.getSelectionModel().getSelectedItem() == null) {
//            resetGuildCombo();
//        }
    }

    /**
     * Clears the selection of a Volunteer.
     *
     * @param event
     */
    @FXML
    private void handleClearVolunteerCombo(ActionEvent event) {
        comboVolunteer.getSelectionModel().clearSelection();
//        if (comboGuild.getSelectionModel().getSelectedItem() == null) {
//            resetVolunteerCombo();
//        }
    }

    private void resetGuildCombo() {
        comboGuild.getItems().clear();
        List<String> guilds = new ArrayList<>();
        guildModel.getCachedGuilds().stream().forEach(g -> guilds.add(g.getName()));
        comboGuild.getItems().addAll(guilds);
    }

    private void resetVolunteerCombo() {
        comboVolunteer.getItems().clear();
        comboVolunteer.setItems(volunteerModel.getCachedVolunteers());
    }

}
