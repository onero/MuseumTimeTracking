/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.model;

import museumtimetracking.be.Volunteer;
import museumtimetracking.bll.VolunteerManager;

/**
 *
 * @author Skovgaard
 */
public class VolunteerModel {

    private final VolunteerManager volunteerMgr;

    private static VolunteerModel instance;

    public static VolunteerModel getInstance() {
        if (instance == null) {
            instance = new VolunteerModel();
        }
        return instance;
    }

    public VolunteerModel() {
        // Instantiate volunteerMgr
        volunteerMgr = new VolunteerManager();
    }

    /**
     * Adds the volunteer to DB.
     *
     * @param volunteer
     */
    public void addVolunteer(Volunteer volunteer) {
        volunteerMgr.addVolunteer(volunteer);
    }

}
