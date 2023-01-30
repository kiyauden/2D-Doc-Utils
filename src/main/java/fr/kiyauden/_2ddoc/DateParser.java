package fr.kiyauden._2ddoc;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static fr.kiyauden._2ddoc.DataFormat.DATE;
import static java.lang.Integer.decode;
import static java.lang.String.format;
import static java.time.LocalDate.of;
import static java.time.Month.JANUARY;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.asList;
import static java.util.regex.Pattern.compile;

/**
 * Parser implementation for {@link DataFormat#DATE}
 * <p>
 * The {@link String} input will be parsed to an {@link java.time.LocalDate}
 */
@Slf4j
class DateParser implements Parser<LocalDate> {

    /**
     * The regex to determine if the date to parse if a HEX date
     */
    private static final Pattern HEX_DATE_FORMAT_REGEX = compile("^[a-fA-F\\d]{4}$");
    /**
     * The base date for a HEX date
     * <p>
     * January 1, 2000
     */
    private static final LocalDate BASE_DATE = of(2000, JANUARY, 1);
    /**
     * The list of formatters to parse the other dates
     */
    private static final List<DateFormater> FORMATTERS = asList(
            new DateFormater("yyyyMMdd"),
            new DateFormater("ddMMyyyy")
    );

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate parse(final String dateString) throws ParsingException {
        log.debug("Parsing date \"{}\"", dateString);
        final Matcher matcher = HEX_DATE_FORMAT_REGEX.matcher(dateString);
        final LocalDate date;
        if (matcher.matches()) {
            date = handleHexFormat(dateString);
        } else {
            date = handleOtherFormats(dateString);
        }

        log.debug("Date parsed as \"{}\"", date);
        return date;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataFormat getHandledFormat() {
        return DATE;
    }

    /**
     * Handles a HEX date format
     * <p>
     * The hexDate corresponds to the number of days to add from the January 1, 2000 {@link DateParser#BASE_DATE}
     * to get the encoded date
     * <p>
     * Example : 111E HEX date means 4382 elapsed days since January 1, 2000. The encoded date is December 31, 2011
     *
     * @param hexDate HEX date string
     * @return a new {@link LocalDate} instance corresponding to the input date string or null if hexDate is "FFFF"
     */
    private LocalDate handleHexFormat(final String hexDate) {
        // FFFF means no date
        if ("FFFF".equals(hexDate)) {
            log.trace("hexDate equals FFFF, returning null");
            return null;
        }
        final Integer numberOfDays = decode("#" + hexDate);
        return BASE_DATE.plusDays(numberOfDays);
    }

    /**
     * Parses the input date string using the formatters declared in {@link DateParser#FORMATTERS}
     *
     * @param dateString the date string to parse
     * @return a new {@link LocalDate} instance corresponding to the input date string
     * @throws ParsingException if no {@link DateParser#FORMATTERS} could parse the input data string
     */
    private LocalDate handleOtherFormats(final String dateString) throws ParsingException {
        for (final DateFormater formatter : FORMATTERS) {
            try {
                final LocalDate parsed = LocalDate.parse(dateString, formatter.getFormatter());
                log.trace("Formatter \"{}\", successfully parsed data \"{}\"", formatter.getPattern(), dateString);
                return parsed;
            } catch (final DateTimeParseException e) {
                // Ignore and try next formatter
                log.trace("Formatter \"{}\", could not parse data \"{}\"", formatter.getPattern(), dateString);
            }
        }
        log.warn("No formatter could parse date {}", dateString);
        throw new ParsingException(format("No formatter could parse date \"%s\"", dateString));
    }

    /**
     * Data class use to get the pattern of a {@link DateTimeFormatter} since it's not accessible
     */
    @Value
    private static class DateFormater {
        /**
         * The pattern
         */
        String pattern;
        /**
         * The DateTimeFormatter created using the pattern
         */
        DateTimeFormatter formatter;

        DateFormater(final String pattern) {
            this.pattern = pattern;
            formatter = ofPattern(pattern);
        }
    }

}
