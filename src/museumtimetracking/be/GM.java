/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.be;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Mathias
 */
public class GM extends APerson {

    private final List<String> listOfGuilds;
    private final ObservableList<String> observableListOfGuilds;

    private StringProperty description;

    public GM(String firstName, String lastName, String email, int phone) {
        super(firstName, lastName, email, phone);
        listOfGuilds = new ArrayList();
        observableListOfGuilds = FXCollections.observableArrayList();
        this.description = new SimpleStringProperty();
    }

    public GM(int ID, String firstName, String lastName, String email, int phone, String description) {
        super(ID, firstName, lastName, email, phone);
        listOfGuilds = new ArrayList();
        observableListOfGuilds = FXCollections.observableArrayList();
        this.description = new SimpleStringProperty(description);
    }

    public GM(String firstName, String lastName, String email, int phone, int ID, String... guildNames) {
        super(ID, firstName, lastName, email, phone);
        listOfGuilds = new ArrayList();
        this.description = new SimpleStringProperty();
        for (String guildName : guildNames) {
            listOfGuilds.add(guildName);
        }
        observableListOfGuilds = FXCollections.observableArrayList();
        for (String guildName : guildNames) {
            observableListOfGuilds.add(guildName);
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

    public void setDescription(String description) {
        this.description.set(description);
    }

    /**
     * Adds a guild to the list of guilds.
     *
     * @param guild
     */
    public void addGuild(String guild) {
        listOfGuilds.add(guild);
    }

    /**
     * Clears the lists. Then adds the guilds to them.
     *
     * @param guildNames
     */
    public void addAllGuilds(List<String> guildNames) {
        listOfGuilds.clear();
        listOfGuilds.addAll(guildNames);
        observableListOfGuilds.clear();
        observableListOfGuilds.addAll(guildNames);
    }

    /**
     * Returns the observable list of guildNames.
     *
     * @return
     */
    public ObservableList<String> getObservableListOfGuilds() {
        return observableListOfGuilds;
    }

    public StringProperty getDescription() {
        return description;
    }

}
