package fr.kiyauden._2ddoc;

/**
 * Service containing methods regarding a {@link Document}
 */
interface IDocumentService {

    /**
     * Checks is a data is mandatory for a document
     *
     * @param documentType the document type
     * @param dataType     the data type
     * @return TRUE if the data is mandatory for the provided document, FALSE otherwise
     * @throws DataExtractionException when the data has no link with the document
     */
    boolean isDataMandatory(final Document documentType, final DataType dataType) throws DataExtractionException;

}
