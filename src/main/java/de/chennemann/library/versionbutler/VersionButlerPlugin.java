/**
 * Christoph Hennemann
 * <p>
 * 12.11.2016, Version 1.0
 */
package de.chennemann.library.versionbutler;

import de.chennemann.library.versionbutler.extension.VersionButlerExtension;
import de.chennemann.library.versionbutler.tasks.IncrementBuildNumberTask;
import de.chennemann.library.versionbutler.tasks.IncrementMajorVersionTask;
import de.chennemann.library.versionbutler.tasks.IncrementMinorVersionTask;
import de.chennemann.library.versionbutler.tasks.IncrementPatchVersionTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.TaskContainer;

import java.util.ArrayList;
import java.util.List;

public class VersionButlerPlugin implements Plugin<Project> {

    private PropertyHandler propertyHandler;
    private VersionButlerExtension versionButlerSettings;

    @Override
    public void apply(Project project) {

        propertyHandler = new PropertyHandler(project.getProjectDir().getPath());
        project.getExtensions().create("versionbutler", VersionButlerExtension.class, propertyHandler);

        initializeTasks(project);

        project.afterEvaluate(p -> {
            versionButlerSettings = p.getExtensions().findByType(VersionButlerExtension.class);

            final String dependentTaskName = versionButlerSettings.getDependentTaskName();
            if (dependentTaskName != null && !dependentTaskName.isEmpty()) {
                final Task buildTask = project.getTasks().findByName(dependentTaskName);
                final Task incrementBuildTask = project.getTasks().findByName("incrementBuildNumber");
                if (buildTask != null && incrementBuildTask != null)
                    buildTask.dependsOn(incrementBuildTask);
            }
        });

    }

    private void initializeTasks(Project project) {

        final List<Task> tasks = new ArrayList<>(3);

        final TaskContainer taskContainer = project.getTasks();
        tasks.add(taskContainer.create("incrementMajorVersion", IncrementMajorVersionTask.class));
        tasks.add(taskContainer.create("incrementMinorVersion", IncrementMinorVersionTask.class));
        tasks.add(taskContainer.create("incrementPatchVersion", IncrementPatchVersionTask.class));
        tasks.add(taskContainer.create("incrementBuildNumber", IncrementBuildNumberTask.class));

        tasks.forEach(task -> task.setGroup("versioning"));
    }

    public PropertyHandler getPropertyHandler() {
        return propertyHandler;
    }
}
