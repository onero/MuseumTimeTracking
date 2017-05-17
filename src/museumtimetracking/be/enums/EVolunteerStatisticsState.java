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

    BOTH_VOLUNTEER_AND_GUILD_CHOSEN,
    ONLY_VOLUNTEER_CHOSEN,
    ONLY_GUILD_CHOSEN,
    NONE_CHOSEN;

    public static EVolunteerStatisticsState getState(Volunteer volunteer, String guildName) {
        if (volunteer != null && guildName != null) {
            return BOTH_VOLUNTEER_AND_GUILD_CHOSEN;
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
