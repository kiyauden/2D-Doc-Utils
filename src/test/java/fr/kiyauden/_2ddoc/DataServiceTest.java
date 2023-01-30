package fr.kiyauden._2ddoc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static fr.kiyauden._2ddoc.DataSource.MESSAGE;
import static fr.kiyauden._2ddoc.DataType.BENEFICIARY_ADDRESS_LINE_1;
import static fr.kiyauden._2ddoc.DataType.BENEFIT_SERVICE_POINT_ADDRESS_LINE_4;
import static fr.kiyauden._2ddoc.DataType.BENEFIT_SERVICE_POINT_COUNTRY;
import static fr.kiyauden._2ddoc.DataType.BENEFIT_SERVICE_POINT_POSTAL_OR_CEDEX_CODE;
import static fr.kiyauden._2ddoc.DataType.CLIENT_NUMBER;
import static fr.kiyauden._2ddoc.DataType.DOCUMENT_ASSOCIATION_TIME_WITH_2DDOC;
import static fr.kiyauden._2ddoc.DataType.DOCUMENT_CATEGORY;
import static fr.kiyauden._2ddoc.DataType.DOCUMENT_SUB_CATEGORY;
import static fr.kiyauden._2ddoc.DataType.INVOICE_AMOUNT_INCLUDING_TAX;
import static fr.kiyauden._2ddoc.DataType.INVOICE_NUMBER;
import static fr.kiyauden._2ddoc.Document.DOCUMENT_01;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataServiceTest {

    private DataService service;

    @Mock
    private IParserService parserService;

    @Mock
    private IDocumentService documentService;

    @BeforeEach
    void beforeEach() throws DataExtractionException, ParsingException {
        service = new DataService(parserService, documentService);

        // Mocks the parser, so it returns the data read from the 2D-Doc as string,
        // since it not the job of this test to check if the parsing is correct
        when(parserService.parse(anyString(), any(DataFormat.class))).thenAnswer(i -> i.getArguments()[0]);
        when(documentService.isDataMandatory(eq(DOCUMENT_01), any())).thenReturn(false);
    }

    @Test
    void extractData_whenDataFromMessageSegment_shouldParseData() throws DataExtractionException {
        final String dataFrom2dDoc = "26FR247500010MME/NATACHA/SPECIMEN\u001D221 RUE DE LA RUE\u001D1812345678910112\u001D02FACTURE FNB\u001D03GP\u001D1D9,99\u001D19575645792\u001D07195113";
        final List<Data> expectedData = asList(
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_COUNTRY, false, "FR"),
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_POSTAL_OR_CEDEX_CODE, false, "75000"),
                buildExpectedDataFromMessageSegment(BENEFICIARY_ADDRESS_LINE_1, false, "MME/NATACHA/SPECIMEN"),
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_ADDRESS_LINE_4, false,
                                                    "1 RUE DE LA RUE"),
                buildExpectedDataFromMessageSegment(INVOICE_NUMBER, false, "12345678910112"),
                buildExpectedDataFromMessageSegment(DOCUMENT_CATEGORY, false, "FACTURE FNB"),
                buildExpectedDataFromMessageSegment(DOCUMENT_SUB_CATEGORY, false, "GP"),
                buildExpectedDataFromMessageSegment(INVOICE_AMOUNT_INCLUDING_TAX, false, "9,99"),
                buildExpectedDataFromMessageSegment(CLIENT_NUMBER, false, "575645792"),
                buildExpectedDataFromMessageSegment(DOCUMENT_ASSOCIATION_TIME_WITH_2DDOC, false, "195113")
        );

        final List<Data> data = service.extractData(dataFrom2dDoc, DOCUMENT_01, MESSAGE);
        assertThat(data, containsInAnyOrder(expectedData.toArray()));
    }

    @Test
    void extractData_whenDataFromMessageSegment_inAnyOrder_shouldParseData() throws DataExtractionException {
        final List<String> baseData = asList("26FR", "2475000", "10MME/NATACHA/SPECIMEN\u001D",
                                             "221 RUE DE LA RUE\u001D", "1812345678910112\u001D", "02FACTURE FNB\u001D",
                                             "03GP\u001D", "1D9,99\u001D", "19575645792\u001D", "07195113");

        final List<Data> expectedData = asList(
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_COUNTRY, false, "FR"),
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_POSTAL_OR_CEDEX_CODE, false, "75000"),
                buildExpectedDataFromMessageSegment(BENEFICIARY_ADDRESS_LINE_1, false, "MME/NATACHA/SPECIMEN"),
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_ADDRESS_LINE_4, false,
                                                    "1 RUE DE LA RUE"),
                buildExpectedDataFromMessageSegment(INVOICE_NUMBER, false, "12345678910112"),
                buildExpectedDataFromMessageSegment(DOCUMENT_CATEGORY, false, "FACTURE FNB"),
                buildExpectedDataFromMessageSegment(DOCUMENT_SUB_CATEGORY, false, "GP"),
                buildExpectedDataFromMessageSegment(INVOICE_AMOUNT_INCLUDING_TAX, false, "9,99"),
                buildExpectedDataFromMessageSegment(CLIENT_NUMBER, false, "575645792"),
                buildExpectedDataFromMessageSegment(DOCUMENT_ASSOCIATION_TIME_WITH_2DDOC, false, "195113")
        );

        // Shuffle 100 times, should be enough to test all combinaisons
        for (int i = 0; i < 100; i++) {
            Collections.shuffle(baseData);
            final String dataFrom2dDoc = String.join("", baseData);

            final List<Data> data = service.extractData(dataFrom2dDoc, DOCUMENT_01, MESSAGE);
            assertThat(data, containsInAnyOrder(expectedData.toArray()));
        }
    }

    @Test
    void extractData_whenDataIsMandatory_shouldMarkAsMandatory() throws DataExtractionException {
        // Sets BENEFIT_SERVICE_POINT_COUNTRY as mandatory
        when(documentService.isDataMandatory(DOCUMENT_01, BENEFIT_SERVICE_POINT_COUNTRY)).thenReturn(true);

        final String dataFrom2dDoc = "26FR247500010MME/NATACHA/SPECIMEN\u001D221 RUE DE LA RUE\u001D1812345678910112\u001D02FACTURE FNB\u001D03GP\u001D1D9,99\u001D19575645792\u001D07195113";
        final List<Data> expectedData = asList(
                // Should be mandatory
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_COUNTRY, true, false, "FR"),
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_POSTAL_OR_CEDEX_CODE, false, "75000"),
                buildExpectedDataFromMessageSegment(BENEFICIARY_ADDRESS_LINE_1, false, "MME/NATACHA/SPECIMEN"),
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_ADDRESS_LINE_4, false,
                                                    "1 RUE DE LA RUE"),
                buildExpectedDataFromMessageSegment(INVOICE_NUMBER, false, "12345678910112"),
                buildExpectedDataFromMessageSegment(DOCUMENT_CATEGORY, false, "FACTURE FNB"),
                buildExpectedDataFromMessageSegment(DOCUMENT_SUB_CATEGORY, false, "GP"),
                buildExpectedDataFromMessageSegment(INVOICE_AMOUNT_INCLUDING_TAX, false, "9,99"),
                buildExpectedDataFromMessageSegment(CLIENT_NUMBER, false, "575645792"),
                buildExpectedDataFromMessageSegment(DOCUMENT_ASSOCIATION_TIME_WITH_2DDOC, false, "195113")
        );

        final List<Data> data = service.extractData(dataFrom2dDoc, DOCUMENT_01, MESSAGE);
        assertThat(data, containsInAnyOrder(expectedData.toArray()));
    }

    @Test
    void extractData_whenDataFromMessageSegment_andDataTruncated_shouldParseData() throws DataExtractionException {
        // Data 10 ending with RS, data is truncated
        final String dataFrom2dDoc = "26FR247500010MME/NATACHA/SPEC\u001E221 RUE DE LA RUE\u001D1812345678910112\u001D02FACTURE FNB\u001D03GP\u001D1D9,99\u001D19575645792\u001D07195113";
        final List<Data> expectedData = asList(
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_COUNTRY, false, "FR"),
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_POSTAL_OR_CEDEX_CODE, false, "75000"),
                buildExpectedDataFromMessageSegment(BENEFICIARY_ADDRESS_LINE_1, true, "MME/NATACHA/SPEC"),
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_ADDRESS_LINE_4, false,
                                                    "1 RUE DE LA RUE"),
                buildExpectedDataFromMessageSegment(INVOICE_NUMBER, false, "12345678910112"),
                buildExpectedDataFromMessageSegment(DOCUMENT_CATEGORY, false, "FACTURE FNB"),
                buildExpectedDataFromMessageSegment(DOCUMENT_SUB_CATEGORY, false, "GP"),
                buildExpectedDataFromMessageSegment(INVOICE_AMOUNT_INCLUDING_TAX, false, "9,99"),
                buildExpectedDataFromMessageSegment(CLIENT_NUMBER, false, "575645792"),
                buildExpectedDataFromMessageSegment(DOCUMENT_ASSOCIATION_TIME_WITH_2DDOC, false, "195113")
        );

        final List<Data> data = service.extractData(dataFrom2dDoc, DOCUMENT_01, MESSAGE);
        assertThat(data, containsInAnyOrder(expectedData.toArray()));
    }

    @Test
    void extractData_whenDataFromMessageSegment_andEndingVariableLengthDataWithGS_shouldParseData()
            throws DataExtractionException {
        final String dataFrom2dDoc = "26FR2475000221 RUE DE LA RUE\u001D1812345678910112\u001D02FACTURE FNB\u001D03GP\u001D1D9,99\u001D19575645792\u001D0719511310MME/NATACHA/SPECIMEN\u001D";
        final List<Data> expectedData = asList(
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_COUNTRY, false, "FR"),
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_POSTAL_OR_CEDEX_CODE, false, "75000"),
                buildExpectedDataFromMessageSegment(BENEFICIARY_ADDRESS_LINE_1, false, "MME/NATACHA/SPECIMEN"),
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_ADDRESS_LINE_4, false,
                                                    "1 RUE DE LA RUE"),
                buildExpectedDataFromMessageSegment(INVOICE_NUMBER, false, "12345678910112"),
                buildExpectedDataFromMessageSegment(DOCUMENT_CATEGORY, false, "FACTURE FNB"),
                buildExpectedDataFromMessageSegment(DOCUMENT_SUB_CATEGORY, false, "GP"),
                buildExpectedDataFromMessageSegment(INVOICE_AMOUNT_INCLUDING_TAX, false, "9,99"),
                buildExpectedDataFromMessageSegment(CLIENT_NUMBER, false, "575645792"),
                buildExpectedDataFromMessageSegment(DOCUMENT_ASSOCIATION_TIME_WITH_2DDOC, false, "195113")
        );

        final List<Data> data = service.extractData(dataFrom2dDoc, DOCUMENT_01, MESSAGE);
        assertThat(data, containsInAnyOrder(expectedData.toArray()));
    }

    @Test
    void extractData_whenDataFromMessageSegment_andEndingVariableLengthDataWithoutGS_shouldParseData()
            throws DataExtractionException {
        // GS missing from end of data because it is optional
        final String dataFrom2dDoc = "26FR2475000221 RUE DE LA RUE\u001D1812345678910112\u001D02FACTURE FNB\u001D03GP\u001D1D9,99\u001D19575645792\u001D0719511310MME/NATACHA/SPECIMEN";
        final List<Data> expectedData = asList(
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_COUNTRY, false, "FR"),
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_POSTAL_OR_CEDEX_CODE, false, "75000"),
                buildExpectedDataFromMessageSegment(BENEFICIARY_ADDRESS_LINE_1, false, "MME/NATACHA/SPECIMEN"),
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_ADDRESS_LINE_4, false,
                                                    "1 RUE DE LA RUE"),
                buildExpectedDataFromMessageSegment(INVOICE_NUMBER, false, "12345678910112"),
                buildExpectedDataFromMessageSegment(DOCUMENT_CATEGORY, false, "FACTURE FNB"),
                buildExpectedDataFromMessageSegment(DOCUMENT_SUB_CATEGORY, false, "GP"),
                buildExpectedDataFromMessageSegment(INVOICE_AMOUNT_INCLUDING_TAX, false, "9,99"),
                buildExpectedDataFromMessageSegment(CLIENT_NUMBER, false, "575645792"),
                buildExpectedDataFromMessageSegment(DOCUMENT_ASSOCIATION_TIME_WITH_2DDOC, false, "195113")
        );

        final List<Data> data = service.extractData(dataFrom2dDoc, DOCUMENT_01, MESSAGE);
        assertThat(data, containsInAnyOrder(expectedData.toArray()));
    }

    @Test
    void extractData_whenDataFromMessageSegment_andDataBiggerThanMaxLength_shouldThrowException() {
        // Data 22 has a max length of 38, here is 39
        final String dataFrom2dDoc = "26FR247500010MME/NATACHA/SPECIMEN\u001D221 RUE DE LA RUEEEEEEEEEEEEEEEEEEEEEEEEE\u001D1812345678910112\u001D02FACTURE FNB\u001D03GP\u001D1D9,99\u001D19575645792\u001D07195113";

        assertThrows(DataExtractionException.class,
                     () -> service.extractData(dataFrom2dDoc, DOCUMENT_01, MESSAGE));
    }

    @Test
    void extractData_whenDataFromMessageSegment_andDataIsExactlyMaxLength_shouldParseData()
            throws DataExtractionException {
        // Data 22 has a max length of 38, here is 38
        final String dataFrom2dDoc = "26FR247500010MME/NATACHA/SPECIMEN\u001D221 RUE DE LA RUEEEEEEEEEEEEEEEEEEEEEEEE\u001D1812345678910112\u001D02FACTURE FNB\u001D03GP\u001D1D9,99\u001D19575645792\u001D07195113";
        final List<Data> expectedData = asList(
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_COUNTRY, false, "FR"),
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_POSTAL_OR_CEDEX_CODE, false, "75000"),
                buildExpectedDataFromMessageSegment(BENEFICIARY_ADDRESS_LINE_1, false, "MME/NATACHA/SPECIMEN"),
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_ADDRESS_LINE_4, false,
                                                    "1 RUE DE LA RUEEEEEEEEEEEEEEEEEEEEEEEE"),
                buildExpectedDataFromMessageSegment(INVOICE_NUMBER, false, "12345678910112"),
                buildExpectedDataFromMessageSegment(DOCUMENT_CATEGORY, false, "FACTURE FNB"),
                buildExpectedDataFromMessageSegment(DOCUMENT_SUB_CATEGORY, false, "GP"),
                buildExpectedDataFromMessageSegment(INVOICE_AMOUNT_INCLUDING_TAX, false, "9,99"),
                buildExpectedDataFromMessageSegment(CLIENT_NUMBER, false, "575645792"),
                buildExpectedDataFromMessageSegment(DOCUMENT_ASSOCIATION_TIME_WITH_2DDOC, false, "195113")
        );

        final List<Data> data = service.extractData(dataFrom2dDoc, DOCUMENT_01, MESSAGE);
        assertThat(data, containsInAnyOrder(expectedData.toArray()));
    }


    @Test
    void extractData_whenDataFromMessageSegment_andUnknownIdentifier_shouldThrowException() {
        // Resets the mocks because it is not used here and mockito is not happy :)
        reset(parserService);
        reset(documentService);

        // Data EE does not exists
        final String dataFrom2dDoc = "EEFR247500010MME/NATACHA/SPECIMEN\u001D";

        assertThrows(DataExtractionException.class,
                     () -> service.extractData(dataFrom2dDoc, DOCUMENT_01, MESSAGE));
    }

    //------------------------------------------------------------------------------------------------------------------
    // Tests when minimum length 0 and 0 chars
    // It's not clear in the specification if it's possible or not, so we will handle it just in case
    //------------------------------------------------------------------------------------------------------------------

    @Test
    void extractData_whenDataFromMessageSegment_andVariableLengthWithoutTruncate_andDataWithMinLength0Has0Characters_shouldParseData()
            throws DataExtractionException {
        // Data 22 has a min length of 0, and here as 0 character
        // Ending with GS not truncated
        final String dataFrom2dDoc = "26FR247500010MME/NATACHA/SPECIMEN\u001D22\u001D1812345678910112\u001D02FACTURE FNB\u001D03GP\u001D1D9,99\u001D19575645792\u001D07195113";
        final List<Data> expectedData = asList(
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_COUNTRY, false, "FR"),
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_POSTAL_OR_CEDEX_CODE, false, "75000"),
                buildExpectedDataFromMessageSegment(BENEFICIARY_ADDRESS_LINE_1, false, "MME/NATACHA/SPECIMEN"),
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_ADDRESS_LINE_4, false, null),
                buildExpectedDataFromMessageSegment(INVOICE_NUMBER, false, "12345678910112"),
                buildExpectedDataFromMessageSegment(DOCUMENT_CATEGORY, false, "FACTURE FNB"),
                buildExpectedDataFromMessageSegment(DOCUMENT_SUB_CATEGORY, false, "GP"),
                buildExpectedDataFromMessageSegment(INVOICE_AMOUNT_INCLUDING_TAX, false, "9,99"),
                buildExpectedDataFromMessageSegment(CLIENT_NUMBER, false, "575645792"),
                buildExpectedDataFromMessageSegment(DOCUMENT_ASSOCIATION_TIME_WITH_2DDOC, false, "195113")
        );

        final List<Data> data = service.extractData(dataFrom2dDoc, DOCUMENT_01, MESSAGE);
        assertThat(data, containsInAnyOrder(expectedData.toArray()));
    }

    @Test
    void extractData_whenDataFromMessageSegment_andVariableLengthWithTruncate_andDataWithMinLength0Has0Characters_shouldParseData()
            throws DataExtractionException {
        // Data 60 has a min length of 0, and here as 0 character
        // Ending with RS, data is truncated
        final String dataFrom2dDoc = "26FR247500010MME/NATACHA/SPECIMEN\u001D22\u001E1812345678910112\u001D02FACTURE FNB\u001D03GP\u001D1D9,99\u001D19575645792\u001D07195113";
        final List<Data> expectedData = asList(
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_COUNTRY, false, "FR"),
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_POSTAL_OR_CEDEX_CODE, false, "75000"),
                buildExpectedDataFromMessageSegment(BENEFICIARY_ADDRESS_LINE_1, false, "MME/NATACHA/SPECIMEN"),
                buildExpectedDataFromMessageSegment(BENEFIT_SERVICE_POINT_ADDRESS_LINE_4, true, null),
                buildExpectedDataFromMessageSegment(INVOICE_NUMBER, false, "12345678910112"),
                buildExpectedDataFromMessageSegment(DOCUMENT_CATEGORY, false, "FACTURE FNB"),
                buildExpectedDataFromMessageSegment(DOCUMENT_SUB_CATEGORY, false, "GP"),
                buildExpectedDataFromMessageSegment(INVOICE_AMOUNT_INCLUDING_TAX, false, "9,99"),
                buildExpectedDataFromMessageSegment(CLIENT_NUMBER, false, "575645792"),
                buildExpectedDataFromMessageSegment(DOCUMENT_ASSOCIATION_TIME_WITH_2DDOC, false, "195113")
        );

        final List<Data> data = service.extractData(dataFrom2dDoc, DOCUMENT_01, MESSAGE);
        assertThat(data, containsInAnyOrder(expectedData.toArray()));
    }

    //------------------------------------------------------------------------------------------------------------------
    // TEST TOOLS
    //------------------------------------------------------------------------------------------------------------------

    private Data buildExpectedDataFromMessageSegment(final DataType dataType, final boolean mandatory,
                                                     final boolean truncated,
                                                     final Object value) {
        return Data.builder()
                .dataType(dataType)
                .mandatory(mandatory)
                .truncated(truncated)
                .source(MESSAGE)
                // ofNullable here so if the value provided is null it will create an empty optional
                .value(ofNullable(value))
                .build();
    }

    private Data buildExpectedDataFromMessageSegment(final DataType dataType,
                                                     final boolean truncated,
                                                     final Object value) {
        return buildExpectedDataFromMessageSegment(dataType, false, truncated, value);
    }

}
