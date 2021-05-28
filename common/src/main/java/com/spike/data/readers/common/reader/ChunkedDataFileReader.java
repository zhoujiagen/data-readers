package com.spike.data.readers.common.reader;

import com.google.common.primitives.Bytes;
import com.spike.data.readers.common.DataFileReader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;

import java.io.*;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

public class ChunkedDataFileReader<OUT> implements DataFileReader<OUT>, Closeable {
    public static final int DEFAULT_CHUNK_SIZE = 8192;

    protected final int chunkSize;
    protected ByteBuf byteBuf;
    private FileInputStream fis;

    public ChunkedDataFileReader(int chunkSize, String filePath) throws FileNotFoundException {
        checkArgument(chunkSize % 1024 == 0, "chunkSize must be multiplier of 1024");
        this.chunkSize = chunkSize;
        this.byteBuf = Unpooled.wrappedBuffer(new byte[chunkSize]).clear();

        checkArgument(filePath != null && filePath.length() > 0, "empty file path");
        File file = Paths.get(filePath).toFile();
        checkArgument(file.exists(), "file " + filePath + " not exist");
        fis = new FileInputStream(file);
    }

    public ChunkedDataFileReader(String filePath) throws FileNotFoundException {
        this(DEFAULT_CHUNK_SIZE, filePath);
    }

    @Override
    public void close() throws IOException {
        if (fis != null) {
            fis.close();
        }
    }

    /**
     * Read next chunk data.
     *
     * @throws EOFException if there is no more data because the end of the file has been reached.
     * @throws IOException  if an I/O error occurs.
     */
    private void readNextChunk() throws IOException {
        checkState(fis != null, "no file attached");

        byteBuf = byteBuf.clear();
        int bytesRead = fis.read(byteBuf.array());
        if (bytesRead == -1) {
            throw new EOFException();
        } else {
            byteBuf = byteBuf.writerIndex(bytesRead);
        }
    }

    @Override
    public byte readByte() throws IOException {
        if (byteBuf.readableBytes() == 0) {
            readNextChunk();
        }

        return byteBuf.readByte();
    }

    @Override
    public byte[] readByte(int n) throws IOException {
        checkArgument(n > 0, "n must be positive");

        if (byteBuf.readableBytes() == 0) {
            readNextChunk();
        }

        if (byteBuf.readableBytes() >= n) {
            return ByteBufUtil.getBytes(byteBuf.readBytes(n));
        }

        byte[] result = ByteBufUtil.getBytes(byteBuf);
        int bytesToRead = n - result.length;
        while (true) {
            try {
                readNextChunk();
            } catch (EOFException e) {
                return result;
            }
            int readableBytes = byteBuf.readableBytes();
            if (bytesToRead <= readableBytes) {
                result = Bytes.concat(result, ByteBufUtil.getBytes(byteBuf.readBytes(bytesToRead)));
                break;
            } else {
                result = Bytes.concat(result, ByteBufUtil.getBytes(byteBuf));
                bytesToRead -= readableBytes;
            }
        }
        return result;
    }
}
