package fr.kiyauden._2ddoc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.InvalidKeyException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

import static fr.kiyauden._2ddoc.SignatureStatus.CERTIFICATE_INVALID;
import static fr.kiyauden._2ddoc.SignatureStatus.INVALID;
import static fr.kiyauden._2ddoc.SignatureStatus.NO_CERTIFICATE;
import static fr.kiyauden._2ddoc.SignatureStatus.VALID;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignatureServiceTest {

    @Mock(answer = RETURNS_DEEP_STUBS)
    private X509Certificate certificate1;
    @Mock(answer = RETURNS_DEEP_STUBS)
    private X509Certificate certificate2;
    @Mock
    private Signature signatureMock;

    private ISignatureService signatureService;

    @BeforeEach
    void beforeEach() {
        signatureService = new SignatureService();
    }

    @Test
    void verifySignature_whenCertificateFoundAndStillValid_shouldReturnVALID()
            throws SignatureVerificationException, SignatureException, InvalidKeyException {

        when(certificate1.getIssuerDN().getName())
                .thenReturn("CN=FR00,OU=0002 00000000000000,O=AC DE TEST,C=FR");
        when(certificate1.getSubjectDN().getName())
                .thenReturn("CN=0001,OU=0002 00000000000000,O=CERTIFICAT DE TEST,C=FR");

        when(certificate2.getIssuerDN().getName())
                .thenReturn("CN=FR29,OU=0002 00000000000000,O=AC DE TEST,C=FR");

        final String header = "DC04FR000001198519D31201FR"; // Should use certificate 1
        final String data = "data";
        final String signature = "signature";

        final SignatureStatus signatureStatus;
        try (final MockedStatic<Signature> signatureMockedStatic = mockStatic(Signature.class)) {
            signatureMockedStatic.when(() -> Signature.getInstance(anyString())).thenReturn(signatureMock);
            when(signatureMock.verify(any())).thenReturn(true);

            signatureStatus = signatureService.verifySignature(header + data, signature,
                                                               "FR00", "0001",
                                                               asList(certificate2, certificate1));

            // Checks that the correct certificate is used
            verify(signatureMock, times(1)).initVerify(certificate1);
            verify(signatureMock, never()).initVerify(certificate2);
        }

        assertEquals(VALID, signatureStatus);
        assertTrue(signatureStatus.isValid());
    }

    @Test
    void verifySignature_whenCertificateFoundAndStillValid_butSignatureInvalid_shouldReturnInvalid()
            throws SignatureVerificationException, SignatureException {

        when(certificate1.getIssuerDN().getName())
                .thenReturn("CN=FR00,OU=0002 00000000000000,O=AC DE TEST,C=FR");
        when(certificate1.getSubjectDN().getName())
                .thenReturn("CN=0001,OU=0002 00000000000000,O=CERTIFICAT DE TEST,C=FR");

        when(certificate2.getIssuerDN().getName())
                .thenReturn("CN=FR29,OU=0002 00000000000000,O=AC DE TEST,C=FR");

        final String header = "DC04FR000001198519D31201FR"; // Should use certificate 1
        final String data = "data";
        final String signature = "invalid signature";

        final SignatureStatus signatureStatus;
        try (final MockedStatic<Signature> signatureMockedStatic = mockStatic(Signature.class)) {
            signatureMockedStatic.when(() -> Signature.getInstance(anyString())).thenReturn(signatureMock);
            when(signatureMock.verify(any())).thenReturn(false);

            signatureStatus = signatureService.verifySignature(header + data, signature,
                                                               "FR00", "0001",
                                                               asList(certificate2, certificate1));
        }

        assertEquals(INVALID, signatureStatus);
        assertFalse(signatureStatus.isValid());
    }

    @Test
    void verifySignature_whenCertificateFoundButNotValidAnymore_shouldReturnInvalid()
            throws SignatureVerificationException, CertificateNotYetValidException,
            CertificateExpiredException {

        when(certificate1.getIssuerDN().getName())
                .thenReturn("CN=FR00,OU=0002 00000000000000,O=AC DE TEST,C=FR");
        when(certificate1.getSubjectDN().getName())
                .thenReturn("CN=0001,OU=0002 00000000000000,O=CERTIFICAT DE TEST,C=FR");

        when(certificate2.getIssuerDN().getName())
                .thenReturn("CN=FR29,OU=0002 00000000000000,O=AC DE TEST,C=FR");

        final String header = "DC04FR000001198519D31201FR"; // Should use certificate 1
        final String data = "data";
        final String signature = "signature";

        // The certificate is not valid anymore
        doThrow(new CertificateExpiredException()).when(certificate1).checkValidity();

        final SignatureStatus signatureStatus =
                signatureService.verifySignature(header + data, signature,
                                                 "FR00", "0001",
                                                 asList(certificate2, certificate1));

        assertEquals(CERTIFICATE_INVALID, signatureStatus);
        assertFalse(signatureStatus.isValid());
    }

    @Test
    void verifySignature_whenNoCertificateIsFound_shouldReturnNO_CERTIFICATE() throws SignatureVerificationException {
        final String header = "DC04FR0AXT4A0E840E8A0101FR";
        final String data = "data";
        final String signature = "signature";

        final SignatureStatus signatureStatus =
                signatureService.verifySignature(header + data, signature,
                                                 "FR0A", "AXT4",
                                                 emptyList());
        assertEquals(NO_CERTIFICATE, signatureStatus);
        assertFalse(signatureStatus.isValid());
    }

}
