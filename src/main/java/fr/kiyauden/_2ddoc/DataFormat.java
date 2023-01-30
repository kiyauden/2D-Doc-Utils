package fr.kiyauden._2ddoc;

/**
 * The data formats of the data
 * <p>
 * The library user must use the "Parsed as" info to cast the {@link Data#getValue()} from {@link Object} to the corresponding type
 */
public enum DataFormat {
    /**
     * Integer value
     * <p>
     * Parsed as {@link Integer}
     */
    INTEGER,
    /**
     * Simple text value
     * <p>
     * Parsed as {@link String}
     */
    TEXT,
    /**
     * Date value
     * <p>
     * Parsed as {@link java.time.LocalDate}
     */
    DATE,
    /**
     * Time value
     * <p>
     * Parsed as {@link java.time.LocalTime}
     */
    TIME,
    /**
     * Currency value
     * <p>
     * Parsed as {@link Double}
     */
    AMOUNT,
    /**
     * Boolean value
     * <p>
     * Parsed as {@link Boolean}
     */
    BOOLEAN,
    /**
     * URL value
     * <p>
     * Parsed as {@link String}
     */
    URL
}
