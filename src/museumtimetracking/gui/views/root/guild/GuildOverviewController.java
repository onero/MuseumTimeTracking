/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.guild;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
public class GuildOverviewController implements Initializable {

    @FXML
    private TableColumn<Guild, String> clmGuildDescription;
    @FXML
    private TableColumn<Guild, String> clmGuildName;

    @FXML
    private BorderPane guildBorderPane;

    @FXML
    private TableView<Guild> tableGuild;

    private final GuildModel guildModel;

    private Stage primStage;

    public GuildOverviewController() {
        this.guildModel = GuildModel.getInstance();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableGuild.setItems(guildModel.getCachedGuilds());

        clmGuildName.setCellValueFactory(g -> g.getValue().getNameProperty());
        clmGuildDescription.setCellValueFactory(g -> g.getValue().getDescriptionProperty());
    }

    /**
     * Deletes the selected guild(s) from tableView and DB. Goes to GuildModel.
     */
    @FXML
    private void handleDeleteGuild() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("ADVARSEL!");
        alert.setHeaderText(" Tryk 'Ja' for at slette permanent. \n Tryk 'Nej' for at fortryde.");
        ButtonType yesButton = new ButtonType("Ja", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Nej", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButton, noButton);
        alert.showAndWait().ifPresent(type -> {

            if (type == yesButton) {
                Guild deleteGuild = tableGuild.getSelectionModel().getSelectedItem();
                guildModel.deleteGuild(deleteGuild);

            }
        });

    }

    @FXML
    private void handleArchiveBtn() {
        // TODO Adamino: Beware of nullpointer ex.
        Guild guildToArchive = tableGuild.getSelectionModel().getSelectedItem();
        guildModel.archiveGuild(guildToArchive);
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
