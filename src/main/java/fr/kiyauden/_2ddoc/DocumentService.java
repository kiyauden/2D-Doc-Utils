package fr.kiyauden._2ddoc;

import com.google.inject.Singleton;
import fr.kiyauden._2ddoc.Document.DatatypeDefinition;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * Implementation of {@link IDocumentService}
 */
@Singleton
class DocumentService implements IDocumentService {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDataMandatory(final Document documentType, final DataType dataType)
            throws DataExtractionException {
        final boolean isInMandatoryList = documentType.getMandatoryData().stream()
                .anyMatch(datatypeDefinition -> datatypeDefinition.getDataType().equals(dataType));

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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<DataType> computeMissingData(final Document documentType, final List<DataType> datatypes) {
        final List<DatatypeDefinition> mandatoryData = documentType.getMandatoryData();

        final List<DataType> missingData = new ArrayList<>();

        for (final DatatypeDefinition mandatoryDatum : mandatoryData) {
            final DataType dataType = mandatoryDatum.getDataType();

            final boolean found = datatypes.stream().anyMatch(dataType1 -> dataType1.equals(dataType));
            if (!found) {
                final List<DataType> interchangeableDataType = mandatoryDatum.getInterchangeableDataType();
                final boolean interchangeable = datatypes.containsAll(interchangeableDataType);
                if (!interchangeable) {
                    missingData.add(dataType);
                }
            }
        }

        return missingData;
    }

}
