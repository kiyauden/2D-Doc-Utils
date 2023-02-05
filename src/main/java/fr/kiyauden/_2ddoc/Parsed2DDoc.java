package fr.kiyauden._2ddoc;

import lombok.Builder;
import lombok.Value;

import java.util.List;

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
    List<Data> data;
    /**
     * The status of the signature
     */
    SignatureStatus signatureStatus;
    /**
     * The raw data of the 2D-DOC
     */
    String raw;

}
