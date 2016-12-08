/**
 * Christoph Hennemann
 * <p>
 * 12.11.2016, Version 1.0
 */
package de.chennemann.library.versionbutler;

import de.chennemann.library.versionbutler.util.StreamUtility;
import nu.studer.java.util.OrderedProperties;

import java.io.*;

public class PropertyHandler {

    private final String projectFilePath;
    private OrderedProperties properties = null;
    private String versionFilePath = null;

    public PropertyHandler(String projectFilePath) {
        this.projectFilePath = projectFilePath;
    }

    public void incrementVersion(String incrementKey) {

        final OrderedProperties properties = getProperties();
        int keyVersionValue = getVersionKeyValue(properties, incrementKey);
        setVersionKeyValue(properties, incrementKey, ++keyVersionValue);

        if (!incrementKey.equals(KEYS.BUILD_NUMBER_KEY)) {
            int versionCodeValue = getVersionKeyValue(properties, KEYS.VERSION_CODE_KEY);
            setVersionKeyValue(properties, KEYS.VERSION_CODE_KEY, ++versionCodeValue);
        }

        saveProperties(properties);

    }

    private OrderedProperties getProperties() {
        if (this.properties == null)
            this.properties = loadProperties();
        return this.properties;
    }

    private OrderedProperties loadProperties() {

        final String versionFilePath = evaluateCompleteFilePath();

        final OrderedProperties properties = new OrderedProperties();
        InputStream versionFileInputStream = null;
        try {

            final File versionFile = new File(versionFilePath);

            if (!versionFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                versionFile.createNewFile();
            }

            versionFileInputStream = new FileInputStream(versionFile);
            properties.load(versionFileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            StreamUtility.silentlyClose(versionFileInputStream);
        }

        if (!properties.containsProperty(KEYS.VERSION_CODE_KEY))
            createDefaultProperties(properties);

        return properties;

    }

    private void createDefaultProperties(OrderedProperties properties) {

        properties.setProperty(KEYS.MAJOR_VERSION_KEY, "0");
        properties.setProperty(KEYS.MINOR_VERSION_KEY, "0");
        properties.setProperty(KEYS.PATCH_VERSION_KEY, "0");
        properties.setProperty(KEYS.VERSION_CODE_KEY, "1");
        properties.setProperty(KEYS.BUILD_NUMBER_KEY, "0");
        properties.setProperty(KEYS.VERSION_SUFFIX_KEY, "");
        properties.setProperty(KEYS.VERSION_NAME_PATTERN_KEY, "v(major).(minor).(patch)-(suffix)");

        saveProperties(properties);

    }

    private void saveProperties(OrderedProperties properties) {
        OutputStream outputStream = null;
        try {
            outputStream = optainPropertiesOutputStream();
            properties.store(outputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            StreamUtility.silentlyClose(outputStream);
        }
    }

    private OutputStream optainPropertiesOutputStream() throws FileNotFoundException {
        return new FileOutputStream(evaluateCompleteFilePath());
    }

    private String evaluateCompleteFilePath() {

        if (this.versionFilePath == null) {
            if (projectFilePath != null && !projectFilePath.isEmpty()) {
                final StringBuilder filePathBuilder = new StringBuilder(projectFilePath);

                if (new File(projectFilePath).isDirectory()) {
                    filePathBuilder.append("\\versionbutler.properties");
                }

                this.versionFilePath = filePathBuilder.toString();
            }
        }

        return this.versionFilePath;
    }

    private int getVersionKeyValue(OrderedProperties properties, String key) {
        return Integer.parseInt(getKeyValue(properties, key));
    }

    private void setVersionKeyValue(OrderedProperties properties, String incrementKey, int incrementedVersion) {
        setKeyValue(properties, incrementKey, String.valueOf(incrementedVersion));
    }

    private String getKeyValue(OrderedProperties properties, String key) {
        return properties.getProperty(key);
    }

    private void setKeyValue(OrderedProperties properties, String incrementKey, String updatedKeyValue) {
        properties.setProperty(incrementKey, updatedKeyValue);
    }

    public String getVersionCode() {
        final OrderedProperties properties = getProperties();
        return String.valueOf(getVersionKeyValue(properties, KEYS.VERSION_CODE_KEY));
    }

    public String getSemanticVersionName() {
        final OrderedProperties properties = getProperties();
        final int majorVersion = getVersionKeyValue(properties, KEYS.MAJOR_VERSION_KEY);
        final int minorVersion = getVersionKeyValue(properties, KEYS.MINOR_VERSION_KEY);
        final int patchVersion = getVersionKeyValue(properties, KEYS.PATCH_VERSION_KEY);
        return String.format("%d.%d.%d", majorVersion, minorVersion, patchVersion);
    }

    public String getVersionName() {

        final OrderedProperties properties = getProperties();
        final String versionNamePattern = getNamePattern(properties);
        final int majorVersion = getVersionKeyValue(properties, KEYS.MAJOR_VERSION_KEY);
        final int minorVersion = getVersionKeyValue(properties, KEYS.MINOR_VERSION_KEY);
        final int patchVersion = getVersionKeyValue(properties, KEYS.PATCH_VERSION_KEY);
        final int buildNumber = getVersionKeyValue(properties, KEYS.BUILD_NUMBER_KEY);
        final String suffix = getKeyValue(properties, KEYS.VERSION_SUFFIX_KEY);

        return versionNamePattern
                .replaceAll("\\(([major)]+)\\)", String.valueOf(majorVersion))
                .replaceAll("\\(([minor)]+)\\)", String.valueOf(minorVersion))
                .replaceAll("\\(([patch)]+)\\)", String.valueOf(patchVersion))
                .replaceAll("\\(([build)]+)\\)", String.valueOf(buildNumber))
                .replaceAll("\\(([suffix)]+)\\)", suffix);
    }

    private String getNamePattern(OrderedProperties properties) {
        return properties.getProperty(KEYS.VERSION_NAME_PATTERN_KEY);
    }

    public static class KEYS {
        public static final String MAJOR_VERSION_KEY = "MAJOR_VERSION";
        public static final String MINOR_VERSION_KEY = "MINOR_VERSION";
        public static final String PATCH_VERSION_KEY = "PATCH_VERSION";
        public static final String VERSION_CODE_KEY = "VERSION_CODE";
        public static final String BUILD_NUMBER_KEY = "BUILD_NUMBER";
        public static final String VERSION_SUFFIX_KEY = "VERSION_SUFFIX_KEY";
        public static final String VERSION_NAME_PATTERN_KEY = "VERSION_NAME_PATTERN";
    }
}
