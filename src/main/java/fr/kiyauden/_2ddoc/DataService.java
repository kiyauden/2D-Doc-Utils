package fr.kiyauden._2ddoc;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fr.kiyauden._2ddoc.Constants.GS;
import static fr.kiyauden._2ddoc.Constants.RS;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

/**
 * Implementation of {@link IDataService}
 */
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Singleton
class DataService implements IDataService {

    private final IParserService parserService;
    private final IDocumentService documentService;

    /**
     * {@inheritDoc}
     */
    @Override
    public ExtractedData extractData(final String data, final Document document, final DataSource source)
            throws DataExtractionException {
        log.debug("Parsing data {} from {} for document {}", data, source, document);
        final ArrayList<Data> dataList = new ArrayList<>();

        int i = 0;
        int j;
        while (i < data.length()) {
            // Look for an identifier
            log.trace("New iteration, i is {}", i);
            final String dataIdentifier = data.substring(i, i + 2);
            log.trace("Found identifier {}", dataIdentifier);

            final Optional<DataType> dataTypeOptional = DataType.findById(dataIdentifier);
            log.trace("Identifier parsed as {}", dataTypeOptional);

            if (!dataTypeOptional.isPresent()) {
                // TODO: 12/06/2023 Peut être ignorer cette donnée au lieu de lancer une exception
                throw new DataExtractionException(format("Malformed data, identifier %s found", dataIdentifier));
            }
            final DataType dataType = dataTypeOptional.get();

            if (dataType.isFixedLength()) {
                // Extracts fixed length data
                final int maxLength = dataType.getMaxLength();
                final int beginIndex = i + 2;
                final int endIndex = beginIndex + maxLength;
                final String value = data.substring(i + 2, endIndex);
                log.trace("Datatype is fixed length, taking {} characters, with value {}", maxLength, value);
                addData(dataList, value, document, source, dataType);
                i = i + 2 + maxLength;
                log.trace("i advancing forward of {} positions, new position is {}", 2 + maxLength, i);
            } else if (dataType.hasMaxLength()) {
                // Case where data is variable length with a max length
                log.trace("Datatype is variable length, min {}, max {}", dataType.getMinLength(),
                          dataType.getMaxLength());

                j = i + 2;
                log.trace("Setting j to {}", j);
                final int maxPos = i + 2 + dataType.getMaxLength();
                boolean found = false;
                while (!found && j <= maxPos) {
                    log.trace("j is now {}", j);
                    // Truncated data
                    if (data.charAt(j) == RS) {
                        final int beginIndex = i + 2;
                        final int endIndex = j;
                        final String value = data.substring(beginIndex, endIndex);
                        log.trace("Found RS char (data is truncated), data is from {} to {} with value \"{}\"",
                                  beginIndex, endIndex, value);

                        addTruncatedData(dataList, value, document, source, dataType);
                        i = j + 1;
                        log.trace("Advancing i to {}", i);
                        found = true;
                    }
                    // Not truncated data
                    else if (data.charAt(j) == GS) {
                        final int beginIndex = i + 2;
                        final int endIndex = j;
                        final String value = data.substring(beginIndex, endIndex);
                        log.trace("Found GS char (data is not truncated), data is from {} to {} with value \"{}\"",
                                  beginIndex, endIndex, value);

                        addData(dataList, value, document, source, dataType);
                        i = j + 1;
                        log.trace("Advancing i to {}", i);
                        found = true;
                    }
                    // Not truncated data at the end of the data
                    else if (j >= data.length() - 1) {
                        final int beginIndex = i + 2;
                        final int endIndex = data.length();
                        final String value = data.substring(beginIndex, endIndex);
                        log.trace(
                                "End of data segment, GS was not found but is optional, taking data from {} to {}, with value \"{}\"",
                                beginIndex, endIndex, value);
                        addData(dataList, value, document, source, dataType);
                        i = j + 1;
                        log.trace("Advancing i to {}", i);
                        found = true;
                    }
                    j++;
                    log.trace("Nothing found yet, incrementing j");
                }
                // If the data exceed its max length
                if (!found) {
                    throw new DataExtractionException(
                            format("Data %s is overflowing, max length %d", dataType, dataType.getMaxLength()));
                }
            } else {
                // Case where data is variable length with no max length
                log.trace("Datatype is variable length with no max length, min {}", dataType.getMinLength());
                j = i + 2;
                log.trace("Setting j to {}", j);
                boolean found = false;
                while (!found && j <= data.length()) {
                    // Truncated data
                    if (data.charAt(j) == RS) {
                        final int beginIndex = i + 2;
                        final int endIndex = j;
                        final String value = data.substring(beginIndex, endIndex);
                        log.trace("Found RS char (data is truncated), data is from {} to {} with value \"{}\"",
                                  beginIndex, endIndex, value);

                        addTruncatedData(dataList, value, document, source, dataType);
                        i = j + 1;
                        log.trace("Advancing i to {}", i);
                        found = true;
                    }
                    // Not truncated data
                    else if (data.charAt(j) == GS) {
                        final int beginIndex = i + 2;
                        final int endIndex = j;
                        final String value = data.substring(beginIndex, endIndex);
                        log.trace("Found GS char (data is not truncated), data is from {} to {} with value \"{}\"",
                                  beginIndex, endIndex, value);

                        addData(dataList, value, document, source, dataType);
                        i = j + 1;
                        log.trace("Advancing i to {}", i);
                        found = true;
                    }
                    // Not truncated data at the end of the data
                    else if (j >= data.length() - 1) {
                        final int beginIndex = i + 2;
                        final int endIndex = data.length();
                        final String value = data.substring(beginIndex, endIndex);
                        log.trace(
                                "End of data segment, GS was not found but is optional, taking data from {} to {}, with value \"{}\"",
                                beginIndex, endIndex, value);
                        addData(dataList, value, document, source, dataType);
                        i = j + 1;
                        log.trace("Advancing i to {}", i);
                        found = true;
                    }
                    j++;
                    log.trace("Nothing found yet, incrementing j");
                }
            }
        }

        // Calculate missing data
        if (!source.equals(DataSource.ANNEX)) {
            final List<DataType> collect = dataList.stream()
                    .map(Data::getDataType)
                    .collect(toList());

            return new ExtractedData(dataList, documentService.computeMissingData(document, collect));
        }

        return new ExtractedData(dataList, ImmutableList.of());
    }

    private void addData(final ArrayList<Data> data, final String value, final Document document,
                         final DataSource source,
                         final DataType dataType, final boolean truncated) throws DataExtractionException {
        final Data build = Data.builder()
                .dataType(dataType)
                .source(source)
                .value(parseValue(value, dataType))
                .stringValue(value)
                .truncated(truncated)
                .mandatory(documentService.isDataMandatory(document, dataType))
                .build();
        data.add(build);
    }

    private Object parseValue(final String value, final DataType dataType) throws DataExtractionException {
        if (value == null || value.length() == 0) {
            return null;
        }
        try {
            return parserService.parse(value, dataType.getType());
        } catch (final ParsingException e) {
            throw new DataExtractionException(format("Parsing of data %s with value %s failed", dataType, value), e);
        }
    }

    private void addData(final ArrayList<Data> data, final String value, final Document document,
                         final DataSource source,
                         final DataType dataType) throws DataExtractionException {
        addData(data, value, document, source, dataType, false);
    }

    private void addTruncatedData(final ArrayList<Data> data, final String value, final Document document,
                                  final DataSource source,
                                  final DataType dataType) throws DataExtractionException {
        addData(data, value, document, source, dataType, true);
    }

}
