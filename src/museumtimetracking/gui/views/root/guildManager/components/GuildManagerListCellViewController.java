/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.guildManager.components;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Rasmus
 */
public class GuildManagerListCellViewController implements Initializable {

    @FXML
    private ImageView imgLogo;
    @FXML
    private Label lblName;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Sets the name label to the parsed String.
     *
     * @param name
     */
    public void bindName(StringProperty name) {
        lblName.textProperty().bind(name);
    }

    /**
     * Unbinds the textProperty for the name label.
     */
    public void unBindName() {
        lblName.textProperty().unbind();
    }

}
