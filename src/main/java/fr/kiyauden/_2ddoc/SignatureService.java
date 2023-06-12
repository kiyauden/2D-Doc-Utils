package fr.kiyauden._2ddoc;

import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base32;
import org.bouncycastle.jcajce.provider.asymmetric.ec.SignatureSpi;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;

import static fr.kiyauden._2ddoc.SignatureStatus.CERTIFICATE_INVALID;
import static fr.kiyauden._2ddoc.SignatureStatus.INVALID;
import static fr.kiyauden._2ddoc.SignatureStatus.NO_CERTIFICATE;
import static fr.kiyauden._2ddoc.SignatureStatus.VALID;
import static java.security.Signature.getInstance;

/**
 * Implementation of {@link ISignatureService}
 */
@Slf4j
@Singleton
public class SignatureService implements ISignatureService {

    /**
     * Algorithm used to verify the 2D-DOC signature
     * <p>
     * Provided by <a href="https://www.bouncycastle.org/">Bouncy Castle</a>
     */
    private static final String SIGNATURE_ALGORITHM = "SHA256withECDSAinP1363Format";

    public SignatureService() {
        initProviders();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SignatureStatus verifySignature(final String headerAndData, final String signature,
                                           final String certificateAuthorityId,
                                           final String certificateId, final List<X509Certificate> certificates)
            throws SignatureVerificationException {

        // Finds the suitable certificate in the list
        final Optional<X509Certificate> certificateOptional = certificates.stream()
                .filter(cer -> cer.getIssuerDN().getName().contains("CN=" + certificateAuthorityId))
                .filter(cer -> cer.getSubjectDN().getName().contains("CN=" + certificateId))
                .findFirst();

        if (!certificateOptional.isPresent()) {
            log.warn("No certificate found for certificate authority \"{}\" and certificate id \"{}\"",
                     certificateAuthorityId, certificateId);
            return NO_CERTIFICATE;
        }

        final X509Certificate certificate = certificateOptional.get();

        // Check if the certificate is still valid
        try {
            certificate.checkValidity();
        } catch (final CertificateExpiredException e) {
            log.warn("A suitable certificate was found but is not valid anymore");
            return CERTIFICATE_INVALID;
        } catch (final CertificateNotYetValidException e) {
            throw new SignatureVerificationException(e);
        }

        // The signature is encoded in base 32, it needs to be decoded
        final Base32 base32 = new Base32();
        final byte[] signatureBytes = base32.decode(signature.getBytes());

        // Actual signature verification
        final boolean valid;
        try {
            final Signature signatureVerification = getInstance(SIGNATURE_ALGORITHM);

            signatureVerification.initVerify(certificate);
            signatureVerification.update(headerAndData.getBytes());

            valid = signatureVerification.verify(signatureBytes);
        } catch (final NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new SignatureVerificationException(e);
        }

        return valid ? VALID : INVALID;
    }

    /**
     * Inits the provider
     * <p>
     * See <a href="https://github.com/bcgit/bc-java/issues/751">https://github.com/bcgit/bc-java/issues/751</a>
     */
    private void initProviders() {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
        Security.insertProviderAt(new BcP1363Provider(), 1);
    }

    /**
     * Custom provider to use {@link SignatureService#SIGNATURE_ALGORITHM} algorithm
     * <p>
     * See <a href="https://github.com/bcgit/bc-java/issues/751">https://github.com/bcgit/bc-java/issues/751</a>
     */
    private static class BcP1363Provider extends Provider {
        public BcP1363Provider() {
            super("BcP1363", 1.0d, "Bouncy Castle - P1363 Bridge");
            put("Signature.SHA256withECDSAinP1363Format", SignatureSpi.ecCVCDSA256.class.getName());
        }
    }

}
