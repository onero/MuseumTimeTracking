/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.be;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class APerson {

    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty fullName;
    private final StringProperty email;
    private final IntegerProperty phone;
    private int ID;

    public APerson(String firstName, String lastName, String email, int phone, int ID) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.fullName = new SimpleStringProperty(firstName + " " + lastName);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleIntegerProperty(phone);
        this.ID = ID;
    }

    public APerson(String firstName, String lastName, String email, int phone) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.fullName = new SimpleStringProperty(firstName + " " + lastName);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleIntegerProperty(phone);
    }

    public String getFullName() {
        return fullName.get();
    }

    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getEmail() {
        return email.get();
    }

    public Integer getPhone() {
        return phone.get();
    }

    public StringProperty getFullNameProperty() {
        return fullName;
    }

    public StringProperty getFirstNameProperty() {
        return firstName;
    }

    public StringProperty getLastNameProperty() {
        return lastName;
    }

    public StringProperty getEmailProperty() {
        return email;
    }

    public IntegerProperty getPhoneProperty() {
        return phone;
    }

}
