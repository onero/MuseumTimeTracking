/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.statistics.VolunteerStatistics;

import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import museumtimetracking.be.Volunteer;
import museumtimetracking.be.enums.EVolunteerStatisticsState;
import static museumtimetracking.be.enums.EVolunteerStatisticsState.*;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.GuildModel;
import museumtimetracking.gui.model.ModelFacade;
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

    private final VolunteerModel volunteerModel;
    private final GuildModel guildModel;

    public VolunteerStatisticsViewController() {
        volunteerModel = ModelFacade.getInstance().getVolunteerModel();
        guildModel = ModelFacade.getInstance().getGuildModel();

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeComboBoxes();
    }

    /**
     * Fills the comboboxes and sets cellfactory on the volunteer combobox
     */
    private void initializeComboBoxes() {
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
        } else {
            List<String> guilds = new ArrayList<>();
            guildModel.getCachedGuilds().stream().forEach(g -> guilds.add(g.getName()));
            Collections.sort(guilds);
            comboGuild.getItems().addAll(guilds);
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
        } else {
            List<Volunteer> volunteers = volunteerModel.getCachedVolunteers();
            Collections.sort(volunteers);
            comboVolunteer.getItems().addAll(volunteers);
        }

    }

    /**
     * updates the hours label to the hours from the specific volunteer and
     * guild and updates comboboxes with the right guilds and volunteers.
     *
     * @param guildName
     */
    private void updateHours(String id) {
        String guildName = comboGuild.getSelectionModel().getSelectedItem();
        Volunteer volunteer = comboVolunteer.getSelectionModel().getSelectedItem();
        EVolunteerStatisticsState state = EVolunteerStatisticsState.getState(volunteer, guildName, id);
        String workHours = "";

        try {
            switch (state) {
                case BOTH_VOLUNTEER_AND_GUILD_CHOSEN_ON_VOLUNTEER_COMBO: {
                    workHours = "" + volunteerModel.getWorkHoursForAVolunteerInAGuild(guildName, volunteer);
                    updateComboGuild(volunteer);
                    comboGuild.getSelectionModel().select(guildName);
                }
                break;
                case BOTH_VOLUNTEER_AND_GUILD_CHOSEN_ON_GUILD_COMBO: {
                    workHours = "" + volunteerModel.getWorkHoursForAVolunteerInAGuild(guildName, volunteer);
                    updateComboVolunteer(guildName);
                    selectVolunteer(volunteer);
                }
                break;
                case ONLY_VOLUNTEER_CHOSEN: {
                    workHours = "" + volunteerModel.getWorkHoursForAVolunteerInAllGuilds(volunteer);
                    updateComboGuild(volunteer);
                }
                break;
                case ONLY_GUILD_CHOSEN: {
                    workHours = "" + guildModel.getWorkHoursInGuild(guildName);
                    updateComboVolunteer(guildName);
                }
                break;
                case NONE_CHOSEN:
                    updateComboGuild(volunteer);
                    updateComboVolunteer(guildName);
                    break;
                default:
                    break;
            }
        } catch (DALException ex) {
            ExceptionDisplayer.display(ex);
        }
        labelHours.setText(workHours);
        resizeCombos();
    }

    /**
     * Calls update on closed combobox.
     */
    @FXML
    private void handleUpdate(Event event) {
        ComboBox combo = (ComboBox) event.getSource();
        updateHours(combo.getId());
    }

    /**
     * Detects which button is pressed and clears the right comboboxes and
     * updates the label accordingly.
     *
     * @param event
     */
    @FXML
    private void handleClearCombo(ActionEvent event) {
        Button button = (Button) event.getSource();
        switch (button.getId()) {
            case "btnVolunteer":
                comboVolunteer.getSelectionModel().clearSelection();
                break;
            case "btnGuild":
                comboGuild.getSelectionModel().clearSelection();
                break;
            case "btnClearAll":
                comboVolunteer.getSelectionModel().clearSelection();
                comboGuild.getSelectionModel().clearSelection();
                break;
            default:
                break;
        }
        updateHours(null);
    }

    /**
     * Clears both comboboxes and resets the hours label.
     */
    public void clearAll() {
        updateComboGuild(null);
        updateComboVolunteer(null);
        updateHours(null);
    }

    /**
     * When called in recursion, this method will select the volunteer in the
     * combobox that matches the volunteer given in the param.
     *
     * @param volunteer
     */
    private void selectVolunteer(Volunteer volunteer) {
        Volunteer volunteerInList = comboVolunteer.getSelectionModel().getSelectedItem();
        if (volunteerInList == null || volunteerInList.getID() != volunteer.getID()) {
            comboVolunteer.getSelectionModel().selectNext();
            selectVolunteer(volunteer);
        }
    }

    private void resizeCombos() {
        comboVolunteer.setVisibleRowCount(comboVolunteer.getItems().size());
        if (comboGuild.getItems().size() >= 5) {
            comboGuild.setVisibleRowCount(5);
        } else {
            comboGuild.setVisibleRowCount(comboGuild.getItems().size());
        }
    }

}
