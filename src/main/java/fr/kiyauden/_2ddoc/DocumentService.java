package fr.kiyauden._2ddoc;

import static java.lang.String.format;

/**
 * Implementation of {@link IDocumentService}
 */
class DocumentService implements IDocumentService {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDataMandatory(final Document documentType, final DataType dataType)
            throws DataExtractionException {
        final boolean isInMandatoryList = documentType.getMandatoryData().contains(dataType);

        if (isInMandatoryList) {
            return true;
        } else {
            final boolean isInOptionalList = documentType.getOptionalData().contains(dataType);
            if (!isInOptionalList) {
                throw new DataExtractionException(
                        format("Data %s %s is not linked to document %s",
                               dataType.name(),
                               dataType.getId(),
                               documentType.name())
                );
            }
        }
        return false;
    }

}
