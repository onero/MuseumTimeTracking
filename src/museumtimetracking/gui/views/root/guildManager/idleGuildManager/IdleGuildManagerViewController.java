/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.guildManager.idleGuildManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import museumtimetracking.be.GuildManager;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.GuildManagerModel;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class IdleGuildManagerViewController implements Initializable {

    @FXML
    private TableColumn<GuildManager, String> clmGMDescription;
    @FXML
    private TableColumn<GuildManager, String> clmGMName;
    @FXML
    private BorderPane guildTableBorderPane;
    @FXML
    private TableView<GuildManager> tableGM;

    @FXML
    private void handleRestoreFromArchive() {
        GuildManager selectedManager = tableGM.getSelectionModel().getSelectedItem();
        if (selectedManager != null) {
            try {
                GuildManagerModel.getInstance().updateIdleManager(selectedManager, false);
            } catch (IOException | DALException ex) {
                ExceptionDisplayer.display(ex);
            }
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clmGMName.setCellValueFactory(gm -> gm.getValue().getFullNameProperty());
        clmGMDescription.setCellValueFactory(gm -> gm.getValue().getDescription());

        try {
            tableGM.setItems(GuildManagerModel.getInstance().getCachedIdleGuildManagers());
        } catch (IOException | DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

}
