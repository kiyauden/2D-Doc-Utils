package fr.kiyauden._2ddoc;

import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import static fr.kiyauden._2ddoc.DataFormat.AMOUNT;

/**
 * Parser implementation for {@link DataFormat#AMOUNT}
 * <p>
 * The {@link String} input data will be parsed as a {@link Double}
 */
@Slf4j
class AmountParser implements Parser<Double> {

    /**
     * The {@link DecimalFormat} format parser used to parse the amounts
     */
    public static final DecimalFormat PARSER =
            new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.FRANCE));

    /**
     * {@inheritDoc}
     */
    @Override
    public Double parse(final String data) throws ParsingException {
        log.debug("Parsing data value \"{}\"", data);
        final Number parsed;

        try {
            parsed = PARSER.parse(data);
        } catch (final ParseException e) {
            log.warn("Could not parse data \"{}\", unsupported format", data);
            throw new ParsingException(e);
        }

        final double parsedDouble = parsed.doubleValue();
        log.debug("Data value parsed as \"{}\"", parsedDouble);
        return parsedDouble;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataFormat getHandledFormat() {
        return AMOUNT;
    }

}
