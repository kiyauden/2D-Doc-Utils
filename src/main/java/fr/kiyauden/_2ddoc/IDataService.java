package fr.kiyauden._2ddoc;

import java.util.List;

/**
 * Service those job is to extract data from the 2D-DOC
 */
interface IDataService {

    /**
     * Extracts the data from the 2D-DOC
     *
     * @param data     the string representation of 2D-DOC data
     * @param document the document type where the data comes from
     * @param source   the source of the data, can be {@link DataSource#MESSAGE} or {@link DataSource#ANNEX}
     * @return the list for data extracted
     * @throws DataExtractionException when an error occurs while extracting data
     */
    List<Data> extractData(final String data, final Document document, final DataSource source)
            throws DataExtractionException;

}
