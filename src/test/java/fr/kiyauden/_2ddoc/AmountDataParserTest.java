package fr.kiyauden._2ddoc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static fr.kiyauden._2ddoc.DataFormat.AMOUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AmountDataParserTest {

    private AmountDataParser parser;

    @BeforeEach
    void beforeEach() {
        parser = new AmountDataParser();
    }

    @Test
    void getHandledFormat_shouldReturnAMOUNT() {
        assertEquals(AMOUNT, parser.getHandledFormat());
    }

    @Test
    void parse_shouldParse() throws ParsingException {
        assertEquals(0.0d, parser.parse("0,00"));
        assertEquals(9.99d, parser.parse("9,99"));
        assertEquals(15798.54d, parser.parse("15798,54"));
        assertEquals(-3.12d, parser.parse("-3,12"));
    }

    @Test
    void parse_whenFormatIsUnsupported_shouldThrowException() {
        assertThrows(ParsingException.class,
                     () -> {
                         parser.parse("--3"); // Unsupported format
                     }
        );
    }

}
