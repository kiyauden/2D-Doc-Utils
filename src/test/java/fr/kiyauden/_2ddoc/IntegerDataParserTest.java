package fr.kiyauden._2ddoc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static fr.kiyauden._2ddoc.DataFormat.INTEGER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IntegerDataParserTest {

    private IntegerDataParser parser;

    @BeforeEach
    void beforeEach() {
        parser = new IntegerDataParser();
    }

    @Test
    void getHandledFormat_shouldReturnINTEGER() {
        assertEquals(INTEGER, parser.getHandledFormat());
    }

    @Test
    void parse_shouldParseDataToInteger() {
        assertEquals(1, parser.parse("1"));
        assertEquals(1, parser.parse("00000001"));
        assertEquals(-1, parser.parse("-1"));
    }

}
