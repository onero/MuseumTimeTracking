/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import museumtimetracking.be.enums.EFXMLName;

public class ModalFactory {

    private static ModalFactory instance;

    private static FXMLLoader loader;

    public static ModalFactory getInstance() {
        if (instance == null) {
            instance = new ModalFactory();
        }
        return instance;
    }

    /**
     * Create new modal stage
     *
     * @param newPrimStage
     * @param modalView
     * @return modalStage
     */
    public Stage createNewModal(Stage newPrimStage, EFXMLName modalView) {
        Stage primStage = newPrimStage;

        loader = new FXMLLoader(getClass().getResource(modalView.toString()));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ModalFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

        Stage modalStage = new Stage();
        modalStage.setScene(new Scene(root));

        //Create new modal window from FXMLLoader
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(primStage);

        return modalStage;
    }

    public FXMLLoader getLoader() {
        return loader;
    }

}
