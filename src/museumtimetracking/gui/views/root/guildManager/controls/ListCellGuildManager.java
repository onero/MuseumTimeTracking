/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.guildManager.controls;

import javafx.scene.Node;
import javafx.scene.control.ListCell;
import museumtimetracking.be.GuildManager;

/**
 *
 * @author Rasmus
 */
public class ListCellGuildManager extends ListCell<GuildManager> {

    private Node view;
    private GuildManagerListCellViewController controller;

    @Override
    protected void updateItem(GuildManager item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            controller.bindName(item.getFullNameProperty());
            setGraphic(view);
        }
    }

    /**
     * Sets the controller for this ListCell.
     *
     * @param controller
     */
    public void setController(GuildManagerListCellViewController controller) {
        this.controller = controller;
    }

    /**
     * Sets the view to display this ListCell.
     *
     * @param view
     */
    public void setView(Node view) {
        this.view = view;
    }

}
