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
import javafx.scene.image.Image;

public abstract class APerson implements Comparable<APerson> {

    protected int ID;
    protected StringProperty firstName;
    protected StringProperty lastName;
    protected StringProperty fullName;
    protected StringProperty email;
    protected IntegerProperty phone;
    protected Image image;

    public APerson() {
    }

    public APerson(int newID, String firstName, String lastName, String email, int phone) {
        this.ID = newID;
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.fullName = new SimpleStringProperty(firstName + " " + lastName);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleIntegerProperty(phone);
    }

    public APerson(String firstName, String lastName, String email, int phone) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.fullName = new SimpleStringProperty(firstName + " " + lastName);
        this.email = new SimpleStringProperty(email);
        this.phone = new SimpleIntegerProperty(phone);
    }

    @Override
    public int compareTo(APerson p) {
        return fullName.get().compareToIgnoreCase(p.getFullName());
    }

    public int getID() {
        return ID;
    }

    public void setID(int id) {
        this.ID = id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
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

    public void setFirstName(String name) {
        firstName.set(name);
    }

    public void setLastName(String name) {
        lastName.set(name);
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public void setPhone(int phone) {
        this.phone.set(phone);
    }

    public void updateFullName() {
        fullName.set(firstName.get() + " " + lastName.get());
    }

    public void setFullName(String fullName) {
        this.fullName.setValue(fullName);
    }

}
