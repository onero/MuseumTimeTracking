/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.model;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import museumtimetracking.be.APerson;
import museumtimetracking.be.GM;
import museumtimetracking.be.Guild;
import museumtimetracking.bll.GuildMGRManager;
import museumtimetracking.exception.DALException;

/**
 *
 * @author Mathias
 */
public class GuildManagerModel {

    private static GuildManagerModel instance;

    private final GuildMGRManager guildMGRManager;

    private final Set<GM> managersFromDB;
    private final ObservableList<GM> cachedManagers;

    private final Set<GM> idleGuildManagersFromDB;
    private final ObservableList<GM> cachedIdleGuildManagers;

    private final Set<GM> gmCandidatesFromDB;
    private final ObservableList<GM> cachedGMCandidates;

    public static GuildManagerModel getInstance() throws IOException, DALException {
        if (instance == null) {
            instance = new GuildManagerModel();
        }
        return instance;
    }

    private GuildManagerModel() throws IOException, DALException {
        guildMGRManager = new GuildMGRManager();
        gmCandidatesFromDB = new TreeSet<>(guildMGRManager.getAllGMCandidates());
        cachedGMCandidates = FXCollections.observableArrayList(gmCandidatesFromDB);
        managersFromDB = guildMGRManager.getAllGuildManagersNotIdle();
        idleGuildManagersFromDB = guildMGRManager.getAllIdleGuildManagers();
        cachedManagers = FXCollections.observableArrayList(managersFromDB);
        cachedIdleGuildManagers = FXCollections.observableArrayList(idleGuildManagersFromDB);

    }

    /**
     * Sort lists in natural order
     */
    public void sortLists() {
        Collections.sort(cachedManagers);
        Collections.sort(cachedIdleGuildManagers);
        Collections.sort(cachedGMCandidates);
    }

    public void searchForPersonWithoutGuild(String searchString) {
        cachedGMCandidates.clear();
        gmCandidatesFromDB.stream()
                .filter(gm -> gm.getFullName().toLowerCase().trim().contains(searchString.toLowerCase().trim()))
                .forEach(foundGM -> {
                    cachedGMCandidates.add(foundGM);
                });
    }

    /**
     * Archive a manager
     *
     * @param selectedManager
     */
    public void updateIdleManager(GM selectedManager, boolean value) throws DALException {
        if (value) {
            cachedManagers.remove(selectedManager);
            cachedIdleGuildManagers.add(selectedManager);
        } else {
            cachedManagers.add(selectedManager);
            cachedIdleGuildManagers.remove(selectedManager);
            sortLists();
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
        GM manager = guildMGRManager.createNewGuildManager(person, guildName);
        cachedManagers.add(manager);
        sortLists();
    }

    /**
     * Returns the list of chached Managers.
     *
     * @return
     */
    public ObservableList<GM> getCachedManagers() {
        return cachedManagers;
    }

    /**
     * Sends the guildManagers data to the database that should be updated.
     *
     * @param manager
     * @param guildsToAdd
     * @param guildsToDelete
     */
    public void updateGuildManager(GM manager, Set<String> guildsToAdd, Set<String> guildsToDelete) throws DALException {
        guildMGRManager.updateGuildManager(manager, guildsToAdd, guildsToDelete);
    }

    /**
     * Deletes the manager from the cached list and sends the manager's id
     * through the layers to delete it from the db.
     *
     * @param guildManager
     */
    public void deleteGuildManager(GM guildManager) throws DALException {
        cachedManagers.remove(guildManager);
        guildMGRManager.deleteGuildManager(guildManager.getID());
        sortLists();
    }

    /**
     *
     * @return
     */
    public ObservableList<GM> getCachedIdleGuildManagers() {
        return cachedIdleGuildManagers;
    }

    public void resetGuildManagers() {
        cachedManagers.clear();
        cachedManagers.addAll(managersFromDB);
    }

    public ObservableList<GM> getCachedGMCandidates() {
        return cachedGMCandidates;
    }

    /**
     * Assign guild to manager
     *
     * @param gm
     * @param guild
     * @throws DALException
     */
    public void assignGuildToManager(GM gm, Guild guild) throws DALException {
        guildMGRManager.assignGuildToManager(gm.getID(), guild.getName());
        guild.setGuildManager(gm);
    }

    /**
     * filters the cached list with the search text via stream.
     *
     * @param searchText
     */
    public void searchActiveManagers(String searchText) {
        cachedManagers.clear();
        managersFromDB.stream()
                .filter(g -> g.getFullName().toLowerCase().contains(searchText.toLowerCase()))
                .forEach(g -> cachedManagers.add(g));
    }

    /**
     * filters the cached list with the search text via stream.
     *
     * @param searchText
     */
    public void searchIdleManagers(String searchText) {
        cachedIdleGuildManagers.clear();
        idleGuildManagersFromDB.stream()
                .filter(g -> g.getFullName().toLowerCase().contains(searchText.toLowerCase()))
                .forEach(g -> cachedIdleGuildManagers.add(g));
    }

}
