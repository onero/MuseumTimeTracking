/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.dal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import museumtimetracking.be.APerson;

/**
 *
 * @author Rasmus
 */
public abstract class APersonDAO {

    /**
     * Creates a new person in the database and returns that persons ID.
     *
     * @param con
     * @param person
     * @return
     * @throws SQLException
     */
    public int createNewPersonInDatabase(Connection con, APerson person) throws SQLException {
        String sql = "INSERT INTO Person "
                + "(FirstName, LastName, Email, Phone) "
                + "VALUES (?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        ps.setString(1, person.getFirstName());
        ps.setString(2, person.getLastName());
        ps.setString(3, person.getEmail());
        ps.setInt(4, person.getPhone());

        ps.executeUpdate();
        ResultSet keys = ps.getGeneratedKeys();
        keys.next();
        return keys.getInt(1);
    }

    /**
     * Updates the parsed person in the database.
     *
     * @param con
     * @param person
     * @throws SQLException
     */
    public void updatePersonInformation(Connection con, APerson person) throws SQLException {
        String sql = "UPDATE Person "
                + "SET FirstName = ?, "
                + "LastName = ?, "
                + "Email = ?, "
                + "Phone = ? "
                + "WHERE ID = ?";
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, person.getFirstName());
        ps.setString(2, person.getLastName());
        ps.setString(3, person.getEmail());
        ps.setInt(4, person.getPhone());
        ps.setInt(5, person.getID());

        ps.executeUpdate();
    }

    public void setPersonImage(Connection con, int id) throws SQLException {
        File file = new File("healadin.png");
        System.out.println(file.getAbsolutePath());
        FileInputStream fis = null;
        String sql = "UPDATE Person "
                + "SET Picture = ? "
                + "WHERE ID = ?";
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(APersonDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setBlob(1, fis);
        ps.setInt(2, id);

        ps.executeUpdate();
    }

    public Image getPersonImage(Connection con, int id) throws SQLException {
        Image img = null;
        String sql = "SELECT Picture FROM Person "
                + "WHERE ID = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            InputStream in = rs.getBinaryStream("Picture");
            img = new Image(in);
        }
        return img;
    }

}
