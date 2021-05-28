package com.spike.data.readers.common;

import io.netty.buffer.ByteBufUtil;

import java.io.IOException;

/**
 * Read data file.
 *
 * @param <OUT> File format description or Void
 */
public interface DataFileReader<OUT> {

    default void hexDump(byte[] bytes) {
        System.out.println(ByteBufUtil.hexDump(bytes));
    }

    default String hexDumpString(byte[] bytes) {
        return ByteBufUtil.hexDump(bytes);
    }


    default OUT readFully() throws IOException {
        throw new UnsupportedOperationException();
    }

    byte readByte() throws IOException;

    default boolean readBit() throws IOException {
        throw new UnsupportedOperationException();
    }

    byte[] readByte(int n) throws IOException;
}
