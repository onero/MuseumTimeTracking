/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.bll;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import museumtimetracking.be.Volunteer;
import museumtimetracking.dal.FacadeDAO;
import museumtimetracking.exception.DALException;

/**
 *
 * @author Skovgaard
 */
public class VolunteerManager {

    private final FacadeDAO facadeDAO;

    public VolunteerManager() throws IOException {
        facadeDAO = FacadeDAO.getInstance();
    }

    /**
     * Adds a new volunteer to DB.
     *
     * @param volunteerToAdd
     * @throws museumtimetracking.exception.DALException
     */
    public void addVolunteer(Volunteer volunteerToAdd) throws DALException {
        facadeDAO.addVolunteer(volunteerToAdd);
    }

    /**
     * Get all idle volunteers in DB
     *
     * @return
     * @throws museumtimetracking.exception.DALException
     */
    public List<Volunteer> getAllIdleVolunteers() throws DALException {
        return facadeDAO.getAllIdleVolunteers();
    }

    /**
     * Gets all the volunteers from the DB.
     *
     * @return
     * @throws museumtimetracking.exception.DALException
     */
    public List<Volunteer> getAllVolunteersNotIdle() throws DALException {
        return facadeDAO.getAllVolunteersNotIdle();
    }

    /**
     * Set the description in DB
     *
     * @param id
     * @param text
     * @throws museumtimetracking.exception.DALException
     */
    public void setVolunteerDescription(int id, String text) throws DALException {
        facadeDAO.setVolunteerDescription(id, text);
    }

    /**
     * Set the volunteer image in DB
     *
     * @param id
     * @param file
     * @throws museumtimetracking.exception.DALException
     * @throws java.io.FileNotFoundException
     */
    public void setVolunteerImage(int id, File file) throws DALException, FileNotFoundException {
        facadeDAO.setVolunteerImage(id, file);
    }

    /**
     * Update the volunteer status
     *
     * @param id
     * @param value
     * @throws museumtimetracking.exception.DALException
     */
    public void updateVolunteerIdle(int id, boolean value) throws DALException {
        facadeDAO.updateVolunteerIdleStatus(id, value);
    }

    /**
     * Deletes the volunteer from DB.
     *
     * @param id
     * @throws museumtimetracking.exception.DALException
     */
    public void deleteVolunteer(int id) throws DALException {
        facadeDAO.deleteVolunteer(id);
    }

    /**
     * Updates the volunteer in the DB.
     *
     * @param updatedVolunteer
     * @throws museumtimetracking.exception.DALException
     */
    public void updateVolunteer(Volunteer updatedVolunteer) throws DALException {
        facadeDAO.updateVolunteerPersonInfo(updatedVolunteer);
    }

    /**
     * Finds the current date. Then adds the parsed hours to the parsed
     * volunteer in the database.
     *
     * @param volunteerID
     * @param guildName
     * @param hours
     */
    public void addHoursToVolunteer(int volunteerID, String guildName, int hours) {
        Date date = new Date(System.currentTimeMillis());
        facadeDAO.addHoursToVolunteer(volunteerID, guildName, date, hours);
    }
}
