/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.be.enums;

public enum ELanguage {

    DANISH("DA"),
    ENGLISH("ENG"),
    GERMAN("DE");

    private final String text;

    private ELanguage(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
