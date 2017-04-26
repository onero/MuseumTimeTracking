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
import javafx.stage.Stage;
import museumtimetracking.be.Guild;
import museumtimetracking.gui.model.GuildModel;

/**
 * FXML Controller class
 *
 * @author Skovgaard
 */
public class NewGuildViewController implements Initializable {

    @FXML
    private Label lblGuildNameAlreadyExsist;
    @FXML
    private TextArea txtAreaGuildDescription;
    @FXML
    private TextField txtFieldGuildName;
    
    private GuildModel guildModel;

    public NewGuildViewController() {
        guildModel = new GuildModel();
    }
    
    
    @FXML
    private void handleAddGuildBtn(ActionEvent event) {
        Guild newGuild = new Guild(txtFieldGuildName.getText(), txtAreaGuildDescription.getText());
        guildModel.addGuild(newGuild);
        
        closeWindow();
    }

    @FXML
    // How to close a window.
    private void handleBackBtn(ActionEvent event) {
        closeWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    private void closeWindow(){
        Stage stage = (Stage) txtFieldGuildName.getScene().getWindow();
        stage.close();
    }

}
