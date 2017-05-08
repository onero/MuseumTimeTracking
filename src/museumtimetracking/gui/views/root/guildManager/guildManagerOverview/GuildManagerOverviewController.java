/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.guildManager.guildManagerOverview;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import museumtimetracking.be.GuildManager;
import static museumtimetracking.be.enums.EFXMLName.*;
import museumtimetracking.exception.AlertFactory;
import museumtimetracking.exception.DALException;
import museumtimetracking.exception.ExceptionDisplayer;
import museumtimetracking.gui.model.GuildManagerModel;
import museumtimetracking.gui.views.ModalFactory;
import museumtimetracking.gui.views.NodeFactory;
import museumtimetracking.gui.views.root.guildManager.guildManagerOverview.manageGuildManagerGuilds.ManageGuildManagerGuildsViewController;

/**
 * FXML Controller class
 *
 * @author Rasmus
 */
public class GuildManagerOverviewController implements Initializable {

    @FXML
    private ListView<GuildManager> lstManagers;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtPhone;
    @FXML
    private ListView<String> lstGuilds;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnNewGuildManager;
    @FXML
    private Button btnDelete;

    private final NodeFactory nodeFactory;

    private GuildManagerModel guildManagerModel;

    private List<TextField> textFields;

    private static final String ADD_GUILD_BUTTON_TEXT = "Tilføj Laug";
    private static final String EDIT_BUTTON_TEXT = "Rediger";
    private static final String SAVE_BUTTON_TEXT = "Gem";
    private static final String NEW_GUILD_MANAGER = "Ny Tovholder";

    private final ModalFactory modalFactory;

    private Set<String> setGuildsToAdd;
    private Set<String> setGuildsToDelete;

    public GuildManagerOverviewController() {
        modalFactory = ModalFactory.getInstance();
        nodeFactory = NodeFactory.getInstance();
        guildManagerModel = null;
        try {
            guildManagerModel = GuildManagerModel.getInstance();
        } catch (IOException | DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        textFields = new ArrayList<>();
        initializeTextFieldList();
        setShowEditability(false);
        setButtonTextToViewMode();
        addListeners();
        setCellFactories();
        lstManagers.setItems(guildManagerModel.getCachedManagers());
    }

    @FXML
    private void handleNewManagerButton() {
        if (btnNewGuildManager.getText().equals(NEW_GUILD_MANAGER)) {
            newManagerModal();
        } else if (btnNewGuildManager.getText().equals(ADD_GUILD_BUTTON_TEXT)) {
            showGuildManagementModal();
        }
    }

    /**
     * Gets the information and sends it down to the database.
     *
     */
    private void handleSaveGuildManagerButton() {
        setButtonTextToViewMode();
        setShowEditability(false);

        GuildManager manager = getNewInformation();
        try {
            guildManagerModel.updateGuildManager(manager, setGuildsToAdd, setGuildsToDelete);
            setSetsToNull();
        } catch (DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    /**
     * Checks which button name is set, and handles the correlating functions.
     */
    @FXML
    private void handleEditButton() {
        GuildManager manager = lstManagers.getSelectionModel().getSelectedItem();
        if (btnEdit.getText().equals(EDIT_BUTTON_TEXT) && manager != null) {
            setShowEditability(true);
            setButtonTextToEditMode();

        } else if (btnEdit.getText().equals(SAVE_BUTTON_TEXT)) {
            setButtonTextToViewMode();
            setShowEditability(false);

            manager = getNewInformation();
            try {
                guildManagerModel.updateGuildManager(manager, setGuildsToAdd, setGuildsToDelete);
                setSetsToNull();
            } catch (DALException ex) {
                ExceptionDisplayer.display(ex);
            }
        }
    }

    /**
     * Sends the selected GuildManager through the layers to delete it and
     * returns the buttons to view mode.
     */
    @FXML
    private void handleDeleteButton() {
        GuildManager managerToDelete = lstManagers.getSelectionModel().getSelectedItem();
        if (managerToDelete != null) {
            Alert deleteAlert = AlertFactory.createDeleteAlert();
            deleteAlert.showAndWait().ifPresent(type -> {
                //If user clicks first button
                if (type == deleteAlert.getButtonTypes().get(0)) {
                    try {
                        guildManagerModel.deleteGuildManager(managerToDelete);
                    } catch (DALException ex) {
                        ExceptionDisplayer.display(ex);
                    }
                }
            });
        }

        setButtonTextToViewMode();
        setSetsToNull();
        lstManagers.refresh();
    }

    /**
     * Opens a modal for the newManagerView.
     */
    private void newManagerModal() {
        Stage primStage = (Stage) txtFirstName.getScene().getWindow();
        Parent newManager = nodeFactory.createNewParent(NEW_MANAGER);

        Stage stage = new Stage();
        stage.setScene(new Scene(newManager));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primStage);

        stage.show();
    }

    /**
     * Calls the different methods that needs to have their cellFactories set.
     */
    private void setCellFactories() {
        setListOfGuildsCellFactory();
        setListOfManagersCellFactory();
    }

    /**
     * Sets the cellFactory of the ManagerList to show the fullName of the
     * Managers.
     */
    private void setListOfManagersCellFactory() {
        lstManagers.setCellFactory(m -> new ListCell<GuildManager>() {
            @Override
            protected void updateItem(GuildManager guildManager, boolean empty) {
                super.updateItem(guildManager, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(guildManager.getFullName());
                }
            }
        });
    }

    /**
     * Sets the cellFactory of the GuildsList to show the name of the guilds.
     */
    private void setListOfGuildsCellFactory() {
        lstGuilds.setCellFactory(g -> new ListCell<String>() {
            @Override
            protected void updateItem(String guildName, boolean empty) {
                super.updateItem(guildName, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(guildName);
                }
            }
        });
    }

    /**
     * Adds a listener to lstManagers.
     */
    private void addListeners() {
        lstManagers.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends GuildManager> observable, GuildManager oldValue, GuildManager newValue) -> {
            if (newValue != oldValue && newValue != null) {
                displayInformation(newValue);
            }
        });
    }

