package com.spike.data.readers.common.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spike.data.readers.common.parser.bfs.BFSModels;
import com.spike.data.readers.common.reader.RandomAccessDataFileReader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestBFSReader {
    public static void test(String bfsFileInput, String bfFileInput) throws IOException {
        Path path = Paths.get(bfsFileInput);
        BFSReader reader = new BFSReader(path);
        BFSModels.BFSFile bfsFile = reader.parse();

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
        System.out.println(gson.toJson(bfsFile));

        try (RandomAccessDataFileReader<Object> dataFileReader =
                     new RandomAccessDataFileReader<Object>(
                             Paths.get(bfFileInput))) {
            reader.read(dataFileReader);
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(Paths.get(".").toFile().getAbsolutePath());

        //test("bfs/main/mysql/frmFile.bfs", "bfs/test/mysql/t1.frm");

        test("bfs/main/mysql/perTableSpace.bfs", "bfs/test/mysql/t1.ibd");
    }
}
