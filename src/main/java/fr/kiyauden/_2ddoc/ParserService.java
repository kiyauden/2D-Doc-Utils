package fr.kiyauden._2ddoc;

import lombok.extern.slf4j.Slf4j;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;

/**
 * Implementation of {@link IParserService}
 */
@Slf4j
class ParserService implements IParserService {

    /**
     * Map used to retrieve the {@link Parser} with its handled {@link DataFormat}
     */
    private final Map<DataFormat, Parser<?>> dataFormatParserMap;

    ParserService(final Set<Parser<?>> parsers) {
        // Initialises the dataFormatParserMap
        dataFormatParserMap = parsers.stream()
                .collect(toMap(Parser::getHandledFormat,
                               parser -> parser,
                               (parser, parser2) -> parser2,
                               () -> new EnumMap<>(DataFormat.class))
                );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object parse(final String value, final DataFormat dataFormat) throws ParsingException {
        log.debug("Parsing value \"{}\" of type {}", value, dataFormat);
        try {
            final Parser<?> parser = dataFormatParserMap.get(dataFormat);
            if (parser == null) {
                log.warn("No parser found for data format \"{}\"", dataFormat);
                throw new ParsingException(format("No parser found for data format \"%s\"", dataFormat));
            }
            final Object parsed = parser.parse(value);
            log.debug("Parsing value \"{}\" of type {} parsed as \"{}\"", value, dataFormat, parsed);
            return parsed;
        } catch (final Exception e) {
            log.warn("An error occurred during the parsing of the value \"{}\" of type {}", value, dataFormat);
            throw new ParsingException(
                    format("An error occurred during the parsing of the value \"%s\" of type \"%s\"", value,
                           dataFormat),
                    e);
        }
    }
}
