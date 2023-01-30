package fr.kiyauden._2ddoc;

/**
 * Exception when an error happen during the data extraction
 */
class DataExtractionException extends Exception {
    DataExtractionException(final String message) {
        super(message);
    }

    public DataExtractionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
