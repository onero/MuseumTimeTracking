/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.bll;

import java.io.IOException;
import java.util.ArrayList;
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

    /**
     * Gets all guilds with no manager from DAO
     *
     * @return
     * @throws DALException
     */
    public List<Guild> getGuildsWithoutManagers() throws DALException {
        return facadeDAO.getGuildsWithoutManagers();
    }

    /**
     * Assign new GM to parsed guild
     *
     * @param id
     * @param guildName
     * @throws DALException
     */
    public void updateGMForGuild(int id, String guildName) throws DALException {
        facadeDAO.updateGMForGuild(id, guildName);
    }

    /**
     * Calculate the total return on investment a
     * guild managers spends on volunteers for a single guild in a month
     *
     * @param GMGoursInAMonth
     * @return Guildmanager return on investment as an int
     */
    public int getGMROIOnVolunteerForAMonth(Guild selectedGuild, int GMGoursInAMonth) throws DALException {
        int total = 0;

        List<Guild> guild = new ArrayList<>();
        guild.add(selectedGuild);
        Map<String, Integer> hoursWorked = getAllHoursWorked(guild);

        int totalGuildHoursForAMonth = hoursWorked.get(selectedGuild.getName());

        if (totalGuildHoursForAMonth != 0) {
            total = totalGuildHoursForAMonth / GMGoursInAMonth;
        }

        return total;
    }
}
