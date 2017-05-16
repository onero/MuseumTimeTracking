/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.model;

import java.io.IOException;
import java.util.List;
import museumtimetracking.be.Guild;
import museumtimetracking.exception.DALException;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Rasmus
 */
public class GuildModelTest {

    public GuildModelTest() {
    }

    /**
     * Test if a guild is added to the cached archivedList of guilds when
     * archived.
     */
    @Test
    public void testGuildAddedToArchiveGuildList() throws Exception {
        Guild guildToArchive = new Guild("TestArchiveGuild", "This guild is for unitTesting", false);
        GuildModel instance = GuildModel.getInstance();
        List<Guild> listOfArchivedGuilds = instance.getCachedArchivedGuilds();
        //Adds the guild to cachedGuilds, so we can move it to archived.
        instance.addGuild(guildToArchive);

        //Checks if the guild is not in archivedList before the action.
        boolean isGuildInArchivedListBeforeAction = listOfArchivedGuilds.contains(guildToArchive);
        instance.archiveGuild(guildToArchive);
        //Checks if the guilds is in the archivedList after the action.
        boolean isGuildInArchivedListAfterAction = listOfArchivedGuilds.contains(guildToArchive);
        //Deletes the guild for good measure.
        instance.deleteGuild(guildToArchive);

        assertNotEquals(isGuildInArchivedListBeforeAction, isGuildInArchivedListAfterAction);
    }

    /**
     * Test if a guild is removed from cachedGuilds when added to archived.
     *
     * @throws IOException
     * @throws DALException
     */
    @Test
    public void testGuildRemovedFromCachedGuildsWhenAddedToArchived() throws IOException, DALException {
        Guild guildToArchive = new Guild("TestRemovedFromCachedWhenArchivedGuild", "This guild is for unitTesting", false);
        GuildModel instance = GuildModel.getInstance();
        List<Guild> listOfCachedGuilds = instance.getCachedGuilds();
        //Adds the guild to chachedGuilds, so we can archive it.
        instance.addGuild(guildToArchive);

        //Checks if the guild is in the list before the action.
        boolean isGuildInListBeforeAction = listOfCachedGuilds.contains(guildToArchive);
        instance.archiveGuild(guildToArchive);
        //Checks if the guild is no longer in the list.
        boolean isGuildInListAfterAction = listOfCachedGuilds.contains(guildToArchive);
        //Deletes the guild for good measure.
        instance.deleteGuild(guildToArchive);

        assertNotEquals(isGuildInListBeforeAction, isGuildInListAfterAction);
    }

    /**
     * Test if a guild is being deleted from cachedGuilds.
     */
    @Test
    public void testDeleteGuildFromCachedGuilds() throws Exception {
        Guild deleteGuild = new Guild("TestDeleteGuildFromCached", "This guild is for unitTesting", false);
        GuildModel instance = GuildModel.getInstance();
        List<Guild> listOfGuilds = instance.getCachedGuilds();
        //Adds the guild  to the list, so we are able to remove it.
        instance.addGuild(deleteGuild);

        //Makes sure the guild is in the list before the action.
        boolean guildIsInListBefore = listOfGuilds.contains(deleteGuild);
        instance.deleteGuild(deleteGuild);
        //Checks if the guild is still there after the action.
        boolean guildsIsInListAfter = listOfGuilds.contains(deleteGuild);

        assertNotEquals(guildIsInListBefore, guildsIsInListAfter);
    }

    /**
     * Test if a guild is being deleted from the cachedAvailableGuilds.
     *
     * @throws IOException
     * @throws DALException
     */
    @Test
    public void testDeleteGuildFromAvailableGuilds() throws IOException, DALException {
        Guild deleteGuild = new Guild("TestDeleteGuildFromAvailable", "This guild is for unitTesting", false);
        GuildModel instance = GuildModel.getInstance();
        List<Guild> listOfGuilds = instance.getCachedAvailableGuilds();
        //Adds the guild  to the list, so we are able to remove it.
        instance.addCachedAvailableGuild(deleteGuild);

        //Makes sure the guild is in the list before the action.
        boolean guildIsInListBefore = listOfGuilds.contains(deleteGuild);
        instance.deleteGuild(deleteGuild);
        //Checks if the guild is still there after the action.
        boolean guildsIsInListAfter = listOfGuilds.contains(deleteGuild);

        assertNotEquals(guildIsInListBefore, guildsIsInListAfter);
    }

    /**
     * Test if a guild is being deleted from cachedArchivedGuilds.
     *
     * @throws IOException
     * @throws DALException
     */
    @Test
    public void testDeleteGuildFromArchivedGuilds() throws IOException, DALException {
        Guild deleteGuild = new Guild("TestDeleteGuildFromArchived", "This guild is for unitTesting", false);
        GuildModel instance = GuildModel.getInstance();
        List<Guild> listOfGuilds = instance.getCachedArchivedGuilds();
        //Adds the guild  to the list, so we are able to remove it.
        instance.archiveGuild(deleteGuild);

        //Makes sure the guild is in the list before the action.
        boolean guildIsInListBefore = listOfGuilds.contains(deleteGuild);
        instance.deleteGuild(deleteGuild);
        //Checks if the guild is still there after the action.
        boolean guildsIsInListAfter = listOfGuilds.contains(deleteGuild);

        assertNotEquals(guildIsInListBefore, guildsIsInListAfter);
    }

