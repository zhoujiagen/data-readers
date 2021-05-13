package com.spike.data.readers.common.reader;

import com.spike.data.readers.common.DataFileReader;
import com.spike.data.readers.common.DataFileStreamReader;

/**
 * Read data file stream.
 *
 * @param <FOUT> Data file output
 * @param <SOUT> Data file stream output
 */
public abstract class ChunkedDataFileStreamReader<FOUT, SOUT> implements DataFileStreamReader<FOUT> {
    private final ChunkedDataFileReader<FOUT> dataFileReader;

    public ChunkedDataFileStreamReader(ChunkedDataFileReader<FOUT> dataFileReader) {
        this.dataFileReader = dataFileReader;
    }

    @Override
    public DataFileReader<FOUT> reader() {
        return dataFileReader;
    }

}
