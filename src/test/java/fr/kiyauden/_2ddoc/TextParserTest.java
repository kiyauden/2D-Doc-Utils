package fr.kiyauden._2ddoc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static fr.kiyauden._2ddoc.DataFormat.TEXT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TextParserTest {

    private TextParser parser;

    @BeforeEach
    void beforeEach() {
        parser = new TextParser();
    }

    @Test
    void getHandledFormat_shouldReturnTEXT() {
        assertEquals(TEXT, parser.getHandledFormat());
    }

    @Test
    void parse_shouldReturnInputDataAsIs() {
        assertEquals("     DATA  ", parser.parse("     DATA  "));
    }

}
