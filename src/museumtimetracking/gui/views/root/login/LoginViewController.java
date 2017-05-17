/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.login;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import museumtimetracking.be.Guild;
import museumtimetracking.gui.views.root.MTTMainControllerView;

/**
 * FXML Controller class
 *
 * @author Skovgaard
 */
public class LoginViewController implements Initializable {

    @FXML
    private ProgressIndicator spinner;
    @FXML
    private Button btnLogin;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label errorMessage;

//    private final LoginModel loginModel;
    private static LoginViewController instance;

    public static LoginViewController getInstance() {
        return instance;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMessage.setText("");
        spinner.setVisible(false);
    }

    

}
