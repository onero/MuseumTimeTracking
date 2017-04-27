/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.bll;

import museumtimetracking.be.APerson;
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
     *
     * @param person
     * @param guildName
     */
    public void createNewGuildManager(APerson person, String guildName) {
        facadeDAO.createNewGuildManager(person, guildName);
    }
}
