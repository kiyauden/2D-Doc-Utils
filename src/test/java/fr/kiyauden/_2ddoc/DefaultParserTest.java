package fr.kiyauden._2ddoc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;

import static fr.kiyauden._2ddoc.DataSource.ANNEX;
import static fr.kiyauden._2ddoc.DataSource.MESSAGE;
import static fr.kiyauden._2ddoc.Document.DOC_01;
import static fr.kiyauden._2ddoc.Header.ofVersion04;
import static fr.kiyauden._2ddoc.SignatureStatus.VALID;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultParserTest {

    private final LocalDate date = LocalDate.of(2023, Month.FEBRUARY, 5);

    @Mock
    private IHeaderService headerService;
    @Mock
    private IDataService dataService;
    @Mock
    private ISignatureService signatureService;

    private Parser parser;


    @BeforeEach
    void beforeEach() {
        parser = new DefaultParser(headerService, dataService, signatureService);
    }

    @Test
    void parse_whenInputIsNotA2DDoc_shouldThrowException()
            throws UnsupportedException, HeaderExtractionException, NotFoundException {
        when(headerService.parseHeader("Something")).thenThrow(NotFoundException.class);

        assertThrows(IllegalArgumentException.class,
                     () -> parser.parse("Something", emptyList())
        );
    }

    @Test
    void parse_when2DDDocIsUnsupported_shouldThrowException()
            throws UnsupportedException, HeaderExtractionException, NotFoundException {
        when(headerService.parseHeader("Something")).thenThrow(UnsupportedException.class);

        assertThrows(UnsupportedException.class,
                     () -> parser.parse("Something", emptyList())
        );
    }

    @Test
    void parse_whenHeaderExtractionFails_shouldThrowException()
            throws UnsupportedException, HeaderExtractionException, NotFoundException {
        when(headerService.parseHeader("Something")).thenThrow(HeaderExtractionException.class);

        assertThrows(ParsingException.class,
                     () -> parser.parse("Something", emptyList())
        );
    }

    @Test
    void parse_whenDataExtractionFails_shouldThrowException()
            throws UnsupportedException, HeaderExtractionException, NotFoundException, DataExtractionException {

        final String input = "DC04FR0AXT4A0E840E8A0101FR26FR247500010MME/NATACHA/SPECIMEN\u001D221 RUE DE LA RUE\u001D1812345678910112\u001D02FACTURE FNB\u001D03GP\u001D1D9,99\u001D19575645792\u001D07195113\u001FGJOZJCU2HBPFIQEJ2IHWPQOQTGURB6LCELPXCH3LVS574NM27UTYRHUIMZWEDFJQVVKIAGWIIF72IV6YFNZTUGYTBXSQO2LOOV6BEMY";

        final Header header = ofVersion04("FR0A", "AXT4", date, date,
                                          DOC_01, "01", "FR");
        when(headerService.parseHeader(input)).thenReturn(header);

        when(dataService.extractData(anyString(), eq(DOC_01), any())).thenThrow(DataExtractionException.class);

        assertThrows(ParsingException.class,
                     () -> parser.parse(input, emptyList())
        );
    }

    @Test
    void parse_whenSignatureVerificationFails_shouldThrowException()
            throws UnsupportedException, HeaderExtractionException, NotFoundException, DataExtractionException,
            SignatureVerificationException {

        final String input = "DC04FR0AXT4A0E840E8A0101FR26FR247500010MME/NATACHA/SPECIMEN\u001D221 RUE DE LA RUE\u001D1812345678910112\u001D02FACTURE FNB\u001D03GP\u001D1D9,99\u001D19575645792\u001D07195113\u001FGJOZJCU2HBPFIQEJ2IHWPQOQTGURB6LCELPXCH3LVS574NM27UTYRHUIMZWEDFJQVVKIAGWIIF72IV6YFNZTUGYTBXSQO2LOOV6BEMY";

        final Header header = ofVersion04("FR0A", "AXT4", date, date,
                                          DOC_01, "01", "FR");
        when(headerService.parseHeader(input)).thenReturn(header);

        when(dataService.extractData(anyString(), eq(DOC_01), any())).thenReturn(
                new ExtractedData(emptyList(), emptyList()));

        when(signatureService.verifySignature(any(), any(), any(), any(), any())).thenThrow(
                SignatureVerificationException.class);

        assertThrows(ParsingException.class,
                     () -> parser.parse(input, emptyList())
        );
    }

    @Test
    void parse_shouldParse()
            throws UnsupportedException, HeaderExtractionException, NotFoundException, DataExtractionException,
            SignatureVerificationException, ParsingException {

        final String input = "DC04FR0AXT4A0E840E8A0101FR26FR247500010MME/NATACHA/SPECIMEN\u001D221 RUE DE LA RUE\u001D1812345678910112\u001D02FACTURE FNB\u001D03GP\u001D1D9,99\u001D19575645792\u001D07195113\u001FGJOZJCU2HBPFIQEJ2IHWPQOQTGURB6LCELPXCH3LVS574NM27UTYRHUIMZWEDFJQVVKIAGWIIF72IV6YFNZTUGYTBXSQO2LOOV6BEMY\u001D221 RUE DE LA RUE\u001D02FACTURE FNB\u001D";
        final String headerString = "DC04FR0AXT4A0E840E8A0101FR";
        final String message = "26FR247500010MME/NATACHA/SPECIMEN\u001D221 RUE DE LA RUE\u001D1812345678910112\u001D02FACTURE FNB\u001D03GP\u001D1D9,99\u001D19575645792\u001D07195113";
        final String signature = "GJOZJCU2HBPFIQEJ2IHWPQOQTGURB6LCELPXCH3LVS574NM27UTYRHUIMZWEDFJQVVKIAGWIIF72IV6YFNZTUGYTBXSQO2LOOV6BEMY";
        final String annex = "221 RUE DE LA RUE\u001D02FACTURE FNB\u001D";

        final Header header = ofVersion04("FR0A", "AXT4", date, date,
                                          DOC_01, "01", "FR");
        when(headerService.parseHeader(input)).thenReturn(header);
        when(dataService.extractData(anyString(), eq(DOC_01), any())).thenReturn(
                new ExtractedData(emptyList(), emptyList()));
        when(signatureService.verifySignature(any(), any(), any(), any(), any())).thenReturn(VALID);

        final Parsed2DDoc parsed2DDoc = parser.parse(input, emptyList());

        verify(headerService, times(1)).parseHeader(input);
        verify(dataService, times(1)).extractData(message, DOC_01, MESSAGE);
        verify(dataService, times(1)).extractData(annex, DOC_01, ANNEX);
        verify(signatureService, times(1)).verifySignature(headerString + message, signature,
                                                           header.getCertificationAuthorityId(),
                                                           header.getCertificateId(), emptyList());

        assertTrue(parsed2DDoc.isValid());
    }

}
