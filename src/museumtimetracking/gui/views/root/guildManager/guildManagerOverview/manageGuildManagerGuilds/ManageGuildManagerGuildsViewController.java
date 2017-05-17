/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.guildManager.guildManagerOverview.manageGuildManagerGuilds;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import museumtimetracking.be.Guild;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.GuildModel;

/**
 * FXML Controller class
 *
 * @author Rasmus
 */
public class ManageGuildManagerGuildsViewController implements Initializable {

    @FXML
    private ListView<String> lstAvialableGuilds;
    @FXML
    private ListView<String> lstManagerGuilds;

    private GuildModel guildModel;

    private final ObservableList<String> oAvaiableGuilds;
    private final ObservableList<String> oManagerGuilds;

    private final Set<String> setGuildsToAdd;
    private final Set<String> setGuildsToDelete;

    private boolean leftListSelected;

    public ManageGuildManagerGuildsViewController() {
        oAvaiableGuilds = FXCollections.observableArrayList();
        oManagerGuilds = FXCollections.observableArrayList();

        try {
            guildModel = GuildModel.getInstance();
        } catch (DALException ex) {
            ExceptionDisplayer.display(ex);
        }

        setGuildsToAdd = new HashSet<>();
        setGuildsToDelete = new HashSet<>();
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addListeners();
        lstAvialableGuilds.setItems(oAvaiableGuilds);
        lstManagerGuilds.setItems(oManagerGuilds);
    }

    @FXML
    private void handleMoveGuildButton() {
        //TODO rkl: Check for rightside.
        if (leftListSelected) {
            moveFromAvailableToManager();
        } else {
            moveFromManagerToAvailable();
        }
    }

    /**
     * Adds the parsed guilds to the managers list. Then gets the list of all
     * guilds and remove duplicates from it.
     *
     * @param managerGuilds
     */
    public void addGuilds(List<String> managerGuilds) {
        //TODO rkl: More comments/refactor. Ask Adam about streams.
        //Just use the manager guilds.
        oManagerGuilds.addAll(managerGuilds);
        for (Guild cachedGuild : guildModel.getCachedGuilds()) {
            oAvaiableGuilds.add(cachedGuild.getName());
        }
        List<String> toRemove = new ArrayList<>();
        for (String guildName : oAvaiableGuilds) {
            for (String managerGuild : managerGuilds) {
                if (guildName.equals(managerGuild)) {
                    toRemove.add(guildName);
                }
            }
        }
        oAvaiableGuilds.removeAll(toRemove);

    }

    private void addListeners() {
        lstAvialableGuilds.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            leftListSelected = true;
        });

        lstManagerGuilds.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            leftListSelected = false;
        });
    }

    /**
     * Moves a guild from available list tot manager list. Also adds it to
     * listOfGuildsToAdd.
     */
    private void moveFromAvailableToManager() {
        String guildName = lstAvialableGuilds.getSelectionModel().getSelectedItem();
        if (guildName != null) {
            oAvaiableGuilds.remove(guildName);
            oManagerGuilds.add(guildName);

            setGuildsToAdd.add(guildName);
            setGuildsToDelete.remove(guildName);
        }
    }

    /**
     * Moves a guild from manager list to available list. Also adds it to
     * listOfGuildsToDelete.
     */
    private void moveFromManagerToAvailable() {
        String guildName = lstManagerGuilds.getSelectionModel().getSelectedItem();
        if (guildName != null) {
            oManagerGuilds.remove(guildName);
            oAvaiableGuilds.add(guildName);

            setGuildsToDelete.add(guildName);
            setGuildsToAdd.remove(guildName);
        }
    }

    @FXML
    private void handleCancelButton() {
        closeModal();
    }

    @FXML
    private void handleOkButton() {
        closeModal();
    }

    /**
     * Closes the modal.
     */
    private void closeModal() {
        Stage stage = (Stage) lstAvialableGuilds.getScene().getWindow();
        stage.close();
    }

    /**
     * Returns a list of the guilds that should be added to the guildManager.
     *
     * @return
     */
    public Set<String> getSetGuildsToAdd() {
        return setGuildsToAdd;
    }

    /**
     * Returns a list of all the guilds that should be deleted from the
     * guildManager.
     *
     * @return
     */
    public Set<String> getSetGuildsToDelete() {
        return setGuildsToDelete;
    }

    /**
     * Returns the observable list containing how his guilds looks.
     *
     * @return
     */
    public ObservableList<String> getManagerGuilds() {
        return oManagerGuilds;
    }

}
