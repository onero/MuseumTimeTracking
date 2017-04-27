/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.model;

import museumtimetracking.be.APerson;
import museumtimetracking.bll.GuildMGRManager;

/**
 *
 * @author Mathias
 */
public class GuildManagerModel {

    private static GuildManagerModel instance;

    private final GuildMGRManager guildMGRManager;

    public static GuildManagerModel getInstance() {
        if (instance == null) {
            instance = new GuildManagerModel();
        }
        return instance;
    }

    private GuildManagerModel() {
        guildMGRManager = new GuildMGRManager();
    }

    /**
     ** Sends the Person object through to the GuildMGRManager to add it to the
     * DB.
     *
     * @param person
     * @param guildName
     */
    public void createNewGuildManager(APerson person, String guildName) {
        guildMGRManager.createNewGuildManager(person, guildName);
    }
}
