package com.spike.data.readers.common.parser.bfs;

import com.spike.data.readers.common.reader.RandomAccessDataFileReader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestBFSDataReader {
    public static void test(String bfsFileInput, String bfFileInput) throws IOException {
        Path path = Paths.get(bfsFileInput);
        BFSDataReader bfsDataReader = new BFSDataReader(path);

        try (RandomAccessDataFileReader<Object> dataFileReader =
                     new RandomAccessDataFileReader<Object>(
                             Paths.get(bfFileInput))) {
            bfsDataReader.read(dataFileReader);
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(Paths.get(".").toFile().getAbsolutePath());

        //test("bfs/main/mysql/frmFile.bfs", "bfs/test/mysql/t1.frm");

        test("bfs/main/mysql/perTableSpace.bfs", "bfs/test/mysql/t1.ibd");
    }
}
