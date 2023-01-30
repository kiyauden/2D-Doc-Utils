package fr.kiyauden._2ddoc;


/**
 * Interface for a parser service
 * <p>
 * Its job is to parse the data extracted from the 2D-Doc (always a string) to the corresponding java class
 */
interface IParserService {
    /**
     * Parses the provided string value to the correct java type for a given data format
     *
     * @param value      the string value to be parsed
     * @param dataFormat the data format in which the value should be parsed
     * @return the parsed value as an Object to keep genericity
     * @throws ParsingException when an error occurred while parsing a data
     */
    Object parse(final String value, final DataFormat dataFormat) throws ParsingException;
}
