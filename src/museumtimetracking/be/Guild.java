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

    public StringProperty getNameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty getDescriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

}
