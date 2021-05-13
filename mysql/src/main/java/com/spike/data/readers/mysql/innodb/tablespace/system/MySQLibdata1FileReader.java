package com.spike.data.readers.mysql.innodb.tablespace.system;

import com.spike.data.readers.common.annotation.DataFileFormatDescription;
import com.spike.data.readers.common.reader.ChunkedDataFileReader;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MySQLibdata1FileReader extends ChunkedDataFileReader<MySQLibdata1FileReader.MySQLibdata1FileDescription> {
    public MySQLibdata1FileReader(int chunkSize, String filePath) throws FileNotFoundException {
        super(chunkSize, filePath);
    }

    public MySQLibdata1FileReader(String filePath) throws FileNotFoundException {
        super(filePath);
    }

    @Override
    public MySQLibdata1FileDescription readFully() throws IOException {
        return null;
    }

    @DataFileFormatDescription
    public static class MySQLibdata1FileDescription {

    }
}
