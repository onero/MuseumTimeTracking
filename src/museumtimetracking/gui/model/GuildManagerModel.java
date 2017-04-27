/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.model;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import museumtimetracking.be.APerson;
import museumtimetracking.be.GuildManager;
import museumtimetracking.bll.GuildMGRManager;

/**
 *
 * @author Mathias
 */
public class GuildManagerModel {

    private static GuildManagerModel instance;

    private final GuildMGRManager guildMGRManager;

    private List<GuildManager> managersFromDB;
    private ObservableList<GuildManager> cachedManagers;

    public static GuildManagerModel getInstance() {
        if (instance == null) {
            instance = new GuildManagerModel();
        }
        return instance;
    }

    private GuildManagerModel() {
        guildMGRManager = new GuildMGRManager();
        managersFromDB = guildMGRManager.getAllGuildManagers();
        cachedManagers = FXCollections.observableArrayList(managersFromDB);
        for (GuildManager cachedManager : cachedManagers) {
            System.out.println(cachedManager.getFullName());
            for (String guildName : cachedManager.getListOfGuilds()) {
                System.out.println(guildName);
            }
            System.out.println("\n");
        }
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

    /**
     * Returns the list of chached Managers.
     *
     * @return
     */
    public ObservableList<GuildManager> getCachedManagers() {
        return cachedManagers;
    }

}
