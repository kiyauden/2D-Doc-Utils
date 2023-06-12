package fr.kiyauden._2ddoc;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Scanner;

import static fr.kiyauden._2ddoc.DataSource.MESSAGE;
import static fr.kiyauden._2ddoc.DataType.BENEFICIARY_ADDRESS_LINE_1;
import static fr.kiyauden._2ddoc.DataType.BENEFIT_SERVICE_POINT_ADDRESS_LINE_4;
import static fr.kiyauden._2ddoc.DataType.BENEFIT_SERVICE_POINT_COUNTRY;
import static fr.kiyauden._2ddoc.DataType.BENEFIT_SERVICE_POINT_POSTAL_OR_CEDEX_CODE;
import static fr.kiyauden._2ddoc.Document.DOC_01;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EndToEndTest {

    private static X509Certificate fr00Certificate;

    @BeforeAll
    static void beforeAll() throws CertificateException {
        final InputStream is = EndToEndTest.class.getClassLoader().getResourceAsStream("e2e/FR00.pem");
        final CertificateFactory cf = CertificateFactory.getInstance("X.509");
        fr00Certificate = (X509Certificate) cf.generateCertificate(is);
    }

    @Test
    void endToEndTest_forDocument01() throws UnsupportedException, ParsingException {
        final Parser parser = Utils2dDoc.newParser();

        final Parsed2DDoc parsed = parser.parse(load2ddoc("01"), singletonList(fr00Certificate));

        final Header header = Header.ofVersion03("FR00", "0001", LocalDate.of(2012, Month.OCTOBER, 15),
                                                 LocalDate.of(2015, Month.JULY, 27),
                                                 DOC_01, "01");

        final Data data26 = Data.builder()
                .dataType(BENEFIT_SERVICE_POINT_COUNTRY)
                .value("FR")
                .stringValue("FR")
                .mandatory(true)
                .truncated(false)
                .source(MESSAGE)
                .build();

        final Data data24 = Data.builder()
                .dataType(BENEFIT_SERVICE_POINT_POSTAL_OR_CEDEX_CODE)
                .value("75000")
                .stringValue("75000")
                .mandatory(true)
                .truncated(false)
                .source(MESSAGE)
                .build();

        final Data data10 = Data.builder()
                .dataType(BENEFICIARY_ADDRESS_LINE_1)
                .value("MME/SPECIMEN/NATACHA")
                .stringValue("MME/SPECIMEN/NATACHA")
                .mandatory(true)
                .truncated(false)
                .source(MESSAGE)
                .build();

        final Data data22 = Data.builder()
                .dataType(BENEFIT_SERVICE_POINT_ADDRESS_LINE_4)
                .value("145 AVENUE DES SPECIMENS")
                .stringValue("145 AVENUE DES SPECIMENS")
                .mandatory(true)
                .truncated(false)
                .source(MESSAGE)
                .build();

        final List<Data> allData = asList(data26, data24, data10, data22);

        assertEquals(header, parsed.getHeader());
        assertEquals(allData, parsed.getExtractedData().getData());
        // TODO: 12/06/2023 Trouver comment tester la validité avec le certificat qui est expiré
    }

    private String load2ddoc(final String name) {
        return new Scanner(EndToEndTest.class.getClassLoader().getResourceAsStream("e2e/2ddoc/" + name + ".2ddoc"),
                           "UTF-8").useDelimiter("\\A")
                .next();
    }

}