    /**
     * Displayes the information of the given GuildManager.
     *
     * @param manager
     */
    private void displayInformation(GuildManager manager) {
        txtFirstName.setText(manager.getFirstName());
        txtLastName.setText(manager.getLastName());
        txtEmail.setText(manager.getEmail());
        txtPhone.setText(manager.getPhone() + "");
        lstGuilds.setItems(manager.getObservableListOfGuilds());
    }

    /**
     * Sets disable on the textfields if the shown variable is false counterwise
     * if true. Also edits colours and opacity, so that the TextFields are
     * readable.
     *
     * @param shown
     */
    private void setShowEditability(boolean shown) {
        lstGuilds.setDisable(true);
        for (TextField textField : textFields) {
            textField.setDisable(!shown);
        }
        if (!shown) {
            for (TextField textField : textFields) {
            }
        } else {
            for (TextField textField : textFields) {
            }
        }
    }

    /**
     * initializes the lit of the textfields.
     */
    private void initializeTextFieldList() {
        textFields.add(txtEmail);
        textFields.add(txtFirstName);
        textFields.add(txtLastName);
        textFields.add(txtPhone);
    }

    /**
     * Sets the buttons' text to the Strings from the constants. Edit mode is
     * meant for editing GuildMasters.
     */
    private void setButtonTextToEditMode() {
        btnNewGuildManager.setText(ADD_GUILD_BUTTON_TEXT);
        btnDelete.setDisable(false);
        btnDelete.setVisible(true);
        btnEdit.setText(SAVE_BUTTON_TEXT);
        lstManagers.setDisable(true);
    }

    /**
     * Sets the buttons' text to the Strings from the constants. View mode is
     * meant only for viewing.
     */
    private void setButtonTextToViewMode() {
        btnNewGuildManager.setText(NEW_GUILD_MANAGER);
        btnDelete.setDisable(true);
        btnDelete.setVisible(false);
        btnEdit.setText(EDIT_BUTTON_TEXT);
        lstManagers.setDisable(false);
    }

    /**
     * Show the modal for the guildManagament.
     *
     * @throws IOException
     */
    private void showGuildManagementModal() {
        Stage primStage = (Stage) lstGuilds.getScene().getWindow();
        Stage stage = modalFactory.createNewModal(primStage, MANAGE_MANAGER_GUILDS);
        ManageGuildManagerGuildsViewController controller = modalFactory.getLoader().getController();

        GuildManager manager = lstManagers.getSelectionModel().getSelectedItem();
        controller.addGuilds(manager.getListOfGuilds());

        stage.showAndWait();
        //TODO ALH: Clean up this mess!
        setGuildsToAdd = controller.getSetGuildsToAdd();
        setGuildsToDelete = controller.getSetGuildsToDelete();
        lstGuilds.setItems(controller.getManagerGuilds());
    }

    /**
     * Sets the sets to null.
     */
    private void setSetsToNull() {
        setGuildsToAdd = null;
        setGuildsToDelete = null;
    }

    /**
     * Gets all the new information about the manager that has been edited and
     * gives it to him.
     *
     * @return
     */
    private GuildManager getNewInformation() {
        GuildManager manager = lstManagers.getSelectionModel().getSelectedItem();
        manager.setFirstName(txtFirstName.getText());
        manager.setLastName(txtLastName.getText());
        manager.updateFullName();
        manager.setEmail(txtEmail.getText());
        manager.setPhone(Integer.parseInt(txtPhone.getText()));
        return manager;
    }

    @FXML
    private void handleArchiveGM() {
        GuildManager selectedManager = lstManagers.getSelectionModel().getSelectedItem();
        if (selectedManager != null) {
            try {
                guildManagerModel.updateIdleManager(selectedManager, true);
            } catch (DALException ex) {
                ExceptionDisplayer.display(ex);
            }
        }
    }
}