    /**
     * Test of addGuild method, of class GuildModel.
     */
    @Test
    public void testAddGuild() throws Exception {
        Guild guild = new Guild("TestAddGuild", "This guild is for unitTesting", false);
        GuildModel instance = GuildModel.getInstance();
        List<Guild> listOfGuilds = instance.getCachedGuilds();

        //Check if the guild is not in the list before the action.
        boolean guildIsInListBefore = listOfGuilds.contains(guild);
        instance.addGuild(guild);
        //Check if the guilds is in the list after the action.
        boolean guildIsInListAfter = listOfGuilds.contains(guild);
        //Deletes the guild from the list again for good measure.
        instance.deleteGuild(guild);

        assertNotEquals(guildIsInListBefore, guildIsInListAfter);
    }

    /**
     * Test if a guild is removed from the archivedList when restoreGuild() is
     * called.
     */
    @Test
    public void testGuildRemovedFromArchivedWhenRestoreGuild() throws Exception {
        Guild guildToRestore = new Guild("TestRestoreFromArchived", "This guild is for unitTesting", false);
        GuildModel instance = GuildModel.getInstance();
        List<Guild> listOfArchivedGuilds = instance.getCachedArchivedGuilds();
        //Adds the guild to archived, to see if it gets removed.
        instance.archiveGuild(guildToRestore);

        //Checks if the guild is in the list before the action.
        boolean isGuildInListBeforeAction = listOfArchivedGuilds.contains(guildToRestore);
        instance.restoreGuild(guildToRestore);
        //Checks if the guild is in the list after the action.
        boolean isGuildInListAfterAction = listOfArchivedGuilds.contains(guildToRestore);
        //Deletes the guild for good measure.
        instance.deleteGuild(guildToRestore);

        assertNotEquals(isGuildInListBeforeAction, isGuildInListAfterAction);
    }

    /**
     * Test if a guild is added to cahcedGuild when restoreGuild() is called.
     *
     * @throws IOException
     * @throws DALException
     */
    @Test
    public void testGuildIsAddedToCachedWhenRestoreGuild() throws IOException, DALException {
        Guild guildToRestore = new Guild("TestRestoreToCached", "This guild is for unitTesting", false);
        GuildModel instance = GuildModel.getInstance();
        List<Guild> listOfCachedGuilds = instance.getCachedGuilds();
        //Adds the guild to archived, so we can restore it.
        instance.archiveGuild(guildToRestore);

        //Checks if the guild is in the list before the action.
        boolean isGuildInListBeforeAction = listOfCachedGuilds.contains(guildToRestore);
        instance.restoreGuild(guildToRestore);
        //Checks if the guild is in the list after the action.
        boolean isGuildInListAfterAction = listOfCachedGuilds.contains(guildToRestore);
        //Deletes the guild for good measure.
        instance.deleteGuild(guildToRestore);

        assertNotEquals(isGuildInListBeforeAction, isGuildInListAfterAction);
    }

    /**
     * Test of addCachedAvailableGuild method, of class GuildModel.
     */
    @Test
    public void testAddCachedAvailableGuild() throws IOException, DALException {
        Guild guildToAdd = new Guild("TestGuildAddedToCachedAvailableGuilds", "This guild is for unitTesting", false);
        GuildModel instance = GuildModel.getInstance();
        List<Guild> listOfAvailableGuilds = instance.getCachedAvailableGuilds();

        //Checks if the guild is not in the list before.
        boolean isGuildInListBeforeAction = listOfAvailableGuilds.contains(guildToAdd);
        instance.addCachedAvailableGuild(guildToAdd);
        //Checks if the guild is in the list after.
        boolean isGuildInListAfterACtion = listOfAvailableGuilds.contains(guildToAdd);
        //Deletes the guild for good measure.
        instance.deleteGuild(guildToAdd);

        assertNotEquals(isGuildInListBeforeAction, isGuildInListAfterACtion);

    }

    /**
     * Test of removeCachedAvailableGuild method, of class GuildModel.
     */
    @Test
    public void testRemoveCachedAvailableGuild() throws IOException, DALException {
        Guild guildToRemove = new Guild("GuildToRemoveFromAvailable", "This guild is for unitTesting", false);
        GuildModel instance = GuildModel.getInstance();
        List<Guild> listOfAvailableGuilds = instance.getCachedAvailableGuilds();
        //Adds the guild to the list, so we can remove it.
        instance.addCachedAvailableGuild(guildToRemove);

        //Checks if the guild is in the list before the action.
        boolean isGuildInListBefore = listOfAvailableGuilds.contains(guildToRemove);
        instance.removeCachedAvailableGuild(guildToRemove);
        //Checks if the guild is not in the list after the action.
        boolean isGuildInListAfter = listOfAvailableGuilds.contains(guildToRemove);

        assertNotEquals(isGuildInListBefore, isGuildInListAfter);
    }
}
