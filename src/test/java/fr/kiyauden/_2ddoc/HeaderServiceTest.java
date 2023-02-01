package fr.kiyauden._2ddoc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static fr.kiyauden._2ddoc.Document.DOCUMENT_01;
import static fr.kiyauden._2ddoc.Header.ofVersion02;
import static fr.kiyauden._2ddoc.Header.ofVersion03;
import static fr.kiyauden._2ddoc.Header.ofVersion04;
import static java.time.LocalDate.of;
import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HeaderServiceTest {

    private static final LocalDate DATE = of(2023, JANUARY, 30);

    @Mock
    private IParserService parserServiceMock;

    private IHeaderService headerService;


    @BeforeEach
    void beforeEach() throws ParsingException {
        headerService = new HeaderService(parserServiceMock);
    }

    @Test
    void parseHeader_forFormat02_shouldParse()
            throws UnsupportedException, ParsingException, NotFoundException, HeaderExtractionException {
        // Mocks parser service for the dates, since it is not the job of the HeaderService to parse the date
        when(parserServiceMock.parse(anyString(), eq(DataFormat.DATE))).thenReturn(DATE);

        final String headerString = "DC02FR0AXT4A0E840E8A01";
        final Header expectedHeader = ofVersion02(
                "FR0A",
                "XT4A",
                DATE,
                DATE,
                DOCUMENT_01
        );

        final Header actualHeader = headerService.parseHeader(headerString);
        assertEquals(expectedHeader, actualHeader);
    }

    @Test
    void parseHeader_forFormat03_shouldParse()
            throws UnsupportedException, ParsingException, NotFoundException, HeaderExtractionException {
        // Mocks parser service for the dates, since it is not the job of the HeaderService to parse the date
        when(parserServiceMock.parse(anyString(), eq(DataFormat.DATE))).thenReturn(DATE);

        final String headerString = "DC03FR0AXT4A0E840E8A0101";
        final Header expectedHeader = ofVersion03(
                "FR0A",
                "XT4A",
                DATE,
                DATE,
                DOCUMENT_01,
                "01"
        );

        final Header actualHeader = headerService.parseHeader(headerString);
        assertEquals(expectedHeader, actualHeader);
    }

    @Test
    void parseHeader_forFormat03_andPerimeterIDIsUnknown_shouldThrowException() {
        // Perimeter ID FF is not supported / unknown
        final String headerString = "DC03FR0AXT4A0E840E8A01FF";
        assertThrows(UnsupportedException.class, () -> headerService.parseHeader(headerString));
    }

    @Test
    void parseHeader_forFormat04_shouldParse()
            throws UnsupportedException, ParsingException, NotFoundException, HeaderExtractionException {
        // Mocks parser service for the dates, since it is not the job of the HeaderService to parse the date
        when(parserServiceMock.parse(anyString(), eq(DataFormat.DATE))).thenReturn(DATE);

        final String headerString = "DC04FR0AXT4A0E840E8A0101FR";
        final Header expectedHeader = ofVersion04(
                "FR0A",
                "XT4A",
                DATE,
                DATE,
                DOCUMENT_01,
                "01",
                "FR"
        );

        final Header actualHeader = headerService.parseHeader(headerString);
        assertEquals(expectedHeader, actualHeader);
    }

    @Test
    void parseHeader_forFormat04_andPerimeterIDIsUnknown_shouldThrowException() {
        // Perimeter ID FF is not supported / unknown
        final String headerString = "DC04FR0AXT4A0E840E8A01FFFR";
        assertThrows(UnsupportedException.class, () -> headerService.parseHeader(headerString));
    }

    @Test
    void parseHeader_forUnknownFormat_shouldThrowException() {
        // Format 99 does not exist
        final String headerString = "DC99FR0AXT4A0E840E8A0101FR";

        assertThrows(UnsupportedException.class, () -> headerService.parseHeader(headerString));
    }

    @Test
    void parseHeader_forFormat01_shouldThrowException() {
        // Format 01 is deprecated, this library should not support it
        final String headerString = "DC01FR0AXT4A0E840E8A0101FR";

        final UnsupportedException unsupportedException = assertThrows(UnsupportedException.class,
                                                                       () -> headerService.parseHeader(headerString));
        assertEquals("Version 01 is deprecated by the ANTS and is not supported by this library",
                     unsupportedException.getMessage());
    }

    @Test
    void parseHeader_whenUnsupportedDocument_shouldThrowException() {
        // Document FF is not supported / unknown
        final String headerString = "DC04FR0AXT4A0E840E8AFF01FR";

        assertThrows(UnsupportedException.class, () -> headerService.parseHeader(headerString));
    }

    @Test
    void parseHeader_whenDatesExtractionFails_shouldThrowException() throws ParsingException {
        when(parserServiceMock.parse(anyString(), eq(DataFormat.DATE))).thenThrow(ParsingException.class);

        final String headerString = "DC04FR0AXT4A0E840E8A0101FR";

        assertThrows(HeaderExtractionException.class, () -> headerService.parseHeader(headerString));
    }

}
