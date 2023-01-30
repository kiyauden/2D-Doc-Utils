package fr.kiyauden._2ddoc;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

/**
 * Constants used by the library
 */
@NoArgsConstructor(access = PRIVATE)
class Constants {

    /**
     * ASCII group separator
     * <p>
     * Decimal 29, Hexadecimal 1D
     * <p>
     * Used to indicate the end of a variable length data
     */
    static final char GS = 0x1D;

    /**
     * ASCII record separator
     * <p>
     * Decimal 30, Hexadecimal 1E
     * <p>
     * Used to indicate the end of a truncated variable length data
     */
    static final char RS = 0x1E;

    /**
     * ASCII unit separator
     * <p>
     * Decimal 31, Hexadecimal 1F
     * <p>
     * Used to indicate the start of the signature
     */
    static final char US = 0x1F;

}
