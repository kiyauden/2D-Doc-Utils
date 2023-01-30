package fr.kiyauden._2ddoc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static fr.kiyauden._2ddoc.DataFormat.URL;
import static org.junit.jupiter.api.Assertions.assertEquals;

class URLParserTest {

    private URLParser parser;

    @BeforeEach
    void beforeEach() {
        parser = new URLParser();
    }

    @Test
    void getHandledFormat_shouldReturnURL() {
        assertEquals(URL, parser.getHandledFormat());
    }

    @Test
    void parse_shouldParseData() {
        assertEquals("huissier-justice.fr/1896547853AB",
                     parser.parse("NB2WS43TNFSXELLKOVZXI2LDMUXGM4RPGE4DSNRVGQ3TQNJTIFBA"));
    }

}
