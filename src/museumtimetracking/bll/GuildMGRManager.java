/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.bll;

import java.util.List;
import java.util.Set;
import javafx.collections.ObservableList;
import museumtimetracking.be.APerson;
import museumtimetracking.be.GuildManager;
import museumtimetracking.dal.FacadeDAO;

/**
 *
 * @author Mathias
 */
public class GuildMGRManager {

    private final FacadeDAO facadeDAO;

    public GuildMGRManager() {
        facadeDAO = FacadeDAO.getInstance();
    }

    /**
     * Sends the Person object through to the facadeDAO to add it to the DB.
     * Returns the new GuildManager.
     *
     * @param person
     * @param guildName
     * @return
     */
    public GuildManager createNewGuildManager(APerson person, String guildName) {
        return facadeDAO.createNewGuildManager(person, guildName);
    }

    /**
     * Gets a list of GuildManagers from the FacadeDAO that holds all
     * GuildManagers and it's Guilds.
     *
     * @return
     */
    public List<GuildManager> getAllGuildManagers() {
        return facadeDAO.getAllGuildManagers();
    }

    /**
     * Sends the informtion through to DAL to be updated.
     *
     * @param manager
     * @param guildsToAdd
     * @param guildsToDele
     */
    public void updateGuildManager(GuildManager manager, Set<String> guildsToAdd, Set<String> guildsToDele) {
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
            for (String guildName : guildsToAdd) {
                managerGuilds.add(guildName);
            }
        }
        if (guildsToDelete != null) {
            for (String guildName : guildsToDelete) {
                managerGuilds.remove(guildName);
            }
        }
    }

    /**
     * Sends the managerID through the layers to delete it from the db.
     *
     * @param GuildManagerID
     */
    public void deleteGuildManager(int GuildManagerID) {
        facadeDAO.deleteGuildManagerFromDB(GuildManagerID);
    }
}
