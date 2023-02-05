package fr.kiyauden._2ddoc;

import lombok.extern.slf4j.Slf4j;

import static fr.kiyauden._2ddoc.DataFormat.TEXT;

/**
 * Parser implementation for {@link DataFormat#TEXT}
 * <p>
 * The {@link String} input data will be returned as is
 */
@Slf4j
class TextDataParser implements DataParser<String> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String parse(final String data) {
        log.debug("Parsing data value \"{}\", returning it as is", data);
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataFormat getHandledFormat() {
        return TEXT;
    }

}
