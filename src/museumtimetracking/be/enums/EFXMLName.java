/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.be.enums;

public enum EFXMLName {

    MAIN_FOLDER("/museumtimetracking/gui/views/root/"),
    /*
     * Statistic
     */
    STATISTICS_OVERVIEW(MAIN_FOLDER + "statistics/StatisticsView.fxml"),
    CHART_GUILD_HOURS_OVERVIEW(MAIN_FOLDER + "statistics/guildHoursOverview/ChartGuildHoursOverview.fxml"),
    ROI_GM_HOURS(MAIN_FOLDER + "statistics/ROIOverview/ROIGmHoursView.fxml"),
    VOLUNTEER_STATISTICS(MAIN_FOLDER + "statistics/VolunteerStatistics/VolunteerStatisticsView.fxml"),
    /*
     * Guild
     */
    ACTIVE_GUILD(MAIN_FOLDER + "activeGuilds/GuildOverview.fxml"),
    EDIT_GUILD(MAIN_FOLDER + "activeGuilds/editGuild/EditGuildView.fxml"),
    ARCHIVED_GUILD(MAIN_FOLDER + "archivedGuilds/ArchivedGuildView.fxml"),
    /*
     * Manager
     */
    MANAGER_OVERVIEW(MAIN_FOLDER + "guildManager/guildManagerOverview/GuildManagerOverview.fxml"),
    NEW_MANAGER(MAIN_FOLDER + "guildManager/newGuildManager/NewGuildManagerView.fxml"),
    MANAGE_MANAGER_GUILDS(MAIN_FOLDER + "guildManager/guildManagerOverview/manageGuildManagerGuilds/ManageGuildManagerGuildsView.fxml"),
    /*
     * Volunteer
     */
    VOLUNTEER_OVERVIEW(MAIN_FOLDER + "volunteer/VolunteerOverview.fxml"),
    ADD_NEW_VOLUNTEER(MAIN_FOLDER + "volunteer/newVolunteer/NewVolunteerView.fxml"),
    VOLUNTEER_INFO(MAIN_FOLDER + "volunteer/volunteerInfo/VolunteerInfoView.fxml"),
    LIST_CELL_VOLUNTEER(MAIN_FOLDER + "volunteer/controls/VolunteerListCellView.fxml"),
    ADD_HOURS_VOLUNTEER(MAIN_FOLDER + "volunteer/addHours/AddVolunteersHoursView.fxml"),
    /**
     * Idle
     */
    IDLE_OVERVIEW(MAIN_FOLDER + "idle/IdleView.fxml");

    private final String text;

    private EFXMLName(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
