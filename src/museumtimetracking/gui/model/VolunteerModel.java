/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.model;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import museumtimetracking.be.Volunteer;
import museumtimetracking.bll.VolunteerManager;

/**
 *
 * @author Skovgaard
 */
public class VolunteerModel {

    private final VolunteerManager volunteerMgr;

    private static VolunteerModel instance;

    private final ObservableList<Volunteer> cachedVolunteers;
    private final ObservableList<Volunteer> cachedIdleVolunteers;

    private final List<Volunteer> volunteerFromDB;
    private final List<Volunteer> idleVolunteersFromDB;

    public static VolunteerModel getInstance() {
        if (instance == null) {
            instance = new VolunteerModel();
        }
        return instance;
    }

    public VolunteerModel() {
        // Instantiate volunteerMgr
        volunteerMgr = new VolunteerManager();
        volunteerFromDB = volunteerMgr.getAllVolunteersNotIdle();
        idleVolunteersFromDB = volunteerMgr.getAllIdleVolunteers();
        cachedVolunteers = FXCollections.observableArrayList(volunteerFromDB);
        cachedIdleVolunteers = FXCollections.observableArrayList(idleVolunteersFromDB);
    }

    public ObservableList<Volunteer> getCachedVolunteers() {
        return cachedVolunteers;
    }

//    public void deleteVolunteer(Volunteer deleteVolunteer){
//        volunteerMgr.deleteGuild(deleteVolunteer);
//
//    }
    /**
     * Adds the volunteer to DB.
     *
     * @param volunteer
     */
    public void addVolunteer(Volunteer volunteer) {
        volunteerMgr.addVolunteer(volunteer);
    }

    public ObservableList<Volunteer> getCachedIdleVolunteers() {
        return cachedIdleVolunteers;
    }

    /**
     * Set the parsed volunteer as idle
     *
     * @param volunteer
     */
    public void addIdleVolunteer(Volunteer volunteer) {
        cachedVolunteers.remove(volunteer);
        cachedIdleVolunteers.add(volunteer);
        volunteerMgr.updateVolunteerIdle(true);
    }

}
