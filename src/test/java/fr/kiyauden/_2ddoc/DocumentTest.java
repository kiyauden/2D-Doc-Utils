package fr.kiyauden._2ddoc;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAndIs;
import static fr.kiyauden._2ddoc.DataType.BENEFICIARY_ADDRESS_LINE_1;
import static fr.kiyauden._2ddoc.Document.DOCUMENT_01;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DocumentTest {

    @Test
    void testIfMandatoryDataListsAreImmutables() {
        for (final Document document : Document.values()) {
            assertThrows(UnsupportedOperationException.class,
                         () -> document.getMandatoryData().add(BENEFICIARY_ADDRESS_LINE_1));
            assertThrows(UnsupportedOperationException.class,
                         () -> document.getMandatoryData().set(1, BENEFICIARY_ADDRESS_LINE_1));
        }
    }

    @Test
    void testIfOptionalDataListsAreImmutables() {
        for (final Document document : Document.values()) {
            assertThrows(UnsupportedOperationException.class,
                         () -> document.getOptionalData().add(BENEFICIARY_ADDRESS_LINE_1));
            assertThrows(UnsupportedOperationException.class,
                         () -> document.getOptionalData().set(1, BENEFICIARY_ADDRESS_LINE_1));
        }
    }

    @Test
    void testIfDataInMandatoryListIsNotPresentInOptionalList() {
        for (final Document document : Document.values()) {
            final List<DataType> mandatoryData = document.getMandatoryData();
            final List<DataType> optionalData = document.getOptionalData();
            for (final DataType mandatoryDatum : mandatoryData) {
                assertFalse(optionalData.contains(mandatoryDatum),
                            format("Mandatory data \"%s\" is present in optional data list, should not happen",
                                   mandatoryDatum));
            }
        }
    }

    @Test
    void testIfDataInOptionalListIsNotPresentInMandatoryList() {
        for (final Document document : Document.values()) {
            final List<DataType> mandatoryData = document.getMandatoryData();
            final List<DataType> optionalData = document.getOptionalData();
            for (final DataType optionalDatum : optionalData) {
                assertFalse(mandatoryData.contains(optionalDatum),
                            format("Optional data \"%s\" is present in optional data list, should not happen",
                                   optionalDatum));
            }
        }
    }

    @Test
    void findById_whenDataWithIdExists_shouldReturnIt() {
        final Optional<Document> document = Document.findById("01");
        assertThat(document, isPresentAndIs(DOCUMENT_01));
    }

    @Test
    void findById_whenDataWithIdDoesNotExists_shouldReturnEmptyOptional() {
        final Optional<Document> document = Document.findById("EE");
        assertThat(document, isEmpty());
    }

}
