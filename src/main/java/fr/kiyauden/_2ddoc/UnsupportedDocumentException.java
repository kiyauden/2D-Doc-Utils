package fr.kiyauden._2ddoc;

/**
 * Exception when a 2D-DOC document is not supported
 */
public class UnsupportedDocumentException extends UnsupportedException {
    UnsupportedDocumentException(final String message) {
        super(message);
    }
}
