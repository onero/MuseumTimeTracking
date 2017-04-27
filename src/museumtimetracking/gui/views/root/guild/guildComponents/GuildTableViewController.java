/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.guild.guildComponents;

import java.io.IOException;
import java.net.URL;
import static java.util.Collections.list;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import museumtimetracking.be.Guild;
import museumtimetracking.be.enums.EFXMLName;
import museumtimetracking.gui.model.GuildModel;
import museumtimetracking.gui.views.root.guild.editGuild.EditGuildViewController;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class GuildTableViewController implements Initializable {

    @FXML
    private HBox buttonBar;

    @FXML
    private TableColumn<Guild, String> clmGuildDescription;
    @FXML
    private TableColumn<Guild, String> clmGuildName;
    @FXML
    private BorderPane guildTableBorderPane;
    @FXML
    private TableView<Guild> tableGuild;

    private static GuildTableViewController instance;

    public static GuildTableViewController getIntance() {
        return instance;
    }

    private final GuildModel guildModel;

    private Stage primStage;

    public GuildTableViewController() {
        guildModel = GuildModel.getInstance();
    }

    @FXML
    private void handleArchiveBtn(ActionEvent event) {
    }

    @FXML
    private void handleDeleteGuild(ActionEvent event) {
    }

    @FXML
    private void handleEditGuild(MouseEvent event) throws IOException {

        if (event.getClickCount() == 2) {
            Guild selectedGuild = tableGuild.getSelectionModel().getSelectedItem();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(EFXMLName.EDIT_GUILD.toString()));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));

            //Create new modal window from FXMLLoader
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initOwner(primStage);

            EditGuildViewController controller = loader.getController();
            controller.setCurrentGuild(selectedGuild);

            newStage.show();
        }

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        // Below the guilds is being set inside the tableView.
        tableGuild.setItems(guildModel.getCachedGuilds());

        clmGuildName.setCellValueFactory(g -> g.getValue().getNameProperty());
        clmGuildDescription.setCellValueFactory(g -> g.getValue().getDescriptionProperty());
    }

    @FXML
    private void handleAddGuildBtn() throws IOException {
        primStage = (Stage) tableGuild.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(EFXMLName.ADD_NEW_GUILD.toString()));
        Parent root = loader.load();

        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));

        //Create new modal window from FXMLLoader
        newStage.initModality(Modality.WINDOW_MODAL);
        newStage.initOwner(primStage);

        newStage.show();

    }

}
