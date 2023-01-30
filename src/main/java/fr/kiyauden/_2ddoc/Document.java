package fr.kiyauden._2ddoc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static fr.kiyauden._2ddoc.DataType.BENEFICIARY_ADDRESS_LINE_1;
import static fr.kiyauden._2ddoc.DataType.BENEFICIARY_FIRSTNAME;
import static fr.kiyauden._2ddoc.DataType.BENEFICIARY_LASTNAME;
import static fr.kiyauden._2ddoc.DataType.BENEFICIARY_PHONE_NUMBER;
import static fr.kiyauden._2ddoc.DataType.BENEFICIARY_QUALITY_AND_OR_TITLE;
import static fr.kiyauden._2ddoc.DataType.BENEFIT_SERVICE_POINT_ADDRESS_LINE_2;
import static fr.kiyauden._2ddoc.DataType.BENEFIT_SERVICE_POINT_ADDRESS_LINE_3;
import static fr.kiyauden._2ddoc.DataType.BENEFIT_SERVICE_POINT_ADDRESS_LINE_4;
import static fr.kiyauden._2ddoc.DataType.BENEFIT_SERVICE_POINT_ADDRESS_LINE_5;
import static fr.kiyauden._2ddoc.DataType.BENEFIT_SERVICE_POINT_COUNTRY;
import static fr.kiyauden._2ddoc.DataType.BENEFIT_SERVICE_POINT_LOCALITY_OR_CEDEX_LABEL;
import static fr.kiyauden._2ddoc.DataType.BENEFIT_SERVICE_POINT_POSTAL_OR_CEDEX_CODE;
import static fr.kiyauden._2ddoc.DataType.CLIENT_NUMBER;
import static fr.kiyauden._2ddoc.DataType.CONTRACT_EFFECTIVE_DATE;
import static fr.kiyauden._2ddoc.DataType.CONTRACT_NUMBER;
import static fr.kiyauden._2ddoc.DataType.CO_BENEFICIARY_FIRSTNAME;
import static fr.kiyauden._2ddoc.DataType.CO_BENEFICIARY_LASTNAME;
import static fr.kiyauden._2ddoc.DataType.CO_BENEFICIARY_QUALITY_AND_OR_TITLE;
import static fr.kiyauden._2ddoc.DataType.CO_BENEFICIARY_RECIPIENT_ADDRESS_LINE_1;
import static fr.kiyauden._2ddoc.DataType.CO_INVOICE_RECIPIENT_ADDRESS_LINE_1;
import static fr.kiyauden._2ddoc.DataType.CO_INVOICE_RECIPIENT_FIRSTNAME;
import static fr.kiyauden._2ddoc.DataType.CO_INVOICE_RECIPIENT_LASTNAME;
import static fr.kiyauden._2ddoc.DataType.CO_INVOICE_RECIPIENT_QUALITY_AND_OR_TITLE;
import static fr.kiyauden._2ddoc.DataType.INVOICE_AMOUNT_INCLUDING_TAX;
import static fr.kiyauden._2ddoc.DataType.INVOICE_NUMBER;
import static fr.kiyauden._2ddoc.DataType.INVOICE_RECIPIENT_ADDRESS_LINE_1;
import static fr.kiyauden._2ddoc.DataType.INVOICE_RECIPIENT_ADDRESS_LINE_2;
import static fr.kiyauden._2ddoc.DataType.INVOICE_RECIPIENT_ADDRESS_LINE_3;
import static fr.kiyauden._2ddoc.DataType.INVOICE_RECIPIENT_ADDRESS_LINE_4;
import static fr.kiyauden._2ddoc.DataType.INVOICE_RECIPIENT_ADDRESS_LINE_5;
import static fr.kiyauden._2ddoc.DataType.INVOICE_RECIPIENT_COUNTRY;
import static fr.kiyauden._2ddoc.DataType.INVOICE_RECIPIENT_FIRSTNAME;
import static fr.kiyauden._2ddoc.DataType.INVOICE_RECIPIENT_LASTNAME;
import static fr.kiyauden._2ddoc.DataType.INVOICE_RECIPIENT_LOCALITY_OR_CEDEX_LABEL;
import static fr.kiyauden._2ddoc.DataType.INVOICE_RECIPIENT_PHONE_NUMBER;
import static fr.kiyauden._2ddoc.DataType.INVOICE_RECIPIENT_POSTAL_OR_CEDEX_CODE;
import static fr.kiyauden._2ddoc.DataType.INVOICE_RECIPIENT_QUALITY_AND_OR_TITLE;
import static fr.kiyauden._2ddoc.DataType.SUBSCRIBER_IDENTIFIER;
import static fr.kiyauden._2ddoc.DataType.UNMENTIONED_CO_BENEFICIARY_PRESENCE;
import static fr.kiyauden._2ddoc.DataType.UNMENTIONED_CO_INVOICE_RECIPIENT_PRESENCE;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.unmodifiableList;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

