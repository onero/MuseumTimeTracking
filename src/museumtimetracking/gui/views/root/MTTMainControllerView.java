/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
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
public class MTTMainControllerView implements Initializable {

    @FXML
    private AnchorPane anchorPaneGuild;

    @FXML
    private BorderPane borderPane;
    @FXML
    private BarChart<?, ?> chartHoursOverview;
    @FXML
    private TableColumn<Guild, String> clmGuildName;
    @FXML
    private TableColumn<Guild, String> clmbGuildDescription;
    @FXML
    private JFXComboBox<?> cmbGuildManager;
    @FXML
    private GridPane gridPaneGuild;
    @FXML
    private Tab tabGM;
    @FXML
    private JFXTabPane tabPane;
    @FXML
    private Tab tabPaneGuild;
    @FXML
    private Tab tabVolunteer;
    @FXML
    private TableView<Guild> tblGuild;
    @FXML
    private JFXTextArea txtDescription;
    @FXML
    private JFXTextField txtGuildName;
    @FXML
    private VBox vBoxGuildOptions;

    private GuildModel guildModel;

    private ModalFactory modalFactory;

    private Guild selectedGuild;

    public MTTMainControllerView() {
        modalFactory = ModalFactory.getInstance();
        try {
            this.guildModel = GuildModel.getInstance();
        } catch (IOException | DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    @FXML
    private void handleEditGuild(MouseEvent event) {
        setGuildOptionsVisibility(true);

        selectedGuild = tblGuild.getSelectionModel().getSelectedItem();

        giveDataToChartHoursOverview(selectedGuild.getName());

        //Enter edit mode
        if (event.getClickCount() == 2) {

            Stage primStage = (Stage) tblGuild.getScene().getWindow();

            Stage editGuildModal = modalFactory.createNewModal(primStage, EFXMLName.EDIT_GUILD);

            EditGuildViewController controller = modalFactory.getLoader().getController();

            controller.setCurrentGuild(selectedGuild);

            editGuildModal.show();
        }
    }

    /**
     * Set the visibility of guild options
     *
     * @param shown
     */
    public void setGuildOptionsVisibility(boolean shown) {
        vBoxGuildOptions.setDisable(!shown);
        vBoxGuildOptions.setVisible(shown);
    }

    @FXML
    private void handleAddGuild() {
        String name = txtGuildName.getText();
        String description = txtDescription.getText();
        if (name != null && description != null) {
            Guild newGuild = new Guild(name, description, false);
            try {
                guildModel.addGuild(newGuild);
            } catch (DALException ex) {
                ExceptionDisplayer.display(ex);
            }
        }
    }

    @FXML
    private void handleArchiveGuid() {
        try {
            guildModel.archiveGuild(selectedGuild);
        } catch (DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    @FXML
    private void handleDeleteGuid() {
        Alert alert = AlertFactory.createDeleteAlert();
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

    @FXML
    private void handleSelectGuildManager(ActionEvent event) {
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tblGuild.setItems(guildModel.getCachedGuilds());

        clmGuildName.setCellValueFactory(g -> g.getValue().getNameProperty());
        clmbGuildDescription.setCellValueFactory(g -> g.getValue().getDescriptionProperty());

    }

    /**
     *
     * @param title
     */
    private void giveDataToChartHoursOverview(String title) {
        chartHoursOverview.getData().clear();
        chartHoursOverview.setTitle(title);
        List<Guild> guilds = guildModel.getGuildsFromDB();
        Map<String, Integer> guildHours = guildModel.getMapOfHoursPerGuild();

        XYChart.Series hoursSeries = new XYChart.Series<>();

        for (Guild guild : guilds) {
            hoursSeries.getData().add(new XYChart.Data<>(guild.getName(), guildHours.get(guild.getName())));
        }

        chartHoursOverview.getData().add(hoursSeries);
    }

}
