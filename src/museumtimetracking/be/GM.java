/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.be;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Mathias
 */
public class GM extends APerson implements Externalizable {

    private List<String> listOfGuilds;

    private transient ObservableList<String> observableListOfGuilds;

    private StringProperty description;

    public GM() {
    }

    public GM(String firstName, String lastName, String email, int phone) {
        super(firstName, lastName, email, phone);
        listOfGuilds = new ArrayList();
        observableListOfGuilds = FXCollections.observableArrayList();
        this.description = new SimpleStringProperty();
    }

    public GM(List<String> listOfGuilds, String firstName, String lastName, String email, int phone) {
        super(firstName, lastName, email, phone);
        this.observableListOfGuilds = FXCollections.observableArrayList(listOfGuilds);
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

    public StringProperty getDescriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void removeGuild(String guildToRemove) {
        observableListOfGuilds.remove(guildToRemove);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        ID = in.readInt();
        firstName = new SimpleStringProperty((String) in.readObject());
        lastName = new SimpleStringProperty((String) in.readObject());
        fullName = new SimpleStringProperty((String) in.readObject());
        email = new SimpleStringProperty((String) in.readObject());
        phone = new SimpleIntegerProperty(in.readInt());
        this.observableListOfGuilds = FXCollections.observableArrayList((List<String>) in.readObject());
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(getID());
        out.writeObject(getFirstName());
        out.writeObject(getLastName());
        out.writeObject(getFullName());
        out.writeObject(getEmail());
        out.writeInt(getPhone());
        out.writeObject(listOfGuilds);
    }

}
