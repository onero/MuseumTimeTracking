/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.guild.newGuild;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Skovgaard
 */
public class NewGuildViewController implements Initializable {

    @FXML
    private Button btnBack;
    @FXML
    private Label lblGuildNameAlreadyExsist;
    @FXML
    private TextArea txtAreaGuildDescription;
    @FXML
    private TextField txtFieldGuildName;

    @FXML
    private void handleAddGuildBtn(ActionEvent event) {
    }

    @FXML
    private void handleBackBtn(ActionEvent event) {
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
