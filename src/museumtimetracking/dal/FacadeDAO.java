/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.dal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import museumtimetracking.be.APerson;
import museumtimetracking.be.GuildManager;

/**
 *
 * @author Mathias
 */
public class FacadeDAO {

    private static FacadeDAO instance;

    private GuildManagerDAO guildManagerDAO;

    public static FacadeDAO getInstance() {
        if (instance == null) {
            instance = new FacadeDAO();
        }
        return instance;
    }

    private FacadeDAO() {
        try {
            guildManagerDAO = new GuildManagerDAO();
        } catch (IOException ex) {
            System.out.println("Couldn't get guildManagerDAO\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Creates the person in the DB. Then adds its guild, so it's a manager.
     * Then return a GuildManager with its accoiated guilds.
     *
     * TODO RKL: Refactor to transaction.
     *
     * @param person
     * @param guildName
     * @return
     */
    public GuildManager createNewGuildManager(APerson person, String guildName) {
        try {
            return guildManagerDAO.createNewGuildManager(person, guildName);
        } catch (SQLException ex) {
            System.out.println("Couldn't add guild manager to DB\n"
                    + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Gets a list of guild managers that are each filled with the appropriate
     * guilds from the GuildManagerDAO.
     *
     * @return
     */
    public List<GuildManager> getAllGuildManagers() {
        try {
            return guildManagerDAO.addGuildsToGuildManagers(guildManagerDAO.getAllGuildManagers());
        } catch (SQLException ex) {
            System.out.println("Couldn't get all guild managers from DB\n" + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Updates the guildManager in the database.
     *
     * @param manager
     * @param guildsToAdd
     * @param guildsToDelete
     */
    public void updateGuildManager(GuildManager manager, List<String> guildsToAdd, List<String> guildsToDelete) {
        try {
            guildManagerDAO.updateGuildManagerInDatabase(manager, guildsToAdd, guildsToDelete);
        } catch (SQLException ex) {
            System.out.println("Couldn't update GuildManager in database!\n"
                    + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
