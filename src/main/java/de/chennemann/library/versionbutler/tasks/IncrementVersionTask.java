/**
 * Christoph Hennemann
 * <p>
 * 12.11.2016, Version 1.0
 */
package de.chennemann.library.versionbutler.tasks;

import de.chennemann.library.versionbutler.VersionButlerPlugin;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

public abstract class IncrementVersionTask extends DefaultTask {

    @TaskAction
    public void increment() {
        final VersionButlerPlugin plugin = getProject().getPlugins().findPlugin(VersionButlerPlugin.class);
        plugin.getPropertyHandler().incrementVersion(getIncrementKey());
    }

    protected abstract String getIncrementKey();

}
