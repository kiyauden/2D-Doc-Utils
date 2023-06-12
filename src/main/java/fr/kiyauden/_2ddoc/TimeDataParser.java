package fr.kiyauden._2ddoc;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static fr.kiyauden._2ddoc.DataFormat.TIME;
import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;

/**
 * Parser implementation for {@link DataFormat#DATE}
 * <p>
 * The {@link String} input will be parsed to an {@link LocalDate}
 */
@Slf4j
class TimeDataParser implements DataParser<LocalTime> {
    /**
     * The formatter to parse the time
     */
    private static final TimeFormater FORMATTER = new TimeFormater("HHmmss");


    /**
     * {@inheritDoc}
     */
    @Override
    public LocalTime parse(final String timeString) throws ParsingException {
        log.debug("Parsing time \"{}\"", timeString);
        try {
            final LocalTime parsed = LocalTime.parse(timeString, FORMATTER.getFormatter());
            log.trace("Formatter \"{}\", successfully parsed data \"{}\"", FORMATTER.getPattern(), timeString);
            return parsed;
        } catch (final DateTimeParseException e) {
            // Ignore and try next formatter
            log.trace("Formatter \"{}\", could not parse data \"{}\"", FORMATTER.getPattern(), timeString);
            throw new ParsingException(
                    format("Formatter \"%s\", could not parse data \"%s\"", FORMATTER.getPattern(), timeString));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataFormat getHandledFormat() {
        return TIME;
    }

    @Value
    private static class TimeFormater {
        /**
         * The pattern
         */
        String pattern;
        /**
         * The DateTimeFormatter created using the pattern
         */
        DateTimeFormatter formatter;

        TimeFormater(final String pattern) {
            this.pattern = pattern;
            formatter = ofPattern(pattern);
        }
    }

}
