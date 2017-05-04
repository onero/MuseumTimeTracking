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
import museumtimetracking.be.Guild;
import museumtimetracking.be.GuildManager;
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
     * @throws museumtimetracking.exception.DALException
     */
    public void addVolunteer(Volunteer volunteer) throws DALException {
        try {
            volunteerDAO.createVolunteer(volunteer);
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
    public GuildManager createNewGuildManager(APerson person, String guildName) throws DALException {
        try {
            return guildManagerDAO.createNewGuildManager(person, guildName);
        } catch (SQLException ex) {
            throw new DALException(DB_CONNECTION_ERROR, ex);
        }
    }

    /**
     * Gets a list of guild managers that are each filled with the appropriate
     * guilds from the GuildManagerDAO.
     *
     * @return
     * @throws museumtimetracking.exception.DALException
     */
    public List<GuildManager> getAllGuildManagers() throws DALException {
        try {
            return guildManagerDAO.addGuildsToGuildManagers(guildManagerDAO.getAllGuildManagers());
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
    public void updateGuildManager(GuildManager manager, Set<String> guildsToAdd, Set<String> guildsToDelete) throws DALException {
        try {
            guildManagerDAO.updateGuildManagerInDatabase(manager, guildsToAdd, guildsToDelete);
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
}
