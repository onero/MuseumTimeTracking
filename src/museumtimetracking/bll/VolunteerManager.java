/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.bll;

import java.util.List;
import museumtimetracking.be.Volunteer;
import museumtimetracking.dal.VolunteerDAO;

/**
 *
 * @author Skovgaard
 */
public class VolunteerManager {
    
    private final VolunteerDAO volunteerDAO;
    
    public VolunteerManager(){
        volunteerDAO = VolunteerDAO.getInstance();
    }
    
    /**
     * Adds a new volunteer to DB.
     * @param volunteerToAdd 
     */
    public void addVolunteer(Volunteer volunteerToAdd){
        volunteerDAO.createVolunteer(volunteerToAdd);
    }
    
    /**
     * Gets all the volunteers from the DB.
     *
     * @return
     */
    public List<Volunteer> getAllVolunteersNotIdle() {
        return volunteerDAO.getAllVolunteersNotIdle();
    }
    
}
