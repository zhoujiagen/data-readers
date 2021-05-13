package com.spike.data.readers.common;

public interface DataFileStreamReader<OUT> {
    DataFileReader reader();

    OUT read();
}
