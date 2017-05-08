/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import museumtimetracking.be.APerson;
import museumtimetracking.be.Guild;
import museumtimetracking.be.GuildManager;
import museumtimetracking.bll.GuildMGRManager;
import museumtimetracking.exception.DALException;

/**
 *
 * @author Mathias
 */
public class GuildManagerModel {

    private static GuildManagerModel instance;

    private final GuildMGRManager guildMGRManager;

    private final Set<GuildManager> managersFromDB;
    private final ObservableList<GuildManager> cachedManagers;

    private final Set<GuildManager> idleGuildManagersFromDB;
    private final ObservableList<GuildManager> cachedIdleGuildManagers;

    private final List<GuildManager> gmCandidatesFromDB;
    private final ObservableList<GuildManager> cachedGMCandidates;

    public static GuildManagerModel getInstance() throws IOException, DALException {
        if (instance == null) {
            instance = new GuildManagerModel();
        }
        return instance;
    }

    private GuildManagerModel() throws IOException, DALException {
        guildMGRManager = new GuildMGRManager();
        gmCandidatesFromDB = new ArrayList<>(guildMGRManager.getAllGMCandidates());
        cachedGMCandidates = FXCollections.observableArrayList(gmCandidatesFromDB);
        managersFromDB = guildMGRManager.getAllGuildManagersNotIdle();
        idleGuildManagersFromDB = guildMGRManager.getAllIdleGuildManagers();
        cachedManagers = FXCollections.observableArrayList(managersFromDB);
        cachedIdleGuildManagers = FXCollections.observableArrayList(idleGuildManagersFromDB);
    }

    public void searchForPersonWithoutGuild(String searchString) {
        cachedGMCandidates.clear();
        gmCandidatesFromDB.stream()
                .filter(gm -> gm.getFullName().toLowerCase().contains(searchString.toLowerCase()))
                .forEach(foundGM -> {
                    cachedGMCandidates.add(foundGM);
                });
    }

    /**
     * Archive a manager
     *
     * @param selectedManager
     */
    public void updateIdleManager(GuildManager selectedManager, boolean value) throws DALException {
        if (value) {
            cachedManagers.remove(selectedManager);
            cachedIdleGuildManagers.add(selectedManager);
        } else {
            cachedManagers.add(selectedManager);
            cachedIdleGuildManagers.remove(selectedManager);
        }
        guildMGRManager.archiveManager(selectedManager.getID(), value);
    }

    /**
     ** Sends the Person object through to the GuildMGRManager to add it to the
     * DB. Then gets the new guildMananger and adds it to the cachedMemory.
     *
     * @param person
     * @param guildName
     * @throws museumtimetracking.exception.DALException
     */
    public void createNewGuildManager(APerson person, String guildName) throws DALException {
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
    public void updateGuildManager(GuildManager manager, Set<String> guildsToAdd, Set<String> guildsToDelete) throws DALException {
        guildMGRManager.updateGuildManager(manager, guildsToAdd, guildsToDelete);
    }

    /**
     * Deletes the manager from the cached list and sends the manager's id
     * through the layers to delete it from the db.
     *
     * @param guildManager
     */
    public void deleteGuildManager(GuildManager guildManager) throws DALException {
        cachedManagers.remove(guildManager);
        guildMGRManager.deleteGuildManager(guildManager.getID());
    }

    /**
     *
     * @return
     */
    public ObservableList<GuildManager> getCachedIdleGuildManagers() {
        return cachedIdleGuildManagers;
    }

    public void searchGuildManagers(String newValue) {
        cachedManagers.clear();
        managersFromDB.stream()
                .filter(g -> g.getFullName().toLowerCase().contains(newValue.toLowerCase()))
                .forEach(g -> cachedManagers.add(g));
    }

    public void resetGuildManagers() {
        cachedManagers.clear();
        cachedManagers.addAll(managersFromDB);
    }

    public ObservableList<GuildManager> getCachedGMCandidates() {
        return cachedGMCandidates;
    }

    /**
     * Assign guild to manager
     *
     * @param gm
     * @param guild
     * @throws DALException
     */
    public void assignGuildToManager(GuildManager gm, Guild guild) throws DALException {
        guildMGRManager.assignGuildToManager(gm.getID(), guild.getName());
        guild.setGuildManager(gm);
    }

}
