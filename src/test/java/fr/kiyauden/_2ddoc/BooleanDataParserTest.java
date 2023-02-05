package fr.kiyauden._2ddoc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static fr.kiyauden._2ddoc.DataFormat.BOOLEAN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BooleanDataParserTest {

    private BooleanDataParser parser;

    @BeforeEach
    void beforeEach() {
        parser = new BooleanDataParser();
    }

    @Test
    void getHandledFormat_shouldReturnBOOLEAN() {
        assertEquals(BOOLEAN, parser.getHandledFormat());
    }

    @Test
    void parse_shouldParseData() throws ParsingException {
        assertEquals(true, parser.parse("1"));
        assertEquals(false, parser.parse("0"));
    }

    @Test
    void parse_whenFormatIsUnsupported_shouldThrowException() {
        assertThrows(ParsingException.class,
                     () -> {
                         parser.parse("SOMETHING"); // Unsupported format
                     }
        );
    }

}
