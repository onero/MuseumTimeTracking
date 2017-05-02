/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.model;

import java.util.List;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import museumtimetracking.be.APerson;
import museumtimetracking.be.GuildManager;
import museumtimetracking.bll.GuildMGRManager;

/**
 *
 * @author Mathias
 */
public class GuildManagerModel {

    private static GuildManagerModel instance;

    private final GuildMGRManager guildMGRManager;

    private final List<GuildManager> managersFromDB;
    private final ObservableList<GuildManager> cachedManagers;

    public static GuildManagerModel getInstance() {
        if (instance == null) {
            instance = new GuildManagerModel();
        }
        return instance;
    }

    private GuildManagerModel() {
        guildMGRManager = new GuildMGRManager();
        managersFromDB = guildMGRManager.getAllGuildManagers();
        cachedManagers = FXCollections.observableArrayList(managersFromDB);
    }

    /**
     ** Sends the Person object through to the GuildMGRManager to add it to the
     * DB. Then gets the new guildMananger and adds it to the cachedMemory.
     *
     * @param person
     * @param guildName
     */
    public void createNewGuildManager(APerson person, String guildName) {
        GuildManager manager = guildMGRManager.createNewGuildManager(person, guildName);
        cachedManagers.add(manager);
    }

    /**
     * Returns the list of chached Managers.
     *
     * @return
     */
    public ObservableList<GuildManager> getCachedManagers() {
        return cachedManagers;
    }

    /**
     * Sends the guildManagers data to the database that should be updated.
     *
     * @param manager
     * @param guildsToAdd
     * @param guildsToDelete
     */
    public void updateGuildManager(GuildManager manager, Set<String> guildsToAdd, Set<String> guildsToDelete) {
        guildMGRManager.updateGuildManager(manager, guildsToAdd, guildsToDelete);
    }

    /**
     * Deletes the manager from the cached list and sends the manager's id
     * through the layers to delete it from the db.
     *
     * @param guildManager
     */
    public void deleteGuildManager(GuildManager guildManager) {
        cachedManagers.remove(guildManager);
        guildMGRManager.deleteGuildManager(guildManager.getID());
    }

}
