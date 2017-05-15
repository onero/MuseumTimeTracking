/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.scene.image.Image;
import museumtimetracking.be.Volunteer;
import museumtimetracking.be.enums.ELanguage;

/**
 *
 * @author Skovgaard
 */
public class VolunteerDAO extends APersonDAO {

    private final DBConnectionManager cm;

    public VolunteerDAO() throws IOException {
        this.cm = DBConnectionManager.getInstance();
    }

    /**
     * Get all idle volunteers
     *
     * @return
     * @throws com.microsoft.sqlserver.jdbc.SQLServerException
     */
    public List<Volunteer> getAllIdleVolunteers() throws SQLServerException, SQLException {

        List<Volunteer> volunteers = new ArrayList<>();

        String sql = "SELECT * FROM Volunteer v "
                + "JOIN Person p ON p.ID = v.PersonID "
                + "WHERE v.IsIdle = 1";

        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                volunteers.add(getOneVolunteer(rs));
            }
        }
        return volunteers;
    }

    /**
     * All the volunteers from DB.
     *
     * @return
     * @throws com.microsoft.sqlserver.jdbc.SQLServerException
     */
    public List<Volunteer> getAllVolunteersNotIdle() throws SQLServerException, SQLException {
        List<Volunteer> volunteers = new ArrayList<>();
        String sql = "SELECT * FROM Volunteer v "
                + "JOIN Person p ON p.ID = v.PersonID "
                + "WHERE v.IsIdle = 0 ";

        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                volunteers.add(getOneVolunteer(rs));
            }
        }
        return volunteers;
    }

    // Get one volunteer from DB.
    private Volunteer getOneVolunteer(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");
        String firstName = rs.getString("FirstName");
        String lastName = rs.getString("LastName");
        String eMail = rs.getString("Email");
        int phoneNumber = rs.getInt("Phone");
        boolean isIdle = rs.getBoolean("IsIdle");
        String language = rs.getString("Language");
        String description = rs.getString("Description");
        InputStream in = rs.getBinaryStream("Picture");
        Image img = null;
        if (in != null) {
            img = new Image(in);
        }

        ELanguage Elang = ELanguage.getLanguageByString(language);

        Volunteer volunteer = new Volunteer(id, firstName, lastName, eMail, phoneNumber, isIdle, Elang);
        volunteer.setDescription(description);
        volunteer.setImage(img);

        return volunteer;
    }

    /**
     * Create a Volunteer in the Person table.
     *
     * @param newVolunteer
     * @return
     * @throws com.microsoft.sqlserver.jdbc.SQLServerException
     */
    public Volunteer createVolunteer(Volunteer newVolunteer) throws SQLServerException, SQLException {
        try (Connection con = cm.getConnection()) {
            int id = createNewPersonInDatabase(con, newVolunteer);
            addVolunteer(con, id);
            updateVolunteerInfo(con, newVolunteer.getDescription(), id);
            newVolunteer.setID(id);
            return newVolunteer;
        }
    }

    /**
     * Add the Volunteer to the Volunteer table
     *
     * @param personID
     */
    private void addVolunteer(Connection con, int personID) throws SQLServerException, SQLException {
        String sql = "INSERT INTO Volunteer "
                + "(PersonID, IsIdle) "
                + "VALUES (?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, personID);
        ps.setInt(2, 0);

        ps.executeUpdate();

    }

    /**
     * Set the description of the volunteer
     *
     * @param id
     * @param text
     * @throws java.sql.SQLException
     */
    public void setVolunteerDescription(int id, String text) throws SQLException, SQLException {
        try (Connection con = cm.getConnection()) {
            String sql = "UPDATE Volunteer "
                    + "SET Description = ? "
                    + "WHERE PersonID = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, text);
            ps.setInt(2, id);

            ps.executeUpdate();
        }
    }

    /**
     * Updates the new edits for the volunteer and saves it in the DB.
     *
     * @param volunteer
     * @throws com.microsoft.sqlserver.jdbc.SQLServerException
     */
    public void updateVolunteerPersonInfo(Volunteer volunteer) throws SQLServerException, SQLException {
        try (Connection con = cm.getConnection()) {
            // updatePersonInformation is from a abstract class "APersonDAO".
            updatePersonInformation(con, volunteer);
            updateVolunteerInfo(con, volunteer.getDescription(), volunteer.getID());
        }
    }

    /**
     * Update volunteer description
     *
     * @param con
     * @param volunteer
     * @throws SQLException
     */
    private void updateVolunteerInfo(Connection con, String description, int id) throws SQLException {
        String sql = "UPDATE Volunteer "
                + "SET Description = ? "
                + "WHERE PersonID = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, description);
        ps.setInt(2, id);

        ps.executeUpdate();

    }

    /**
     * Update the idle status of the volunteer
     *
     * @param id
     * @param value
     * @throws com.microsoft.sqlserver.jdbc.SQLServerException
     */
    public void updateVolunteerIdleStatus(int id, boolean value) throws SQLServerException, SQLException {
        String sql;
        if (value) {
            sql = "UPDATE Volunteer "
                    + "SET IsIdle = 1 "
                    + "WHERE PersonID = ?";
        } else {
            sql = "UPDATE Volunteer "
                    + "SET IsIdle = 0 "
                    + "WHERE PersonID = ?";
        }

        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();
        }
    }

    /**
     * Deletes the selected volunteer from the DB.
     *
     * @param id
     * @throws com.microsoft.sqlserver.jdbc.SQLServerException
     */
    public void deleteVolunteer(int id) throws SQLServerException, SQLException {
        try (Connection con = cm.getConnection()) {
            deletePersonFromDatabaseByID(con, id);
        }

    }

    /**
     * Set the volunteer image
     *
     * @param id
     * @param file
     * @throws com.microsoft.sqlserver.jdbc.SQLServerException
     * @throws java.io.FileNotFoundException
     */
    public void setVolunteerImage(int id, File file) throws SQLServerException, SQLException, FileNotFoundException {
        try (Connection con = cm.getConnection()) {
            setPersonImage(con, id, file);
        }
    }

    /**
     * Adds hours to a volunteer in the database.
     *
     * @param volunteerID
     * @param guildName
     * @param date
     * @param hours
     */
    public void addHoursToVolunteer(int volunteerID, String guildName, Date date, int hours) throws SQLException {
        String sql = "INSERT INTO VolunteerWork "
                + "(VolunteerID, GuildName, Date, Hours) "
                + "VALUES (?,?,?,?)";
        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, volunteerID);
            ps.setString(2, guildName);
            ps.setTimestamp(3, new java.sql.Timestamp(date.getTime()));
            ps.setInt(4, hours);

            ps.executeUpdate();
        }
    }
}
