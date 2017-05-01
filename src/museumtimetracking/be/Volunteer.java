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

public class Volunteer extends APerson {

    private BooleanProperty isIdle;
    
    private StringProperty language;

    public Volunteer(int ID, String firstName, String lastName, String email, int phone, boolean isIdle, String language) {
        super(ID, firstName, lastName, email, phone);
        this.isIdle = new SimpleBooleanProperty(isIdle);
        this.language = new SimpleStringProperty(language);
    }

    public Volunteer(String firstName, String lastName, String email, int phone) {
        super(firstName, lastName, email, phone);
        this.isIdle = new SimpleBooleanProperty(false);
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

    public StringProperty getLanguage() {
        return language;
    }
    
    

}
