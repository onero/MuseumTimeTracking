/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.dal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import museumtimetracking.MuseumTimeTracking;
import museumtimetracking.be.APerson;
import museumtimetracking.be.GM;
import museumtimetracking.be.Guild;
import museumtimetracking.be.Volunteer;
import museumtimetracking.dal.db.GuildDAO;
import museumtimetracking.dal.db.GuildManagerDAO;
import museumtimetracking.dal.db.VolunteerDAO;
import museumtimetracking.dal.fileWriting.GMFileDAO;
import museumtimetracking.dal.fileWriting.GuildFileDAO;
import museumtimetracking.dal.fileWriting.VolunteerFileDAO;
import museumtimetracking.exception.DALException;
import museumtimetracking.gui.model.GuildManagerModel;
import museumtimetracking.gui.model.GuildModel;
import museumtimetracking.gui.model.VolunteerModel;

/**
 *
 * @author Mathias
 */
public class DALFacade {

    private static DALFacade instance;

    public static final String DB_CONNECTION_ERROR = "Kunne ikke forbinde\nStarter programmet i offline tilstand";

    private final GuildDAO guildDAO;

    private final VolunteerDAO volunteerDAO;

    private final GuildManagerDAO guildManagerDAO;

    private final GuildFileDAO guildFileDAO;

    private final GMFileDAO GMFileDAO;

    private final VolunteerFileDAO volunteerFileDAO;

    public static DALFacade getInstance() {
        if (instance == null) {
            instance = new DALFacade();
        }
        return instance;
    }

    private DALFacade() {
        guildDAO = new GuildDAO();
        volunteerDAO = new VolunteerDAO();
        guildManagerDAO = new GuildManagerDAO();
        guildFileDAO = new GuildFileDAO();
        GMFileDAO = new GMFileDAO();
        volunteerFileDAO = new VolunteerFileDAO();
    }

