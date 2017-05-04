/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.guild.newGuild;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import museumtimetracking.be.Guild;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.GuildModel;

/**
 * FXML Controller class
 *
 * @author Skovgaard
 */
public class NewGuildViewController implements Initializable {

    @FXML
    private Label lblGuildNameAlreadyExsist;
    //TODO @Skovgaard
    @FXML
    private TextArea txtAreaGuildDescription;
    @FXML
    private TextField txtFieldGuildName;

    private GuildModel guildModel;

    public NewGuildViewController() {
        try {
            guildModel = GuildModel.getInstance();
        } catch (IOException | DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    @FXML
    private void handleAddGuildBtn() {
        //TODO Skovgaard: Add validation.
        Guild newGuild = new Guild(txtFieldGuildName.getText(), txtAreaGuildDescription.getText(), false);
        try {
            guildModel.addGuild(newGuild);
        } catch (DALException ex) {
            ExceptionDisplayer.display(ex);
        }

        closeWindow();
    }

    @FXML
    // How to close a window.
    private void handleBackBtn() {
        closeWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    private void closeWindow() {
        Stage stage = (Stage) txtFieldGuildName.getScene().getWindow();
        stage.close();
    }

}
