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
import museumtimetracking.be.enums.ELanguage;

public class Volunteer extends APerson {

    private BooleanProperty isIdle;

    private ELanguage language;

    private StringProperty description;

    public Volunteer(int ID, String firstName, String lastName, String email, int phone, boolean isIdle, ELanguage language) {
        super(ID, firstName, lastName, email, phone);
        this.isIdle = new SimpleBooleanProperty(isIdle);
        this.language = language;
        description = new SimpleStringProperty();
    }

    public Volunteer(String firstName, String lastName, String email, int phone) {
        super(firstName, lastName, email, phone);
        this.isIdle = new SimpleBooleanProperty(false);
        description = new SimpleStringProperty();
    }

    public boolean getIsIdle() {
        return isIdle.get();
    }

    public void setIsIdle(boolean isIdle) {
        this.isIdle.set(isIdle);
    }

    public BooleanProperty getIsIdleProperty() {
        return isIdle;
    }

    public void setIsIdleProperty(BooleanProperty isIdle) {
        this.isIdle = isIdle;
    }

    public ELanguage getLanguage() {
        return language;
    }

    public StringProperty getDescriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

}
