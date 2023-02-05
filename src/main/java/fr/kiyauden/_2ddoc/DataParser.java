package fr.kiyauden._2ddoc;

/**
 * Interface for a parser
 * <p>
 * Its job is to parse the data extracted from the 2D-Doc (always a string) to the corresponding java class
 *
 * @param <T> the returned type of this parser
 */
interface DataParser<T> {
    /**
     * Parses a data into the T type
     *
     * @param data the data to parse
     * @return the data parsed into the T type
     * @throws ParsingException when an error occurred while parsing a data
     */
    T parse(String data) throws ParsingException;

    /**
     * Returns the handled data format for this parser
     *
     * @return the handled format
     */
    DataFormat getHandledFormat();
}
