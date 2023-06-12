package fr.kiyauden._2ddoc;

import com.google.inject.Inject;
import fr.kiyauden._2ddoc.Parsed2DDoc.Parsed2DDocBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import static fr.kiyauden._2ddoc.Constants.GS;
import static fr.kiyauden._2ddoc.Constants.US;
import static fr.kiyauden._2ddoc.DataSource.ANNEX;
import static fr.kiyauden._2ddoc.DataSource.MESSAGE;

/**
 * Implementation of {@link Parser}
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class DefaultParser implements Parser {

    private final IHeaderService headerService;
    private final IDataService dataService;
    private final ISignatureService signatureService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Parsed2DDoc parse(final String input, final List<X509Certificate> certificates)
            throws UnsupportedException, ParsingException {
        final Parsed2DDocBuilder builder = Parsed2DDoc.builder();

        // Header extraction
        final Header header;
        try {
            // Whole input is passed because the IHeaderService will only take the necessary parts
            header = headerService.parseHeader(input);
        } catch (final NotFoundException e) {
            // If not found, it means that the input is not a 2D-DOC
            throw new IllegalArgumentException(e);
        } catch (final HeaderExtractionException e) {
            throw new ParsingException(e);
        }

        final int headerLength = header.getVersion().getHeaderLength();
        final int signatureStart = input.lastIndexOf(US) + 1;

        final String messageSegment = input.substring(headerLength, signatureStart - 1);

        final List<Data> data;

        // Data extraction
        final List<DataType> missingMandatoryDataFromMessage;
        try {
            final ExtractedData extractedDataFromMessage = dataService.extractData(messageSegment,
                                                                                   header.getDocumentType(),
                                                                                   MESSAGE);
            data = new ArrayList<>(extractedDataFromMessage.getData());
            missingMandatoryDataFromMessage = extractedDataFromMessage.getMissingMandatoryData();
        } catch (final DataExtractionException e) {
            throw new ParsingException(e);
        }

        // Checks if 2D-DOC has annex
        final int annexStart = input.indexOf(GS, signatureStart);
        if (annexStart > -1) {
            final String annexSegment = input.substring(annexStart + 1);
            try {
                final ExtractedData extractedDataFromAnnex = dataService.extractData(annexSegment,
                                                                                     header.getDocumentType(),
                                                                                     ANNEX);
                data.addAll(extractedDataFromAnnex.getData());
            } catch (final DataExtractionException e) {
                throw new ParsingException(e);
            }
        }

        // Signature verification
        final SignatureStatus signatureStatus;
        try {
            final int signatureEnd = annexStart > -1 ? annexStart : input.length();
            signatureStatus = signatureService.verifySignature(input.substring(0, signatureStart - 1),
                                                               input.substring(signatureStart,
                                                                               signatureEnd),
                                                               header.getCertificationAuthorityId(),
                                                               header.getCertificateId(), certificates);
        } catch (final SignatureVerificationException e) {
            throw new ParsingException(e);
        }

        builder.header(header);
        builder.signatureStatus(signatureStatus);
        builder.raw(input);

        // No mandatory data check on annex
        builder.extractedData(new ExtractedData(data, missingMandatoryDataFromMessage));

        builder.valid(computeValidity(signatureStatus, missingMandatoryDataFromMessage));

        return builder.build();
    }

    private boolean computeValidity(final SignatureStatus signatureStatus,
                                    final List<DataType> missingMandatoryDataFromMessage) {
        final boolean signatureValid = signatureStatus.isValid();
        final boolean hasNoMissingData = missingMandatoryDataFromMessage.isEmpty();

        if (!signatureValid) {
            log.debug("The signature is invalid, 2D-DOC will be set to invalid");
        }
        if (!hasNoMissingData) {
            log.debug("The 2D-DOC has missing data, will be set to invalid");
        }

        return signatureValid && hasNoMissingData;
    }

}
