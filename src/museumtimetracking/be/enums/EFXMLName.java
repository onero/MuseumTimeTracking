/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.be.enums;

public enum EFXMLName {

    MAIN_FOLER("/museumtimetracking/gui/views/root/"),
    /*
     * Statistic
     */
    STATISTICS_OVERVIEW(MAIN_FOLER + "statistics/StatisticsView.fxml"),
    /*
     * Guild
     */
    ACTIVE_GUILD(MAIN_FOLER + "activeGuilds/GuildOverview.fxml"),
    EDIT_GUILD(MAIN_FOLER + "activeGuilds/editGuild/EditGuildView.fxml"),
    ARCHIVED_GUILD(MAIN_FOLER + "archivedGuilds/ArchivedGuildView.fxml"),
    /*
     * Manager
     */
    MANAGER_OVERVIEW(MAIN_FOLER + "guildManager/guildManagerOverview/GuildManagerOverview.fxml"),
    NEW_MANAGER(MAIN_FOLER + "guildManager/newGuildManager/NewGuildManagerView.fxml"),
    MANAGE_MANAGER_GUILDS(MAIN_FOLER + "guildManager/guildManagerOverview/manageGuildManagerGuilds/ManageGuildManagerGuildsView.fxml"),
    /*
     * Volunteer
     */
    VOLUNTEER_OVERVIEW(MAIN_FOLER + "volunteer/VolunteerOverview.fxml"),
    ADD_NEW_VOLUNTEER(MAIN_FOLER + "volunteer/newVolunteer/NewVolunteerView.fxml"),
    VOLUNTEER_INFO(MAIN_FOLER + "volunteer/volunteerInfo/VolunteerInfoView.fxml"),
    LIST_CELL_VOLUNTEER(MAIN_FOLER + "volunteer/controls/VolunteerListCellView.fxml"),
    ADD_HOURS_VOLUNTEER(MAIN_FOLER + "volunteer/addHours/AddVolunteersHoursView.fxml"),
    /**
     * Idle
     */
    IDLE_OVERVIEW(MAIN_FOLER + "idle/IdleView.fxml");

    private final String text;

    private EFXMLName(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
