/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
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

    private final List<Guild> archivedGuilds;

    private final GuildManager guildManager;

    private GuildModel() {
        // Instantiate guildManager
        guildManager = new GuildManager();
        // Puts in all the guilds from DB from Manager to Model.
        guildsFromDB = guildManager.getAllGuilds();
        archivedGuilds = new ArrayList<>();
        // Puts the guilds from the DB inside a ObservableList.
        cachedGuilds = FXCollections.observableArrayList(guildsFromDB);
        
    }
    // Made a getter to call in GuildTableViewController.
    public ObservableList<Guild> getCachedGuilds() {
        return cachedGuilds;
    }

    public List<Guild> getArchivedGuilds() {
        return archivedGuilds;
    }

    /**
     * Adds the guild to DB.
     *
     * @param guild
     */
    public void addGuild(Guild guild) {
        guildManager.addGuild(guild);
    }

    /**
     *
     * @return guilds from DB
     */
    public List<Guild> getAllGuilds() {
        return guildManager.getAllGuilds();
    }

    /**
     * Update guild in DB with new info
     *
     * @param guildToUpdate
     * @param updatedGuild
     */
    public void updateGuild(String guildToUpdate, Guild updatedGuild) {
        guildManager.updateGuild(guildToUpdate, updatedGuild);
    }

}
