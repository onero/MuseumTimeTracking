/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.login;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import museumtimetracking.gui.model.LoginModel;
import museumtimetracking.gui.model.ModelFacade;
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

    private LoginModel loginModel;

    private static LoginViewController instance;

    public static LoginViewController getInstance() {
        return instance;
    }

    public LoginViewController() {
        loginModel = ModelFacade.getInstance().getLoginModel();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        errorMessage.setText("");
        spinner.setVisible(false);
    }

    @FXML
    private void handleLoginBtn() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        if (!username.isEmpty() && !password.isEmpty()) {
            startLoginProcess(username, password);
        } else if (username.isEmpty()) {
            setErrorMessage("Indtast et brugernavn.");
        } else if (password.isEmpty()) {
            setErrorMessage("Indtast et kodeord.");
        }
    }

    /**
     * Starts by checking if the username exsists, if it does then checks if the
     * password is correct aswell.
     *
     * @param username
     * @param password
     */
    private void startLoginProcess(String username, String password) {
        String wrongPassword = "Hej " + txtUsername.getText() + " kodeordet er forkert.\nPrøv igen.";
        String wrongUsername = txtUsername.getText() + " findes ikke.";

        setLoginMode(true);
        // Checks if username is correct.
        if (loginModel.validUsername(username)) {
            // Checks if password is correct. If it is, then login.
            if (loginModel.validPassword(password)) {
                MTTMainControllerView.getInstance().setAdminMode();
                Stage primStage = (Stage) btnLogin.getScene().getWindow();
                primStage.close();
            } else {
                // Password is wrong.
                denyAccess();
                errorMessage.setText(wrongPassword);
            }
        } else {
            // Username is wrong.
            denyAccess();
            errorMessage.setText(wrongUsername);
        }
    }

    /**
     * Sets the error text.
     *
     * @param errorMessage
     */
    public void setErrorMessage(String errorMessage) {
        setLoginMode(false);
        this.errorMessage.setVisible(true);
        this.errorMessage.setWrapText(true);
        this.errorMessage.setText(errorMessage);
    }

    /**
     * When pressing the loginBtn we get a loading spinner and the loginBtn is
     * being set as disable so the user wont be able to click more than once.
     *
     * @param visible
     */
    private void setLoginMode(boolean visible) {
        spinner.setVisible(visible);
        btnLogin.setDisable(visible);
        errorMessage.setVisible(false);
    }

    private void denyAccess() {
        setLoginMode(false);
        this.errorMessage.setVisible(true);
        //Clears the PasswordField for better usability
        txtPassword.clear();
    }

}
