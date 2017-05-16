/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.model;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import museumtimetracking.be.GM;
import museumtimetracking.be.Guild;
import museumtimetracking.bll.GuildManager;
import museumtimetracking.exception.DALException;

public class GuildModel {

    private static GuildModel instance;

    public static GuildModel getInstance() throws IOException, DALException {
        if (instance == null) {
            instance = new GuildModel();
        }
        return instance;
    }

    private final List<Guild> guildsFromDB;

    private final ObservableList<Guild> cachedGuilds;

    private final ObservableList<Guild> cachedAvailableGuilds;

    private final List<Guild> archivedGuildsFromDB;

    private final ObservableList<Guild> cachedArchivedGuilds;

    private final GuildManager guildManager;

    private Map<String, Integer> guildHours;

    private Map<String, Integer> guildROI;

    private GuildModel() throws IOException, DALException {
        // Instantiate guildManager
        guildManager = new GuildManager();
        // Puts in all the guilds from DB to Manager and after Model.
        guildsFromDB = guildManager.getAllGuildsNotArchived();
        archivedGuildsFromDB = guildManager.getAllGuildsArchived();
        // Puts the guilds from the DB inside a ObservableList.
        cachedGuilds = FXCollections.observableArrayList(guildsFromDB);
        cachedAvailableGuilds = FXCollections.observableArrayList(guildManager.getGuildsWithoutManagers());
        cachedArchivedGuilds = FXCollections.observableArrayList(archivedGuildsFromDB);
        guildHours = guildManager.getAllHoursWorked(guildsFromDB);

        Collections.sort(guildsFromDB);

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
        guildHours = guildManager.getAllHoursWorked(guildsFromDB);
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
     * Calculate the total return on investment a guild managers spends on
     * volunteers for a single guild in a month, for all guilds parsed.
     *
     * @param selectedGuilds
     * @param GMWorkHours
     * @return
     * @throws museumtimetracking.exception.DALException
     */
    public Map<String, Integer> getGMROIOnVolunteerForAMonth(List<Guild> selectedGuilds, int GMWorkHours) throws DALException {
        guildROI = guildManager.getGMROIOnVolunteerForAMonth(selectedGuilds, GMWorkHours);
        return guildROI;
    }

    /**
     * Gets the value from guildROI.
     *
     * @param guildName the key for the map.
     * @return ROI for the guild as int. Or zero if there is no ROI.
     */
    public int getROIForAGuild(String guildName) {
        try {
            return guildROI.get(guildName);
        } catch (NullPointerException nex) {
            return 0;
        }
    }
}
