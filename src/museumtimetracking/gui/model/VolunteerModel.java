/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.model;

import java.io.Externalizable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jxl.write.WriteException;
import museumtimetracking.be.Volunteer;
import museumtimetracking.bll.VolunteerManager;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.views.root.MTTMainControllerView;

/**
 *
 * @author Skovgaard
 */
public class VolunteerModel implements Externalizable, IASyncUpdate, ISaveModel<VolunteerModel> {

    private transient VolunteerManager volunteerMgr;

    private List<Volunteer> volunteerFromDB;
    private ObservableList<Volunteer> cachedVolunteers;

    private List<Volunteer> idleVolunteersFromDB;
    private ObservableList<Volunteer> cachedIdleVolunteers;

    public VolunteerModel() {
    }

    public VolunteerModel(boolean onlineMode) throws DALException {
        volunteerMgr = new VolunteerManager();
        // Instantiate volunteerMgr
        volunteerFromDB = volunteerMgr.getAllVolunteersNotIdle();
        cachedVolunteers = FXCollections.observableArrayList(volunteerFromDB);
        idleVolunteersFromDB = volunteerMgr.getAllIdleVolunteers();
        cachedIdleVolunteers = FXCollections.observableArrayList(idleVolunteersFromDB);

        Collections.sort(volunteerFromDB);
        Collections.sort(idleVolunteersFromDB);

        saveModel(this);

    }

    @Override
    public void saveModel(VolunteerModel model) {
        volunteerMgr.saveModel(this);
    }

    /**
     * Sort the lists in natural order
     */
    public void sortCachedLists() {
        Collections.sort(cachedVolunteers);
        Collections.sort(cachedIdleVolunteers);
    }

    public ObservableList<Volunteer> getCachedVolunteers() {
        return cachedVolunteers;
    }

    public void setVolunteerDescription(int id, String text) throws DALException {
        volunteerMgr.setVolunteerDescription(id, text);
        MTTMainControllerView.getInstance().handleUpdate();
    }

    /**
     * Set the volunteer Image
     *
     * @param id
     * @param file
     * @throws museumtimetracking.exception.DALException
     * @throws java.io.FileNotFoundException
     */
    public void setVolunteerImage(int id, File file) throws DALException, FileNotFoundException {
        volunteerMgr.setVolunteerImage(id, file);
        MTTMainControllerView.getInstance().handleUpdate();
    }

    /**
     * Updates the volunteer in the DB.
     *
     * @param updatedVolunteer
     * @throws museumtimetracking.exception.DALException
     */
    public void updateVolunteer(Volunteer updatedVolunteer) throws DALException {
        volunteerMgr.updateVolunteer(updatedVolunteer);
        MTTMainControllerView.getInstance().handleUpdate();

    }

    /**
     * Deletes the selected volunteer from DB.
     *
     * @param volunteerToDelete
     * @throws museumtimetracking.exception.DALException
     */
    public void deleteVolunteer(Volunteer volunteerToDelete) throws DALException {
        volunteerMgr.deleteVolunteer(volunteerToDelete.getID());
        // Removes the volunteer from the listview.
        cachedVolunteers.remove(volunteerToDelete);
        cachedIdleVolunteers.remove(volunteerToDelete);
        MTTMainControllerView.getInstance().handleUpdate();
    }

    /**
     * Adds the volunteer to DB.
     *
     * @param newVolunteer
     * @throws museumtimetracking.exception.DALException
     */
    public void addVolunteer(Volunteer newVolunteer) throws DALException {
        Volunteer volunteer = volunteerMgr.addVolunteer(newVolunteer);
        cachedVolunteers.add(volunteer);
        MTTMainControllerView.getInstance().handleUpdate();
    }

    public ObservableList<Volunteer> getCachedIdleVolunteers() {
        return cachedIdleVolunteers;
    }

    /**
     * Set the parsed volunteer as idle
     *
     * @param volunteer
     * @param value
     * @throws museumtimetracking.exception.DALException
     */
    public void updateIdleVolunteer(Volunteer volunteer, boolean value) throws DALException {
        if (value) {
            cachedVolunteers.remove(volunteer);
            cachedIdleVolunteers.add(volunteer);
        } else {
            cachedVolunteers.add(volunteer);
            cachedIdleVolunteers.remove(volunteer);
        }
        volunteerMgr.updateVolunteerIdle(volunteer.getID(), value);
        MTTMainControllerView.getInstance().handleUpdate();
    }

