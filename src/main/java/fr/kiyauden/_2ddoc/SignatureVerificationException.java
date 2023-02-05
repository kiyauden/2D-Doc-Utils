package fr.kiyauden._2ddoc;

/**
 * Exception when an error occurs while verifying a signature
 */
public class SignatureVerificationException extends Exception {
    SignatureVerificationException(final String message) {
        super(message);
    }

    SignatureVerificationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    SignatureVerificationException(final Throwable cause) {
        super(cause);
    }
}
