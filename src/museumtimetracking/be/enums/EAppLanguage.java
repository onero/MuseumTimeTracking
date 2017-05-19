/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.be.enums;

public enum EAppLanguage {

    DANISH("da_DK"),
    ENGLISH("eng");

    private final String text;

    private EAppLanguage(final String text) {
        this.text = text;
    }

    public static EAppLanguage getLanguageByString(String language) {
        switch (language) {
            case "DA":
                return DANISH;
            case "ENG":
                return ENGLISH;
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return text;
    }

}
