package com.spike.data.readers.common.reader;

import com.spike.data.readers.common.DataFileReader;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;

public class RandomAccessDataFileReader<OUT> implements DataFileReader<OUT>, Closeable {

    private final RandomAccessFile raf;

    public RandomAccessDataFileReader(String filePath) throws FileNotFoundException {
        this.raf = new RandomAccessFile(filePath, "r");
    }

    public RandomAccessDataFileReader(Path path) throws FileNotFoundException {
        this.raf = new RandomAccessFile(path.toFile(), "r");
    }

    @Override
    public void close() throws IOException {
        raf.close();
    }

    @Override
    public byte readByte() throws IOException {
        return raf.readByte();
    }

    @Override
    public byte[] readByte(int n) throws IOException {
        byte[] result = new byte[n];
        int bytesRead = raf.read(result);
        if (bytesRead == -1) {
            throw new EOFException();
        }
        return Arrays.copyOf(result, bytesRead);
    }
}
