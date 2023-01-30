package fr.kiyauden._2ddoc;

/**
 * Exception when an error occurs unexpectedly while parsing a data
 */
class ParsingException extends Exception {
    ParsingException(final String message) {
        super(message);
    }

    ParsingException(final String message, final Throwable cause) {
        super(message, cause);
    }

    ParsingException(final Throwable cause) {
        super(cause);
    }
}
