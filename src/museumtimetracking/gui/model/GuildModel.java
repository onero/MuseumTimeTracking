/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.model;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import museumtimetracking.be.Guild;
import museumtimetracking.bll.GuildManager;

public class GuildModel {

    private static GuildModel instance;

    public static GuildModel getInstance() {
        if (instance == null) {
            instance = new GuildModel();
        }
        return instance;
    }

    private final List<Guild> guildsFromDB;

    private final ObservableList<Guild> cachedGuilds;

    private final List<Guild> archivedGuildsFromDB;

    private final ObservableList<Guild> cachedArchivedGuilds;

    private final GuildManager guildManager;

    private GuildModel() {
        // Instantiate guildManager
        guildManager = new GuildManager();
        // Puts in all the guilds from DB to Manager and after Model.
        guildsFromDB = guildManager.getAllGuildsNotArchived();
        archivedGuildsFromDB = guildManager.getAllGuildsArchived();
        // Puts the guilds from the DB inside a ObservableList.
        cachedGuilds = FXCollections.observableArrayList(guildsFromDB);
        cachedArchivedGuilds = FXCollections.observableArrayList(archivedGuildsFromDB);

    }

    /**
     * Archives the parsed guild in DB
     *
     * @param guildToArchive
     */
    public void archiveGuild(Guild guildToArchive) {
        cachedArchivedGuilds.add(guildToArchive);
        guildManager.archiveGuild(guildToArchive);
        cachedGuilds.remove(guildToArchive);
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
     * Deletes the guild from tableView and DB.
     * Comes from GuildTableViewController and goes to GuildManager.
     *
     * @param deleteGuild
     */
    public void deleteGuild(Guild deleteGuild) {
        guildManager.deleteGuild(deleteGuild);
        // Removes the guild from the list.
        cachedGuilds.remove(deleteGuild);
    }

    /**
     * Adds the guild to DB.
     *
     * @param guild
     */
    public void addGuild(Guild guild) {
        guildManager.addGuild(guild);
        cachedGuilds.add(guild);
    }

    /**
     * Restores guild from archive
     *
     * @param guildToRestore
     */
    public void restoreGuild(Guild guildToRestore) {
        guildManager.restoreGuild(guildToRestore);
        cachedArchivedGuilds.remove(guildToRestore);
        cachedGuilds.add(guildToRestore);
    }

    /**
     * Update guild in DB with new info
     *
     * @param guildToUpdate
     * @param updatedGuild
     */
    public void updateGuild(String guildToUpdate, Guild updatedGuild) {
        guildManager.updateGuild(guildToUpdate, updatedGuild);
        // Made a stream with lamda which updates the list from the database.
        cachedGuilds.stream()
                .filter(g -> g.getName().equalsIgnoreCase(updatedGuild.getName()))
                .forEach(g -> {
                    g.setName(updatedGuild.getName());
                    g.setDescription(updatedGuild.getDescription());
                });
        
        
    }

}
