package fr.kiyauden._2ddoc;

import lombok.Builder;
import lombok.Value;

import java.util.Optional;

import static lombok.AccessLevel.PACKAGE;

/**
 * Data class representing a 2D-DOC
 */
@Value
@Builder(access = PACKAGE)
public class Parsed2DDoc {

    /**
     * The header data for the 2D-DOC
     */
    Header header;
    /**
     * The data extracted from the 2D-DOC
     */
    ExtractedData extractedData;
    /**
     * The status of the signature
     */
    SignatureStatus signatureStatus;
    /**
     * The raw data of the 2D-DOC
     */
    String raw;

    /**
     * Valid if signature valid and no missing data
     */
    boolean valid;

    /**
     * Find a data if it exits
     *
     * @param type the data type to find
     * @return an optional containing the data
     */
    public Optional<Data> findData(final DataType type) {
        return extractedData.getData()
                .stream()
                .filter(data -> data.getDataType().equals(type))
                .findFirst();
    }

    /**
     * Gets the value for a data
     *
     * @param type the data type to find
     * @return an optional containing the value object
     */
    public Optional<Object> getValueForData(final DataType type) {
        return findData(type).flatMap(Data::getValue);
    }
}
