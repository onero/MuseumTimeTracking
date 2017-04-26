/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.be;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mathias
 */
public class GuildManager extends APerson {

    private final List<Guild> listOfGuilds;

    public GuildManager(String firstName, String lastName, String email, int phone) {
        super(firstName, lastName, email, phone);
        listOfGuilds = new ArrayList();
    }

    public GuildManager(String firstName, String lastName, String email, int phone, int ID) {
        super(firstName, lastName, email, phone, ID);
        listOfGuilds = new ArrayList();
    }

    public GuildManager(String firstName, String lastName, String email, int phone, int ID, Guild... guilds) {
        super(firstName, lastName, email, phone, ID);
        listOfGuilds = new ArrayList();
        for (Guild guild : guilds) {
            listOfGuilds.add(guild);
        }
    }

    /**
     * Returns the list of guilds.
     *
     * @return
     */
    public List<Guild> getListOfGuilds() {
        return listOfGuilds;
    }

    /**
     * Adds a guild to the list of guilds.
     *
     * @param guild
     */
    public void addGuild(Guild guild) {
        listOfGuilds.add(guild);
    }

}
