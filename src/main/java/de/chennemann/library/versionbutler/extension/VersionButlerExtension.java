/**
 * Christoph Hennemann
 * <p>
 * 12.11.2016, Version 1.0
 */
package de.chennemann.library.versionbutler.extension;

import de.chennemann.library.versionbutler.PropertyHandler;

public class VersionButlerExtension {

    private PropertyHandler propertyHandler;
    private String dependentTaskName;

    public VersionButlerExtension(final PropertyHandler propertyHandler) {
        this(propertyHandler, "");
    }

    public VersionButlerExtension(final PropertyHandler propertyHandler, final String dependentTaskName) {
        this.propertyHandler = propertyHandler;
        this.dependentTaskName = dependentTaskName;
    }

    public String getDependentTaskName() {
        return dependentTaskName;
    }

    public void setDependentTaskName(String dependentTaskName) {
        this.dependentTaskName = dependentTaskName;
    }


    /**
     * Evaluate and retrieve the Version Code of the current application.
     * The version Code is the version number of the current release.
     * With every increment of the major, minor or patch version the version code will be increased as well.
     * @return The version code in its string representation.
     */
    public String getVersionCode() {
        return propertyHandler.getVersionCode();
    }

    /**
     *
     * @return
     */
    public String getVersionName() {
        return propertyHandler.getVersionName();
    }

    public String getSemanticVersionName() {
        return propertyHandler.getSemanticVersionName();
    }
}
