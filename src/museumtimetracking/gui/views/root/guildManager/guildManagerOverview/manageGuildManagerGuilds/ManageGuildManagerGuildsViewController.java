/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.guildManager.guildManagerOverview.manageGuildManagerGuilds;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import museumtimetracking.be.Guild;
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

    private final GuildModel guildModel;

    private ObservableList<String> oAvaiableGuilds;
    private ObservableList<String> oManagerGuilds;

    private final List<String> listGuildsToAdd;
    private final List<String> listGuildsToDelete;

    private boolean leftListSelected;

    public ManageGuildManagerGuildsViewController() {
        oAvaiableGuilds = FXCollections.observableArrayList();
        oManagerGuilds = FXCollections.observableArrayList();

        listGuildsToAdd = new ArrayList<>();
        listGuildsToDelete = new ArrayList<>();

        guildModel = GuildModel.getInstance();
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
    private void handleMoveGuildButton(ActionEvent event) {
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
            if (checkIfNotInList(guildName, listGuildsToAdd)) {
                listGuildsToAdd.add(guildName);
                listGuildsToDelete.remove(guildName);
            }
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
            if (checkIfNotInList(guildName, listGuildsToDelete)) {
                listGuildsToDelete.add(guildName);
                listGuildsToAdd.remove(guildName);
            }
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        closeModal();
    }

    @FXML
    private void handleOkButton(ActionEvent event) {
        System.out.println("Guilds to add:");
        for (String name : listGuildsToAdd) {
            System.out.println(name);
        }
        System.out.println("Guilds to delete:");
        for (String name : listGuildsToDelete) {
            System.out.println(name);
        }
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
     * Checks if the parsed name is not in the parsed list. Returns true if it
     * is not in the list.
     *
     * @param guildName
     * @param listOfGuilds
     * @return
     */
    private boolean checkIfNotInList(String guildName, List<String> listOfGuilds) {
        boolean isNotInList = true;
        for (String guild : listOfGuilds) {
            if (guild.equals(guildName)) {
                isNotInList = false;
            }
        }
        return isNotInList;
    }

    /**
     * Returns a list of the guilds that should be added to the guildManager.
     *
     * @return
     */
    public List<String> getListGuildsToAdd() {
        return listGuildsToAdd;
    }

    /**
     * Returns a list of all the guilds that should be deleted from the
     * guildManager.
     *
     * @return
     */
    public List<String> getListGuildsToDelete() {
        return listGuildsToDelete;
    }

}
