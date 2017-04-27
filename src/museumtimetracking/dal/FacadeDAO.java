/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.dal;

import java.io.IOException;
import java.sql.SQLException;
import museumtimetracking.be.APerson;
import museumtimetracking.be.Guild;
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
     * Sends the Person object through to the DAO to add it to the DB.
     *
     * @param person
     * @param guild
     */
    public void createNewGuildManager(APerson person, Guild guild) {
        try {
            GuildManager guildManager = guildManagerDAO.createNewGuildManager(person);
            guildManagerDAO.addGuildToManager(guildManager.getID(), guild.getName());
        } catch (SQLException ex) {
            System.out.println("Couldn't add guild manager to DB\n" + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
