package com.spike.data.readers.common.reader;

import com.spike.data.readers.common.DataFileReader;
import com.spike.data.readers.common.DataFileStreamReader;

public abstract class RandomAccessDataFileStreamReader<FOUT, SOUT> implements DataFileStreamReader<SOUT> {
    private final RandomAccessDataFileReader<FOUT> dataFileReader;

    public RandomAccessDataFileStreamReader(RandomAccessDataFileReader<FOUT> dataFileReader) {
        this.dataFileReader = dataFileReader;
    }

    @Override
    public DataFileReader<FOUT> reader() {
        return dataFileReader;
    }
}
