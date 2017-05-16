/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.be.enums;

public enum ETabPaneID {

    STATISTICS,
    GUILD_OVERVIEW,
    ARCHIVED_GUILD,
    GM,
    VOLUNTEER,
    IDLE;

    public static ETabPaneID getLanguageByString(String tabPaneID) {
        switch (tabPaneID) {
            case "statistics":
                return STATISTICS;
            case "guildOverView":
                return GUILD_OVERVIEW;
            case "archivedGuild":
                return ARCHIVED_GUILD;
            case "manager":
                return GM;
            case "volunteer":
                return VOLUNTEER;
            case "idle":
                return IDLE;
            default:
                return null;
        }
    }

}
