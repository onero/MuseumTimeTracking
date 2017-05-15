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
import jxl.write.WriteException;
import museumtimetracking.be.Guild;
import museumtimetracking.bll.fileWriters.ExcelWriter;
import museumtimetracking.dal.FacadeDAO;
import museumtimetracking.exception.DALException;
import museumtimetracking.gui.model.GuildModel;

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
     * Export all guild hours to excel sheet
     *
     * @throws IOException
     * @throws WriteException
     * @throws DALException
     */
    public void exportGuildHoursToExcel() throws IOException, WriteException, DALException {
        ExcelWriter newFile = new ExcelWriter();
        //TODO ALH: Add FileOutput
        newFile.setOutputFile("D:\\Download\\adamino.xls");
        newFile.createNewExcel("Raport over laug");

        newFile.createCaptions("Laug", "Timer");

        Map<String, Integer> guildHours = GuildModel.getInstance().getMapOfHoursPerGuild();

        List<String> keys = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : guildHours.entrySet()) {
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }

        newFile.createContent(keys, values);

        newFile.writeExcelToFile();
    }

}
