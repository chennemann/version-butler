/**
 * Christoph Hennemann
 * <p>
 * 12.11.2016, Version 1.0
 */
package de.chennemann.library.versionbutler.tasks;

import de.chennemann.library.versionbutler.PropertyHandler;

public class IncrementMajorVersionTask extends IncrementVersionTask {
    @Override
    protected String getIncrementKey() {
        return PropertyHandler.KEYS.MAJOR_VERSION_KEY;
    }
}
