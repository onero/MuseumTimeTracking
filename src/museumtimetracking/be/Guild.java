/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.be;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Guild {

    private final StringProperty name;

    private final StringProperty description;

    private final BooleanProperty isArchived;

    public Guild(String name, String description, boolean archived) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        isArchived = new SimpleBooleanProperty(archived);
    }
    
    // TODO Skovgaard: Make a new constructor which take a new name and description.

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

}
