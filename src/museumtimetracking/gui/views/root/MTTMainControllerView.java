/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import static museumtimetracking.be.enums.EFXMLName.*;
import museumtimetracking.gui.views.NodeFactory;
import museumtimetracking.gui.views.root.guild.guildComponents.GuildTableViewController;

/**
 *
 * @author gta1
 */
public class MTTMainControllerView implements Initializable {

    @FXML
    private BorderPane borderPaneMain;
    @FXML
    private BorderPane borderPaneMainAtBottomBorderPane;

    private final NodeFactory nodeFactory;

    private final Node guildOverview;
    private final Node managerOverview;
    private final Node guildTable;

    public MTTMainControllerView() {
        nodeFactory = NodeFactory.getInstance();

        guildOverview = nodeFactory.createNewView(GUILD_OVERVIEW);
        managerOverview = nodeFactory.createNewView(MANAGER_OVERVIEW);
        guildTable = nodeFactory.createNewView(GUILD_TABLE);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        borderPaneMain.setCenter(guildOverview);
        borderPaneMainAtBottomBorderPane.setCenter(guildTable);
        GuildTableViewController.getIntance().setButtonVisibility(false);
    }

    @FXML
    private void handleGuildsButton(ActionEvent event) {
        borderPaneMain.setCenter(guildOverview);
    }

    @FXML
    private void handleGuildManagersButton(ActionEvent event) {
        borderPaneMain.setCenter(managerOverview);
    }

}
