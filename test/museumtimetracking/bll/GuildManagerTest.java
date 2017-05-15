/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.bll;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author gta1
 */
public class GuildManagerTest {

    public GuildManagerTest() {
    }

    @Test
    public void testCalculatePositiveGMROIOnVolunteerForAMonth() {
        int GMhoursADay = 1;
        int GMworkDaysInWeek = 5;
        int GMweeksInMonth = 4;

        int GMWorkHours = GMhoursADay * GMworkDaysInWeek * GMweeksInMonth;

        int volunteerHoursADay = 5;
        int volunteerDaysInWeek = 3;
        int volunteerWeeksInMonth = 3;

        int volunteerHours = volunteerHoursADay * volunteerDaysInWeek * volunteerWeeksInMonth;

        assertTrue(volunteerHours > GMWorkHours);

    }

    @Test
    public void testCalculateNegativeGMROIOnVolunteerForAMonth() {
        int GMhoursADay = 1;
        int GMworkDaysInWeek = 5;
        int GMweeksInMonth = 4;

        int GMWorkHours = GMhoursADay * GMworkDaysInWeek * GMweeksInMonth;

        int volunteerHoursADay = 1;
        int volunteerDaysInWeek = 3;
        int volunteerWeeksInMonth = 3;

        int volunteerHours = volunteerHoursADay * volunteerDaysInWeek * volunteerWeeksInMonth;

        assertTrue(volunteerHours < GMWorkHours);

    }

    @Test
    public void testCalculateEvenGMROIOnVolunteerForAMonth() {
        int GMhoursADay = 1;
        int GMworkDaysInWeek = 5;
        int GMweeksInMonth = 4;

        int GMWorkHours = GMhoursADay * GMworkDaysInWeek * GMweeksInMonth;

        int volunteerHoursADay = 1;
        int volunteerDaysInWeek = 5;
        int volunteerWeeksInMonth = 4;

        int volunteerHours = volunteerHoursADay * volunteerDaysInWeek * volunteerWeeksInMonth;

        assertTrue(volunteerHours == GMWorkHours);

    }

}
