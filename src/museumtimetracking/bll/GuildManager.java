/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.bll;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import jxl.write.WriteException;
import museumtimetracking.be.Guild;
import museumtimetracking.be.Volunteer;
import museumtimetracking.dal.fileWriting.excel.ExcelWriter;
import museumtimetracking.dal.fileWriting.excel.IExcel;
import museumtimetracking.dal.DALFacade;
import museumtimetracking.exception.DALException;
import museumtimetracking.gui.model.GuildModel;

/**
 *
 * @author Skovgaard
 */
public class GuildManager implements IExcel {

    private final DALFacade facadeDAO;

    public GuildManager() {
        facadeDAO = DALFacade.getInstance();
    }

    /**
     * Adds a new guild to DB.
     *
     * @param guildToAdd
     * @throws museumtimetracking.exception.DALException
     */
    public void addGuild(Guild guildToAdd) throws DALException {
        facadeDAO.addGuild(guildToAdd);
    }

    /**
     * Deletes guilds from tableView and DB. Comes from GuildModel and goes to
     * GuildDao.
     *
     * @param deleteGuild
     * @throws museumtimetracking.exception.DALException
     */
    public void deleteGuild(Guild deleteGuild) throws DALException {
        facadeDAO.deleteGuild(deleteGuild);
    }

    /**
     * Archive guild in in DB
     *
     * @param guildToArchive
     * @throws museumtimetracking.exception.DALException
     */
    public void archiveGuild(Guild guildToArchive) throws DALException {
        facadeDAO.archiveGuild(guildToArchive);
    }

    /**
     * Gets all the guilds from the DB.
     *
     * @return
     * @throws museumtimetracking.exception.DALException
     */
    public List<Guild> getAllGuildsNotArchived() throws DALException {
        List<Guild> guilds = facadeDAO.getAllGuildsNotArchivedFromDB();

        return guilds;
    }

    /**
     * Gets all the guilds from the DB if connection is available.
     * Else get all guilds from file, if available
     *
     * @return guilds as List<Guild>
     * @throws museumtimetracking.exception.DALException
     */
    public List<Guild> getAllGuildsArchived() throws DALException {
        List<Guild> guilds = facadeDAO.getAllGuildsArchived();

        return guilds;
    }

    /**
     * Restore guild from archive in DB
     *
     * @param guildToRestore
     * @throws museumtimetracking.exception.DALException
     */
    public void restoreGuild(Guild guildToRestore) throws DALException {
        facadeDAO.restoreGuild(guildToRestore);
    }

    /**
     * Update guild in DB with new info
     *
     * @param guildToUpdate
     * @param updatedGuild
     */
    public void updateGuild(String guildToUpdate, Guild updatedGuild) throws DALException {
        facadeDAO.updateGuild(guildToUpdate, updatedGuild);
    }

    /**
     * Returns a Map containg all hours worked for each guild.
     *
     * @param guilds
     * @return
     * @throws DALException
     */
    public Map<String, Integer> getAllHoursWorked(List<Guild> guilds) throws DALException {
        List<String> guildNames = new LinkedList<>();
        for (Guild guild : guilds) {
            guildNames.add(guild.getName());
        }
        return facadeDAO.getAllHoursWorked(guildNames);
    }

    /**
     * Returns a Map containing all hours workd for each guild for between today
     * and a month back.
     *
     * @param guilds
     * @return
     * @throws DALException
     */
    public Map<String, Integer> getAllHoursWorkedAMonthBack(List<Guild> guilds) throws DALException {
        List<String> guildNames = new LinkedList<>();
        for (Guild guild : guilds) {
            guildNames.add(guild.getName());
        }

        DateFormat yearFormatter = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        String date = yearFormatter.format(new Date());
        String time = timeFormatter.format(new Date());
        String startDate = getDateAMonthBack(date) + " " + time;
        String endDate = date + " " + time;

        return facadeDAO.getAllHoursWorkedForSpecificPeriod(guildNames, startDate, endDate);
    }

