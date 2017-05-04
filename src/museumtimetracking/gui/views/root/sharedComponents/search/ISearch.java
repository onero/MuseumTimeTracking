/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.views.root.sharedComponents.search;

/**
 *
 * @author Mathias
 */
public interface ISearch {

    public final String TEXT_FOR_SEARCHBAR = "Søg";

    public abstract void setPromptText();

    public abstract void initializeListenerForTextField();

    public abstract void updateListView(String newValue);

    public abstract void clearSearchBar();
}
