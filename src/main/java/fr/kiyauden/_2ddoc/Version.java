package fr.kiyauden._2ddoc;

import lombok.Getter;

import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static lombok.AccessLevel.PRIVATE;

/**
 * Enum representing the 2D-DOC versions
 */
public enum Version {
    VERSION_02("02"),
    VERSION_03("03"),
    VERSION_04("04");

    /**
     * String representation of the version
     */
    @Getter(PRIVATE)
    private final String versionString;

    /**
     * Map used to find a version by its ID
     */
    private static final Map<String, Version> BY_VERSION;

    static {
        BY_VERSION = stream(Version.values()).collect(toMap(Version::getVersionString, identity()));
    }

    Version(final String versionString) {
        this.versionString = versionString;
    }

    /**
     * Finds a version by its version string
     *
     * @param version the version string of the version enum to find
     * @return an optional containing the found version or an empty optional if nothing is found
     */
    static Optional<Version> findByVersionString(final String version) {
        return ofNullable(BY_VERSION.get(version));
    }

}
