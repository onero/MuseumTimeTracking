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
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import museumtimetracking.be.Volunteer;
import museumtimetracking.dal.DALFacade;
import museumtimetracking.dal.fileWriting.excel.ExcelWriter;
import museumtimetracking.dal.fileWriting.excel.IExcel;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
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

        newFile.createCaptions(0, "Frivillig", "Email", "Telefon", "Antal timer i laug");

        writeDataToFile(newFile, values[0], 1);

        //If a second list is parsed, prints its information under the first list.
        if (values.length > 1 && values[1] != null) {
            newFile.createCaptions(values[0].size() + 1, "Inaktive Frivillige");

            writeDataToFile(newFile, values[1], values[1].size() + 2);
        }

        newFile.writeExcelToFile();
    }

    /**
     * Takes the parsed list and writes its information in the parsed file
     * beginning on the parsed row.
     *
     * @param <T>
     * @param newFile
     * @param list
     * @param row
     * @throws WriteException
     * @throws RowsExceededException
     * @throws IOException
     * @throws DALException
     */
    private <T> void writeDataToFile(ExcelWriter newFile, List<T> list, int row) throws WriteException, RowsExceededException, IOException, DALException {
        List<String> names = new ArrayList<>();
        List<String> emails = new ArrayList<>();
        List<Integer> phones = new ArrayList<>();
        List<Integer> hours = new ArrayList<>();
        List<Volunteer> volunteers = (List<Volunteer>) list;
        volunteers.stream()
                .forEachOrdered(v -> {
                    names.add(v.getFullName());
                    emails.add(v.getEmail());
                    phones.add(v.getPhone());
                    try {
                        hours.add(getWorkHoursForAVolunteerInAllGuilds(v));
                    } catch (DALException ex) {
                        ExceptionDisplayer.display(ex);
                    }
                });

        newFile.createVolunteerContent(row, names, emails, phones, hours);
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
