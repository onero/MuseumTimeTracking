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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import jxl.write.WriteException;
import museumtimetracking.be.GM;
import museumtimetracking.be.Guild;
import museumtimetracking.be.Volunteer;
import museumtimetracking.bll.GuildManager;
import museumtimetracking.dal.fileWriting.GuildFileDAO;
import museumtimetracking.exception.AlertFactory;
import museumtimetracking.exception.DALException;

public class GuildModel implements Externalizable {

    private static GuildModel instance;

    private List<Guild> guildsFromDB;

    private ObservableList<Guild> cachedGuilds;

    private List<Guild> availableGuildsFromDB;

    private ObservableList<Guild> cachedAvailableGuilds;

    private List<Guild> archivedGuildsFromDB;

    private ObservableList<Guild> cachedArchivedGuilds;

    private transient GuildManager guildManager;

    private Map<String, Integer> guildHours;

    private Map<String, Integer> guildROI;

    public static GuildModel getInstance() throws DALException {
        if (instance == null) {
            try {
                instance = new GuildModel(true);
            } catch (DALException ex) {
                instance = new GuildFileDAO().loadModel();
                Alert alert = new AlertFactory().createExceptionAlert("Kontroller internetforbindelse\nForsøger at starte programmet igen om et øjeblik");
                alert.show();
                try {
                    Thread.sleep(5000);
                    alert.resultProperty().setValue(ButtonType.OK);
                } catch (InterruptedException ex1) {
                    Logger.getLogger(GuildModel.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
        return instance;
    }

    public GuildModel() {
    }

    public GuildModel(boolean onlineMode) throws DALException {
        guildManager = new GuildManager();
        // Instantiate guildManager
        // Puts in all the guilds from DB/Local file to Manager and after Model.
        guildsFromDB = guildManager.getAllGuildsNotArchived();
        cachedGuilds = FXCollections.observableArrayList(guildsFromDB);

        archivedGuildsFromDB = guildManager.getAllGuildsArchived();
        cachedArchivedGuilds = FXCollections.observableArrayList(archivedGuildsFromDB);

        availableGuildsFromDB = guildManager.getGuildsWithoutManagers();
        cachedAvailableGuilds = FXCollections.observableArrayList(availableGuildsFromDB);

        guildHours = guildManager.getAllHoursWorked(guildsFromDB);

        guildROI = guildManager.getGMROIOnVolunteerForAMonth(cachedGuilds, 2);

        Collections.sort(guildsFromDB);

        guildManager.saveGuildModel(this);

    }

    /**
     * Sort lists in natural order
     */
    public void sortLists() {
        Collections.sort(cachedGuilds);
        Collections.sort(cachedAvailableGuilds);
        Collections.sort(cachedArchivedGuilds);
    }

    /**
     * Archives the parsed guild in DB
     *
     * @param guildToArchive
     * @throws museumtimetracking.exception.DALException
     */
    public void archiveGuild(Guild guildToArchive) throws DALException {
        cachedArchivedGuilds.add(guildToArchive);
        guildManager.archiveGuild(guildToArchive);
        cachedGuilds.remove(guildToArchive);
        sortLists();
    }

    // Made a getter to call in GuildOverviewController to update the tableview.
    public ObservableList<Guild> getCachedGuilds() {
        return cachedGuilds;
    }

    /**
     *
     * @return memorycached guilds
     */
    public ObservableList<Guild> getCachedArchivedGuilds() {
        return cachedArchivedGuilds;
    }

    /**
     * Deletes the guild from tableView and DB. Comes from
     * GuildTableViewController and goes to GuildManager.
     *
     * @param deleteGuild
     */
    public void deleteGuild(Guild deleteGuild) throws DALException {
        // Removes the guild from the list.
        cachedGuilds.remove(deleteGuild);
        cachedAvailableGuilds.remove(deleteGuild);
        cachedArchivedGuilds.remove(deleteGuild);
        guildManager.deleteGuild(deleteGuild);
    }

    /**
     * Adds the guild to DB.
     *
     * @param guild
     */
    public void addGuild(Guild guild) throws DALException {
        guildManager.addGuild(guild);
        cachedGuilds.add(guild);
        sortLists();

    }

    /**
     * Restores guild from archive
     *
     * @param guildToRestore
     */
    public void restoreGuild(Guild guildToRestore) throws DALException {
        guildManager.restoreGuild(guildToRestore);
        cachedArchivedGuilds.remove(guildToRestore);
        cachedGuilds.add(guildToRestore);
        sortLists();
    }

    /**
     * Update guild in DB with new info
     *
     * @param guildToUpdate
     * @param updatedGuild
     */
    public void updateGuild(String guildToUpdate, Guild updatedGuild) throws DALException {
        // Made a stream with lamda which updates the list from the database.
        cachedGuilds.stream()
                .filter(g -> g.getName().equalsIgnoreCase(guildToUpdate))
                .forEach(g -> {
                    g.setName(updatedGuild.getName());
                    g.setDescription(updatedGuild.getDescription());
                });
        guildManager.updateGuild(guildToUpdate, updatedGuild);
        sortLists();
    }

    /**
     * Returns the Map containg the hours worked for each Guild.
     *
     * @return
     * @throws museumtimetracking.exception.DALException
     */
    public Map<String, Integer> getMapOfHoursPerGuild() throws DALException {
        Map<String, Integer> hours = guildManager.getAllHoursWorked(guildsFromDB);
        if (hours != null) {
            guildHours = hours;
        }
        return getGuildHours();
    }

    public Map<String, Integer> getGuildHours() {
        return guildHours;
    }

    /**
     * Returns the List containg the Guilds gotten from DB.
     *
     * @return
     */
    public List<Guild> getGuildsFromDB() {
        return guildsFromDB;
    }

    /**
     * filters the cached list with the search text via stream.
     *
     * @param newValue
     */
    public void searchGuilds(String newValue) {
        cachedGuilds.clear();
        guildsFromDB.stream()
                .filter(g -> g.getName().toLowerCase().contains(newValue.toLowerCase())
                || g.getGuildManager() != null && g.getGuildManager().getFullName().toLowerCase().contains(newValue.toLowerCase()))
                .forEach(g -> cachedGuilds.add(g));
    }

    /**
     * filters the cached list with the search text via stream.
     *
     * @param newValue
     */
    public void searchInactiveGuilds(String newValue) {
        cachedArchivedGuilds.clear();
        archivedGuildsFromDB.stream()
                .filter(g -> g.getName().toLowerCase().contains(newValue.toLowerCase()))
                .forEach(g -> cachedArchivedGuilds.add(g));
    }

    /**
     * Clears the cached list and adds them anew.
     */
    public void resetGuilds() {
        cachedGuilds.clear();
        cachedGuilds.addAll(guildsFromDB);
    }

    public void resetArchivedGuilds() {
        cachedArchivedGuilds.clear();
        cachedArchivedGuilds.addAll(archivedGuildsFromDB);
    }

    /**
     * Return the list of available guilds.
     *
     * @return
     */
    public ObservableList<Guild> getCachedAvailableGuilds() {
        return cachedAvailableGuilds;
    }

    /**
     * Assign new GM to parsed guild
     *
     * @param gm
     * @param guild
     */
    public void updateGMForGuild(GM gm, Guild guild) throws DALException {
        guildManager.updateGMForGuild(gm.getID(), guild.getName());
        guild.setGuildManager(gm);
    }

    /**
     * Add new available guild
     *
     * @param guildToAdd
     */
    public void addCachedAvailableGuild(Guild guildToAdd) {
        cachedAvailableGuilds.add(guildToAdd);
        sortLists();
    }

    /**
     * Remove guild from available
     *
     * @param guildToRemove
     */
    public void removeCachedAvailableGuild(Guild guildToRemove) {
        cachedAvailableGuilds.remove(guildToRemove);
    }

    /**
     * Export all guild hours to excel sheet
     *
     * @param location
     * @throws IOException
     * @throws WriteException
     * @throws DALException
     */
    public void exportGuildHoursToExcel(String location) throws IOException, WriteException, DALException {
        getMapOfHoursPerGuild();

        //Create Guild name keys (Will be strings)
        List keys = new ArrayList<>(guildHours.keySet());

        //Create hour values (will be integers)
        List values = new ArrayList<>(guildHours.values());

        guildManager.exportToExcel(location, keys, values);
    }

    /**
     * Gets the value from guildROI.
     *
     * @param guildName the key for the map.
     * @return ROI for the guild as int. Or zero if there is no ROI.
     */
    public int[] getROIForAGuild(String guildName) {
        try {
            int[] roi = new int[3];
            int roiForGuild = guildROI.get(guildName);
            roi[0] = roiForGuild / 4;
            roi[1] = roiForGuild;
            roi[2] = roiForGuild * 12;
            return roi;
        } catch (NullPointerException nex) {
            return null;
        }
    }

    //TODO RKL: JAVADOC!
    public List<String> getGuildsAVolunteerHasWorkedOn(Volunteer volunteer) throws DALException {
        return guildManager.getGuildsAVolunteerHasWorkedOn(volunteer);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(guildsFromDB);

        out.writeObject(availableGuildsFromDB);

        out.writeObject(archivedGuildsFromDB);

        out.writeObject(guildHours);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        guildManager = new GuildManager();
        guildsFromDB = (List<Guild>) in.readObject();
        cachedGuilds = FXCollections.observableArrayList(guildsFromDB);

        availableGuildsFromDB = (List<Guild>) in.readObject();
        cachedAvailableGuilds = FXCollections.observableArrayList(availableGuildsFromDB);

        archivedGuildsFromDB = (List<Guild>) in.readObject();
        cachedArchivedGuilds = FXCollections.observableArrayList(archivedGuildsFromDB);

        guildHours = (Map<String, Integer>) in.readObject();

        guildROI = new HashMap<>();

        Collections.sort(guildsFromDB);
    }

    /**
     * Gets all hours that has been added to a guild.
     *
     * @param guildName
     * @return
     */
    public Integer getWorkHoursInGuild(String guildName) throws DALException {
        return guildManager.getWorkHoursInGuild(guildName);

    }

    public Map<String, Integer> getGuildROI() {
        return guildROI;
    }

}
