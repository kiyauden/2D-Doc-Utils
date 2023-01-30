package fr.kiyauden._2ddoc;

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
import static fr.kiyauden._2ddoc.DataType.INVOICE_NUMBER;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
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
        // TODO : Add assert true when a fixed length data will be added
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

}
