/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.volunteer.addHours;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import museumtimetracking.be.Guild;
import museumtimetracking.be.Volunteer;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.GuildModel;
import museumtimetracking.gui.model.VolunteerModel;

/**
 * FXML Controller class
 *
 * @author Rasmus
 */
public class AddVolunteersHoursViewController implements Initializable {

    @FXML
    private ListView<Guild> lstGuilds;
    @FXML
    private Label lblGuildName;
    @FXML
    private Spinner<Integer> spnHours;

    private final int MINNIMUM_RANGE = 0;
    private final int MAXIMUM_RANGE = 20;
    private final int INITIAL_VALUE = 8;

    private GuildModel guildModel;
    private VolunteerModel volunteerModel;
    private Volunteer volunteer;

    public AddVolunteersHoursViewController() {
        try {
            guildModel = GuildModel.getInstance();
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
        setCellFactory();
        lstGuilds.setItems(guildModel.getCachedGuilds());
        addListeners();
        initializeSpinner();
    }

    @FXML
    private void handleDocumenButton(ActionEvent event) {
        Guild guild = lstGuilds.getSelectionModel().getSelectedItem();
        if (guild != null) {
            try {
                int hours = spnHours.getValue();
                volunteerModel.addHoursToVolunteer(volunteer.getID(), guild.getName(), hours);
                closeModal();
            } catch (DALException ex) {
                ExceptionDisplayer.display(ex);
            }
        } else {
            ExceptionDisplayer.display(new NullPointerException("Der er ikke valgt noget Laug!"));
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        closeModal();
    }

    /**
     * Adds a changeListener for lstGuilds.
     */
    private void addListeners() {
        lstGuilds.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Guild>() {
            @Override
            public void changed(ObservableValue<? extends Guild> observable, Guild oldValue, Guild newValue) {
                if (newValue != oldValue) {
                    lblGuildName.setText(newValue.getName());
                }
            }
        });
    }

    /**
     * Sets the cellFactory of the lstGuilds.
     */
    private void setCellFactory() {
        lstGuilds.setCellFactory(g -> new ListCell<Guild>() {
            @Override
            protected void updateItem(Guild item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    /**
     * Sets the volunteer.
     *
     * @param volunteer
     */
    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    /**
     * Closes the modal.
     */
    private void closeModal() {
        Stage stage = (Stage) lblGuildName.getScene().getWindow();
        stage.close();
    }

    /**
     * Creates a new SpinnerValueFactory for Integer and sets the minnimum,
     * maximum and initial values.
     */
    private void initializeSpinner() {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory
                = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        MINNIMUM_RANGE,
                        MAXIMUM_RANGE,
                        INITIAL_VALUE);
        spnHours.setValueFactory(valueFactory);
    }

}
