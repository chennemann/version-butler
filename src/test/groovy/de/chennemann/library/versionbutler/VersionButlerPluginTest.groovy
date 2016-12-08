package de.chennemann.library.versionbutler

import de.chennemann.library.versionbutler.tasks.IncrementVersionTask
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.BeforeClass
import org.junit.Test

import static org.junit.Assert.assertTrue
/**
 * Christoph Hennemann
 *
 * 12.11.2016, Version 1.0
 *
 *
 */
class VersionButlerPluginTest {

    private static Project project;

    @BeforeClass
    static void setUpPluginTestSuite() {
        project = ProjectBuilder.builder().build();
        project.getPlugins().apply 'version-butler'
    }

    @Test
    void plugin_should_add_increment_major_version_task() {
        assertTrue(project.tasks.findByName("incrementMajorVersion") != null)
        assertTrue(project.tasks.incrementMajorVersion instanceof IncrementVersionTask)
    }

    @Test
    void plugin_should_add_increment_minor_version_task() {
        assertTrue(project.tasks.findByName("incrementMinorVersion") != null)
        assertTrue(project.tasks.incrementMinorVersion instanceof IncrementVersionTask)
    }

    @Test
    void plugin_should_add_increment_patch_version_task() {
        assertTrue(project.tasks.findByName("incrementPatchVersion") != null)
        assertTrue(project.tasks.incrementPatchVersion instanceof IncrementVersionTask)
    }
}
