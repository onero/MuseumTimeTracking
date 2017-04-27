/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.guildManager.guildManagerOverview;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import static museumtimetracking.be.enums.EFXMLName.*;
import museumtimetracking.gui.views.NodeFactory;

/**
 * FXML Controller class
 *
 * @author Rasmus
 */
public class GuildManagerOverviewController implements Initializable {

    @FXML
    private ListView<?> lstManagers;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPhone;

    private final NodeFactory nodeFactory;

    public GuildManagerOverviewController() {
        nodeFactory = NodeFactory.getInstance();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleNewManagerButton(ActionEvent event) {
        newManagerModal();
    }

    @FXML
    private void handleEditButton(ActionEvent event) {
    }

    @FXML
    private void handleDeleteButton(ActionEvent event) {
    }

    private void newManagerModal() {
        Stage primStage = (Stage) txtFirstName.getScene().getWindow();
        Parent newManager = nodeFactory.createNewParent(NEW_MANAGER);

        Stage stage = new Stage();
        stage.setScene(new Scene(newManager));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primStage);

        stage.show();
    }

    @FXML
    private void handleAddGuildButton(ActionEvent event) {
    }

}
