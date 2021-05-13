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
        ByteBufUtil.hexDump(bytes);
    }

    OUT readFully() throws IOException;

    byte readByte() throws IOException;

    byte[] readByte(int n) throws IOException;
}