    /**
     * Takes the parsed string and finds the day one month from that date.
     *
     * @param date a String that must represent a date with the format
     * yyyy-MM-dd
     * @return a String representing the date one month from that date.
     */
    private String getDateAMonthBack(String date) {

        String[] yearArray = date.split("-");
        int year, month, day;
        year = Integer.parseInt(yearArray[0]);
        month = Integer.parseInt(yearArray[1]);
        day = Integer.parseInt(yearArray[2]);

        return formatTime(year, month, day);
    }

    /**
     * Takes the parameters and calculates the date one month from that day.
     *
     * @param year as int, ex 2017
     * @param month as int, ex 1
     * @param day as int, ex 22
     * @return
     */
    private String formatTime(int year, int month, int day) {
        String date;
        day -= 30;
        if (day < 0) {
            day *= -1;
            day = 30 - day;
            month--;
            if (month <= 0) {
                month = 12;
                year--;
            }
        }

        date = year + "-";
        if (month < 10) {
            date += "0";
        }
        date += month + "-";
        if (day < 10) {
            date += "0";
        }
        date += day;
        return date;
    }

    /**
     * Gets all guilds with no manager from DAO
     *
     * @return
     * @throws DALException
     */
    public List<Guild> getGuildsWithoutManagers() throws DALException {
        return facadeDAO.getGuildsWithoutManagers();
    }

    /**
     * Assign new GM to parsed guild
     *
     * @param id
     * @param guildName
     * @throws DALException
     */
    public void updateGMForGuild(int id, String guildName) throws DALException {
        facadeDAO.updateGMForGuild(id, guildName);
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
        newFile.createNewExcel("Rapport over laug");

        newFile.createCaptions("Laug", "Timer");

        newFile.createLabelNumberContent((List<String>) values[0], (List<Integer>) values[1]);

        newFile.writeExcelToFile();
    }

    /*
     * Calculate the total return on investment a guild managers spends on
     * volunteers for a single guild in a month
     *
     * @param
     * selectedGuilds
     * @param
     * GMHoursInAMonth
     * @
     * return a Map containing the names of the guilds and their ROI.
     * @
     * throws museumtimetracking.exception.DALException
     *
     */
    public Map<String, Integer> getGMROIOnVolunteerForAMonth(List<Guild> selectedGuilds, int GMHoursInAMonth) throws DALException {
        Map<String, Integer> hoursWorked = getAllHoursWorkedAMonthBack(selectedGuilds);
        Map<String, Integer> ROIs = new HashMap<>();

        for (Guild selectedGuild : selectedGuilds) {
            if (hoursWorked.get(selectedGuild.getName()) > 0) {
                ROIs.put(selectedGuild.getName(), getROI(hoursWorked
                        .get(selectedGuild.getName()), GMHoursInAMonth));
            }
        }

        return ROIs;
    }

    /**
     * Divides the two parameters so long hoursWorked is not 0.
     *
     * @param hoursWorked
     * @param GMHoursInAMonth
     * @return
     */
    private int getROI(int hoursWorked, int GMHoursInAMonth) {
        if (hoursWorked != 0) {
            return hoursWorked / GMHoursInAMonth;
        }
        return 0;
    }

    public List<String> getGuildsAVolunteerHasWorkedOn(Volunteer volunteer) throws DALException {
        return facadeDAO.getGuildsAVolunteerHasWorkedOn(volunteer);
    }

    /**
     * <<<<<<< HEAD
     * Save the entire guild model
     *
     * @param model
     * @throws IOException
     */
    public void saveGuildModel(GuildModel model) {
        facadeDAO.saveGuildModelToFile(model);
    }

    /**
     * Load entire guid model
     *
     * @return
     * @throws IOException
     */
    public GuildModel loadGuildModelFromFile() throws IOException {
        return facadeDAO.loadGuildModelFromFile();
    }

    /*
     * Gets all hours that has been added to a guild.
     *
     * @param guildName
     * @return
     */
    public Integer getWorkHoursInGuild(String guildName) throws DALException {
        int hours = 0;

        List<Integer> hoursList = facadeDAO.getWorkHoursInGuild(guildName);

        for (Integer workHours : hoursList) {
            hours += workHours;
        }

        return hours;
    }
}
