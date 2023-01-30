package fr.kiyauden._2ddoc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static fr.kiyauden._2ddoc.DataType.BENEFICIARY_ADDRESS_LINE_1;
import static fr.kiyauden._2ddoc.DataType.INVOICE_RECIPIENT_ADDRESS_LINE_1;
import static fr.kiyauden._2ddoc.Document.DOCUMENT_01;
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
        assertTrue(service.isDataMandatory(DOCUMENT_01, BENEFICIARY_ADDRESS_LINE_1));
    }

    @Test
    void isDataMandatory_whenDataIsNotMandatory_shouldReturnFalse() throws DataExtractionException {
        assertFalse(service.isDataMandatory(DOCUMENT_01, INVOICE_RECIPIENT_ADDRESS_LINE_1));
    }

    @Test
    void isDataMandatory_whenDataIsNotLinkedToDocument_shouldThrowException() {
        // TODO : add this test when other documents will be added
        assertTrue(true);
    }
}
