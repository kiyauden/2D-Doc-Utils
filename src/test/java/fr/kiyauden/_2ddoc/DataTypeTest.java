package fr.kiyauden._2ddoc;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAndIs;
import static fr.kiyauden._2ddoc.DataType.BENEFICIARY_ADDRESS_LINE_1;
import static fr.kiyauden._2ddoc.DataType.DOCUMENT_ASSOCIATION_DATE_WITH_2DDOC;
import static fr.kiyauden._2ddoc.DataType.DOCUMENT_ASSOCIATION_TIME_WITH_2DDOC;
import static fr.kiyauden._2ddoc.DataType.DOCUMENT_CATEGORY;
import static fr.kiyauden._2ddoc.DataType.DOCUMENT_EXPIRY_DATE;
import static fr.kiyauden._2ddoc.DataType.DOCUMENT_NUMBER_OF_PAGES;
import static fr.kiyauden._2ddoc.DataType.DOCUMENT_SUB_CATEGORY;
import static fr.kiyauden._2ddoc.DataType.DOCUMENT_UNIQUE_ID;
import static fr.kiyauden._2ddoc.DataType.DOCUMENT_URL;
import static fr.kiyauden._2ddoc.DataType.EDITOR_OF_2DDOC;
import static fr.kiyauden._2ddoc.DataType.INVOICE_NUMBER;
import static fr.kiyauden._2ddoc.DataType.INVOICE_RECIPIENT_COUNTRY;
import static fr.kiyauden._2ddoc.DataType.ISSUING_APPLICATION;
import static fr.kiyauden._2ddoc.DataType.ISSUING_APPLICATION_VERSION;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataTypeTest {

    @Test
    void checkForLabelDuplicates() {
        // Checks if a label was not used multiple times
        final List<String> idList = Arrays.stream(DataType.values())
                .map(DataType::getLabel)
                .collect(Collectors.toList());

        final Set<String> set = new HashSet<>();

        for (final String id : idList) {
            assertTrue(set.add(id),
                       format("Label \"%s\" is used multiple times, please check DataType enum configuration", id));
        }
    }

    @Test
    void testIsFixedLength() {
        assertFalse(BENEFICIARY_ADDRESS_LINE_1.isFixedLength());
        assertTrue(INVOICE_RECIPIENT_COUNTRY.isFixedLength());
    }

    @Test
    void testHasNoMaxSize() {
        assertTrue(BENEFICIARY_ADDRESS_LINE_1.hasMaxLength());
        assertFalse(INVOICE_NUMBER.hasMaxLength());
    }

    @Test
    void findById_whenDataWithIdExists_shouldReturnIt() {
        final Optional<DataType> dataType = DataType.findById("10");
        assertThat(dataType, isPresentAndIs(BENEFICIARY_ADDRESS_LINE_1));
    }

    @Test
    void findById_whenDataWithIdDoesNotExists_shouldReturnEmptyOptional() {
        final Optional<DataType> dataType = DataType.findById("EE");
        assertThat(dataType, isEmpty());
    }

    @Test
    void getComplementaryDataTypes_ShouldReturnAll() {
        final List<DataType> expectedDataTypes = asList(
                DOCUMENT_UNIQUE_ID,
                DOCUMENT_CATEGORY,
                DOCUMENT_SUB_CATEGORY,
                ISSUING_APPLICATION,
                ISSUING_APPLICATION_VERSION,
                DOCUMENT_ASSOCIATION_DATE_WITH_2DDOC,
                DOCUMENT_ASSOCIATION_TIME_WITH_2DDOC,
                DOCUMENT_EXPIRY_DATE,
                DOCUMENT_NUMBER_OF_PAGES,
                EDITOR_OF_2DDOC,
                DOCUMENT_URL
        );

        final List<DataType> actualDataTypes = DataType.getComplementaryDataTypes();
        assertEquals(expectedDataTypes.size(), actualDataTypes.size());
        assertThat(expectedDataTypes, Matchers.containsInAnyOrder(actualDataTypes.toArray()));
    }

}
