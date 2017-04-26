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
import javafx.scene.Node;
import museumtimetracking.be.enums.EFXMLName;

public class NodeFactory {

    private static NodeFactory instance;

    public static NodeFactory getInstance() {
        if (instance == null) {
            instance = new NodeFactory();
        }
        return instance;
    }

    /**
     * Create the parsed view and return the Node
     *
     * @param viewName
     * @return
     */
    public Node createNewView(EFXMLName viewName) {
        Node node = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewName.toString()));
        try {
            node = loader.load();
        } catch (IOException ex) {
            System.out.println("Couldn't create " + viewName);
            Logger.getLogger(NodeFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return node;
    }

}
