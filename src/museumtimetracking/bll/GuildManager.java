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

    private GuildDAO guildDAO;

    public GuildManager() {
        guildDAO = GuildDAO.getInstance();
    }

    /**
     * Adds a new guild to DB.
     * @param guildToAdd
     */
    public void addGuild(Guild guildToAdd) {
        guildDAO.addGuild(guildToAdd);
    }

    public List<Guild> getAllGuilds(){
        return guildDAO.getAllGuilds();     
    }
    
}