package fr.kiyauden._2ddoc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents the validity of a signature as well as the reason for the validity or invalidity
 */
@RequiredArgsConstructor
public enum SignatureStatus {

    /**
     * The signature is valid
     */
    VALID(true),
    /**
     * The signature has been done, but is not valid
     */
    INVALID(false),
    /**
     * No suitable certificate was found
     */
    NO_CERTIFICATE(false),
    /**
     * A suitable certificate was found, but is not valid anymore
     */
    CERTIFICATE_INVALID(false);

    /**
     * The validity for the signature
     */
    @Getter
    final boolean valid;

}
