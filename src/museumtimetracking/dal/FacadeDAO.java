/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.dal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import museumtimetracking.be.APerson;
import museumtimetracking.be.GM;
import museumtimetracking.be.Guild;
import museumtimetracking.be.Volunteer;
import museumtimetracking.exception.DALException;

/**
 *
 * @author Mathias
 */
public class FacadeDAO {

    private static FacadeDAO instance;

    public static final String DB_CONNECTION_ERROR = "Kunne ikke forbinde til DB";

    private final GuildDAO guildDAO;

    private final VolunteerDAO volunteerDAO;

    private final GuildManagerDAO guildManagerDAO;

    public static FacadeDAO getInstance() throws IOException {
        if (instance == null) {
            instance = new FacadeDAO();
        }
        return instance;
    }

    private FacadeDAO() throws IOException {
        guildDAO = new GuildDAO();
        volunteerDAO = new VolunteerDAO();
        guildManagerDAO = new GuildManagerDAO();
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
    public List<Guild> getAllGuildsNotArchived() throws DALException {
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
            throw new DALException("Det var ikke muligt at hente de dokumenterede timer.", ex);
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
            throw new DALException(
                    "Den frivillige har allerede fået dokumenteret tid får dette Laug idag.", ex);
        }
    }

    public List<Guild> getGuildsWithoutManagers() throws DALException {
        try {
            return guildDAO.getGuildsWithoutManagers();
        } catch (SQLException ex) {
            throw new DALException(
                    "De ledige laug uden tovholdere kunne ikke hentes fra databasen.", ex);
        }
    }
}
