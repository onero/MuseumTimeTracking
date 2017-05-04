/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.guild;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import museumtimetracking.be.Guild;
import museumtimetracking.be.enums.EFXMLName;
import museumtimetracking.exception.AlertFactory;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.GuildModel;
import museumtimetracking.gui.views.ModalFactory;
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
    @FXML
    private BarChart<String, Integer> chartHoursOverview;

    private final ModalFactory modalFactory;

    private GuildModel guildModel;

    private Stage primStage;

    public GuildOverviewController() {
        modalFactory = ModalFactory.getInstance();
        try {
            this.guildModel = GuildModel.getInstance();
        } catch (IOException | DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableGuild.setItems(guildModel.getCachedGuilds());

        clmGuildName.setCellValueFactory(g -> g.getValue().getNameProperty());
        clmGuildDescription.setCellValueFactory(g -> g.getValue().getDescriptionProperty());
        initializeChartHoursOverview();
    }

    /**
     * Deletes the selected guild from tableView and DB. Goes to GuildModel.
     */
    @FXML
    private void handleDeleteGuild() {
        String message = "Tryk 'Ja' for at slette permanent. \n Tryk 'Nej' for at fortryde.";
        Alert alert = AlertFactory.createAlert(AlertType.WARNING, message);
        alert.showAndWait().ifPresent(type -> {
            //If the first button ("YES") is clicked
            if (type == alert.getButtonTypes().get(0)) {
                Guild deleteGuild = tableGuild.getSelectionModel().getSelectedItem();
                try {
                    guildModel.deleteGuild(deleteGuild);
                } catch (DALException ex) {
                    ExceptionDisplayer.display(ex);
                }
            }
        });

    }

    @FXML
    private void handleArchiveBtn() {
        Guild guildToArchive = tableGuild.getSelectionModel().getSelectedItem();
        if (guildToArchive != null) {
            try {
                guildModel.archiveGuild(guildToArchive);
            } catch (DALException ex) {
                ExceptionDisplayer.display(ex);
            }
        }
    }

    @FXML
    private void handleEditGuild(MouseEvent event) throws IOException {

        if (event.getClickCount() == 2) {
            Guild selectedGuild = tableGuild.getSelectionModel().getSelectedItem();

            primStage = (Stage) tableGuild.getScene().getWindow();

            Stage editGuildModal = modalFactory.createNewModal(primStage, EFXMLName.EDIT_GUILD);

            EditGuildViewController controller = modalFactory.getLoader().getController();

            controller.setCurrentGuild(selectedGuild);

            editGuildModal.show();
        }
    }

    @FXML
    private void handleAddGuildBtn() throws IOException {
        primStage = (Stage) tableGuild.getScene().getWindow();

        Stage editGuildModal = modalFactory.createNewModal(primStage, EFXMLName.ADD_NEW_GUILD);

        editGuildModal.show();

    }

    /**
     * Sets the title of the chart and give it it's initial data.
     */
    private void initializeChartHoursOverview() {
        chartHoursOverview.setTitle("Dokumenterede Timer");
        giveDataToChartHoursOverview("Laug");
    }

    /**
     *
     * @param title
     */
    private void giveDataToChartHoursOverview(String title) {
        chartHoursOverview.getData().clear();
        List<Guild> guilds = guildModel.getGuildsFromDB();
        Map<String, Integer> guildHours = guildModel.getMapOfHoursPerGuild();

        XYChart.Series hoursSeries = new XYChart.Series<>();
        hoursSeries.setName(title);

        for (Guild guild : guilds) {
            hoursSeries.getData().add(new XYChart.Data<>(guild.getName(), guildHours.get(guild.getName())));
        }

        chartHoursOverview.getData().add(hoursSeries);
    }

}
