package fr.kiyauden._2ddoc;

import lombok.Value;

import java.util.List;

@Value
public class ExtractedData {

    List<Data> data;
    List<DataType> missingMandatoryData;

}
