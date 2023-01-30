package fr.kiyauden._2ddoc;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static fr.kiyauden._2ddoc.DataFormat.AMOUNT;
import static fr.kiyauden._2ddoc.DataFormat.BOOLEAN;
import static fr.kiyauden._2ddoc.DataFormat.DATE;
import static fr.kiyauden._2ddoc.DataFormat.INTEGER;
import static fr.kiyauden._2ddoc.DataFormat.TEXT;
import static fr.kiyauden._2ddoc.DataFormat.TIME;
import static fr.kiyauden._2ddoc.DataFormat.URL;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * The data types that can be found inside a 2D-DOC
 */
public enum DataType {

    //------------------------------------------------------------------------------------------------------------------
    // 2D-Doc complementary data, used as optional data for most documents
    //------------------------------------------------------------------------------------------------------------------
    DOCUMENT_UNIQUE_ID(
            "01",
            "Identifiant unique du document",
            TEXT,
            0
    ),
    DOCUMENT_CATEGORY(
            "02",
            "Catégorie de document",
            TEXT,
            0
    ),
    DOCUMENT_SUB_CATEGORY(
            "03",
            "Sous-catégorie de document",
            TEXT,
            0
    ),
    ISSUING_APPLICATION(
            "04",
            "Application de composition",
            TEXT,
            0
    ),
    ISSUING_APPLICATION_VERSION(
            "05",
            "Version de l’application de composition",
            TEXT,
            0
    ),
    DOCUMENT_ASSOCIATION_DATE_WITH_2DDOC(
            "06",
            "Date de l’association entre le document et le code 2D-DOC",
            DATE,
            4,
            4
    ),
    DOCUMENT_ASSOCIATION_TIME_WITH_2DDOC(
            "07",
            "Heure de l’association entre le document et le code 2D-DOC",
            TIME,
            6,
            6
    ),
    DOCUMENT_EXPIRY_DATE(
            "08",
            "Date d’expiration du document",
            DATE,
            4,
            4
    ),
    DOCUMENT_NUMBER_OF_PAGES(
            "09",
            "Nombre de pages du document",
            INTEGER,
            4,
            4
    ),
    EDITOR_OF_2DDOC(
            "0A",
            "Éditeur du 2D-DOC",
            TEXT,
            9,
            9
    ),
    DOCUMENT_URL(
            "0C",
            "URL du document",
            URL,
            0
    ),
    //------------------------------------------------------------------------------------------------------------------
    // Data for "Justificatif de domicile" documents
    // Documents 00 - 01 - 02
    //------------------------------------------------------------------------------------------------------------------
    BENEFICIARY_ADDRESS_LINE_1(
            "10",
            "Ligne 1 de la norme adresse postale du bénéficiaire de la prestation",
            TEXT,
            0,
            38
    ),
    BENEFICIARY_QUALITY_AND_OR_TITLE(
            "11",
            "Qualité et/ou titre de la personne bénéficiaire de la prestation",
            TEXT,
            0,
            38
    ),
    BENEFICIARY_FIRSTNAME(
            "12",
            "Prénom de la personne bénéficiaire de la prestation",
            TEXT,
            0,
            38
    ),
    BENEFICIARY_LASTNAME(
            "13",
            "Nom de la personne bénéficiaire de la prestation",
            TEXT,
            0,
            38
    ),
    INVOICE_RECIPIENT_ADDRESS_LINE_1(
            "14",
            "Ligne 1 de la norme adresse postale du destinataire de la facture",
            TEXT,
            0,
            38
    ),
    INVOICE_RECIPIENT_QUALITY_AND_OR_TITLE(
            "15",
            "Qualité et/ou titre de la personne destinataire de la facture",
            TEXT,
            0,
            38
    ),
    INVOICE_RECIPIENT_FIRSTNAME(
            "16",
            "Prénom de la personne destinataire de la facture",
            TEXT,
            0,
            38
    ),
    INVOICE_RECIPIENT_LASTNAME(
            "17",
            "Nom de la personne destinataire de la facture",
            TEXT,
            0,
            38
    ),
    INVOICE_NUMBER(
            "18",
            "Numéro de la facture",
            TEXT,
            0
    ),
    CLIENT_NUMBER(
            "19",
            "Numéro de client",
            TEXT,
            0
    ),
    CONTRACT_NUMBER(
            "1A",
            "Numéro du contrat",
            TEXT,
            0
    ),
    SUBSCRIBER_IDENTIFIER(
            "1B",
            "Identifiant du souscripteur du contrat",
            TEXT,
            0
    ),
    CONTRACT_EFFECTIVE_DATE(
            "1C",
            "Date d’effet du contrat",
            DATE,
            8,
            8
    ),
    INVOICE_AMOUNT_INCLUDING_TAX(
            "1D",
            "Montant TTC de la facture",
            AMOUNT,
            0,
            16
    ),
    BENEFICIARY_PHONE_NUMBER(
            "1E",
            "Numéro de téléphone du bénéficiaire de la prestation",
            TEXT,
            0,
            30
    ),
    INVOICE_RECIPIENT_PHONE_NUMBER(
            "1F",
            "Numéro de téléphone du destinataire de la facture",
            TEXT,
            0,
            30
    ),
    UNMENTIONED_CO_BENEFICIARY_PRESENCE(
            "1G",
            "Présence d’un co-bénéficiaire de la prestation non mentionné dans le code",
            BOOLEAN,
            0,
            1
    ),
    UNMENTIONED_CO_INVOICE_RECIPIENT_PRESENCE(
            "1H",
            "Présence d’un co-destinataire de la facture non mentionné dans le code",
            BOOLEAN,
            0,
            1
    ),
    CO_BENEFICIARY_RECIPIENT_ADDRESS_LINE_1(
            "1I",
            "Ligne 1 de la norme adresse postale du co-bénéficiaire de la prestation",
            TEXT,
            0,
            38
    ),
    CO_BENEFICIARY_QUALITY_AND_OR_TITLE(
            "1J",
            "Qualité et/ou titre du co-bénéficiaire de la prestation",
            TEXT,
            0,
            38
    ),
    CO_BENEFICIARY_FIRSTNAME(
            "1K",
            "Prénom du co-bénéficiaire de la prestation",
            TEXT,
            0,
            38
    ),
    CO_BENEFICIARY_LASTNAME(
            "1L",
            "Nom du co-bénéficiaire de la prestation",
            TEXT,
            0,
            38
    ),
    CO_INVOICE_RECIPIENT_ADDRESS_LINE_1(
            "1M",
            "Ligne 1 de la norme adresse postale du co-destinataire de la facture",
            TEXT,
            0,
            38
    ),
    CO_INVOICE_RECIPIENT_QUALITY_AND_OR_TITLE(
            "1N",
            "Qualité et/ou titre du co-destinataire de la facture",
            TEXT,
            0,
            38
    ),
    CO_INVOICE_RECIPIENT_FIRSTNAME(
            "1O",
            "Prénom du co-destinataire de la facture",
            TEXT,
            0,
            38
    ),
    CO_INVOICE_RECIPIENT_LASTNAME(
            "1P",
            "Nom du co-destinataire de la facture",
            TEXT,
            0,
            38
    ),
    BENEFIT_SERVICE_POINT_ADDRESS_LINE_2(
            "20",
            "Ligne 2 de la norme adresse postale du point de service des prestations",
            TEXT,
            0,
            38
    ),
    BENEFIT_SERVICE_POINT_ADDRESS_LINE_3(
            "21",
            "Ligne 3 de la norme adresse postale du point de service des prestations",
            TEXT,
            0,
            38
    ),
    BENEFIT_SERVICE_POINT_ADDRESS_LINE_4(
            "22",
            "Ligne 4 de la norme adresse postale du point de service des prestations",
            TEXT,
            0,
            38
    ),
    BENEFIT_SERVICE_POINT_ADDRESS_LINE_5(
            "23",
            "Ligne 5 de la norme adresse postale du point de service des prestations",
            TEXT,
            0,
            38
    ),
    BENEFIT_SERVICE_POINT_POSTAL_OR_CEDEX_CODE(
            "24",
            "Code postal ou code cedex du point de service des prestations",
            TEXT,
            5,
            5
    ),
    BENEFIT_SERVICE_POINT_LOCALITY_OR_CEDEX_LABEL(
            "25",
            "Localité de destination ou libellé cedex du point de service des prestations",
            TEXT,
            0,
            32
    ),
    BENEFIT_SERVICE_POINT_COUNTRY(
            "26",
            "Pays de service des prestations",
            TEXT,
            2,
            2
    ),
    INVOICE_RECIPIENT_ADDRESS_LINE_2(
            "27",
            "Ligne 2 de la norme adresse postale du destinataire de la facture",
            TEXT,
            0,
            38
    ),
    INVOICE_RECIPIENT_ADDRESS_LINE_3(
            "28",
            "Ligne 3 de la norme adresse postale du destinataire de la facture",
            TEXT,
            0,
            38
    ),
    INVOICE_RECIPIENT_ADDRESS_LINE_4(
            "29",
            "Ligne 4 de la norme adresse postale du destinataire de la facture",
            TEXT,
            0,
            38
    ),
    INVOICE_RECIPIENT_ADDRESS_LINE_5(
            "2A",
            "Ligne 5 de la norme adresse postale du destinataire de la facture",
            TEXT,
            0,
            38
    ),
    INVOICE_RECIPIENT_POSTAL_OR_CEDEX_CODE(
            "2B",
            "Code postal ou code cedex du destinataire de la facture",
            TEXT,
            5,
            5
    ),
    INVOICE_RECIPIENT_LOCALITY_OR_CEDEX_LABEL(
            "2C",
            "Localité de destination ou libellé cedex du destinataire de la facture",
            TEXT,
            0,
            32
    ),
    INVOICE_RECIPIENT_COUNTRY(
            "2D",
            "Pays du destinataire de la facture",
            TEXT,
            2,
            2
    );

