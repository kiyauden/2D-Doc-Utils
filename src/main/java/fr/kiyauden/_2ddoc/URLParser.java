package fr.kiyauden._2ddoc;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base32;

import static fr.kiyauden._2ddoc.DataFormat.URL;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Parser implementation for {@link DataFormat#URL}
 * <p>
 * The {@link String} input data will be parsed as a {@link String} corresponding to an url
 */
@Slf4j
class URLParser implements Parser<String> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String parse(final String data) {
        log.debug("Parsing data value \"{}\"", data);

        final Base32 base32 = new Base32();
        final byte[] decoded = base32.decode(data);

        final String parsed = new String(decoded, UTF_8);
        log.debug("Data value parsed as \"{}\"", parsed);
        return parsed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataFormat getHandledFormat() {
        return URL;
    }

}
