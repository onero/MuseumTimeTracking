/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.be.enums;

import museumtimetracking.be.Volunteer;

/**
 *
 * @author Mathias
 */
public enum EVolunteerStatisticsState {

    BOTH_VOLUNTEER_AND_GUILD_CHOSEN_ON_VOLUNTEER_COMBO,
    BOTH_VOLUNTEER_AND_GUILD_CHOSEN_ON_GUILD_COMBO,
    ONLY_VOLUNTEER_CHOSEN,
    ONLY_GUILD_CHOSEN,
    NONE_CHOSEN;

    //TODO MSP: Documentation! :D
    /**
     *
     * @param volunteer
     * @param guildName
     * @return
     */
    public static EVolunteerStatisticsState getState(Volunteer volunteer, String guildName, String id) {
        if (volunteer != null && guildName != null && id.equals("volunteer")) {
            return BOTH_VOLUNTEER_AND_GUILD_CHOSEN_ON_VOLUNTEER_COMBO;
        } else if (volunteer != null && guildName != null && id.equals("guild")) {
            return BOTH_VOLUNTEER_AND_GUILD_CHOSEN_ON_GUILD_COMBO;
        } else if (volunteer != null && guildName == null) {
            return ONLY_VOLUNTEER_CHOSEN;
        } else if (volunteer == null && guildName != null) {
            return ONLY_GUILD_CHOSEN;
        } else if (volunteer == null && guildName == null) {
            return NONE_CHOSEN;
        } else {
            return null;
        }
    }

}
