/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.volunteer.idleVolunteers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import museumtimetracking.be.Volunteer;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class IdleVolunteerViewController implements Initializable {

    @FXML
    private TableColumn<String, Volunteer> clmGuildDescription;
    @FXML
    private TableColumn<String, Volunteer> clmGuildName;
    @FXML
    private BorderPane guildTableBorderPane;
    @FXML
    private TableView<Volunteer> tableVolunteer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        tableVolunteer.setItems();
    }

    @FXML
    private void handleRestoreFromArchive() {
    }

}
