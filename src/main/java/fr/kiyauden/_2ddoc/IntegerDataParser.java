package fr.kiyauden._2ddoc;

import lombok.extern.slf4j.Slf4j;

import static fr.kiyauden._2ddoc.DataFormat.INTEGER;
import static java.lang.Integer.parseInt;

/**
 * Parser implementation for {@link DataFormat#INTEGER}
 * <p>
 * The {@link String} input will be parsed to an {@link Integer}
 */
@Slf4j
class IntegerDataParser implements DataParser<Integer> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer parse(final String data) {
        log.debug("Parsing data value \"{}\"", data);
        final int parsed = parseInt(data);

        log.debug("Data value parsed as \"{}\"", parsed);
        return parsed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataFormat getHandledFormat() {
        return INTEGER;
    }

}
