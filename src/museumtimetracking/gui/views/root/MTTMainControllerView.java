/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root;

import com.jfoenix.controls.JFXTabPane;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import static museumtimetracking.be.enums.EFXMLName.*;
import museumtimetracking.gui.views.NodeFactory;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class MTTMainControllerView implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private Tab tabGM;
    @FXML
    private JFXTabPane tabPane;
    @FXML
    private Tab tabPaneActiveGuild;
    @FXML
    private Tab tabPaneArchivedGuild;
    @FXML
    private Tab tabVolunteer;

    private final Node guildOverView;
    private final Node archivedGuild;

    private final NodeFactory nodeFactory;

    public MTTMainControllerView() {
        nodeFactory = NodeFactory.getInstance();

        guildOverView = nodeFactory.createNewView(GUILD_OVERVIEW);
        archivedGuild = nodeFactory.createNewView(ARCHIVED_TABLE);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tabPaneActiveGuild.setContent(guildOverView);
        tabPaneArchivedGuild.setContent(archivedGuild);
    }

}
