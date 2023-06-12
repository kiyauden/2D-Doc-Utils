package fr.kiyauden._2ddoc;

/**
 * Exception when a 2D-DOC is not supported
 */
public class UnsupportedException extends Exception {
    UnsupportedException(final String message) {
        super(message);
    }
}
