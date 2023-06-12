package fr.kiyauden._2ddoc;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Optional;

import static fr.kiyauden._2ddoc.DataFormat.DATE;
import static fr.kiyauden._2ddoc.Header.ofVersion02;
import static fr.kiyauden._2ddoc.Header.ofVersion03;
import static fr.kiyauden._2ddoc.Header.ofVersion04;
import static fr.kiyauden._2ddoc.Version.VERSION_02;
import static fr.kiyauden._2ddoc.Version.VERSION_03;
import static fr.kiyauden._2ddoc.Version.VERSION_04;
import static fr.kiyauden._2ddoc.Version.findByVersionString;
import static java.lang.String.format;

/**
 * Implementation of {@link IHeaderService}
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Singleton
class HeaderService implements IHeaderService {

    private final IParserService parserService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Header parseHeader(final String header)
            throws UnsupportedException, NotFoundException, HeaderExtractionException {
        log.debug("Parsing header \"{}\"", header);

        // Parses the identification marker
        final String idMarker = header.substring(0, 2);
        log.trace("Identification marker is \"{}\"", idMarker);
        if (!"DC".equals(idMarker)) {
            log.warn("Not a 2D-DOC, header must start with \"DC\", was \"{}\"", idMarker);
            throw new NotFoundException(format("Not a 2D-DOC, header must start with \"DC\", was \"%s\"", idMarker));
        }

        // Parses the version
        final String versionString = header.substring(2, 4);
        log.trace("Version is \"{}\"", versionString);
        if ("01".equals(versionString)) {
            log.warn("Version 01 is deprecated by the ANTS and is not supported by this library");
            throw new UnsupportedVersionException(
                    "Version 01 is deprecated by the ANTS and is not supported by this library");
        }
        final Optional<Version> versionOptional = findByVersionString(versionString);
        if (!versionOptional.isPresent()) {
            log.warn("Version \"{}\" is not supported", versionString);
            throw new UnsupportedVersionException(format("Version \"%s\" is not supported", versionString));
        }

        // Extracts the certification authority
        final String certificationAuthority = header.substring(4, 8);
        log.trace("Certification authority is \"{}\"", certificationAuthority);
        // Extracts certificate identifier
        final String certificateIdentifier = header.substring(8, 12);
        log.trace("Certificate identifier is \"{}\"", certificateIdentifier);

        final LocalDate emissionDate;
        final LocalDate signatureDate;
        try {
            // Parses the emission date
            final String emissionDateString = header.substring(12, 16);
            log.trace("Emission HEX date is \"{}\"", emissionDateString);
            emissionDate = (LocalDate) parserService.parse(emissionDateString, DATE);
            log.trace("Parsed emission date is \"{}\"", emissionDate);

            // Parses the signature date
            final String signatureDateString = header.substring(16, 20);
            log.trace("Signature HEX date is \"{}\"", signatureDateString);
            signatureDate = (LocalDate) parserService.parse(signatureDateString, DATE);
            log.trace("Parsed signature date is \"{}\"", signatureDate);
        } catch (final ParsingException e) {
            log.warn("An error occurred while extracting dates the header");
            throw new HeaderExtractionException("An error occurred while extracting dates the header", e);
        }

        // Parses the document type
        final String documentTypeString = header.substring(20, 22);
        log.trace("Document type is \"{}\"", documentTypeString);
        final Optional<Document> documentOptional = Document.findById(documentTypeString);
        if (!documentOptional.isPresent()) {
            log.warn("Document type \"{}\" is not supported", documentTypeString);
            throw new UnsupportedDocumentException(format("Document type \"%s\" is not supported", documentTypeString));
        }
        final Document document = documentOptional.get();
        log.trace("Parsed document type is \"{}\"", document);

        Header parsedHeader = null;
        final Version version = versionOptional.get();

        if (VERSION_02.equals(version)) {
            parsedHeader = ofVersion02(certificationAuthority, certificateIdentifier, emissionDate, signatureDate,
                                       document);
        }

        String perimeterId = null;

        if (VERSION_03.equals(version) || VERSION_04.equals(version)) {
            // Extracts the perimeter identifier
            perimeterId = header.substring(22, 24);
            log.trace("Version is \"{}\", perimeter identifier is \"{}\"", versionString, perimeterId);

            if (!"01".equals(perimeterId)) {
                log.warn("Perimeter with id \"{}\" is not supported, only \"01\" is", perimeterId);
                throw new UnsupportedException(
                        format("Perimeter with id \"%s\" is not supported, only \"01\" is", perimeterId));
            }

            if (VERSION_03.equals(version)) {
                parsedHeader = ofVersion03(certificationAuthority, certificateIdentifier, emissionDate, signatureDate,
                                           document,
                                           perimeterId);
            }

        }
        if (VERSION_04.equals(version)) {
            // Extracts the issuing country
            final String issuingCountry = header.substring(24, 26);
            log.trace("Version is \"04\", issuing country is \"{}\"", issuingCountry);
            parsedHeader = ofVersion04(certificationAuthority, certificateIdentifier, emissionDate, signatureDate,
                                       document,
                                       perimeterId, issuingCountry);
        }

        log.debug("Header parsed as {}", parsedHeader);
        return parsedHeader;
    }
}