    /**
     * Map used to find a data type by its ID
     */
    private static final Map<String, DataType> BY_ID;

    static {
        BY_ID = stream(DataType.values()).collect(toMap(DataType::getId, identity()));
    }

    /**
     * The data type ID
     */
    @Getter
    private final String id;
    /**
     * The data type name/label
     */
    @Getter
    private final String label;
    /**
     * The format of the data
     */
    @Getter
    private final DataFormat type;
    /**
     * The minimum length of the data in the 2D-DOC
     */
    @Getter
    private final int minLength;
    /**
     * The minimum length of the data in the 2D-DOC
     * <p>
     * If -1, there is no max length
     */
    @Getter
    private final int maxLength;
    /**
     * Boolean indicating if the data is of fixed length
     * <p>
     * TRUE when {@link DataType#minLength} == {@link DataType#maxLength}
     * <p>
     * FALSE otherwise
     */
    @Getter
    private final boolean fixedLength;

    /**
     * Boolean indicating if the data has a max length, meaning that the data is unlimited in length or not
     * <p>
     * TRUE when {@link DataType#maxLength} == -1
     * <p>
     * FALSE otherwise
     */
    @Accessors(fluent = true)
    @Getter
    private final boolean hasMaxLength;

    DataType(final String id, final String label, final DataFormat type, final int minLength, final int maxLength) {
        this.id = id;
        this.label = label;
        this.type = type;
        this.minLength = minLength;
        this.maxLength = maxLength;
        fixedLength = minLength == maxLength;
        hasMaxLength = maxLength != -1;
    }

    DataType(final String id, final String label, final DataFormat type, final int minLength) {
        this(id, label, type, minLength, -1);
    }

    /**
     * Finds a data type by its ID
     *
     * @param id the ID of the data type to find
     * @return an optional containing the found data type or an empty optional if nothing is found
     */
    static Optional<DataType> findById(final String id) {
        return ofNullable(BY_ID.get(id));
    }

    /**
     * Returns the "complementary data" types, those with an ID starting by "O"
     *
     * @return the list of "complementary data" types
     */
    static List<DataType> getComplementaryDataTypes() {
        return stream(values())
                .filter(dataType -> dataType.getId().startsWith("0"))
                .collect(toList());
    }

}
