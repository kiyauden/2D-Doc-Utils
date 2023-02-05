package fr.kiyauden._2ddoc;

import lombok.extern.slf4j.Slf4j;

import static fr.kiyauden._2ddoc.DataFormat.BOOLEAN;
import static java.lang.String.format;

/**
 * Parser implementation for {@link DataFormat#BOOLEAN}
 * <p>
 * The {@link String} input data will be parsed as a {@link Boolean}
 */
@Slf4j
class BooleanDataParser implements DataParser<Boolean> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean parse(final String data) throws ParsingException {
        log.debug("Parsing data \"{}\"", data);

        if ("1".equals(data)) {
            return true;
        } else if ("0".equals(data)) {
            return false;
        }

        log.warn("Could not parse data \"{}\", unsupported format", data);
        throw new ParsingException(format("Could not parse data \"%s\", unsupported format", data));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataFormat getHandledFormat() {
        return BOOLEAN;
    }

}
