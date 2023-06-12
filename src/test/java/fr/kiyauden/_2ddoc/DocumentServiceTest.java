package fr.kiyauden._2ddoc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static fr.kiyauden._2ddoc.DataType.BENEFICIARY_ADDRESS_LINE_1;
import static fr.kiyauden._2ddoc.DataType.BENEFICIARY_FIRSTNAME;
import static fr.kiyauden._2ddoc.DataType.BENEFICIARY_LASTNAME;
import static fr.kiyauden._2ddoc.DataType.BENEFICIARY_QUALITY_AND_OR_TITLE;
import static fr.kiyauden._2ddoc.DataType.BENEFIT_SERVICE_POINT_ADDRESS_LINE_4;
import static fr.kiyauden._2ddoc.DataType.BENEFIT_SERVICE_POINT_COUNTRY;
import static fr.kiyauden._2ddoc.DataType.BENEFIT_SERVICE_POINT_POSTAL_OR_CEDEX_CODE;
import static fr.kiyauden._2ddoc.DataType.INVOICE_RECIPIENT_ADDRESS_LINE_1;
import static fr.kiyauden._2ddoc.Document.DOC_01;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DocumentServiceTest {

    private DocumentService service;

    @BeforeEach
    void beforeEach() {
        service = new DocumentService();
    }

    @Test
    void isDataMandatory_whenDataIsMandatory_shouldReturnTrue() throws DataExtractionException {
        assertTrue(service.isDataMandatory(DOC_01, BENEFICIARY_ADDRESS_LINE_1));
    }

    @Test
    void isDataMandatory_whenDataIsNotMandatory_shouldReturnFalse() throws DataExtractionException {
        assertFalse(service.isDataMandatory(DOC_01, INVOICE_RECIPIENT_ADDRESS_LINE_1));
    }

    @Test
    void isDataMandatory_whenDataIsNotLinkedToDocument_shouldThrowException() {
        // TODO : add this test when other documents will be added
        assertTrue(true);
    }

    @Test
    void computeMissingData_shouldReturnMissingData() {
        // Base data without interchangeable data
        final List<DataType> baseData = asList(BENEFIT_SERVICE_POINT_ADDRESS_LINE_4,
                                               BENEFIT_SERVICE_POINT_POSTAL_OR_CEDEX_CODE,
                                               BENEFIT_SERVICE_POINT_COUNTRY);

        final List<DataType> dataTypes = service.computeMissingData(DOC_01, baseData);

        final List<DataType> expectedData = asList(BENEFICIARY_ADDRESS_LINE_1,
                                                   BENEFICIARY_QUALITY_AND_OR_TITLE,
                                                   BENEFICIARY_FIRSTNAME,
                                                   BENEFICIARY_LASTNAME);

        assertThat(dataTypes, containsInAnyOrder(expectedData.toArray()));
    }

    @Test
    void computeMissingData_withInterchangeableData_shouldReturnMissingData() {
        // Base data without interchangeable data
        final List<DataType> baseData = new ArrayList<>(asList(BENEFIT_SERVICE_POINT_ADDRESS_LINE_4,
                                                               BENEFIT_SERVICE_POINT_POSTAL_OR_CEDEX_CODE,
                                                               BENEFIT_SERVICE_POINT_COUNTRY));

        // Data interchangeable with BENEFICIARY_QUALITY_AND_OR_TITLE, BENEFICIARY_FIRSTNAME, BENEFICIARY_LASTNAME
        // Should not add those data in the missing data list
        baseData.add(BENEFICIARY_ADDRESS_LINE_1);

        final List<DataType> dataTypes = service.computeMissingData(DOC_01, baseData);

        assertEquals(0, dataTypes.size());
    }

    @Test
    void computeMissingData_withInterchangeableData2_shouldReturnMissingData() {
        // Base data without interchangeable data
        final List<DataType> baseData = new ArrayList<>(asList(BENEFIT_SERVICE_POINT_ADDRESS_LINE_4,
                                                               BENEFIT_SERVICE_POINT_POSTAL_OR_CEDEX_CODE,
                                                               BENEFIT_SERVICE_POINT_COUNTRY));

        // Data interchangeable with BENEFICIARY_QUALITY_AND_OR_TITLE, BENEFICIARY_FIRSTNAME, BENEFICIARY_LASTNAME
        // Should not add those data in the missing data list
        baseData.add(BENEFICIARY_QUALITY_AND_OR_TITLE);
        baseData.add(BENEFICIARY_FIRSTNAME);
        baseData.add(BENEFICIARY_LASTNAME);

        final List<DataType> dataTypes = service.computeMissingData(DOC_01, baseData);

        assertEquals(0, dataTypes.size());
    }

    @Test
    void computeMissingData_withInterchangeableData_butMissingSome_shouldReturnMissingData() {
        // Base data without interchangeable data
        final List<DataType> baseData = new ArrayList<>(asList(BENEFIT_SERVICE_POINT_ADDRESS_LINE_4,
                                                               BENEFIT_SERVICE_POINT_POSTAL_OR_CEDEX_CODE,
                                                               BENEFIT_SERVICE_POINT_COUNTRY));

        // Data interchangeable with BENEFICIARY_ADDRESS_LINE_1
        // Should add BENEFICIARY_ADDRESS_LINE_1 to the list because BENEFICIARY_FIRSTNAME, BENEFICIARY_LASTNAME are not present
        // Should add BENEFICIARY_FIRSTNAME, BENEFICIARY_LASTNAME
        baseData.add(BENEFICIARY_QUALITY_AND_OR_TITLE);

        final List<DataType> dataTypes = service.computeMissingData(DOC_01, baseData);

        final List<DataType> expectedData = asList(BENEFICIARY_ADDRESS_LINE_1,
                                                   BENEFICIARY_FIRSTNAME,
                                                   BENEFICIARY_LASTNAME);

        assertThat(dataTypes, containsInAnyOrder(expectedData.toArray()));
    }
}