/**
 * The 2D-DOC documents types supported by the library
 */
@RequiredArgsConstructor
public enum Document {

    /**
     * Document 01
     * <p>
     * ID : 01
     * <p>
     * Type for user : Justificatif de domicile
     * <p>
     * Type for emitter : Factures de fournisseur d'énergie | Factures de téléphonie | Factures de fournisseur d'accès internet | Factures de fournisseur d'eau
     */
    DOCUMENT_01("01",
                true,
                unmodifiableList(asList(
                        BENEFICIARY_ADDRESS_LINE_1,
                        BENEFICIARY_QUALITY_AND_OR_TITLE,
                        BENEFICIARY_FIRSTNAME,
                        BENEFICIARY_LASTNAME,
                        BENEFIT_SERVICE_POINT_ADDRESS_LINE_4,
                        BENEFIT_SERVICE_POINT_POSTAL_OR_CEDEX_CODE,
                        BENEFIT_SERVICE_POINT_COUNTRY
                )),
                unmodifiableList(
                        concat(
                                of(INVOICE_RECIPIENT_ADDRESS_LINE_1,
                                   INVOICE_RECIPIENT_QUALITY_AND_OR_TITLE,
                                   INVOICE_RECIPIENT_FIRSTNAME,
                                   INVOICE_RECIPIENT_LASTNAME,
                                   INVOICE_NUMBER,
                                   CLIENT_NUMBER,
                                   CONTRACT_NUMBER,
                                   SUBSCRIBER_IDENTIFIER,
                                   CONTRACT_EFFECTIVE_DATE,
                                   INVOICE_AMOUNT_INCLUDING_TAX,
                                   BENEFICIARY_PHONE_NUMBER,
                                   INVOICE_RECIPIENT_PHONE_NUMBER,
                                   UNMENTIONED_CO_BENEFICIARY_PRESENCE,
                                   UNMENTIONED_CO_INVOICE_RECIPIENT_PRESENCE,
                                   CO_BENEFICIARY_RECIPIENT_ADDRESS_LINE_1,
                                   CO_BENEFICIARY_QUALITY_AND_OR_TITLE,
                                   CO_BENEFICIARY_FIRSTNAME,
                                   CO_BENEFICIARY_LASTNAME,
                                   CO_INVOICE_RECIPIENT_ADDRESS_LINE_1,
                                   CO_INVOICE_RECIPIENT_QUALITY_AND_OR_TITLE,
                                   CO_INVOICE_RECIPIENT_FIRSTNAME,
                                   CO_INVOICE_RECIPIENT_LASTNAME,
                                   BENEFIT_SERVICE_POINT_ADDRESS_LINE_2,
                                   BENEFIT_SERVICE_POINT_ADDRESS_LINE_3,
                                   BENEFIT_SERVICE_POINT_ADDRESS_LINE_5,
                                   BENEFIT_SERVICE_POINT_LOCALITY_OR_CEDEX_LABEL,
                                   INVOICE_RECIPIENT_ADDRESS_LINE_2,
                                   INVOICE_RECIPIENT_ADDRESS_LINE_3,
                                   INVOICE_RECIPIENT_ADDRESS_LINE_4,
                                   INVOICE_RECIPIENT_ADDRESS_LINE_5,
                                   INVOICE_RECIPIENT_POSTAL_OR_CEDEX_CODE,
                                   INVOICE_RECIPIENT_LOCALITY_OR_CEDEX_LABEL,
                                   INVOICE_RECIPIENT_COUNTRY
                                ), DataType.getComplementaryDataTypes().stream())
                                .collect(toList())),
                "Justificatif de domicile",
                "Factures de fournisseur d'énergie | Factures de téléphonie | Factures de fournisseur d'accès internet | Factures de fournisseur d'eau"
    );

    /**
     * Map used to find a document type by its ID
     */
    private static final Map<String, Document> BY_ID;

    static {
        BY_ID = stream(Document.values()).collect(toMap(Document::getId, identity()));
    }

    /**
     * The document type id
     */
    @Getter
    private final String id;
    /**
     * A boolean indicating if the emission data <strong>HAS</strong> to be in the header
     */
    @Getter
    private final boolean emissionDateMandatory;
    /**
     * A list containing the mandatory data types for a document type
     * <p>
     * This list is immutable
     */
    @Getter
    private final List<DataType> mandatoryData;
    /**
     * A list containing the optional data types for a document type
     * <p>
     * This list is immutable
     */
    @Getter
    private final List<DataType> optionalData;
    /**
     * A label indicating what the document is for a user
     */
    @Getter
    private final String typeForUser;
    /**
     * A label indicating what the document is for the transmitter
     */
    @Getter
    private final String typeForTransmitter;

    /**
     * Finds a document type by its ID
     *
     * @param id the ID of the document to find
     * @return an optional containing the found document or an empty optional if nothing is found
     */
    public static Optional<Document> findById(final String id) {
        return ofNullable(BY_ID.get(id));
    }

}

