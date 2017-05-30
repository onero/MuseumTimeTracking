/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jxl.write.WriteException;
import museumtimetracking.be.APerson;
import museumtimetracking.be.GM;
import museumtimetracking.be.Guild;
import museumtimetracking.bll.GMManager;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.views.root.MTTMainControllerView;

/**
 *
 * @author Mathias
 */
public class GuildManagerModel implements Externalizable, IASyncUpdate, ISaveModel<GuildManagerModel> {

    private transient GMManager gmManager;

    private Set<GM> managersFromDB;
    private ObservableList<GM> cachedManagers;

    private Set<GM> idleGuildManagersFromDB;
    private ObservableList<GM> cachedIdleGuildManagers;

    private Set<GM> gmCandidatesFromDB;
    private ObservableList<GM> cachedGMCandidates;

    private int descriptionRestriction;

    public GuildManagerModel() {
    }

    public GuildManagerModel(boolean onlineMode) throws DALException {
        gmManager = new GMManager();
//        instantiateCollections();
        gmCandidatesFromDB = new TreeSet<>(gmManager.getAllGMCandidates());
        managersFromDB = gmManager.getAllGuildManagersNotIdle();
        idleGuildManagersFromDB = gmManager.getAllIdleGuildManagers();

        cachedGMCandidates = FXCollections.observableArrayList(gmCandidatesFromDB);
        cachedManagers = FXCollections.observableArrayList(managersFromDB);
        cachedIdleGuildManagers = FXCollections.observableArrayList(idleGuildManagersFromDB);

        descriptionRestriction = gmManager.getGmDescriptionRestriction();

        saveModel(this);

    }

    @Override
    public void saveModel(GuildManagerModel model) {
        gmManager.saveModel(this);
    }

    @Override
    public void updateData() {
        Runnable task = () -> {
            Platform.runLater(() -> {
                try {
                    instantiateCollections();
                } catch (DALException ex) {
                    ExceptionDisplayer.display(ex);
                }
            });
        };
        new Thread(task).start();
    }

    private void instantiateCollections() throws DALException {
        gmCandidatesFromDB = new TreeSet<>(gmManager.getAllGMCandidates());
        cachedGMCandidates.clear();
        cachedGMCandidates.addAll(cachedManagers);

        managersFromDB = gmManager.getAllGuildManagersNotIdle();
        cachedManagers.clear();
        cachedManagers.addAll(managersFromDB);

        idleGuildManagersFromDB = gmManager.getAllIdleGuildManagers();
        cachedIdleGuildManagers.clear();
        cachedIdleGuildManagers.addAll(idleGuildManagersFromDB);

        gmManager.saveModel(this);
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
        gmManager.archiveManager(selectedManager.getID(), value);

        MTTMainControllerView.getInstance().handleUpdate();
    }

    /**
     ** Sends the Person object through to the GuildMGRManager to add it to the
     * DB. Then gets the new guildMananger and adds it to the cachedMemory.
     *
     * @param person
     * @param guild
     * @throws museumtimetracking.exception.DALException
     */
    public void createNewGuildManager(APerson person, Guild guild) throws DALException {
        GM manager = gmManager.createNewGuildManager(person, guild.getName());
        guild.setGuildManager(manager);
        cachedManagers.add(manager);
        sortLists();

        MTTMainControllerView.getInstance().handleUpdate();
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
        gmManager.updateGuildManager(manager, guildsToAdd, guildsToDelete);

        MTTMainControllerView.getInstance().handleUpdate();
    }

    /**
     * Deletes the manager from the cached list and sends the manager's id
     * through the layers to delete it from the db.
     *
     * @param guildManager
     */
    public void deleteGuildManager(GM guildManager) throws DALException {
        cachedManagers.remove(guildManager);
        cachedGMCandidates.remove(guildManager);
        cachedIdleGuildManagers.remove(guildManager);
        gmManager.deleteGuildManager(guildManager.getID());
        sortLists();

        MTTMainControllerView.getInstance().handleUpdate();
    }

    /**
     *
     * @return
     */
    public ObservableList<GM> getCachedIdleGuildManagers() {
        return cachedIdleGuildManagers;
    }

    /**
     * Clears and resets the cached guild managers
     */
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
        gmManager.assignGuildToManager(gm.getID(), guild.getName());
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

    /**
     * Removes a guild from the manager
     *
     * @param guildToRemove
     */
    public void removeGuildFromManager(String guildToRemove) {
        cachedManagers.stream()
                .filter(gm -> gm.getObservableListOfGuilds().contains(guildToRemove))
                .forEach(gm -> gm.removeGuild(guildToRemove));
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(managersFromDB);
        out.writeObject(gmCandidatesFromDB);
        out.writeObject(idleGuildManagersFromDB);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        gmManager = new GMManager();
        managersFromDB = (Set<GM>) in.readObject();
        cachedManagers = FXCollections.observableArrayList(managersFromDB);
        gmCandidatesFromDB = (Set<GM>) in.readObject();
        cachedGMCandidates = FXCollections.observableArrayList(gmCandidatesFromDB);
        idleGuildManagersFromDB = (Set<GM>) in.readObject();
        cachedIdleGuildManagers = FXCollections.observableArrayList(idleGuildManagersFromDB);
    }

    /**
     * Finds all guildNames and their acosiated ROI and export them to an excel.
     *
     * @param location
     * @throws IOException
     * @throws DALException
     * @throws WriteException
     */
    public void exportROIToExcel(String location) throws IOException, DALException, WriteException {
        GuildModel guildModel = ModelFacade.getInstance().getGuildModel();
        List<Guild> guilds = guildModel.getGuildsFromDB();
        List<String> guildNames = new ArrayList<>();
        List<Integer> guildROI = new ArrayList<>();

        //Gets the ROI for each guild and puts it in guildROI so that
        //the guild at index x in guildNames mathces the ROI at index x in guildROI.
        for (Guild guild : guilds) {
            int roi = guildModel.getROIForAGuild(guild.getName());
            if (roi != 0) {
                guildNames.add(guild.getName());
                guildROI.add(roi);
            }
        }

        gmManager.exportToExcel(location, new ArrayList<>(guildNames), new ArrayList<>(guildROI));
    }

    /**
     * Returns the restriction of the description for a GM.
     *
     * @return
     */
    public int getDescriptionRestriction() {
        return descriptionRestriction;
    }

}
