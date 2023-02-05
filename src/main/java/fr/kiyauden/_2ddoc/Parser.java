package fr.kiyauden._2ddoc;

import java.security.cert.X509Certificate;
import java.util.List;

/**
 * Interface for a parser
 */
public interface Parser {

    /**
     * Parses a 2D-DOC
     *
     * @param input        the input 2D-DOC string representation
     * @param certificates the list of certificates to use for the signature verification
     * @return an object representation of a 2D-DOC
     * @throws IllegalArgumentException when the input is not a 2D-DOC
     * @throws UnsupportedException     when the 2D-DOC version is not supported or the document is not supported
     * @throws ParsingException         when any other error happens
     */
    Parsed2DDoc parse(String input, List<X509Certificate> certificates) throws UnsupportedException, ParsingException;

}
