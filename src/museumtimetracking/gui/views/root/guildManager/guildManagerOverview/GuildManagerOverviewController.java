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
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import museumtimetracking.be.GM;
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
    private ButtonBar GMOptions;
    @FXML
    private ListView<GM> lstManagers;
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
    @FXML
    private Button btnArchiveManager;
    @FXML
    private TextArea txtDescription;

    private final NodeFactory nodeFactory;

    private GuildManagerModel guildManagerModel;

    private List<TextField> textFields;

    private static final String ADD_GUILD_BUTTON_TEXT = "Tilføj/fjern Laug";
    private static final String EDIT_BUTTON_TEXT = "Rediger";
    private static final String SAVE_BUTTON_TEXT = "Gem";
    private static final String NEW_GUILD_MANAGER_TEXT = "Ny Tovholder";
    private static final String CANCEL_BUTTON_TEXT = "Annuller";
    private static final String DELETE_BUTTON_TEXT = "Slet";

    private final ModalFactory modalFactory;

    private Set<String> setGuildsToAdd;
    private Set<String> setGuildsToDelete;

    public GuildManagerOverviewController() {
        modalFactory = ModalFactory.getInstance();
        nodeFactory = NodeFactory.getInstance();
        guildManagerModel = null;
        try {
            guildManagerModel = GuildManagerModel.getInstance();
        } catch (DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
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

        setGMOptionsVisibility(false);
    }

    /**
     * Set the visibility of the GM options buttons
     *
     * @param shown
     */
    private void setGMOptionsVisibility(boolean shown) {
        GMOptions.setDisable(!shown);
        GMOptions.setVisible(shown);
    }

    @FXML
    private void handleNewManagerButton() {
        if (btnNewGuildManager.getText().equals(NEW_GUILD_MANAGER_TEXT)) {
            newManagerModal();
        } else if (btnNewGuildManager.getText().equals(ADD_GUILD_BUTTON_TEXT)) {
            showGuildManagementModal();
        }
    }

    /**
     * Checks which button name is set, and handles the correlating functions.
     */
    @FXML
    private void handleEditButton() {
        GM manager = lstManagers.getSelectionModel().getSelectedItem();
        if (btnEdit.getText().equals(EDIT_BUTTON_TEXT)) {
            setShowEditability(true);
            setButtonTextToEditMode();

        } else if (btnEdit.getText().equals(SAVE_BUTTON_TEXT)) {
            saveInformation(manager);
        }
        lstManagers.refresh();
    }

    private void saveInformation(GM manager) {
        setButtonTextToViewMode();
        setShowEditability(false);
        updateInformation(manager);
        try {
            guildManagerModel.updateGuildManager(manager, setGuildsToAdd, setGuildsToDelete);
            setSetsToNull();
        } catch (DALException ex) {
            ExceptionDisplayer.display(ex);
        }
    }

    /**
     * Sends the selected GuildManager through the layers to delete it and
     * returns the buttons to view mode.
     */
    @FXML
    private void handleDeleteButton() {
        GM managerToDelete = lstManagers.getSelectionModel().getSelectedItem();
        if (btnDelete.getText().equals(DELETE_BUTTON_TEXT)) {
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
        } else if (btnDelete.getText().equals(CANCEL_BUTTON_TEXT)) {
            setShowEditability(false);
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
        lstManagers.setCellFactory(m -> new ListCell<GM>() {
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
        lstManagers.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends GM> observable, GM oldValue, GM newValue) -> {
            if (newValue != oldValue && newValue != null) {
                displayInformation(newValue);

                setGMOptionsVisibility(true);
            }
        });
    }

    /**
     * Displayes the information of the given GuildManager.
     *
     * @param manager
     */
    private void displayInformation(GM manager) {
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
        lstGuilds.setEditable(false);
        for (TextField textField : textFields) {
            textField.setDisable(!shown);
        }
        if (!shown) {
            setColor("black");
        } else {
            setColor("#c18100");
        }
    }

    /**
     * Sets the color for the textFields.
     *
     * @param color
     */
    private void setColor(String color) {
        for (TextField textField : textFields) {
            textField.setStyle("-fx-text-fill: " + color + ";");
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
        btnDelete.setText(CANCEL_BUTTON_TEXT);
        btnEdit.setText(SAVE_BUTTON_TEXT);
        lstManagers.setDisable(true);
        btnArchiveManager.setDisable(true);
        btnArchiveManager.setVisible(false);

    }

    /**
     * Sets the buttons' text to the Strings from the constants. View mode is
     * meant only for viewing.
     */
    private void setButtonTextToViewMode() {
        btnNewGuildManager.setText(NEW_GUILD_MANAGER_TEXT);
        btnDelete.setText(DELETE_BUTTON_TEXT);
        btnEdit.setText(EDIT_BUTTON_TEXT);
        lstManagers.setDisable(false);
        btnArchiveManager.setDisable(false);
        btnArchiveManager.setVisible(true);
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

        GM manager = lstManagers.getSelectionModel().getSelectedItem();
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
    private void updateInformation(GM manager) {
        manager.setFirstName(txtFirstName.getText());
        manager.setLastName(txtLastName.getText());
        manager.updateFullName();
        manager.setEmail(txtEmail.getText());
        manager.setPhone(Integer.parseInt(txtPhone.getText()));
    }

    @FXML
    private void handleArchiveGM() {
        GM selectedManager = lstManagers.getSelectionModel().getSelectedItem();
        if (selectedManager != null) {
            try {
                guildManagerModel.updateIdleManager(selectedManager, true);
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
        guildManagerModel.searchActiveManagers(searchText);
    }

    @FXML
    private void handleSelectGM(MouseEvent event) {
        if (event.getClickCount() == 2) {
            setShowEditability(true);
            setButtonTextToEditMode();
        }
    }
}
