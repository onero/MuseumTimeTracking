/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.guildManager.guildManagerOverview;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import museumtimetracking.be.GuildManager;
import static museumtimetracking.be.enums.EFXMLName.*;
import museumtimetracking.gui.model.GuildManagerModel;
import museumtimetracking.gui.views.NodeFactory;

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

    private final NodeFactory nodeFactory;

    private final GuildManagerModel guildManagerModel;

    private List<TextField> textFields;

    private final String ADD_GUILD_BUTTON_TEXT = "Tilføj Laug";
    private final String DELETE_GUILD_BUTTON_GUILD = "Slet Laug";
    private final String EDIT_BUTTON_TEXT = "Rediger";
    private final String SAVE_BUTTON_TEXT = "Gem";
    private final String CANCEL_BUTTON_TEXT = "Anuller";
    private final String NEW_GUILD_MANAGER = "Ny Tovholder";
    private final String DELETE_GUILDMANAGER = "Slet";

    @FXML
    private Button btnEdit;
    @FXML
    private Button btnNewGuildManager;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnSave;

    public GuildManagerOverviewController() {
        nodeFactory = NodeFactory.getInstance();
        guildManagerModel = GuildManagerModel.getInstance();
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
        newManagerModal();
    }

    /**
     * Checks which button name is set, and handles the correlating functions.
     */
    @FXML
    private void handleEditButton() {
        if (btnEdit.getText().equals(EDIT_BUTTON_TEXT)) {
            setShowEditability(true);
            setButtonTextToEditMode();

        } else if (btnEdit.getText().equals(CANCEL_BUTTON_TEXT)) {
            setShowEditability(false);
            setButtonTextToViewMode();
        }
    }

    @FXML
    private void handleDeleteButton() {
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

    @FXML
    private void handleAddGuildButton() {
        setButtonTextToViewMode();
        setShowEditability(false);
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
        lstManagers.setCellFactory(v -> new ListCell<GuildManager>() {
            @Override
            protected void updateItem(GuildManager item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.getFullName());
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
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item);
                }
            }

        });
    }

    /**
     * Adds a listener to lstManagers.
     */
    private void addListeners() {
        lstManagers.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends GuildManager> observable, GuildManager oldValue, GuildManager newValue) -> {
            if (newValue != oldValue) {
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
        for (TextField textField : textFields) {
            textField.setDisable(!shown);
        }
        if (!shown) {
            for (TextField textField : textFields) {
                textField.setStyle("-fx-text-fill:#000;");
                textField.setStyle("-fx-opacity: 1.0;");
            }
        } else {
            for (TextField textField : textFields) {
                textField.setStyle("-fx-text-fill:#003996;");
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
        btnDelete.setText(DELETE_GUILD_BUTTON_GUILD);
        btnSave.setDisable(false);
        btnSave.setVisible(true);
        btnEdit.setText(CANCEL_BUTTON_TEXT);
    }

    /**
     * Sets the buttons' text to the Strings from the constants. View mode is
     * meant only for viewing.
     */
    private void setButtonTextToViewMode() {
        btnNewGuildManager.setText(NEW_GUILD_MANAGER);
        btnDelete.setText(DELETE_GUILDMANAGER);
        btnSave.setDisable(true);
        btnSave.setVisible(false);
        btnEdit.setText(EDIT_BUTTON_TEXT);
    }
}
