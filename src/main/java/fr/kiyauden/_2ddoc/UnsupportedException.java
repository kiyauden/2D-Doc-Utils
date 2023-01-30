package fr.kiyauden._2ddoc;

/**
 * Exception when a 2D-DOC version is not supported
 */
class UnsupportedException extends Exception {
    UnsupportedException(final String message) {
        super(message);
    }
}
