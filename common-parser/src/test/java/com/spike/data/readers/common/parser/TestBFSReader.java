package com.spike.data.readers.common.parser;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spike.data.readers.common.parser.bfs.BFSModels;
import com.spike.data.readers.common.reader.RandomAccessDataFileReader;
import org.antlr.v4.runtime.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class TestBFSReader {
    private

    static final Map<Path, String> errorMsgCollector = Maps.newHashMap();

    static ANTLRErrorStrategy errorStrategy = new DefaultErrorStrategy() {
    };

    static class MyErrorListener extends BaseErrorListener {
        private Path path;

        public MyErrorListener(Path path) {
            this.path = path;
        }

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                                int charPositionInLine, String msg, RecognitionException e) {
            errorMsgCollector.put(path, msg);
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(Paths.get(".").toFile().getAbsolutePath());
        Path path = Paths.get("mysql/src/main/bfs/frmFile.bfs");
        BFSReader reader = new BFSReader(path);
        BFSModels.BFSFile bfsFile = reader.parse();

        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
        System.out.println(gson.toJson(bfsFile));

        try (RandomAccessDataFileReader<Object> dataFileReader =
                     new RandomAccessDataFileReader<Object>(
                             Paths.get("mysql/src/main/bfs/t1.frm"))) {
            reader.read(dataFileReader);
        }
    }
}
