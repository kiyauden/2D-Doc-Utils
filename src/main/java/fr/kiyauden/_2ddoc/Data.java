package fr.kiyauden._2ddoc;

import lombok.Builder;
import lombok.Value;

import java.util.Optional;

import static lombok.AccessLevel.PACKAGE;

/**
 * Class representing a data extracted from a 2D-DOC
 */
@Value
@Builder(access = PACKAGE)
public class Data {
    /**
     * The {@link DataType} of this data
     * <p>
     * The library user must use the "Parsed as" info in {@link DataType#getType()}
     * to cast the {@link Data#getValue()} from {@link Object} to the corresponding type
     */
    DataType dataType;
    /**
     * The value of the data
     * <p>
     * Can be optional empty when only the identifier is present, the specification allows a min size of 0 so no data
     * <p>
     * Should probably not happen, but it is allowed
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    Optional<Object> value;
    /**
     * The source of the data
     */
    DataSource source;
    /**
     * Boolean indicating if this data is mandatory for the {@link Document} from where it is extracted
     * TRUE when mandatory, FALSE otherwise
     */
    boolean mandatory;
    /**
     * Boolean indicating if the data is truncated
     * <p>
     * Means that the data is not complete
     * <p>
     * TRUE is the data is truncated, FALSE otherwise
     */
    boolean truncated;

}
