/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.archivedGuilds;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import museumtimetracking.be.Guild;
import museumtimetracking.exception.AlertFactory;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.GuildModel;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class ArchivedGuildViewController implements Initializable {

    @FXML
    private TableColumn<Guild, String> clmGuildDescription;
    @FXML
    private TableColumn<Guild, String> clmGuildName;
    @FXML
    private Label lblGuildAmount;
    @FXML
    private TableView<Guild> tableGuild;
    @FXML
    private ButtonBar btnbButtons;

    private GuildModel guildModel;

    private Guild selectedGuild;

    public ArchivedGuildViewController() {
        try {
            guildModel = GuildModel.getInstance();
        } catch (DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setGuildOptionsVisibility(false);
        tableGuild.setItems(guildModel.getCachedArchivedGuilds());

        clmGuildName.setCellValueFactory(g -> g.getValue().getNameProperty());
        clmGuildDescription.setCellValueFactory(g -> g.getValue().getDescriptionProperty());

        lblGuildAmount.textProperty().bind(Bindings.size((guildModel.getCachedArchivedGuilds())).asString());
    }

    /**
     * Set the visibility of guild options
     *
     * @param shown
     */
    public void setGuildOptionsVisibility(boolean shown) {
        btnbButtons.setDisable(!shown);
        btnbButtons.setVisible(shown);
    }

    @FXML
    private void handleDeleteGuid(ActionEvent event) {
        if (selectedGuild != null) {
            Alert alert = new AlertFactory().createDeleteAlert();
            alert.showAndWait().ifPresent(type -> {
                //If the first button ("YES") is clicked
                if (type == alert.getButtonTypes().get(0)) {
                    try {
                        guildModel.deleteGuild(selectedGuild);
                    } catch (DALException ex) {
                        ExceptionDisplayer.display(ex);
                    }
                }
            });
        }
    }

    @FXML
    private void handleSelectGuild(MouseEvent event) {
        setGuildOptionsVisibility(true);
        selectedGuild = tableGuild.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void handlerestoreGuid() {
        if (selectedGuild != null) {
            try {
                guildModel.restoreGuild(selectedGuild);
            } catch (DALException ex) {
                ExceptionDisplayer.display(ex);
            }
        }
    }

    /**
     * Makes the model search in the cachedLists
     *
     * @param searchText
     */
    public void handleSearch(String searchText) {
        guildModel.searchInactiveGuilds(searchText);
    }

}
