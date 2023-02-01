package fr.kiyauden._2ddoc;

/**
 * Exception when an error happen during the header extraction
 */
class HeaderExtractionException extends Exception {
    HeaderExtractionException(final String message) {
        super(message);
    }

    HeaderExtractionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
