/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.guild.editGuild;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import museumtimetracking.be.enums.EFXMLName;
import museumtimetracking.gui.views.NodeFactory;
import museumtimetracking.gui.views.root.guild.guildComponents.GuildTableViewController;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class EditGuildViewController implements Initializable {

    @FXML
    private Button btnSave;
    @FXML
    private BorderPane editGuildBorderPane;
    @FXML
    private TextArea txtGuildDescription;
    @FXML
    private TextField txtGuildName;

    private final Node guildTable;

    public EditGuildViewController() {
        guildTable = NodeFactory.getInstance().createNewView(EFXMLName.GUILD_TABLE);
        GuildTableViewController.getIntance().setButtonVisibility(false);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editGuildBorderPane.setLeft(guildTable);
    }

    @FXML
    private void handleSaveGuild(ActionEvent event) {
    }
}
