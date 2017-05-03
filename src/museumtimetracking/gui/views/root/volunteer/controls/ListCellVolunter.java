/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.volunteer.controls;

import javafx.scene.Node;
import javafx.scene.control.ListCell;
import museumtimetracking.be.Volunteer;

/**
 *
 * @author Rasmus
 */
public class ListCellVolunter extends ListCell<Volunteer> {

    private VolunteerListCellViewController controller;
    private Node view;

    @Override
    protected void updateItem(Volunteer item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            controller.bindName(item.getFullNameProperty());
            setGraphic(view);
        }
    }

    /**
     * Sets the controller of the ListCell.
     *
     * @param controller
     */
    public void setController(VolunteerListCellViewController controller) {
        this.controller = controller;
    }

    /**
     * Sets the view of the ListCell.
     *
     * @param view
     */
    public void setView(Node view) {
        this.view = view;
    }

}
