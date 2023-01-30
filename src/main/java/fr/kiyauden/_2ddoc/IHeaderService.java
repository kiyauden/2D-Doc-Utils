package fr.kiyauden._2ddoc;

/**
 * Service whose job is to parse the 2D-DOC header
 */
interface IHeaderService {

    /**
     * Parses the 2D-DOC header
     *
     * @param header the string representation of the 2D-DOC header
     * @return the parsed header
     * @throws ParsingException     when an error occurs while parsing the header
     * @throws UnsupportedException when the 2D-DOC version was recognized but not supported
     * @throws NotFoundException    when the header format does not look like a 2D-DOC header
     */
    Header parseHeader(final String header) throws ParsingException, UnsupportedException, NotFoundException;

}
