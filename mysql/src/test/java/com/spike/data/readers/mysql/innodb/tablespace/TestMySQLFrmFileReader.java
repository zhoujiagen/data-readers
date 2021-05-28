package com.spike.data.readers.mysql.innodb.tablespace;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestMySQLFrmFileReader {

    @Test
    public void testRead() {
        System.out.println(Paths.get(".").toFile().getAbsolutePath());
        final String fileName = "src/main/bfs/t1.frm";

        try (MySQLFrmFileReader reader = new MySQLFrmFileReader(8192, fileName)) {
            MySQLFrmFileReader.MySQLFrmFileDescription result = reader.readFully();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            System.out.println(gson.toJson(result));
        } catch (EOFException e) {
            // ignore
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
