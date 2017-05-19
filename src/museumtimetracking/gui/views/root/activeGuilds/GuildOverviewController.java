/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.activeGuilds;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import museumtimetracking.be.GM;
import museumtimetracking.be.Guild;
import museumtimetracking.be.enums.EFXMLName;
import museumtimetracking.exception.AlertFactory;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.GuildManagerModel;
import museumtimetracking.gui.model.GuildModel;
import museumtimetracking.gui.model.ModelFacade;
import museumtimetracking.gui.views.ModalFactory;
import museumtimetracking.gui.views.root.MTTMainControllerView;
import museumtimetracking.gui.views.root.activeGuilds.editGuild.EditGuildViewController;

/**
 * FXML Controller class
 *
 * @author gta1
 */
public class GuildOverviewController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableColumn<Guild, String> clmGM;
    @FXML
    private ButtonBar guildOptions;
    @FXML
    private Label lblGuildAmount;
    @FXML
    private Pane newGuildPane;

    @FXML
    private TableView<Guild> tableGuild;

    @FXML
    private TableColumn<Guild, String> clmGuildName;
    @FXML
    private TableColumn<Guild, String> clmGuildDescription;
    @FXML
    private JFXComboBox<GM> cmbGuildManager;
    @FXML
    private TextArea txtDescription;
    @FXML
    private JFXTextField txtGuildName;

    private static GuildOverviewController instance;

    private GuildModel guildModel;

    private GuildManagerModel guildManagerModel;

    private ModalFactory modalFactory;

    private Guild selectedGuild;

    public static final String GUILD_NAME_ERROR = "Indtast venligst et navn på lauget";

    private GM selectedGuildManager;

    public static GuildOverviewController getInstance() {
        return instance;
    }

    public GuildOverviewController() {
        modalFactory = ModalFactory.getInstance();
        guildModel = ModelFacade.getInstance().getGuildModel();
        guildManagerModel = ModelFacade.getInstance().getGuildManagerModel();

    }

    @FXML
    private void handleComboClear() {
        cmbGuildManager.getSelectionModel().clearSelection();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        initializeGuildTable();
        initializeComboBox();

        setGuildOptionsVisilibity(false);
        lblGuildAmount.textProperty().bind(Bindings.size((guildModel.getCachedGuilds())).asString());

    }

    /**
     * Set visibility of guild option buttonbar
     *
     * @param shown
     */
    private void setGuildOptionsVisilibity(boolean shown) {
        guildOptions.setDisable(!shown);
        guildOptions.setVisible(shown);
    }

    /**
     * Set Guild Mangers in COmboBox
     */
    private void initializeComboBox() {
        cmbGuildManager.setItems(guildManagerModel.getCachedGMCandidates());

        cmbGuildManager.setCellFactory(gm -> new ListCell<GM>() {
            @Override
            protected void updateItem(GM guildManager, boolean empty) {
                super.updateItem(guildManager, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(guildManager.getFullName());
                }
            }
        });

        cmbGuildManager.setButtonCell(
                new ListCell<GM>() {
            @Override
            protected void updateItem(GM guildManager, boolean bln) {
                super.updateItem(guildManager, bln);
                if (bln) {
                    setText("");
                } else {
                    setText(guildManager.getFullName());
                }
            }
        });
    }

    /**
     * Fill the Guild TableView
     */
    private void initializeGuildTable() {
        tableGuild.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableGuild.setItems(guildModel.getCachedGuilds());

        clmGuildName.setCellValueFactory(g -> g.getValue().getNameProperty());
        clmGuildDescription.setCellValueFactory(g -> g.getValue().getDescriptionProperty());
        clmGM.setCellValueFactory(gm -> {
            if (gm.getValue().getGuildManager() != null) {
                return gm.getValue().getGuildManager().getFullNameProperty();
            }
            return new SimpleStringProperty("");
        });
    }

    @FXML
    private void handleSelectGuild(MouseEvent event) {

        selectedGuild = tableGuild.getSelectionModel().getSelectedItem();
        setGuildOptionsVisilibity(true);

        //Enter edit mode
        if (event.getClickCount() == 2) {
            getEditGuildView();
        }
    }

    @FXML
    private void handleEditGuild() {
        getEditGuildView();
    }

    private void getEditGuildView() {
        Stage primStage = (Stage) txtDescription.getScene().getWindow();

        Stage editGuildModal = modalFactory.createNewModal(primStage, EFXMLName.EDIT_GUILD);

        EditGuildViewController controller = modalFactory.getLoader().getController();

        controller.setCurrentGuild(selectedGuild);

        editGuildModal.showAndWait();
        tableGuild.refresh();
    }

    @FXML
    private void handleAddGuild() {
        String name = txtGuildName.getText();
        String description = txtDescription.getText();
        if (name != null && !name.isEmpty() && description != null && !name.isEmpty()) {
            Guild newGuild = new Guild(name, description, false);
            try {
                guildModel.addGuild(newGuild);
                guildModel.addCachedAvailableGuild(newGuild);
                //TODO RKL: Make sure "selectedGuildManager" is sat to null where needed.
                if (selectedGuildManager != null) {
                    guildManagerModel.assignGuildToManager(selectedGuildManager, newGuild);
                    selectedGuildManager = null;
                }
            } catch (DALException ex) {
                ExceptionDisplayer.display(ex);
            }
        } else {
            MTTMainControllerView.getInstance().displaySnackWarning("Skriv venligst et navn på lauget!");

        }
        txtGuildName.setText("");
        txtDescription.setText("");
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
        Alert alert = new AlertFactory().createDeleteAlert();
        alert.showAndWait().ifPresent(type -> {
            //If the first button ("YES") is clicked
            if (type == alert.getButtonTypes().get(0)) {
                try {
                    guildModel.deleteGuild(selectedGuild);
                    guildManagerModel.removeGuildFromManager(selectedGuild.getName());
                } catch (DALException ex) {
                    ExceptionDisplayer.display(ex);
                }
            }
        });

    }

    @FXML
    private void handleSelectGuildManager() {
        if (cmbGuildManager.getSelectionModel().getSelectedItem() != null) {
            selectedGuildManager = cmbGuildManager.getSelectionModel().getSelectedItem();
        }
    }

    /**
     * Makes the model search in the cachedLists
     *
     * @param searchText
     */
    public void handleSearch(String searchText) {
        guildModel.searchGuilds(searchText);
    }

    public void refreshTable() {
        tableGuild.refresh();
    }

}
