/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.guild.archivedGuilds;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import museumtimetracking.be.Guild;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class ArchivedGuildViewController implements Initializable {

    @FXML
    private HBox buttonBar;
    @FXML
    private TableColumn<?, ?> clmArchived;
    @FXML
    private TableColumn<Guild, String> clmGuildDescription;
    @FXML
    private TableColumn<Guild, String> clmGuildName;
    @FXML
    private BorderPane guildTableBorderPane;
    @FXML
    private TableView<Guild> tableGuild;

    @FXML
    private void handleRemoveFromArchive(ActionEvent event) {
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
