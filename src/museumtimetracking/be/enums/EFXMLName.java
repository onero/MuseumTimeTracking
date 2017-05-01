/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.be.enums;

public enum EFXMLName {

    MAIN_FOLER("/museumtimetracking/gui/views/root/"),
    /*
     * Guild
     */
    GUILD_OVERVIEW(MAIN_FOLER + "guild/GuildOverview.fxml"),
    ARCHIVED_TABLE(MAIN_FOLER + "guild/archivedGuilds/ArchivedGuildView.fxml"),
    ADD_NEW_GUILD(MAIN_FOLER + "guild/newGuild/NewGuildView.fxml"),
    EDIT_GUILD(MAIN_FOLER + "guild/editGuild/EditGuildView.fxml"),
    /*
     * Manager
     */
    MANAGER_OVERVIEW(MAIN_FOLER + "guildManager/guildManagerOverview/GuildManagerOverview.fxml"),
    NEW_MANAGER(MAIN_FOLER + "guildManager/newGuildManager/NewGuildManagerView.fxml"),
    /*
     * Volunteer
     */
    VOLUNTEER_OVERVIEW(MAIN_FOLER + "volunteer/VolunteerOverview.fxml"),
    ADD_NEW_VOLUNTEER(MAIN_FOLER + "volunteer/newVolunteer/NewVolunteerView.fxml"),
    VOLUNTEER_INFO(MAIN_FOLER + "volunteer/volunteerInfo/VolunteerInfoView.fxml");

    private final String text;

    private EFXMLName(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