    /**
     * Adds hours to a volunteer.
     *
     * @param volunteerID
     * @param guildName
     * @param hours
     * @throws museumtimetracking.exception.DALException
     */
    public void addHoursToVolunteer(int volunteerID, String guildName, int hours) throws DALException {
        volunteerMgr.addHoursToVolunteer(volunteerID, guildName, hours);
        MTTMainControllerView.getInstance().handleUpdate();
    }

    /**
     * filters the cached list with the search text via stream.
     *
     * @param newValue
     */
    public void searchActiveVolunteers(String newValue) {
        cachedVolunteers.clear();
        volunteerFromDB.stream()
                .filter(g -> g.getFullName().toLowerCase().contains(newValue.toLowerCase()))
                .forEach(g -> cachedVolunteers.add(g));
    }

    /**
     * filters the cached list with the search text via stream.
     *
     * @param searchText
     */
    public void searchIdleVolunteers(String searchText) {
        cachedIdleVolunteers.clear();
        idleVolunteersFromDB.stream()
                .filter(g -> g.getFullName().toLowerCase().contains(searchText.toLowerCase()))
                .forEach(g -> cachedIdleVolunteers.add(g));
    }

    /**
     * Export volunteer info to excel sheet. If inactiveIncluded is true -
     * Writes both inactive and active volunteers. If false - Only writes active
     * volunteers.
     *
     * @param location
     * @param inactiveIncluded
     * @throws java.io.IOException
     * @throws jxl.write.WriteException
     * @throws museumtimetracking.exception.DALException
     */
    public void exportVolunteerInfoToExcel(String location, boolean inactiveIncluded) throws IOException, WriteException, DALException {
        if (inactiveIncluded) {
            volunteerMgr.exportToExcel(location, cachedVolunteers, cachedIdleVolunteers);
        } else {
            volunteerMgr.exportToExcel(location, cachedVolunteers);
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(volunteerFromDB);
        out.writeObject(idleVolunteersFromDB);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        volunteerMgr = new VolunteerManager();
        volunteerFromDB = (List<Volunteer>) in.readObject();
        cachedVolunteers = FXCollections.observableArrayList(volunteerFromDB);

        idleVolunteersFromDB = (List<Volunteer>) in.readObject();
        cachedIdleVolunteers = FXCollections.observableArrayList(idleVolunteersFromDB);

        Collections.sort(volunteerFromDB);
        Collections.sort(idleVolunteersFromDB);
    }

    /**
     * Gets the total hours for a volunteer in i guild.
     *
     * @param guildName
     * @param volunteer
     * @return
     */
    public int getWorkHoursForAVolunteerInAGuild(String guildName, Volunteer volunteer) throws DALException {
        return volunteerMgr.getWorkHoursForAVolunteerInAGuild(guildName, volunteer);
    }

    /**
     * Gets the total workhours for a volunteer in all guilds.
     *
     * @param volunteer
     * @return
     */
    public Integer getWorkHoursForAVolunteerInAllGuilds(Volunteer volunteer) throws DALException {
        return volunteerMgr.getWorkHoursForAVolunteerInAllGuilds(volunteer);
    }

    /**
     * Gets all volunteers that have worked on specified guild.
     *
     * @param guildName
     * @return
     */
    public Set<Volunteer> getVolunteersThatHasWorkedOnGuild(String guildName) throws DALException {
        return volunteerMgr.getVolunteersThatHasWorkedOnGuild(guildName);
    }

    @Override
    public void updateData() {
        Runnable task = () -> {
            try {
                instatiateCollections();
                Platform.runLater(() -> {
                    cachedVolunteers.clear();
                    cachedVolunteers.addAll(volunteerFromDB);
                    cachedIdleVolunteers.clear();
                    cachedIdleVolunteers.addAll(idleVolunteersFromDB);
                    MTTMainControllerView.getInstance().showUpdate(false);
                });
            } catch (DALException ex) {
                ExceptionDisplayer.display(ex);
            }
        };
        new Thread(task).start();
    }

    private void instatiateCollections() throws DALException {
        volunteerFromDB = volunteerMgr.getAllVolunteersNotIdle();

        idleVolunteersFromDB = volunteerMgr.getAllIdleVolunteers();

        Collections.sort(volunteerFromDB);
        Collections.sort(idleVolunteersFromDB);
    }
}
