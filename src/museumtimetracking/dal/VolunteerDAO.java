/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import museumtimetracking.be.Volunteer;
import museumtimetracking.be.enums.ELanguage;

/**
 *
 * @author Skovgaard
 */
public class VolunteerDAO extends APersonDAO {

    private DBConnectionManager cm;

    private static VolunteerDAO instance;

    public static VolunteerDAO getInstance() {
        if (instance == null) {
            instance = new VolunteerDAO();
        }
        return instance;
    }

    private VolunteerDAO() {
        cm = null;
        try {
            cm = DBConnectionManager.getInstance();
        } catch (IOException ex) {
            System.out.println("Couldn't connect to the database.");
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get all idle volunteers
     *
     * @return
     */
    public List<Volunteer> getAllIdleVolunteers() {
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
        } catch (SQLServerException ex) {
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return volunteers;
    }

    /**
     * All the volunteers from DB.
     *
     * @return
     */
    public List<Volunteer> getAllVolunteersNotIdle() {
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
        } catch (SQLException ex) {
            Logger.getLogger(GuildDAO.class.getName()).log(Level.SEVERE, null, ex);
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

        ELanguage Elang = ELanguage.getLanguageByString(language);

        Volunteer volunteer = new Volunteer(id, firstName, lastName, eMail, phoneNumber, isIdle, Elang);

        return volunteer;
    }

    /**
     * Create a Volunteer in the Person table.
     *
     * @param newVolunteer
     */
    public void createVolunteer(Volunteer newVolunteer) {
        try (Connection con = cm.getConnection()) {
            int id = createNewPersonInDatabase(con, newVolunteer);
            addVolunteer(id);
        } catch (SQLServerException ex) {
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Add the Volunteer to the Volunteer table
     *
     * @param personID
     */
    private void addVolunteer(int personID) {
        String sql = "INSERT INTO Volunteer "
                + "(PersonID, IsIdle) "
                + "VALUES (?, ?)";
        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, personID);
            ps.setInt(2, 0);

            ps.executeUpdate();

        } catch (SQLException sqlException) {
            System.out.println("Couldn't add newVolunteer to DB");
            System.out.println(sqlException);
            Logger
                    .getLogger(VolunteerDAO.class
                            .getName()).log(Level.SEVERE, null, sqlException);
        }
    }

    public void updateVolunteerPersonInfo(Volunteer volunteer) {
        Connection con;
        try {
            con = cm.getConnection();
            updatePersonInformation(con, volunteer);
        } catch (SQLServerException ex) {
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Update the idle status of the volunteer
     *
     * @param value
     */
    public void updateVolunteerIdleStatus(boolean value) {
        String sql;
        if (value) {
            sql = "UPDATE Volunteer "
                    + "SET IsIdle = 1";
        } else {
            sql = "UPDATE Volunteer "
                    + "SET IsIdle = 0";
        }

        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);

            ps.executeUpdate();
        } catch (SQLServerException ex) {
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
