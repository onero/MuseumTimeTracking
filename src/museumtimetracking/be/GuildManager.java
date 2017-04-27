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

    private final List<String> listOfGuilds;

    public GuildManager(String firstName, String lastName, String email, int phone) {
        super(firstName, lastName, email, phone);
        listOfGuilds = new ArrayList();
    }

    public GuildManager(String firstName, String lastName, String email, int phone, int ID) {
        super(firstName, lastName, email, phone, ID);
        listOfGuilds = new ArrayList();
    }

    public GuildManager(String firstName, String lastName, String email, int phone, int ID, String... guildNames) {
        super(firstName, lastName, email, phone, ID);
        listOfGuilds = new ArrayList();
        for (String guildName : guildNames) {
            listOfGuilds.add(guildName);
        }
    }

    /**
     * Returns the list of guilds.
     *
     * @return
     */
    public List<String> getListOfGuilds() {
        return listOfGuilds;
    }

    /**
     * Adds a guild to the list of guilds.
     *
     * @param guild
     */
    public void addGuild(String guild) {
        listOfGuilds.add(guild);
    }

    public void addAllGuilds(List<String> guildNames) {
        listOfGuilds.addAll(guildNames);
    }

}
