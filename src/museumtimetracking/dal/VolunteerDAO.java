/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.dal;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import museumtimetracking.be.Volunteer;

/**
 *
 * @author Skovgaard
 */
public class VolunteerDAO {

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
     * Create a Volunteer in the Person table.
     *
     * @param newVolunteer
     */
    public void createVolunteer(Volunteer newVolunteer) {
        String sql = "INSERT INTO Person "
                + "(FirstName, LastName, Email, Phone) "
                + "VALUES (?,?,?,?)";
        try (Connection con = cm.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, newVolunteer.getFirstName());
            ps.setString(2, newVolunteer.getLastName());
            ps.setString(3, newVolunteer.getEmail());
            ps.setInt(4, newVolunteer.getPhone());

            ps.executeUpdate();
            ResultSet key = ps.getGeneratedKeys();
            key.next();
            int id = key.getInt(1);
            addVolunteer(id);
        } catch (Exception e) {
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
            Logger.getLogger(VolunteerDAO.class.getName()).log(Level.SEVERE, null, sqlException);
        }
    }

}