    /**
     * Adds a new guild to DB.
     *
     * @param guildToAdd
     * @throws museumtimetracking.exception.DALException
     */
    public void addGuild(Guild guildToAdd) throws DALException {
        try {
            guildDAO.addGuild(guildToAdd);
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Get all guild managers not idle
     *
     * @return
     */
    public Set<GM> getAllGuildManagersNotIdle() throws DALException {
        try {
            return guildManagerDAO.getAllGuildManagersNotIdle();
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Update guild in DB with new info
     *
     * @param guildToUpdate
     * @param updatedGuild
     * @throws museumtimetracking.exception.DALException
     */
    public void updateGuild(String guildToUpdate, Guild updatedGuild) throws DALException {
        try {
            guildDAO.updateGuild(guildToUpdate, updatedGuild);
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Archive guild in in DB
     *
     * @param guildToArchive
     * @throws museumtimetracking.exception.DALException
     */
    public void archiveGuild(Guild guildToArchive) throws DALException {
        try {
            guildDAO.archiveGuild(guildToArchive);
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Deletes guilds from tableView and DB. Comes from GuildModel and goes to
     * GuildDao.
     *
     * @param deleteGuild
     * @throws museumtimetracking.exception.DALException
     */
    public void deleteGuild(Guild deleteGuild) throws DALException {
        try {
            guildDAO.deleteGuild(deleteGuild);
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Gets all the guilds from the DB.
     *
     * @return
     * @throws museumtimetracking.exception.DALException
     */
    public List<Guild> getAllGuildsArchived() throws DALException {
        try {
            return guildDAO.getAllGuildsArchived();
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Gets all the guilds from the DB.
     *
     * @return
     * @throws museumtimetracking.exception.DALException
     */
    public List<Guild> getAllGuildsNotArchivedFromDB() throws DALException {
        try {
            return guildDAO.getAllGuildsNotArchived();
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Returns a Map of hours worked for each guild.
     *
     * @param guildNames
     * @return
     * @throws DALException
     */
    public Map<String, Integer> getAllHoursWorked(List<String> guildNames) throws DALException {
        try {
            return guildDAO.getVolunteerWorkHours(guildNames);
        } catch (SQLException ex) {
            throw new DALException(MuseumTimeTracking.bundle.getString("DocumentedHoursWarning"), ex);
        }
    }

    /**
     * Returns a Map of hours work for each guild for at specific period.
     *
     * @param guildNames
     * @param startDate
     * @param endDate
     * @return
     * @throws DALException
     */
    public Map<String, Integer> getAllHoursWorkedForSpecificPeriod(List<String> guildNames, String startDate, String endDate) throws DALException {
        try {
            return guildDAO.getVolunteerWorkHoursForSpecificPeriod(guildNames, startDate, endDate);
        } catch (SQLException ex) {
            throw new DALException(MuseumTimeTracking.bundle.getString("DocumentedHoursWarning"), ex);
        }
    }

    /**
     * Restore guild from archive in DB
     *
     * @param guildToRestore
     * @throws museumtimetracking.exception.DALException
     */
    public void restoreGuild(Guild guildToRestore) throws DALException {
        try {
            guildDAO.restoreGuild(guildToRestore);
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     *
     * @param volunteer
     * @return
     * @throws museumtimetracking.exception.DALException
     */
    public Volunteer addVolunteer(Volunteer volunteer) throws DALException {
        try {
            return volunteerDAO.createVolunteer(volunteer);
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Get all idle volunteers from DB
     *
     * @return
     * @throws DALException
     */
    public List<Volunteer> getAllIdleVolunteers() throws DALException {
        try {
            return volunteerDAO.getAllIdleVolunteers();
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Get all volunteers that are not idle
     *
     * @return
     * @throws museumtimetracking.exception.DALException
     */
    public List<Volunteer> getAllVolunteersNotIdle() throws DALException {
        try {
            return volunteerDAO.getAllVolunteersNotIdle();
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Set the description in DB
     *
     * @param id
     * @param text
     * @throws museumtimetracking.exception.DALException
     */
    public void setVolunteerDescription(int id, String text) throws DALException {
        try {
            volunteerDAO.setVolunteerDescription(id, text);
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
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
        try {
            volunteerDAO.setVolunteerImage(id, file);
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Update the volunteer status
     *
     * @param id
     * @param value
     * @throws museumtimetracking.exception.DALException
     */
    public void updateVolunteerIdleStatus(int id, boolean value) throws DALException {
        try {
            volunteerDAO.updateVolunteerIdleStatus(id, value);
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Deletes the volunteer from DB.
     *
     * @param id
     * @throws museumtimetracking.exception.DALException
     */
    public void deleteVolunteer(int id) throws DALException {
        try {
            volunteerDAO.deleteVolunteer(id);
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Updates the volunteer in the DB.
     *
     * @param updatedVolunteer
     * @throws museumtimetracking.exception.DALException
     */
    public void updateVolunteerPersonInfo(Volunteer updatedVolunteer) throws DALException {
        try {
            volunteerDAO.updateVolunteerPersonInfo(updatedVolunteer);
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Creates the person in the DB. Then adds its guild, so it's a manager.
     * Then return a GuildManager with its accoiated guilds.
     *
     * TODO RKL: Refactor to transaction.
     *
     * @param person
     * @param guildName
     * @return
     * @throws museumtimetracking.exception.DALException
     */
    public GM createNewGuildManager(APerson person, String guildName) throws DALException {
        try {
            return guildManagerDAO.createNewGuildManager(person, guildName);
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Gets a list of idle guild manager guilds from the GuildManagerDAO.
     *
     * @return
     * @throws museumtimetracking.exception.DALException
     */
    public Set<GM> getAllIdleGuildManagers() throws DALException {
        try {
            return guildManagerDAO.getAllIdleGuildManagers();
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Get all GuildManager candidates
     *
     * @return
     * @throws DALException
     */
    public Set<GM> getAllGMCandidates() throws DALException {
        try {
            return guildManagerDAO.getGMCandidates();
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Assign new GM to parsed guild
     *
     * @param id
     * @param guildName
     * @throws DALException
     */
    public void updateGMForGuild(int id, String guildName) throws DALException {
        try {
            guildManagerDAO.updateGMForGuild(id, guildName);
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Updates the guildManager in the database.
     *
     * @param manager
     * @param guildsToAdd
     * @param guildsToDelete
     * @throws museumtimetracking.exception.DALException
     */
    public void updateGuildManager(GM manager, Set<String> guildsToAdd, Set<String> guildsToDelete) throws DALException {
        try {
            guildManagerDAO.updateGuildManagerInDatabase(manager, guildsToAdd, guildsToDelete);
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Assign guild to manager
     *
     * @param it
     * @param guildName
     */
    public void assignGuildToManager(int it, String guildName) throws DALException {
        try {
            guildManagerDAO.assignGuildToManager(it, guildName);
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Deletes the manager from the database.
     *
     * @param GuildManagerID
     * @throws museumtimetracking.exception.DALException
     */
    public void deleteGuildManagerFromDB(int GuildManagerID) throws DALException {
        try {
            guildManagerDAO.deleteGuildManagerFromDB(GuildManagerID);
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Archive a manager
     *
     * @param selectedManager
     */
    public void archiveManager(int id, boolean value) throws DALException {
        try {
            guildManagerDAO.archiveManager(id, value);
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Adds hours to a volunteer in the database.
     *
     * @param volunteerID
     * @param guildName
     * @param date
     * @param hours
     * @throws museumtimetracking.exception.DALException
     */
    public void addHoursToVolunteer(int volunteerID, String guildName, Date date, int hours) throws DALException {
        try {
            volunteerDAO.addHoursToVolunteer(volunteerID, guildName, date, hours);
        } catch (SQLException ex) {
            throw new DALException(MuseumTimeTracking.bundle.getString("TimeNotDocumentedWarning"), ex);
        }
    }

    /**
     * Get all guilds that do not have a manager
     *
     * @return guilds as List<Guild>
     * @throws DALException
     */
    public List<Guild> getGuildsWithoutManagers() throws DALException {
        try {
            return guildDAO.getGuildsWithoutManagers();
        } catch (SQLException ex) {
            throw new DALException(MuseumTimeTracking.bundle.getString("GuildsWithoutGMWarning"), ex);
        }
    }

    /**
     * Get all guilds a volunteer has worked on
     *
     * @param volunteer
     * @return guilds as List<String>
     * @throws DALException
     */
    public List<String> getGuildsAVolunteerHasWorkedOn(Volunteer volunteer) throws DALException {
        try {
            return guildDAO.getGuildsAVolunteerHasWorkedOn(volunteer);
        } catch (SQLException ex) {
            throw new DALException(MuseumTimeTracking.bundle.getString("GuildsVolunteerHasWorkedInWarning"), ex);
        }
    }

    /**
     * guild model
     *
     * @param model
     * @throws IOException
     */
    public void saveGuildModelToFile(GuildModel model) {
        guildFileDAO.saveModelToFile(model);
    }

    /**
     * Load the guildmodel
     *
     * @return GuildModel
     * @throws IOException
     */
    public GuildModel loadGuildModelFromFile() {
        return guildFileDAO.loadModel();
    }

    /**
     * Save the entire GuildManager model
     *
     * @param model
     * @throws IOException
     */
    public void saveGuildManagerModelToFile(GuildManagerModel model) {
        GMFileDAO.saveModelToFile(model);
    }

    /**
     * Load the GuildManager model
     *
     * @return GuildModel
     * @throws IOException
     */
    public GuildManagerModel loadGuildManagerModelFromFile() {
        return GMFileDAO.loadModel();
    }

    /**
     * Save the entire Volunteer model
     *
     * @param model
     * @throws IOException
     */
    public void saveVolunteerModelToFile(VolunteerModel model) {
        volunteerFileDAO.saveModelToFile(model);
    }

    /**
     * Load the Volunteer model
     *
     * @return GuildModel
     * @throws IOException
     */
    public VolunteerModel loadVolunteerModelFromFile() {
        return volunteerFileDAO.loadModel();
    }

    /*
     * Gets the total hours for a volunteer in i guild.
     *
     * @param guildName
     * @param volunteer
     * @return
     * @throws DALException
     */
    public List<Integer> getWorkHoursForAVolunteerInAGuild(String guildName, Volunteer volunteer) throws DALException {
        try {
            return volunteerDAO.getWorkHoursForAVolunteerInAGuild(guildName, volunteer);
        } catch (SQLException ex) {
            throw new DALException(MuseumTimeTracking.bundle.getString("HoursForVolunteerInGuild"), ex);
        }
    }

    /**
     * Gets the total workhours for a volunteer in all guilds.
     *
     * @param volunteer
     * @return
     */
    public List<Integer> getWorkHoursForAVolunteerInAllGuilds(Volunteer volunteer) throws DALException {
        try {
            return volunteerDAO.getWorkHoursForAVolunteerInAllGuilds(volunteer);
        } catch (SQLException ex) {
            throw new DALException(MuseumTimeTracking.bundle.getString("TotalHoursForVolunteer"), ex);
        }
    }

    /**
     * Gets all volunteers that have worked on specified guild.
     *
     * @param guildName
     * @return
     */
    public Set<Volunteer> getVolunteersThatHasWorkedOnGuild(String guildName) throws DALException {
        try {
            return volunteerDAO.getVolunteersThatHasWorkedOnGuild(guildName);
        } catch (SQLException ex) {
            throw new DALException(MuseumTimeTracking.bundle.getString("VolunteersInGuild"), ex);
        }
    }

    /**
     * Gets all hours that has been added to a guild.
     *
     * @param guildName
     * @return
     */
    public List<Integer> getWorkHoursInGuild(String guildName) throws DALException {
        try {
            return guildDAO.getWorkHoursInGuild(guildName);
        } catch (SQLException ex) {
            throw new DALException(MuseumTimeTracking.bundle.getString("HoursInGuild"), ex);
        }
    }

    /**
     * Gets the restriction of a GM's description.
     *
     * @return
     * @throws DALException
     */
    public int getDescriptionRestrictionForGm() throws DALException {
        try {
            return guildManagerDAO.getDescriptionRestriction();
        } catch (SQLException ex) {
            throw new DALException(MuseumTimeTracking.bundle.getString("DBDescriptionLimit"), ex);
        }
    }
}
