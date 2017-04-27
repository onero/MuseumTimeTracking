/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.bll;

import java.util.List;
import museumtimetracking.be.Guild;
import museumtimetracking.dal.GuildDAO;

/**
 *
 * @author Skovgaard
 */
public class GuildManager {

    private final GuildDAO guildDAO;

    public GuildManager() {
        guildDAO = GuildDAO.getInstance();
    }

    /**
     * Adds a new guild to DB.
     *
     * @param guildToAdd
     */
    public void addGuild(Guild guildToAdd) {
        guildDAO.addGuild(guildToAdd);
    }
    /**
     * Deletes guilds from tableView and DB.
     * Comes from GuildModel and goes to GuildDao.
     * @param deleteGuild 
     */
    public void deleteGuild(Guild deleteGuild){
        guildDAO.deleteGuild(deleteGuild);
    }

    /**
     * Gets all the guilds from the DB.
     *
     * @return
     */
    public List<Guild> getAllGuilds() {
        return guildDAO.getAllGuilds();
    }

    /**
     * Update guild in DB with new info
     *
     * @param guildToUpdate
     * @param updatedGuild
     */
    public void updateGuild(String guildToUpdate, Guild updatedGuild) {
        guildDAO.updateGuild(guildToUpdate, updatedGuild);
    }

}
