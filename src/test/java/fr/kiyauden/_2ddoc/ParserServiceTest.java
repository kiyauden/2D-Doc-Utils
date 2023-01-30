package fr.kiyauden._2ddoc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static fr.kiyauden._2ddoc.DataFormat.DATE;
import static fr.kiyauden._2ddoc.DataFormat.INTEGER;
import static fr.kiyauden._2ddoc.DataFormat.TEXT;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParserServiceTest {

    @Mock
    private Parser<String> stringParser;
    @Mock
    private Parser<Integer> integerParser;
    private IParserService parserService;


    @BeforeEach
    void beforeEach() {
        when(stringParser.getHandledFormat()).thenReturn(TEXT);
        when(integerParser.getHandledFormat()).thenReturn(INTEGER);

        parserService = new ParserService(new HashSet<>(asList(stringParser, integerParser)));
    }

    @Test
    void parse_whenDataTEXT_shouldUseTheCorrectParser() throws ParsingException {
        parserService.parse("DATA", TEXT);

        verify(stringParser, times(1)).parse("DATA");
        verify(integerParser, never()).parse("DATA");
    }

    @Test
    void parse_whenDataINTEGER_shouldUseTheCorrectParser() throws ParsingException {
        parserService.parse("DATA", INTEGER);

        verify(stringParser, never()).parse("DATA");
        verify(integerParser, times(1)).parse("DATA");
    }

    @Test
    void parse_whenParsingFails_shouldThrowException() throws ParsingException {
        // Simulates a parsing fail
        when(integerParser.parse("DATA")).thenThrow(NumberFormatException.class);

        assertThrows(ParsingException.class, () -> parserService.parse("DATA", INTEGER));
    }

    @Test
    void parse_whenNoFormatterForDataType_shouldThrowException() {
        assertThrows(ParsingException.class, () -> parserService.parse("DATA", DATE));
    }

}
