package fr.kiyauden._2ddoc;

/**
 * Exception when no 2D-DOC is found
 */
class NotFoundException extends Exception {
    NotFoundException(final String message) {
        super(message);
    }
}
