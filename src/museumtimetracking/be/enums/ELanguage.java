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

    public static ELanguage getLanguageByString(String language) {
        switch (language) {
            case "DA":
                return DANISH;
            case "ENG":
                return ENGLISH;
            case "DE":
                return GERMAN;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return text;
    }

}
