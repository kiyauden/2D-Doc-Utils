package fr.kiyauden._2ddoc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static fr.kiyauden._2ddoc.DataFormat.DATE;
import static java.time.LocalDate.of;
import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateDataParserTest {

    private DateDataParser parser;

    @BeforeEach
    void beforeEach() {
        parser = new DateDataParser();
    }

    @Test
    void getHandledFormat_shouldReturnTEXT() {
        assertEquals(DATE, parser.getHandledFormat());
    }

    @Test
    void parse_forHeaderDate_whenDateIsFFFF_shouldReturnNull() throws ParsingException {
        assertNull(parser.parse("FFFF"));
    }

    @Test
    void parse_forHeaderDate_whenDateIsNotFFFF_shouldParseDate() throws ParsingException {
        final LocalDate expectedDate = of(2011, DECEMBER, 31);
        assertEquals(expectedDate, parser.parse("111E"));
    }

    @Test
    void parse_forMessageDateOfFormatJJMMAAA_shouldParseDate() throws ParsingException {
        final LocalDate expectedDate = of(2023, JANUARY, 29);
        assertEquals(expectedDate, parser.parse("29012023")); // JJMMAAA format
    }

    @Test
    void parse_forMessageDateOfFormatAAAAMMJJ_shouldParseDate() throws ParsingException {
        final LocalDate expectedDate = of(2023, JANUARY, 29);
        assertEquals(expectedDate, parser.parse("20230129")); // AAAAMMJJ format
    }

    @Test
    void parse_forMessageDate_whenFormatIsUnsupported_shouldThrowException() {
        assertThrows(ParsingException.class,
                     () -> {
                         parser.parse("2023-01-29"); // Unsupported format
                     }
        );
    }

}
