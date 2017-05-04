/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.bll;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import museumtimetracking.be.Guild;
import museumtimetracking.dal.FacadeDAO;
import museumtimetracking.exception.DALException;

/**
 *
 * @author Skovgaard
 */
public class GuildManager {

    private final FacadeDAO facadeDAO;

    public GuildManager() throws IOException {
        facadeDAO = FacadeDAO.getInstance();
    }

    /**
     * Adds a new guild to DB.
     *
     * @param guildToAdd
     * @throws museumtimetracking.exception.DALException
     */
    public void addGuild(Guild guildToAdd) throws DALException {
        facadeDAO.addGuild(guildToAdd);
    }

    /**
     * Deletes guilds from tableView and DB. Comes from GuildModel and goes to
     * GuildDao.
     *
     * @param deleteGuild
     * @throws museumtimetracking.exception.DALException
     */
    public void deleteGuild(Guild deleteGuild) throws DALException {
        facadeDAO.deleteGuild(deleteGuild);
    }

    /**
     * Archive guild in in DB
     *
     * @param guildToArchive
     * @throws museumtimetracking.exception.DALException
     */
    public void archiveGuild(Guild guildToArchive) throws DALException {
        facadeDAO.archiveGuild(guildToArchive);
    }

    /**
     * Gets all the guilds from the DB.
     *
     * @return
     * @throws museumtimetracking.exception.DALException
     */
    public List<Guild> getAllGuildsNotArchived() throws DALException {
        return facadeDAO.getAllGuildsNotArchived();
    }

    /**
     * Gets all the guilds from the DB.
     *
     * @return
     * @throws museumtimetracking.exception.DALException
     */
    public List<Guild> getAllGuildsArchived() throws DALException {
        return facadeDAO.getAllGuildsArchived();
    }

    /**
     * Restore guild from archive in DB
     *
     * @param guildToRestore
     * @throws museumtimetracking.exception.DALException
     */
    public void restoreGuild(Guild guildToRestore) throws DALException {
        facadeDAO.restoreGuild(guildToRestore);
    }

    /**
     * Update guild in DB with new info
     *
     * @param guildToUpdate
     * @param updatedGuild
     */
    public void updateGuild(String guildToUpdate, Guild updatedGuild) throws DALException {
        facadeDAO.updateGuild(guildToUpdate, updatedGuild);
    }

    /**
     * Returns a Map containg all hours worked for each guild.
     *
     * @param guilds
     * @return
     * @throws DALException
     */
    public Map<String, Integer> getAllHoursWorked(List<Guild> guilds) throws DALException {
        List<String> guildNames = new LinkedList<>();
        for (Guild guild : guilds) {
            guildNames.add(guild.getName());
        }
        return facadeDAO.getAllHoursWorked(guildNames);
    }

}
