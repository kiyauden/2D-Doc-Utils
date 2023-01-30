package fr.kiyauden._2ddoc;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

import static fr.kiyauden._2ddoc.Version.VERSION_02;
import static lombok.AccessLevel.PRIVATE;

@Value
@Builder(access = PRIVATE)
public class Header {
    /**
     * Version of the header of the 2D-DOC
     */
    Version version;

    /**
     * The certification authority ID
     * <p>
     * Available in :
     * <ul>
     *     <li>{@link Version#VERSION_02}</li>
     *     <li>{@link Version#VERSION_03}</li>
     *     <li>{@link Version#VERSION_04}</li>
     * </ul>
     */
    String certificationAuthorityId;
    /**
     * The certificate ID
     * <p>
     * Available in :
     * <ul>
     *     <li>{@link Version#VERSION_02}</li>
     *     <li>{@link Version#VERSION_03}</li>
     *     <li>{@link Version#VERSION_04}</li>
     * </ul>
     */
    String certificateId;
    /**
     * The emission date of the document
     * <p>
     * Available in :
     * <ul>
     *     <li>{@link Version#VERSION_02}</li>
     *     <li>{@link Version#VERSION_03}</li>
     *     <li>{@link Version#VERSION_04}</li>
     * </ul>
     */
    LocalDate emissionDate;
    /**
     * The creation date of the signature
     * <p>
     * Available in :
     * <ul>
     *     <li>{@link Version#VERSION_02}</li>
     *     <li>{@link Version#VERSION_03}</li>
     *     <li>{@link Version#VERSION_04}</li>
     * </ul>
     */
    LocalDate signatureDate;
    /**
     * The document type provided by the 2D-DOC
     * <p>
     * Available in :
     * <ul>
     *     <li>{@link Version#VERSION_02}</li>
     *     <li>{@link Version#VERSION_03}</li>
     *     <li>{@link Version#VERSION_04}</li>
     * </ul>
     */
    Document documentType;

    /**
     * The perimeter ID
     * <p>
     * Available in :
     * <ul>
     *     <li>{@link Version#VERSION_03}</li>
     *     <li>{@link Version#VERSION_04}</li>
     * </ul>
     */
    String perimeterId;

    /**
     * The issuing country of the document
     * <p>
     * Complies with the <a href="https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2">ISO-3166-Alpha2</a> standard
     * <p>
     * Available in :
     * <ul>
     *     <li>{@link Version#VERSION_04}</li>
     * </ul>
     */
    String issuingCountry;

    /**
     * Creates an instance of the {@link Header} class for a {@link Version#VERSION_02} 2D-DOC
     *
     * @param certificationAuthorityId The certification authority ID
     * @param certificateId            The certificate ID
     * @param emissionDate             The emission date of the document
     * @param signatureDate            The creation date of the signature
     * @param documentType             The document type provided by the 2D-DOC
     * @return an instance of {@link Header} for a {@link Version#VERSION_02} 2D-DOC
     */
    static Header ofVersion02(final String certificationAuthorityId, final String certificateId,
                              final LocalDate emissionDate, final LocalDate signatureDate,
                              final Document documentType) {
        return Header.builder()
                .version(VERSION_02)
                .certificationAuthorityId(certificationAuthorityId)
                .certificateId(certificateId)
                .emissionDate(emissionDate)
                .signatureDate(signatureDate)
                .documentType(documentType)
                .build();
    }

    /**
     * Creates an instance of the {@link Header} class for a {@link Version#VERSION_03} 2D-DOC
     *
     * @param certificationAuthorityId The certification authority ID
     * @param certificateId            The certificate ID
     * @param emissionDate             The emission date of the document
     * @param signatureDate            The creation date of the signature
     * @param documentType             The document type provided by the 2D-DOC
     * @param perimeterId              The perimeter ID
     * @return an instance of {@link Header} for a {@link Version#VERSION_03} 2D-DOC
     */
    static Header ofVersion03(final String certificationAuthorityId, final String certificateId,
                              final LocalDate emissionDate, final LocalDate signatureDate,
                              final Document documentType, final String perimeterId) {
        return Header.builder()
                .version(VERSION_02)
                .certificationAuthorityId(certificationAuthorityId)
                .certificateId(certificateId)
                .emissionDate(emissionDate)
                .signatureDate(signatureDate)
                .documentType(documentType)
                .perimeterId(perimeterId)
                .build();
    }

    /**
     * Creates an instance of the {@link Header} class for a {@link Version#VERSION_04} 2D-DOC
     *
     * @param certificationAuthorityId The certification authority ID
     * @param certificateId            The certificate ID
     * @param emissionDate             The emission date of the document
     * @param signatureDate            The creation date of the signature
     * @param documentType             The document type provided by the 2D-DOC
     * @param perimeterId              The perimeter ID
     * @param issuingCountry           The issuing country of the document
     * @return an instance of {@link Header} for a {@link Version#VERSION_04} 2D-DOC
     */
    static Header ofVersion04(final String certificationAuthorityId, final String certificateId,
                              final LocalDate emissionDate, final LocalDate signatureDate,
                              final Document documentType, final String perimeterId, final String issuingCountry) {
        return Header.builder()
                .version(VERSION_02)
                .certificationAuthorityId(certificationAuthorityId)
                .certificateId(certificateId)
                .emissionDate(emissionDate)
                .signatureDate(signatureDate)
                .documentType(documentType)
                .perimeterId(perimeterId)
                .issuingCountry(issuingCountry)
                .build();
    }
}
