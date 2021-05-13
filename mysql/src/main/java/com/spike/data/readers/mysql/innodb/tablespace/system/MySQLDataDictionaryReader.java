package com.spike.data.readers.mysql.innodb.tablespace.system;

import com.spike.data.readers.common.annotation.DataFileFormatDescription;
import com.spike.data.readers.common.reader.ChunkedDataFileReader;
import com.spike.data.readers.common.reader.ChunkedDataFileStreamReader;

public class MySQLDataDictionaryReader extends ChunkedDataFileStreamReader<
        MySQLibdata1FileReader.MySQLibdata1FileDescription,
        MySQLDataDictionaryReader.MySQLDataDictionaryDescription> {

    public MySQLDataDictionaryReader(ChunkedDataFileReader<MySQLibdata1FileReader.MySQLibdata1FileDescription> dataFileReader) {
        super(dataFileReader);
    }

    @Override
    public MySQLibdata1FileReader.MySQLibdata1FileDescription read() {
        return null;
    }

    @DataFileFormatDescription
    public static class MySQLDataDictionaryDescription {

    }
}
