/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root;

/**
 * An interface for all controllers with some sort of GUI list that needs to be
 * updated by the MTTMaincontroller when the searchbar is altered.
 *
 * @author Mathias
 */
public interface ISearchableController {

    public void handleSearch(String searchText);
}
