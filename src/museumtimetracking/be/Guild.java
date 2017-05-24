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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Guild implements Comparable<Guild>, Externalizable {

    private StringProperty name;

    private StringProperty description;

    private BooleanProperty isArchived;

    private GM guildManager;

    public Guild() {
    }

    public Guild(String name, String description, boolean archived) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        isArchived = new SimpleBooleanProperty(archived);
    }

    @Override
    public int compareTo(Guild guild) {
        return name.get().compareToIgnoreCase(guild.getName());
    }

    /**
     *
     * @return name property
     */
    public StringProperty getNameProperty() {
        return name;
    }

    /**
     *
     * @return name as String
     */
    public String getName() {
        return name.get();
    }

    /**
     *
     * @return description property
     */
    public StringProperty getDescriptionProperty() {
        return description;
    }

    /**
     *
     * @return description property
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * Set name property
     *
     * @param name
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * Set description property
     *
     * @param description as String
     */
    public void setDescription(String description) {
        this.description.set(description);
    }

    /**
     * Update IsArchived value
     *
     * @param value
     */
    public void setIsArchived(boolean value) {
        this.isArchived.set(value);
    }

    public GM getGuildManager() {
        return guildManager;
    }

    public void setGuildManager(GM guildManager) {
        this.guildManager = guildManager;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.name = new SimpleStringProperty((String) in.readObject());
        this.description = new SimpleStringProperty((String) in.readObject());
        isArchived = new SimpleBooleanProperty(in.readBoolean());
        setGuildManager((GM) in.readObject());
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name.get());
        out.writeObject(description.get());
        out.writeBoolean(isArchived.get());
        out.writeObject(guildManager);
    }

}
