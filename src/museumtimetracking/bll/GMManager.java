/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.bll;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import javafx.collections.ObservableList;
import jxl.write.WriteException;
import museumtimetracking.be.APerson;
import museumtimetracking.be.GM;
import museumtimetracking.dal.DALFacade;
import museumtimetracking.dal.fileWriting.excel.ExcelWriter;
import museumtimetracking.dal.fileWriting.excel.IExcel;
import museumtimetracking.exception.DALException;
import museumtimetracking.gui.model.GuildManagerModel;

/**
 *
 * @author Mathias
 */
public class GMManager implements IExcel {

    private final DALFacade facadeDAO;

    public GMManager() {
        facadeDAO = DALFacade.getInstance();
    }

    /**
     * Archive a manager
     *
     * @param selectedManager
     */
    public void archiveManager(int id, boolean value) throws DALException {
        facadeDAO.archiveManager(id, value);
    }

    /**
     * Sends the Person object through to the facadeDAO to add it to the DB.
     * Returns the new GuildManager.
     *
     * @param person
     * @param guildName
     * @return
     * @throws museumtimetracking.exception.DALException
     */
    public GM createNewGuildManager(APerson person, String guildName) throws DALException {
        return facadeDAO.createNewGuildManager(person, guildName);
    }

    /**
     * Get all guild managers not idle
     *
     * @return
     */
    public Set<GM> getAllGuildManagersNotIdle() throws DALException {
        return facadeDAO.getAllGuildManagersNotIdle();
    }

    /**
     * Gets a list of idle Guild Managers GuildManagers and it's Guilds.
     *
     * @return
     * @throws museumtimetracking.exception.DALException
     */
    public Set<GM> getAllIdleGuildManagers() throws DALException {
        return facadeDAO.getAllIdleGuildManagers();
    }

    /**
     * Sends the informtion through to DAL to be updated.
     *
     * @param manager
     * @param guildsToAdd
     * @param guildsToDele
     */
    public void updateGuildManager(GM manager, Set<String> guildsToAdd, Set<String> guildsToDele) throws DALException {
        facadeDAO.updateGuildManager(manager, guildsToAdd, guildsToDele);
        updateGuildsOnManager(manager.getObservableListOfGuilds(), guildsToAdd, guildsToDele);
    }

    /**
     * Updates the cached guildMaster list of guilds with updated information.
     *
     * @param managerGuilds
     * @param guildsToAdd
     * @param guildsToDelete
     */
    private void updateGuildsOnManager(ObservableList<String> managerGuilds, Set<String> guildsToAdd, Set<String> guildsToDelete) {

        if (guildsToAdd != null) {
            managerGuilds.addAll(guildsToAdd);
        }
        if (guildsToDelete != null) {
            managerGuilds.removeAll(guildsToDelete);
        }
    }

    /**
     * Sends the managerID through the layers to delete it from the db.
     *
     * @param GuildManagerID
     * @throws museumtimetracking.exception.DALException
     */
    public void deleteGuildManager(int GuildManagerID) throws DALException {
        facadeDAO.deleteGuildManagerFromDB(GuildManagerID);
    }

    /**
     * Get all GuildManager candidates
     *
     * @return
     * @throws DALException
     */
    public Set<GM> getAllGMCandidates() throws DALException {
        return facadeDAO.getAllGMCandidates();
    }

    /**
     * Assign guild to manager
     *
     * @param id
     * @param guildName
     */
    public void assignGuildToManager(int id, String guildName) throws DALException {
        facadeDAO.assignGuildToManager(id, guildName);
    }

    /**
     * Save the entire GuildManager model
     *
     * @param model
     * @throws IOException
     */
    public void saveModel(GuildManagerModel model) {
        facadeDAO.saveGuildManagerModelToFile(model);
    }

    /**
     * Load entire GuildManager model
     *
     * @return
     * @throws IOException
     */
    public GuildManagerModel loadGuildModelFromFile() {
        return facadeDAO.loadGuildManagerModelFromFile();
    }

    /*
     * Writes the ROI to a excel sheet and saves it.
     *
     * @param <T>
     * @param location
     * @param values
     * @throws IOException
     * @throws WriteException
     * @throws DALException
     */
    @Override
    public <T> void exportToExcel(String location, List<T>... values) throws IOException, WriteException, DALException {
        ExcelWriter newFile = new ExcelWriter();
        newFile.setOutputFile(location);
        newFile.createNewExcel("ROI for Laug");

        newFile.createCaptions(0, "Laug", "Afkast");

        newFile.createLabelNumberContent((List<String>) values[0], (List<Integer>) values[1]);

        newFile.writeExcelToFile();
    }

    /**
     * Gets the restriction of a GM's description.
     *
     * @return
     * @throws DALException
     */
    public int getGmDescriptionRestriction() throws DALException {
        return facadeDAO.getDescriptionRestrictionForGm();
    }

}
