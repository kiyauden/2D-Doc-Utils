package fr.kiyauden._2ddoc;

/**
 * Representing the place where a data was extracted
 */
public enum DataSource {
    /**
     * Message segment
     * <p>
     * Between the header and the signature
     * <p>
     * The data extracted from this place is signed, can be trusted
     */
    MESSAGE,
    /**
     * Annex segment
     * <p>
     * After the signature to the end of the 2D-Doc
     * <p>
     * The data extracted from this place <strong>NOT</strong> signed, <strong>CANNOT</strong> be trusted</p>
     */
    ANNEX
}
