package fr.kiyauden._2ddoc;

/**
 * Exception when a 2D-DOC version is not supported
 */
public class UnsupportedVersionException extends UnsupportedException {
    UnsupportedVersionException(final String message) {
        super(message);
    }
}
