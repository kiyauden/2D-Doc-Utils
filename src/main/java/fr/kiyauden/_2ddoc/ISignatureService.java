package fr.kiyauden._2ddoc;

import java.security.cert.X509Certificate;
import java.util.List;

/**
 * Service whose job is to verify the 2D-DOC signature
 */
interface ISignatureService {

    /**
     * Verify the signature
     *
     * @param headerAndData          the header and the data, what was signed
     * @param signature              the signature as extracted from the 2D-DOC
     * @param certificateAuthorityId the certificate authority ID, extracted from the header
     * @param certificateId          the certificate ID, extracted from the header
     * @param certificates           the list for certificates for the signature verification
     * @return the status of the signature
     * @throws SignatureVerificationException when the signature could not be verified
     */
    SignatureStatus verifySignature(String headerAndData, String signature, String certificateAuthorityId,
                                    String certificateId, List<X509Certificate> certificates)
            throws SignatureVerificationException;

}
