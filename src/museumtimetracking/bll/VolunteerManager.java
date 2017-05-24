/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.bll;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.write.WriteException;
import museumtimetracking.be.Volunteer;
import museumtimetracking.dal.DALFacade;
import museumtimetracking.dal.fileWriting.excel.ExcelWriter;
import museumtimetracking.dal.fileWriting.excel.IExcel;
import museumtimetracking.exception.DALException;
import museumtimetracking.gui.model.VolunteerModel;

/**
 *
 * @author Skovgaard
 */
public class VolunteerManager implements IExcel {

    private final DALFacade facadeDAO;

    public VolunteerManager() {
        facadeDAO = DALFacade.getInstance();
    }

    /**
     * Adds a new volunteer to DB.
     *
     * @param volunteerToAdd
     * @return
     * @throws museumtimetracking.exception.DALException
     */
    public Volunteer addVolunteer(Volunteer volunteerToAdd) throws DALException {
        return facadeDAO.addVolunteer(volunteerToAdd);
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
     * @throws museumtimetracking.exception.DALException
     */
    public void addHoursToVolunteer(int volunteerID, String guildName, int hours) throws DALException {
        Date date = new Date();
        facadeDAO.addHoursToVolunteer(volunteerID, guildName, date, hours);
    }

    /**
     * Export all guild hours to excel sheet
     *
     * @throws IOException
     * @throws WriteException
     * @throws DALException
     */
    @Override
    public <T> void exportToExcel(String location, List<T>... values) throws IOException, WriteException, DALException {
        ExcelWriter newFile = new ExcelWriter();
        newFile.setOutputFile(location);
        newFile.createNewExcel("Rapport over frivillige");

        newFile.createCaptions("Frivillig", "Email", "Telefon", "Antal timer i laug");

        List<String> names = new ArrayList<>();
        List<String> emails = new ArrayList<>();
        List<Integer> phones = new ArrayList<>();
        List<Integer> hours = new ArrayList<>();
        List<Volunteer> volunteers = (List<Volunteer>) values[0];
        volunteers.stream()
                .forEachOrdered(v -> {
                    names.add(v.getFullName());
                    emails.add(v.getEmail());
                    phones.add(v.getPhone());
                    try {
                        hours.add(getWorkHoursForAVolunteerInAllGuilds(v));
                    } catch (DALException ex) {
                        Logger.getLogger(VolunteerManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

        newFile.createVolunteerContent(names, emails, phones, hours);

        newFile.writeExcelToFile();
    }

    /**
     * Save the entire VolunteerModel
     *
     * @param model
     * @throws IOException
     */
    public void saveModel(VolunteerModel model) {
        facadeDAO.saveVolunteerModelToFile(model);
    }

    /**
     * Load entire VolunteerModel
     *
     * @return
     * @throws IOException
     */
    public VolunteerModel loadVolunteerModelFromFile() throws IOException {
        return facadeDAO.loadVolunteerModelFromFile();
    }

    /*
     * Gets the total hours for a volunteer in i guild from DB and adds them.
     *
     * @param guildName
     * @param volunteer
     * @return
     */
    public int getWorkHoursForAVolunteerInAGuild(String guildName, Volunteer volunteer) throws DALException {
        int hours = 0;

        List<Integer> hoursList = facadeDAO.getWorkHoursForAVolunteerInAGuild(guildName, volunteer);

        for (Integer workHours : hoursList) {
            hours += workHours;
        }

        return hours;
    }

    /**
     * Gets the total workhours for a volunteer in all guilds and adds them.
     *
     * @param volunteer
     */
    public Integer getWorkHoursForAVolunteerInAllGuilds(Volunteer volunteer) throws DALException {
        int hours = 0;

        List<Integer> hoursList = facadeDAO.getWorkHoursForAVolunteerInAllGuilds(volunteer);

        for (Integer workHours : hoursList) {
            hours += workHours;
        }

        return hours;
    }

    /**
     * Gets all volunteers that have worked on specified guild.
     *
     * @param guildName
     * @return
     */
    public Set<Volunteer> getVolunteersThatHasWorkedOnGuild(String guildName) throws DALException {
        return facadeDAO.getVolunteersThatHasWorkedOnGuild(guildName);
    }

}
