/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.guild;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import static museumtimetracking.be.enums.EFXMLName.*;
import museumtimetracking.gui.views.NodeFactory;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class GuildOverviewController implements Initializable {

    @FXML
    private BorderPane guildBorderPane;

    private final NodeFactory nodeFactory;

    private final Node guildTable;

    public GuildOverviewController() {
        nodeFactory = NodeFactory.getInstance();
        guildTable = nodeFactory.createNewView(GUILD_TABLE);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        guildBorderPane.setLeft(guildTable);
    }

}
