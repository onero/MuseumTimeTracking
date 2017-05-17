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
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import museumtimetracking.be.enums.ELanguage;

public class Volunteer extends APerson implements Externalizable {

    private BooleanProperty isIdle;

    private ELanguage language;

    private StringProperty description;

    public Volunteer() {
    }

    public Volunteer(int ID, String firstName, String lastName, String email, int phone, boolean isIdle, ELanguage language) {
        super(ID, firstName, lastName, email, phone);
        this.isIdle = new SimpleBooleanProperty(isIdle);
        this.language = language;
        description = new SimpleStringProperty();
    }

    public Volunteer(String firstName, String lastName, String email, int phone, ELanguage language) {
        super(firstName, lastName, email, phone);
        this.isIdle = new SimpleBooleanProperty(false);
        this.language = language;
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

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        ID = in.readInt();
        firstName = new SimpleStringProperty((String) in.readObject());
        lastName = new SimpleStringProperty((String) in.readObject());
        fullName = new SimpleStringProperty((String) in.readObject());
        email = new SimpleStringProperty((String) in.readObject());
        phone = new SimpleIntegerProperty(in.readInt());
        isIdle = new SimpleBooleanProperty(in.readBoolean());
        language = (ELanguage) in.readObject();
        description = new SimpleStringProperty((String) in.readObject());
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(getID());
        out.writeObject(getFirstName());
        out.writeObject(getLastName());
        out.writeObject(getFullName());
        out.writeObject(getEmail());
        out.writeInt(getPhone());
        out.writeBoolean(getIsIdle());
        out.writeObject(getLanguage());
        out.writeObject(getDescription());
    }

}
